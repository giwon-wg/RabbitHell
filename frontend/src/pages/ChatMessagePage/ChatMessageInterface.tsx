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

const ChatMessageInterface = () => {
	const [messages, setMessages] = useState<ChatMessage[]>([]);
	const [newMessage, setNewMessage] = useState('');
	const [isConnected, setIsConnected] = useState(false);
	const stompClient = useRef<Client | null>(null);
	const messagesEndRef = useRef<HTMLDivElement>(null);
	const ROOM_ID = '0';

	// JWT 토큰에서 username 추출 (서버에서 반환된 메시지의 username과 비교하기 위함)
	const token = localStorage.getItem('accessToken');
	const [myUsername, setMyUsername] = useState<string | null>(null);

	const scrollToBottom = () => {
		messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
	};

	useEffect(() => {
		scrollToBottom();
	}, [messages]);

	useEffect(() => {
		if (!token) {
			console.error('❌ JWT 토큰 없음');
			return;
		}
		// JWT 디코딩 (Base64 방식)
		const payloadBase64 = token.split('.')[1];
		if (payloadBase64) {
			try {
				const decoded = JSON.parse(atob(payloadBase64));
				setMyUsername(decoded.cloverName || decoded.username);
			} catch (err) {
				console.error('❌ JWT 디코딩 실패', err);
			}
		}

		connectWebSocket(token);
		return () => {
			stompClient.current?.deactivate();
		};
	}, []);

	const formatTime = (date: Date) => {
		const hours = date.getHours();
		const minutes = date.getMinutes();
		const ampm = hours >= 12 ? '오후' : '오전';
		const displayHours = hours % 12 || 12;
		return `${ampm} ${displayHours}:${minutes.toString().padStart(2, '0')}`;
	};

	const connectWebSocket = (token: string) => {
		const wsUrl = `http://localhost:8080/ws?token=${token}`;
		const client = new Client({
			webSocketFactory: () => new SockJS(wsUrl),
			reconnectDelay: 3000,
			onConnect: () => {
				console.log('✅ WebSocket 연결됨');
				setIsConnected(true);

				client.subscribe(`/sub/chat/${ROOM_ID}`, (message) => {
					const chatMessage: ChatMessage = JSON.parse(message.body);
					setMessages((prev) => [
						...prev,
						{
							...chatMessage,
							timestamp: formatTime(new Date()),
							isMe: chatMessage.username === myUsername,
						},
					]);
				});
			},
			onStompError: (frame) => {
				console.error('❌ STOMP 오류:', frame);
			},
			onWebSocketClose: () => {
				console.warn('🔌 WebSocket 연결 종료');
				setIsConnected(false);
			},
			connectHeaders: {
				Authorization : 'Bearer ${token}',
				roomId: ROOM_ID,

			},
		});

		console.log('🔄 WebSocket 연결 시도 중...');
		client.activate();
		stompClient.current = client;
	};

	const sendMessage = () => {
		if (newMessage.trim() && stompClient.current && isConnected) {
			stompClient.current.publish({
				destination: `/pub/chat/${ROOM_ID}`,
				body: JSON.stringify({
					message: newMessage,
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

	return (
		<div style={{ padding: '1rem', fontFamily: 'sans-serif' }}>
			<div style={{ height: '70vh', overflowY: 'auto', border: '1px solid #ccc', padding: '1rem' }}>
				{messages.map((msg, index) => {
					if (msg.messageType !== 'CHAT') {
						return (
							<div
								key={index}
								style={{
									textAlign: 'center',
									color: msg.messageType === 'ADMIN' ? '#444' : '#888',
									fontStyle: msg.messageType === 'ADMIN' ? 'italic' : 'normal',
									margin: '0.5rem 0',
								}}
							>
								{msg.message}
							</div>
						);
					}

					return (
						<div
							key={index}
							style={{
								display: 'flex',
								justifyContent: msg.isMe ? 'flex-end' : 'flex-start',
								marginBottom: '0.75rem',
							}}
						>
							<div style={{ maxWidth: '70%', textAlign: msg.isMe ? 'right' : 'left' }}>
								{!msg.isMe && (
									<div
										style={{
											fontSize: '0.8rem',
											color: '#666',
											marginBottom: '0.25rem',
										}}
									>
										{msg.username}
									</div>
								)}
								<div
									style={{
										display: 'inline-block',
										backgroundColor: msg.isMe ? '#4f46e5' : '#e5e7eb',
										color: msg.isMe ? 'white' : 'black',
										padding: '0.5rem 1rem',
										borderRadius: '1rem',
										wordBreak: 'break-word',
									}}
								>
									{msg.message}
								</div>
								<div
									style={{
										fontSize: '0.7rem',
										color: '#999',
										marginTop: '0.2rem',
									}}
								>
									{msg.timestamp}
								</div>
							</div>
						</div>
					);
				})}
				<div ref={messagesEndRef} />
			</div>

			<div style={{ display: 'flex', marginTop: '1rem' }}>
				<input
					type="text"
					placeholder={isConnected ? '메시지를 입력하세요...' : '서버에 연결 중...'}
					value={newMessage}
					onChange={(e) => setNewMessage(e.target.value)}
					onKeyDown={handleKeyPress}
					disabled={!isConnected}
					style={{
						flex: 1,
						padding: '0.5rem',
						borderRadius: '0.5rem',
						border: '1px solid #ccc',
						backgroundColor: isConnected ? 'white' : '#f3f4f6',
					}}
				/>
				<button
					onClick={sendMessage}
					disabled={!isConnected || !newMessage.trim()}
					style={{
						marginLeft: '0.5rem',
						padding: '0.5rem 1rem',
						borderRadius: '0.5rem',
						backgroundColor: '#3b82f6',
						color: 'white',
						border: 'none',
					}}
				>
					전송
				</button>
			</div>
		</div>
	);
};

export default ChatMessageInterface;
