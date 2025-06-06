import React, { useState, useRef, useEffect } from 'react';


interface ChatMessage {
	sender: string;
	message: string;
	messageType: 'CHAT' | 'ENTER' | 'QUIT' | 'ADMIN';
	timestamp: Date;
	isMe?: boolean;
}

function Send(props: { size: number }) {
	return null;
}

const ChatInterface = () => {
	const [messages, setMessages] = useState<ChatMessage[]>([
		{ sender: '시스템', message: '플레이어1님이 입장했습니다.', messageType: 'ENTER', timestamp: new Date(Date.now() - 300000) },
		{ sender: '플레이어1', message: '안녕하세요! 새로운 플레이어입니다', messageType: 'CHAT', timestamp: new Date(Date.now() - 280000), isMe: false },
		{ sender: '플레이어2', message: '환영합니다! 같이 던전 가실래요?', messageType: 'CHAT', timestamp: new Date(Date.now() - 260000), isMe: false },
		{ sender: '나', message: '네 좋습니다! 몇 렙이세요?', messageType: 'CHAT', timestamp: new Date(Date.now() - 240000), isMe: true },
		{ sender: '시스템', message: '플레이어3님이 퇴장했습니다.', messageType: 'QUIT', timestamp: new Date(Date.now() - 220000) },
		{ sender: '관리자', message: '🚨 [공지사항] 서버 점검 예정 - 오후 2시부터 30분간', messageType: 'ADMIN', timestamp: new Date(Date.now() - 200000) }
	]);
	const [input, setInput] = useState<string>('');
	const [isConnected, setIsConnected] = useState<boolean>(false);
	const scrollRef = useRef<HTMLDivElement | null>(null);

	useEffect(() => {
		scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
	}, [messages]);

	// 시뮬레이션을 위한 웹소켓 연결 상태 토글
	useEffect(() => {
		const timer = setTimeout(() => {
			setIsConnected(true);
			const enterMsg: ChatMessage = {
				sender: '시스템',
				message: '나님이 입장했습니다.',
				messageType: 'ENTER',
				timestamp: new Date()
			};
			setMessages(prev => [...prev, enterMsg]);
		}, 1000);

		return () => clearTimeout(timer);
	}, []);

	const formatTime = (date: Date) => {
		const hours = date.getHours();
		const minutes = date.getMinutes();
		const ampm = hours >= 12 ? '오후' : '오전';
		const displayHours = hours % 12 || 12;
		return `${ampm} ${displayHours}:${minutes.toString().padStart(2, '0')}`;
	};

	const sendMessage = () => {
		if (!input.trim() || !isConnected) return;

		const newMessage: ChatMessage = {
			sender: '나',
			message: input,
			messageType: 'CHAT',
			timestamp: new Date(),
			isMe: true
		};

		setMessages(prev => [...prev, newMessage]);
		setInput('');

		// 시뮬레이션: 가끔 다른 플레이어 응답
		if (Math.random() > 0.7) {
			setTimeout(() => {
				const responses = [
					'좋은 아이디어네요!',
					'ㅋㅋㅋ',
					'저도 동참할게요',
					'파티 구해요~',
					'누구 레이드 같이 하실분?'
				];
				const randomResponse: ChatMessage = {
					sender: `플레이어${Math.floor(Math.random() * 10) + 1}`,
					message: responses[Math.floor(Math.random() * responses.length)],
					messageType: 'CHAT',
					timestamp: new Date(),
					isMe: false
				};
				setMessages(prev => [...prev, randomResponse]);
			}, 1000 + Math.random() * 2000);
		}
	};

	const renderMessage = (msg: ChatMessage, index: number) => {
		// 시스템 메시지 (입장/퇴장)
		if (msg.messageType === 'ENTER' || msg.messageType === 'QUIT') {
			return (
				<div key={index} className="flex justify-center my-2">
					<div className="bg-gray-300 text-gray-600 text-xs px-3 py-1 rounded-full">
						{msg.message}
					</div>
				</div>
			);
		}

		// 관리자 메시지
		if (msg.messageType === 'ADMIN') {
			return (
				<div key={index} className="flex justify-center my-3">
					<div className="bg-red-100 border border-red-300 text-red-700 text-sm px-4 py-2 rounded-lg max-w-xs text-center">
						{msg.message}
					</div>
				</div>
			);
		}

		// 일반 채팅 메시지
		const isMyMessage = msg.isMe;
		return (
			<div key={index} className={`flex ${isMyMessage ? 'justify-end' : 'justify-start'} mb-3`}>
				<div className={`flex flex-col ${isMyMessage ? 'items-end' : 'items-start'} max-w-xs`}>
					{!isMyMessage && (
						<div className="text-xs text-gray-600 mb-1 px-1">
							{msg.sender}
						</div>
					)}
					<div className="flex items-end space-x-1">
						{isMyMessage && (
							<div className="text-xs text-gray-500 mb-1">
								{formatTime(msg.timestamp)}
							</div>
						)}
						<div className={`px-3 py-2 rounded-2xl ${
							isMyMessage
								? 'bg-blue-500 text-white'
								: 'bg-gray-200 text-gray-800'
						}`}>
							<p className="text-sm">{msg.message}</p>
						</div>
						{!isMyMessage && (
							<div className="text-xs text-gray-500 mb-1">
								{formatTime(msg.timestamp)}
							</div>
						)}
					</div>
				</div>
			</div>
		);
	};

	return (
		<div className="bg-gradient-to-b from-blue-100 to-purple-100 min-h-screen flex flex-col items-center p-4">
			<div className="bg-white shadow-lg rounded-lg w-full max-w-md flex flex-col h-[700px]">
				{/* 헤더 */}
				<div className="bg-gradient-to-r from-purple-500 to-blue-500 text-white p-4 rounded-t-lg">
					<div className="flex items-center justify-between">
						<div>
							<h2 className="font-bold text-lg">🎮 전체 채팅</h2>
							<p className="text-sm opacity-90">RPG 월드 채팅방</p>
						</div>
						<div className={`w-3 h-3 rounded-full ${isConnected ? 'bg-green-400' : 'bg-red-400'}`} />
					</div>
				</div>

				{/* 채팅 영역 */}
				<div className="flex-1 overflow-y-auto p-4 space-y-1">
					{messages.map((msg, index) => renderMessage(msg, index))}
					<div ref={scrollRef} />
				</div>

				{/* 입력 영역 */}
				<div className="p-4 border-t bg-gray-50 rounded-b-lg">
					<div className="flex items-center space-x-2">
						<input
							type="text"
							placeholder={isConnected ? "메시지를 입력하세요..." : "연결 중..."}
							value={input}
							onChange={(e) => setInput(e.target.value)}
							onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
							disabled={!isConnected}
							className="flex-1 px-4 py-2 border border-gray-300 rounded-full outline-none focus:border-blue-500 disabled:bg-gray-100"
						/>
						<button
							onClick={sendMessage}
							disabled={!isConnected || !input.trim()}
							className="bg-blue-500 hover:bg-blue-600 disabled:bg-gray-300 text-white p-2 rounded-full transition-colors"
						>
							<Send size={18} />
						</button>
					</div>
					{isConnected && (
						<div className="text-xs text-gray-500 mt-2 text-center">
							온라인 • 전체 채팅방
						</div>
					)}
				</div>
			</div>
		</div>
	);
};

export default ChatInterface;
