import React, {useEffect, useRef, useState} from 'react';
import SockJS from 'sockjs-client';
import {Client} from '@stomp/stompjs';

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


};

export default ChatMessageToOne;
