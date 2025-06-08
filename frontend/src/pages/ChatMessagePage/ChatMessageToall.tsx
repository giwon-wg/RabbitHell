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

const ROOM_ID = '1';
const SOCKET_URL = 'http://localhost:8080/ws';

const ChatMessageToall = () => {
	const [messages, setMessages] = useState<ChatMessage[]>([]);
	const [newMessage, setNewMessage] = useState('');
	const [isConnected, setIsConnected] = useState(false);
	const [userCount, setUserCount] = useState(0);
	const [myUsername, setMyUsername] = useState<string | null>(null);

	const stompClient = useRef<Client | null>(null);
	const messagesEndRef = useRef<HTMLDivElement>(null);
	const reconnectTimeoutRef = useRef<NodeJS.Timeout | null>(null);

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (token && !isTokenExpired(token)) {
			const username = extractUsernameFromToken(token);
			setMyUsername(username);
		}
	}, []);

	useEffect(() => {
		if (!myUsername) return;

		// Redis 기반 채팅 기록 로딩
		const fetchInitialChatHistory = async () => {
			try {
				const res = await fetch(`/api/chat/${ROOM_ID}/history`);
				const data: ChatMessage[] = await res.json();
				const parsed = data.map((msg) => ({
					...msg,
					isMe: msg.username?.trim().toLowerCase() === myUsername.trim().toLowerCase(),
					timestamp: formatTime(new Date(msg.timestamp || Date.now()))
				}));
				setMessages(parsed);
			} catch (e) {
				console.error('채팅 기록 로딩 실패', e);
			}
		};

		fetchInitialChatHistory();

		const token = localStorage.getItem('accessToken');
		if (token && !isTokenExpired(token)) {
			connectWebSocket(token);
		}
	}, [myUsername]);

	useEffect(() => {
		const handleUnload = () => {
			stompClient.current?.deactivate();
		};

		window.addEventListener('beforeunload', handleUnload);
		window.addEventListener('unload', handleUnload);

		return () => {
			window.removeEventListener('beforeunload', handleUnload);
			window.removeEventListener('unload', handleUnload);
			handleUnload();
		};
	}, []);

	useEffect(() => {
		if (messagesEndRef.current) {
			messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
		}
	}, [messages]);

	const isTokenExpired = (token: string): boolean => {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			return payload.exp < Math.floor(Date.now() / 1000);
		} catch {
			return true;
		}
	};

	const extractUsernameFromToken = (token: string): string | null => {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			return payload.cloverName || payload.username || null;
		} catch {
			return null;
		}
	};

	const fetchInitialUserCount = async () => {
		try {
			const res = await fetch(`/api/chat/rooms/${ROOM_ID}/user-count`);
			const data = await res.json();
			setUserCount(data.count || 1);
		} catch {
			setUserCount(1);
		}
	};

	const connectWebSocket = (token: string) => {
		if (stompClient.current) stompClient.current.deactivate();

		const client = new Client({
			webSocketFactory: () => new SockJS(SOCKET_URL),
			connectHeaders: { Authorization: `Bearer ${token}`, roomId: ROOM_ID },
			reconnectDelay: 3000,
			onConnect: () => {
				setIsConnected(true);
				fetchInitialUserCount();

				client.subscribe(`/sub/chat/${ROOM_ID}`, (message) => {
					const chatMessage: ChatMessage = JSON.parse(message.body);
					setMessages((prev) => [
						...prev,
						{
							...chatMessage,
							timestamp: formatTime(new Date()),
							isMe: chatMessage.username?.trim().toLowerCase() === myUsername?.trim().toLowerCase(),
						},
					]);
				});

				client.subscribe(`/sub/user-count/${ROOM_ID}`, (message) => {
					try {
						const data = JSON.parse(message.body);
						setUserCount(data.count || 0);
					} catch {
						setUserCount(0);
					}
				});
			},
			onStompError: scheduleReconnect,
			onWebSocketClose: () => {
				setIsConnected(false);
				scheduleReconnect();
			},
			onWebSocketError: () => {
				setIsConnected(false);
				scheduleReconnect();
			},
		});

		client.activate();
		stompClient.current = client;
	};

	const scheduleReconnect = () => {
		if (reconnectTimeoutRef.current) clearTimeout(reconnectTimeoutRef.current);
		reconnectTimeoutRef.current = setTimeout(() => {
			const token = localStorage.getItem('accessToken');
			if (token && !isTokenExpired(token)) connectWebSocket(token);
		}, 5000);
	};

	const sendMessage = () => {
		if (newMessage.trim() && stompClient.current && isConnected) {
			stompClient.current.publish({
				destination: `/pub/chat/${ROOM_ID}`,
				body: JSON.stringify({ message: newMessage, sender: myUsername, messageType: 'CHAT' }),
			});
			setNewMessage('');
		}
	};

	const formatTime = (date: Date) => {
		const hours = date.getHours();
		const minutes = date.getMinutes();
		const ampm = hours >= 12 ? '오후' : '오전';
		const displayHours = hours % 12 || 12;
		return `${ampm} ${displayHours}:${minutes.toString().padStart(2, '0')}`;
	};

	return (
		<div style={{ padding: '0.5rem' }}>
			<div style={{ marginBottom: '0.5rem' }}>
				<div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.6rem' }}>
          <span style={{ fontWeight: 'bold', color: isConnected ? '#28a745' : '#dc3545' }}>
            {isConnected ? '🟢 온라인' : '🔴 오프라인'}
          </span>
					<span style={{ fontWeight: 'bold', color: '#495057' }}>👥 접속자 수: {userCount}명</span>
				</div>
			</div>

			<div style={{ height: '65vh', overflowY: 'auto', border: '1px solid #ccc', borderRadius: '0.25rem', padding: '0.5rem' }}>
				{messages.map((msg, index) => (
					msg.messageType === 'ENTER' || msg.messageType === 'QUIT' ? (
						<div key={index} style={{ textAlign: 'center', color: '#999', fontSize: '0.5rem', marginBottom: '0.5rem' }}>
							{msg.messageType === 'ENTER' ? `👋 ${msg.username}님이 입장했습니다.` : `❌ ${msg.username}님이 퇴장했습니다.`}
						</div>
					) : msg.messageType === 'ADMIN' ? (
						<div key={index} style={{ textAlign: 'center', color: '#888', fontStyle: 'italic', fontSize: '0.85rem', marginBottom: '0.5rem' }}>{msg.message}</div>
					) : (
						<div key={index} style={{ display: 'flex', justifyContent: msg.isMe ? 'flex-end' : 'flex-start' }}>
							<div style={{ maxWidth: '70%', textAlign: msg.isMe ? 'right' : 'left' }}>
								{!msg.isMe && <div style={{ fontSize: '0.5rem', color: '#666', marginBottom: '0.25rem' }}>{msg.username}</div>}
								<div style={{ backgroundColor: msg.isMe ? '#4f46e5' : '#e5e7eb', color: msg.isMe ? 'white' : 'black', padding: '0.5rem 1rem', borderRadius: '1rem', marginBottom: '0.25rem' }}>{msg.message}</div>
								<div style={{ fontSize: '0.6rem', color: '#999' }}>{msg.timestamp}</div>
							</div>
						</div>
					)
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
					style={{
						marginLeft: '0.5rem',
						padding: '0.5rem',
						borderRadius: '0.5rem',
						backgroundColor: isConnected && newMessage.trim() ? '#3b82f6' : '#9ca3af',
						color: 'white',
						border: 'none',
						cursor: isConnected && newMessage.trim() ? 'pointer' : 'not-allowed'
					}}
				>
					전송
				</button>
			</div>
		</div>
	);
};

export default ChatMessageToall;
