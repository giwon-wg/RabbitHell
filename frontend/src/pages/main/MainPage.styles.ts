import styled from 'styled-components';

export const MainContainer = styled.div`
	background-image: url('/main.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	width: 100%;
	height: 100dvh;
	display: flex;
	justify-content: flex-start;
	align-items: flex-start;


	padding: 130px 0 0 80px; // 상단 80px, 좌측 80px만 여백
	color: #f8e8d8;
	font-family: 'Pretendard', sans-serif;
	overflow: hidden;
	position: relative;
`;

export const ContentBox = styled.div`
	background-color: rgba(0, 0, 0, 0.5);
	padding: 2px 32px;
	border-radius: 16px;
	box-shadow: 0 0 12px #000;
	text-align: center;
	max-width: 300px;
	width: fit-content;
`;

export const CardColumn = styled.div`
	display: flex;
	flex-direction: column;
	gap: 16px; // 카드와 퀘스트 사이 간격
`;

export const QuestBox = styled.div`
	background-color: rgba(0, 0, 0, 0.6);
	color: #fff;
	padding: 12px 20px;
	border-radius: 12px;
	font-size: 14px;
	box-shadow: 2px 2px 6px #000;
	width: fit-content;
`;

export const MyInfoBox = styled.div`
	background-color: rgba(0, 0, 0, 0.6);
	color: #fff;
	padding: 12px 20px;
	border-radius: 12px;
	font-size: 14px;
	box-shadow: 2px 2px 6px #000;
	width: fit-content;
`;

export const Title = styled.h1`
	font-size: 50px;
	margin-bottom: 32px;
	color: #ffcc66;
`;

export const text1 = styled.h1`
	font-size: 25px;
	margin-bottom: 32px;
	color: #ffcc66;
`;

export const StatText = styled.p`
	font-size: 16px;
	margin: 8px 0;
`;

export const GameButton = styled.button`
	background-color: #3c2c21;
	color: #f9d977;
	border: none;
	padding: 12px 24px;
	font-size: 14px;
	cursor: pointer;
	border-radius: 8px;
	font-family: 'Press Start 2P', cursive;
	box-shadow: 2px 2px 0 #000;
	transition: transform 0.1s;

	&:hover {
		transform: scale(1.05);
	}
`;

export const ErrorText = styled.p`
	color: #ff6666;
	font-size: 16px;
	background-color: rgba(0, 0, 0, 0.6);
	padding: 12px 20px;
	border-radius: 12px;
`;
