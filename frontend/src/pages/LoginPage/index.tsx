import React, { useState } from 'react';
import {
	LoginContainer,
	GameTitle,
	KakaoButton,
	LoginForm,
	Input,
	SubmitButton
} from './styles';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const navigate = useNavigate();

	const handleEmailLogin = async (e: React.FormEvent) => {
		e.preventDefault();

		try {
			const res = await fetch('http://localhost:8080/auth/login', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ email, password }),
			});

			const data = await res.json();

			if (res.ok) {
				localStorage.setItem('accessToken', data.result.accessToken);
				localStorage.setItem('refreshToken', data.result.refreshToken);
				alert('로그인 성공!');
				navigate('/main');
			} else {
				alert(`로그인 실패: ${data.message || '알 수 없는 오류'}`);
			}
		} catch (err) {
			console.error('로그인 중 오류:', err);
			alert('서버 오류 또는 네트워크 문제');
		}
	};

	const handleKakaoLogin = () => {
		window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
	};

	return (
		<LoginContainer>
			<GameTitle src="/game-title.png" alt="Game Title" />

			<LoginForm onSubmit={handleEmailLogin}>
				<Input
					type="text"
					placeholder="이메일"
					value={email}
					onChange={(e) => setEmail(e.target.value)}
				/>
				<Input
					type="password"
					placeholder="비밀번호"
					value={password}
					onChange={(e) => setPassword(e.target.value)}
				/>
				<SubmitButton type="submit">로그인</SubmitButton>
			</LoginForm>

			<KakaoButton onClick={handleKakaoLogin}>
				카카오 로그인
			</KakaoButton>
		</LoginContainer>
	);
};

export default LoginPage;




// import React from 'react';
// import { LoginContainer, GameTitle, KakaoButton } from './styles';
//
// const LoginPage = () => {
// 	const handleKakaoLogin = () => {
// 		console.log("버튼 클릭됨");
// 		window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
// 	};
//
// 	return (
// 		<LoginContainer>
// 			<GameTitle src="/game-title.png" alt="Game Title" />
// 			<KakaoButton onClick={handleKakaoLogin}>
// 				카카오 로그인
// 			</KakaoButton>
// 		</LoginContainer>
// 	);
// };
//
// export default LoginPage;
