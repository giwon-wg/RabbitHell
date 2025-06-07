import React, { useState, useEffect, useRef } from 'react';
import ChatMessageToAll from '../pages/ChatMessagePage/ChatMessageToAll';
import styled from 'styled-components';

const ChatContainer = styled.div`
	width: 260px;
	background-color: #f9f9f9;
	border-left: 1px solid #ddd;
	display: flex;
	flex-direction: column;
	height: 100%;
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

	return (
		<ChatContainer>
			<ChatHeader>
				{['전체', '채팅', '로그', '챗봇'].map((tab) => (
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
					<ChatBodyWrapper>
						<ChatMessageToAll />
					</ChatBodyWrapper>
				)}
				{activeTab === '채팅' && (
					<ChatBodyWrapper>
						<ChatMessageToAll />
					</ChatBodyWrapper>
				)}
				{activeTab === '로그' && (
					<div>행동 로그 준비 중...</div>
				)}
				{activeTab === '챗봇' && (
					<div>챗봇 기능 준비 중...</div>
				)}
			</ChatContent>
		</ChatContainer>
	);
};

export default ChatBox;
