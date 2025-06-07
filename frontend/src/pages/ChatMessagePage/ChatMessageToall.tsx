import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

interface ChatMessage {
	message: string;
	username: string;
	messageType: 'CHAT' | 'ENTER' | 'QUIT' | 'ADMIN';
	timestamp?: string;
	isMe?: boolean;
}

const ChatMessageToall = () => {
	const [messages, setMessages] = useState<ChatMessage[]>([]);
	const [newMessage, setNewMessage] = useState('');
	const [isConnected, setIsConnected] = useState(false);
	const [userCount, setUserCount] = useState(1);
	const [myUsername, setMyUsername] = useState<string | null>(null);
	const [myRole, setMyRole] = useState<string | null>(null);
	const [hasEnteredRoom, setHasEnteredRoom] = useState(false);

	const stompClient = useRef<Client | null>(null);
	const messagesEndRef = useRef<HTMLDivElement>(null);
	const reconnectTimeoutRef = useRef<NodeJS.Timeout | null>(null);
	const ROOM_ID = '1';

	const fetchInitialUserCount = async () => {
		try {
			const response = await fetch(`/api/chat/rooms/${ROOM_ID}/user-count`);
			const data = await response.json();
			if (data.success) {
				setUserCount(data.count || 1);
			} else {
				setUserCount(1);
			}
		} catch {
			setUserCount(1);
		}
	};

	const isTokenExpired = (token: string): boolean => {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			return payload.exp < Math.floor(Date.now() / 1000);
		} catch {
			return true;
		}
	};

	const getValidToken = async (): Promise<string | null> => {
		let token = localStorage.getItem('accessToken');
		if (!token || isTokenExpired(token)) return null;
		return token;
	};

	const extractUserInfoFromToken = (token: string): void => {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			const username = payload.cloverName || payload.username;
			const role = payload.role || null;
			setMyUsername(username);
			setMyRole(role);
		} catch {
			setMyUsername(null);
			setMyRole(null);
		}
	};

	useEffect(() => {
		getValidToken().then(token => token && extractUserInfoFromToken(token));
	}, []);

	useEffect(() => {
		if (!myUsername) return;
		getValidToken().then(token => token && connectWebSocket(token));
	}, [myUsername]);

	const connectWebSocket = (token: string) => {
		if (stompClient.current) stompClient.current.deactivate();

		const client = new Client({
			webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
			reconnectDelay: 3000,
			connectHeaders: { Authorization: `Bearer ${token}`, roomId: ROOM_ID },
			onConnect: () => {
				setIsConnected(true);
				fetchInitialUserCount();
				client.subscribe(`/sub/chat/${ROOM_ID}`, (message) => {
					const chatMessage: ChatMessage = JSON.parse(message.body);
					setMessages(prev => [...prev, {
						...chatMessage,
						timestamp: new Date().toLocaleTimeString(),
						isMe: myUsername?.trim().toLowerCase() === chatMessage.username?.trim().toLowerCase()
					}]);
				});
				client.subscribe(`/sub/user-count/${ROOM_ID}`, (message) => {
					try {
						const data = JSON.parse(message.body);
						setUserCount(data.count || 1);
					} catch {
						setUserCount(0);
					}
				});
			},
			onStompError: handleDisconnect,
			onWebSocketClose: (event) => {
				if (event.code !== 1000) handleDisconnect();
				else setHasEnteredRoom(false);
			},
			onWebSocketError: handleDisconnect,
		});

		client.activate();
		stompClient.current = client;
	};

	const handleDisconnect = () => {
		setIsConnected(false);
		setHasEnteredRoom(false);
		setUserCount(0);
		reconnectTimeoutRef.current = setTimeout(() => {
			getValidToken().then(token => token && connectWebSocket(token));
		}, 5000);
	};

	const sendMessage = () => {
		if (!newMessage.trim() || !stompClient.current || !isConnected) return;
		const isAdmin = myRole === 'ADMIN';
		const destination = isAdmin ? `/pub/chat/${ROOM_ID}/admin/notice` : `/pub/chat/${ROOM_ID}`;
		const messageType = isAdmin ? 'ADMIN' : 'CHAT';
		stompClient.current.publish({
			destination,
			body: JSON.stringify({ message: newMessage, sender: myUsername, messageType })
		});
		setNewMessage('');
	};

	return (
		<div style={{ padding: '0.5rem' }}>
			<div style={{ marginBottom: '0.5rem' }}>
				<div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.6rem' }}>
          <span style={{ fontWeight: 'bold', color: isConnected ? '#28a745' : '#dc3545' }}>
            {isConnected ? '🟢 온라인' : '🔴 오프라인'}
          </span>
					<span style={{ fontWeight: 'bold', color: '#495057' }}>
            👥 접속자 수: {userCount}명
          </span>
				</div>
			</div>
			<div style={{ height: '65vh', overflowY: 'auto', border: '1px solid #ccc', borderRadius: '0.25rem', padding: '0.5rem' }}>
				{messages.map((msg, idx) => (
					<div key={idx} style={{ display: 'flex', justifyContent: msg.isMe ? 'flex-end' : 'flex-start', marginBottom: '0.5rem' }}>
						<div style={{ maxWidth: '70%', textAlign: msg.isMe ? 'right' : 'left' }}>
							{msg.messageType === 'ENTER' && <div style={{ fontSize: '0.5rem', color: '#999', textAlign: 'center' }}>👋 {msg.username}님이 입장했습니다.</div>}
							{msg.messageType === 'QUIT' && <div style={{ fontSize: '0.5rem', color: '#999', textAlign: 'center' }}>❌ {msg.username}님이 퇴장했습니다.</div>}
							{msg.messageType === 'ADMIN' && <div style={{ fontSize: '0.8rem', color: '#888', fontStyle: 'italic', textAlign: 'center' }}>{msg.message}</div>}
							{msg.messageType === 'CHAT' && (
								<>
									{!msg.isMe && <div style={{ fontSize: '0.5rem', color: '#666', marginBottom: '0.25rem' }}>{msg.username}</div>}
									<div style={{ backgroundColor: msg.isMe ? '#4f46e5' : '#e5e7eb', color: msg.isMe ? 'white' : 'black', padding: '0.5rem 1rem', borderRadius: '1rem' }}>{msg.message}</div>
									<div style={{ fontSize: '0.5rem', color: '#999' }}>{msg.timestamp}</div>
								</>
							)}
						</div>
					</div>
				))}
				<div ref={messagesEndRef} />
			</div>
			<div style={{ display: 'flex', marginTop: '0.5rem' }}>
				<input
					type="text"
					placeholder={isConnected ? '메시지를 입력하세요...' : '서버에 연결 중...'}
					value={newMessage}
					onChange={(e) => setNewMessage(e.target.value)}
					onKeyDown={(e) => { if (e.key === 'Enter') sendMessage(); }}
					disabled={!isConnected}
					style={{ flex: 1, padding: '0.5rem', borderRadius: '0.5rem', border: '1px solid #ccc' }}
				/>
				<button
					onClick={sendMessage}
					disabled={!isConnected || !newMessage.trim()}
					style={{ marginLeft: '0.5rem', padding: '0.5rem', borderRadius: '0.5rem', backgroundColor: '#3b82f6', color: 'white', border: 'none' }}
				>전송</button>
			</div>
		</div>
	);
};

export default ChatMessageToall;
