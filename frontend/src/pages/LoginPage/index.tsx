import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
	LoginContainer,
	Background,
	Overlay,
	GameTitle,
	KakaoButton
} from './styles';

const LoginPage = () => {
	const handleKakaoLogin = () => {
		window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
	};

	return (
		<Background>
			<Overlay>
				<LoginContainer>
					<KakaoButton onClick={handleKakaoLogin}>KAKAO LOGIN</KakaoButton>
				</LoginContainer>
			</Overlay>
		</Background>
	);
};

export default LoginPage;
