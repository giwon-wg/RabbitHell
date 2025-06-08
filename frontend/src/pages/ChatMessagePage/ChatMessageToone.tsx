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

const ChatMessageToOne = () => {
	const [messages, setMessages] = useState<ChatMessage[]>([]);
	const [newMessage, setNewMessage] = useState('');
	const [isConnected, setIsConnected] = useState(false);
	const [userCount, setUserCount] = useState(1); // null 대신 0으로 초기화
	const [myUsername, setMyUsername] = useState<string | null>(null);
	const [hasEnteredRoom, setHasEnteredRoom] = useState(false);

	const stompClient = useRef<Client | null>(null);
	const messagesEndRef = useRef<HTMLDivElement>(null);
	const reconnectTimeoutRef = useRef<NodeJS.Timeout | null>(null);
	const ROOM_ID = '2';


	// 초기 접속자 수 가져오기 함수
	const fetchInitialUserCount = async () => {
		try {
			const response = await fetch(`/api/chat/rooms/${ROOM_ID}/user-count`);
			const data = await response.json();

			if (data.success) {
				const count = data.count || 1; // null/undefined면 0으로 설정
				setUserCount(count);
				console.log(`📊 초기 접속자 수: ${count}`);
			} else {
				console.warn('접속자 수 조회 실패:', data.error);
				setUserCount(1); // 실패시 0으로 설정
			}
		} catch (error) {
			console.error('접속자 수 조회 중 오류:', error);
			setUserCount(1); // 오류시 0으로 설정
		}
	};

	const isTokenExpired = (token: string): boolean => {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			const currentTime = Math.floor(Date.now() / 1000);
			return payload.exp < currentTime;
		} catch {
			return true;
		}
	};

	const getValidToken = async (): Promise<string | null> => {
		let token = localStorage.getItem('accessToken');
		if (!token) return null;
		if (isTokenExpired(token)) {
			token = localStorage.getItem('accessToken');
			if (!token || isTokenExpired(token)) return null;
		}
		return token;
	};

	const extractUsernameFromToken = (token: string): string | null => {
		try {
			const payloadBase64 = token.split('.')[1];
			const decoded = JSON.parse(atob(payloadBase64));
			const username = decoded.cloverName || decoded.username;
			setMyUsername(username);
			return username;
		} catch {
			return null;
		}
	};

	useEffect(() => {
		const init = async () => {
			const token = await getValidToken();
			if (token) extractUsernameFromToken(token);
		};
		init();
	}, []);

	useEffect(() => {
		if (!myUsername) return;
		const connect = async () => {
			const token = await getValidToken();
			if (token) connectWebSocket(token);
		};
		connect();
	}, [myUsername]);

	useEffect(() => {
		const handleBeforeUnload = () => {
			if (hasEnteredRoom && stompClient.current && isConnected) {

			}
		};

		const handleUnload = () => {
			if (hasEnteredRoom && stompClient.current && isConnected) {

			}
		};

		window.addEventListener('beforeunload', handleBeforeUnload);
		window.addEventListener('unload', handleUnload);

		return () => {
			window.removeEventListener('beforeunload', handleBeforeUnload);
			window.removeEventListener('unload', handleUnload);
			if (hasEnteredRoom && stompClient.current && isConnected) {

			}
		};
	}, [hasEnteredRoom, isConnected]);

	const formatTime = (date: Date) => {
		const hours = date.getHours();
		const minutes = date.getMinutes();
		const ampm = hours >= 12 ? '오후' : '오전';
		const displayHours = hours % 12 || 12;
		return `${ampm} ${displayHours}:${minutes.toString().padStart(2, '0')}`;
	};


	const connectWebSocket = (token: string) => {
		if (stompClient.current) {
			if (hasEnteredRoom && isConnected) {

			}
			stompClient.current.deactivate();
		}

		const client = new Client({
			webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
			reconnectDelay : 3000,
			connectHeaders : {
				Authorization: `Bearer ${token}`,
				roomId: ROOM_ID,
			},
			onConnect: () => {

				setIsConnected(true);

				// 🔧 WebSocket 연결 즉시 초기 접속자 수 가져오기
				fetchInitialUserCount();
				console.log(`📊 초기 접속자 수 업데이트:`);
				// 채팅 메시지 구독
				client.subscribe(`/sub/chat/${ROOM_ID}`, (message) => {
					const chatMessage: ChatMessage = JSON.parse(message.body);
					const normalizedUsername = myUsername?.trim().toLowerCase();
					const messageSender = chatMessage.username?.trim().toLowerCase();

					setMessages((prev) => [
						...prev,
						{
							...chatMessage,
							timestamp: formatTime(new Date()),
							isMe: normalizedUsername === messageSender,
						},
					]);
				});

				// 🔧 접속자 수 실시간 업데이트 구독
				client.subscribe(`/sub/user-count/${ROOM_ID}`, (message) => {
					try {
						const data = JSON.parse(message.body);
						const count = data.count !== undefined ? data.count : 1; // null/undefined 처리

						console.log(`📊 실시간 접속자 수 업데이트: ${count}`);
						setUserCount(count);
					} catch (error) {
						console.error('접속자 수 메시지 파싱 오류:', error);
						setUserCount(0);
					}
				});
			},
			onStompError: () => {
				setIsConnected(false);
				setHasEnteredRoom(false);
				setUserCount(0); // 🔧 연결 오류시 접속자 수 초기화
				scheduleReconnect();
			},
			onWebSocketClose: (event) => {
				setIsConnected(false);
				if (event.code !== 1000) {
					setUserCount(0); // 🔧 비정상 종료시 접속자 수 초기화
					scheduleReconnect();
				} else {
					setHasEnteredRoom(false);
				}
			},
			onWebSocketError: () => {
				setIsConnected(false);
				setHasEnteredRoom(false);
				setUserCount(0); // 🔧 WebSocket 오류시 접속자 수 초기화
				scheduleReconnect();
			},
		});

		client.activate();
		stompClient.current = client;
	};

	const scheduleReconnect = () => {
		if (reconnectTimeoutRef.current) clearTimeout(reconnectTimeoutRef.current);
		reconnectTimeoutRef.current = setTimeout(async () => {
			const token = await getValidToken();
			if (token) {
				setHasEnteredRoom(false);
				connectWebSocket(token);
			}
		}, 5000);
	};

	const sendMessage = () => {
		if (newMessage.trim() && stompClient.current && isConnected) {
			stompClient.current.publish({
				destination: `/pub/chat/${ROOM_ID}`,
				body: JSON.stringify({
					message: newMessage,
					sender: myUsername,
					messageType: 'CHAT'
				}),
			});
			setNewMessage('');
		}
	};

	const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
		if (e.key === 'Enter' && !e.shiftKey) {
			e.preventDefault();
			sendMessage();
		}
	};

	const handleManualReconnect = async () => {
		const token = await getValidToken();
		if (token) {
			setHasEnteredRoom(false);
			setUserCount(0); // 🔧 수동 재연결시 접속자 수 초기화
			connectWebSocket(token);
		}
	};

	return (
		<div style={{ padding: '0.5rem' }}>
			<div style={{ marginBottom: '0.5rem' }}>
				<div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.6rem',}}>
					<span style={{
						fontWeight: 'bold',
						color: isConnected ? '#28a745' : '#dc3545'
					}}>
						{isConnected ? '🟢 온라인' : '🔴 오프라인'}
					</span>
					<span style={{
						fontWeight: 'bold',
						color: '#495057'
					}}>
						👥 접속자 수: {userCount}명
					</span>
				</div>
			</div>

			<div style={{ height: '65vh', overflowY: 'auto', border: '1px solid #ccc', borderRadius:"0.25rem",padding:"0.5rem" }}>
				{messages.map((msg, index) => {
					if (msg.messageType === 'ENTER' || msg.messageType === 'QUIT') {
						return (
							<div key={index} style={{ textAlign: 'center', color: '#999', fontSize: '0.5rem', marginBottom: '0.5rem' }}>
								{msg.messageType === 'ENTER' && `👋 ${msg.username}님이 입장했습니다.`}
								{msg.messageType === 'QUIT' && `❌ ${msg.username}님이 퇴장했습니다.`}
							</div>
						);
					}

					if (msg.messageType === 'ADMIN') {
						return (
							<div key={index} style={{ textAlign: 'center', color: '#888', fontStyle: 'italic', fontSize: '0.85rem', marginBottom: '0.5rem' }}>
								{msg.message}
							</div>
						);
					}

					if (msg.messageType === 'CHAT') {
						return (
							<div key={index} style={{ display: 'flex', justifyContent: msg.isMe ? 'flex-end' : 'flex-start' }}>
								<div style={{ maxWidth: '70%', textAlign: msg.isMe ? 'right' : 'left' }}>
									{!msg.isMe && <div style={{ fontSize: '0.5rem', color: '#666', marginBottom: '0.25rem' }}>{msg.username}</div>}
									<div style={{
										backgroundColor: msg.isMe ? '#4f46e5' : '#e5e7eb',
										color: msg.isMe ? 'white' : 'black',
										padding: '0.5rem 1rem',
										borderRadius: '1rem',
										marginBottom: '0.25rem'
									}}>
										{msg.message}
									</div>
									<div style={{ fontSize: '0.6rem', color: '#999' }}>{msg.timestamp}</div>
								</div>
							</div>
						);
					}

					return null;
				})}
				<div ref={messagesEndRef} />
			</div>

			<div style={{ display: 'flex', marginTop: '0.5rem' }}>
				<input
					type="text"
					placeholder={isConnected ? '메시지를 입력하세요...' : '서버에 연결 중...'}
					value={newMessage}
					onChange={(e) => setNewMessage(e.target.value)}
					onKeyDown={handleKeyPress}
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

export default ChatMessageToOne;
