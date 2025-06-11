import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { miniFetch } from '../util/authFatch';

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

				const res = await miniFetch("http://localhost:8080/oauth2/clover/me");
				const data = await res.json();

				console.log("miniToken:", miniToken);
				console.log("data:", data);

				if (data.result?.hasClover) {
					console.log("실행됨");

					const fullTokenRes = await miniFetch("http://localhost:8080/auth/full-token", {
						method: "POST",
						headers: {
							"Content-Type": "application/json",
						},
						body: JSON.stringify({
							nickname: data.result.nickname,
							cloverName: data.result.cloverName,
						}),
					});


					console.log("nickname:", data.result.nickname);
					console.log("cloverName:", data.result.cloverName);

					const tokenData = await fullTokenRes.json();
					const accessToken = tokenData.result.accessToken;
					const refreshToken = tokenData.result.refreshToken;

					localStorage.setItem("accessToken", accessToken);
					localStorage.setItem("refreshToken", refreshToken);

					console.log("Token:", accessToken);

					console.log("메인으로 이동");
					navigate("/main");
				} else {
					console.log("클로버 생성 창 실행됨");
					console.log("클로버 생성창으로 이동");
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
