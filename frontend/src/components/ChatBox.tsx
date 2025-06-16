import React, { useState } from 'react';
import ChatMessageToall from '../pages/ChatMessagePage/ChatMessageToall';
import ChatMessageToone from '../pages/ChatMessagePage/ChatMessageToone';
import styled from 'styled-components';

const ChatWrapper = styled.div<{ $isOpen: boolean }>`
	position: fixed;
	top: 70px;
	right: 0;
	display: flex;
	flex-direction: row;
	transform: ${({ $isOpen }) => ($isOpen ? 'translateX(0)' : 'translateX(260px)')};
	transition: transform 0.3s ease-in-out;
	z-index: 200;
`;

const ChatContainer = styled.div`
	width: 260px;
	height: calc(100dvh - 70px);
	background-color: #f9f9f9;
	border-left: 1px solid #ddd;
	display: flex;
	flex-direction: column;
`;

const ToggleButton = styled.button`
	width: 24px;
	height: 40px;
	margin-top: 0;
	background-color: #4f46e5;
	color: white;
	border: none;
	border-radius: 4px 0 0 4px;
	cursor: pointer;
	font-size: 14px;
`;

const ChatHeader = styled.div`
	display: flex;
	justify-content: space-around;
	padding: 8px;
	background-color: #eee;
	border-bottom: 1px solid #ccc;
	font-size: 14px;
	font-weight: bold;
`;

const ChatContent = styled.div`
	flex: 1;
	display: flex;
	flex-direction: column;
	font-size: 14px;
	color: #333;
	overflow: hidden;
	height: 100%;
`;

const ChatBodyWrapper = styled.div`
	flex: 1;
	display: flex;
	flex-direction: column;
	height: 100%;
`;

const ChatBox = () => {
	const [activeTab, setActiveTab] = useState<'전체' | '채팅' | '로그' | '챗봇'>('전체');
	const [isOpen, setIsOpen] = useState(true);

	return (
		<ChatWrapper $isOpen={!isOpen}>
			<ToggleButton onClick={() => setIsOpen(prev => !prev)}>
				{isOpen ? '◀' : '▶'}
			</ToggleButton>

			<ChatContainer>
				<ChatHeader>
					{['전체', '채팅', '로그', '챗봇'].map(tab => (
						<span
							key={tab}
							style={{
								cursor: 'pointer',
								fontWeight: activeTab === tab ? 'bold' : 'normal',
								color: activeTab === tab ? '#4f46e5' : '#666',
							}}
							onClick={() => setActiveTab(tab as any)}
						>
							{tab}
						</span>
					))}
				</ChatHeader>

			<ChatContent>
				{activeTab === '전체' && (
					<ChatBodyWrapper style={{ display: activeTab === '전체' ? 'flex' : 'none' }}>
						<ChatMessageToall />
					</ChatBodyWrapper>
				)}
				{activeTab === '채팅' && (
					<div>개인 채팅 준비 중...</div>
				)}
				{activeTab === '로그' && (
					<div>행동 로그 준비 중...</div>
				)}
				{activeTab === '챗봇' && (
					<div>챗봇 기능 준비 중...</div>
				)}
			</ChatContent>
			</ChatContainer>
		</ChatWrapper>
	);
};

export default ChatBox;
