import React from 'react';

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
			alert(`치료 성공! HP: ${data.result.recoveredHp}, MP: ${data.result.recoveredMp}`);
		} else {
			alert(`치료 실패: ${data.message}`);
		}
	} catch (err) {
		alert('서버 오류 발생');
		console.error(err);
	}
};

const HospitalPage = () => {
	const handleCure = () => {
		cureCharacter(); // 위 함수 호출
	};

	return (
		<div>
			<h1>병원에 오신 것을 환영합니다</h1>
			<button onClick={handleCure}>치료받기</button>
		</div>
	);
};

export default HospitalPage;
