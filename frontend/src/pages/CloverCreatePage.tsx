import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const CloverCreatePage = () => {
	const [nickname, setNickname] = useState('');
	const [cloverName, setCloverName] = useState('');
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate();

	useEffect(() => {
		const miniToken = localStorage.getItem("miniToken");
		if (!miniToken) {
			navigate("/");
			return;
		}

		fetch("http://localhost:8080/oauth2/clover/me", {
			headers: {
				Authorization: `Bearer ${miniToken}`
			}
		})
			.then((res) => res.json())
			.then((data) => {
				if (data.data?.hasClover) {
					navigate("/main");
				} else {
					setLoading(false)
				}
			})
			.catch(() => {
				alert("클로버 확인 실패");
				navigate("/");
			});
	}, [navigate]);

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();

		const miniToken = localStorage.getItem("miniToken");
		if (!miniToken) {
			alert("토큰이 없습니다.");
			return;
		}

		const response = await fetch("http://localhost:8080/auth/token/full", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Authorization": `Bearer ${miniToken}`
			},
			body: JSON.stringify({
				nickname,
				cloverName
			})
		});

		const data = await response.json();

		if (response.ok) {
			localStorage.setItem("accessToken", data.result.accessToken);
			localStorage.setItem("refreshToken", data.result.refreshToken);
			alert("클로버 생성 완료!");
			navigate("/main");
		} else {
			console.error("응답 내용:", data);
			alert("클로버 생성 실패: " + data.message);
		}
	};

	if (loading) return <div>로딩 중...</div>;

	return (
		<div>
			<h1>클로버 생성</h1>
			<form onSubmit={handleSubmit}>
				<input
					placeholder="닉네임"
					value={nickname}
					onChange={(e) => setNickname(e.target.value)}
				/>
				<input
					placeholder="클로버 이름"
					value={cloverName}
					onChange={(e) => setCloverName(e.target.value)}
				/>
				<button type="submit">클로버 생성</button>
			</form>
		</div>
	);
};

export default CloverCreatePage;
