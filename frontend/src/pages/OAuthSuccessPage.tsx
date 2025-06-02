import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuthSuccessPage = () => {
	const navigate = useNavigate();

	useEffect(() => {
		const handleLogin = async () => {
			const params = new URLSearchParams(window.location.search);
			const miniToken = params.get("miniToken");

			if (!miniToken) {
				alert("토큰이 없습니다.");
				navigate("/");
				return;
			}

			localStorage.setItem("miniToken", miniToken);

			try {
				const res = await fetch("http://localhost:8080/oauth2/clover/me", {
					headers: {
						Authorization: `Bearer ${miniToken}`,
					},
				});

				const data = await res.json();

				if (data.result?.hasClover) {
					console.log("메인으로 이동")
					navigate("/main");
				} else {
					console.log("클로버 생성창으로 이동")
					navigate("/create-clover");
				}
			} catch (err) {
				alert("클로버 확인 실패");
				navigate("/");
			}
		};

		handleLogin();
	}, [navigate]);

	return <div>로그인 처리 중...</div>;
};

export default OAuthSuccessPage;
