import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CharacterCreatePage = () => {
	const navigate = useNavigate();
	const [nationId, setNationId] = useState('');
	const [characters, setCharacters] = useState(['', '', '', '']);

	const handleChange = (index: number, value: string) => {
		const updated = [...characters];
		updated[index] = value;
		setCharacters(updated);
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		const accessToken = localStorage.getItem('accessToken');
		if (!accessToken) {
			alert('토큰이 없습니다.');
			return;
		}

		for (let i = 0; i < characters.length; i++) {
			const characterName = characters[i].trim();
			if (!characterName) continue;

			const res = await fetch('http://localhost:8080/characters', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${accessToken}`,
				},
				body: JSON.stringify({
					name: characterName
				}),
			});

			if (!res.ok) {
				const error = await res.json();
				alert(`캐릭터 ${i + 1} 생성 실패: ${error.message}`);
				return;
			}
		}

		alert('캐릭터 4명 생성 완료!');
		navigate('/main');
	};

	return (
		<div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', paddingTop: '60px' }}>
			<h1>캐릭터 생성</h1>

			<form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px', width: '300px' }}>

				{characters.map((name, idx) => (
					<input
						key={idx}
						type="text"
						placeholder={`캐릭터 ${idx + 1} 이름`}
						value={name}
						onChange={(e) => handleChange(idx, e.target.value)}
						required
					/>
				))}

				<button type="submit">생성하기</button>
			</form>
		</div>
	);
};

export default CharacterCreatePage;
