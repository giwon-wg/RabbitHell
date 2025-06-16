import React, { useState } from 'react';

const HospitalPage = () => {
	const [message, setMessage] = useState('');

	const cureCharacter = async () => {
		const token = localStorage.getItem('accessToken');
		try {
			const res = await fetch('http://localhost:8080/villages/hospitals/cure', {
				method: 'PATCH',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${token}`,
				},
			});

			const data = await res.json();

			if (res.ok) {
				setMessage(`🩺 치료 성공! HP: ${data.result.recoveredHp}, MP: ${data.result.recoveredMp}`);
			} else {
				setMessage(`❌ 치료 실패: ${data.message}`);
			}
		} catch (err) {
			setMessage('❌ 서버 오류 발생');
			console.error(err);
		}
	};

	return (
		<div style={styles.container}>
			<div style={styles.card}>
				<h1 style={styles.title}>Doctor. Won</h1>

				<img
					src="/assets/npc/doctor.png"
					alt="의사 이미지"
					style={styles.image}
				/>

				<p style={styles.description}>
					전적으로 저를 믿으셔야 합니다.
				</p>
				<button style={styles.button} onClick={cureCharacter}>
					치료받기
				</button>
				{message && <p style={styles.message}>{message}</p>}
			</div>
		</div>
	);
};

const styles = {
	container: {
		display: 'flex',
		justifyContent: 'center',
		alignItems: 'center',
		height: '100vh',
		// background: 'linear-gradient(to bottom, #000000, #1a237e)',
	},
	card: {
		backgroundColor: '#100702',
		padding: '40px',
		borderRadius: '16px',
		boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
		width: '400px',
		textAlign: 'center' as const,
	},
	title: {
		fontSize: '28px',
		marginBottom: '12px',
		color: '#ffffff',
	},
	image: {
		width: '100%',
		height: 'auto',
		borderRadius: '12px',
		marginBottom: '20px',
	},
	description: {
		fontSize: '16px',
		color: '#555',
		marginBottom: '30px',
	},
	button: {
		backgroundColor: '#ea493b',
		color: '#fff',
		padding: '12px 24px',
		fontSize: '18px',
		border: 'none',
		borderRadius: '8px',
		cursor: 'pointer',
		transition: 'background 0.3s',
	},
	message: {
		marginTop: '20px',
		fontSize: '16px',
		color: '#333',
	},
};

export default HospitalPage;
