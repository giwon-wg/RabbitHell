import React from 'react';
import styled from 'styled-components';

const ChatContainer = styled.div`
	width: 260px;
	background-color: #f9f9f9;
	border-left: 1px solid #ddd;
	display: flex;
	flex-direction: column;
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
	padding: 12px;
	overflow-y: auto;
	font-size: 14px;
	color: #333;
`;

const ChatBox = () => {
	return (
		<ChatContainer>
			<ChatHeader>
				<span>전체</span>
				<span>채팅</span>
				<span>로그</span>
				<span>챗봇</span>
			</ChatHeader>
			<ChatContent>
				<p>토끼1: 넌 못 지나간다!</p>
				<p>토끼2: 뭐야 지나가게 해주세요!</p>
				<p>시스템: 스태미너 475/500</p>
			</ChatContent>
		</ChatContainer>
	);
};

export default ChatBox;
