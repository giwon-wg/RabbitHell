import styled from 'styled-components';

export const Background = styled.div`
	background-image: url('/game-title.png'); // 실제 경로에 맞게 수정
	background-size: cover;
	background-position: center;
	width: 100vw;
	height: 100vh;
	position: relative;
`;

export const Overlay = styled.div`
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0);
	display: flex;
	justify-content: center;
	align-items: center;
`;

export const LoginContainer = styled.div`
	position: absolute;
	left: 6%; // ⭐ 왼쪽 여백
	bottom: 70%; // ⭐ 아래 여백
	text-align: center;
`;

export const GameTitle = styled.h1`
	font-size: 64px;
	color: #ffcc66;
	text-shadow: 3px 3px 0 #000;
	font-family: 'Press Start 2P', cursive;
	margin-bottom: 32px;
`;

export const KakaoButton = styled.button`
	background-color: rgba(0, 0, 0, 0.3);
	color: #f9d977;
	border: none;
	padding: 16px 40px;
	font-size: 42px;
	font-family: 'Press Start 2P', cursive;
	cursor: pointer;
	border-radius: 64px;
	transition: transform 0.1s;

	&:hover {
		transform: scale(1.05);
	}
`;
