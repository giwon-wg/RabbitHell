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

		const response = await fetch("http://localhost:8080/auth/full-token", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Authorization": `Bearer ${miniToken}`
			},
			body: JSON.stringify({
				cloverName
			})
		});

		const data = await response.json();

		if (response.ok) {
			localStorage.setItem("accessToken", data.result.accessToken);
			localStorage.setItem("refreshToken", data.result.refreshToken);
			alert("클로버 생성 완료!");
			navigate("/character/create");
		} else {
			console.error("응답 내용:", data);
			alert("클로버 생성 실패: " + data.message);
		}
	};

	if (loading) return <div>로딩 중...</div>;

	return (
		<div
			style={{
				display: 'flex',
				flexDirection: 'column',
				justifyContent: 'center',
				alignItems: 'center',
				height: '100vh',
				backgroundColor: '#f9f9f9',
				padding: '0 16px',
			}}
		>
			<h1 style={{ marginBottom: '24px', fontSize: '28px', color: '#333' }}>
				클로버 생성
			</h1>

			<form
				onSubmit={handleSubmit}
				style={{
					display: 'flex',
					flexDirection: 'column',
					alignItems: 'center',
					backgroundColor: 'white',
					padding: '32px',
					borderRadius: '16px',
					boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
					width: '100%',
					maxWidth: '400px',
				}}
			>
				<input
					placeholder="클로버 이름"
					value={cloverName}
					onChange={(e) => setCloverName(e.target.value)}
					style={{
						width: '100%',
						padding: '12px 16px',
						fontSize: '16px',
						border: '1px solid #ccc',
						borderRadius: '8px',
						outline: 'none',
						boxShadow: 'inset 0 1px 3px rgba(0,0,0,0.1)',
						marginBottom: '24px',
						transition: 'border-color 0.3s ease',
					}}
					onFocus={(e) => (e.currentTarget.style.borderColor = '#4CAF50')}
					onBlur={(e) => (e.currentTarget.style.borderColor = '#ccc')}
				/>

				<button
					type="submit"
					style={{
						width: '100%',
						padding: '12px 24px',
						backgroundColor: '#4CAF50',
						color: 'white',
						border: 'none',
						borderRadius: '8px',
						cursor: 'pointer',
						fontSize: '16px',
						fontWeight: 'bold',
						boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
						transition: 'background-color 0.3s ease',
					}}
					onMouseEnter={(e) =>
						(e.currentTarget.style.backgroundColor = '#45a049')
					}
					onMouseLeave={(e) =>
						(e.currentTarget.style.backgroundColor = '#4CAF50')
					}
				>
					클로버 생성
				</button>
			</form>
		</div>
	);
};

export default CloverCreatePage;
