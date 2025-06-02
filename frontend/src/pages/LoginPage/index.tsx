import React from 'react';
import { LoginContainer, GameTitle, KakaoButton } from './styles';

const LoginPage = () => {
	const handleKakaoLogin = () => {
		console.log("버튼 클릭됨");
		window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
	};

	return (
		<LoginContainer>
			<GameTitle src="/game-title.png" alt="Game Title" />
			<KakaoButton onClick={handleKakaoLogin}>
				카카오 로그인
			</KakaoButton>
		</LoginContainer>
	);
};

export default LoginPage;
