import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CloverResponse, CharacterPersonalInfoResponse } from '../../types/types';
import { Container, Section, CharacterList } from './MyInfoPage.styles';
import CloverCard from './CloverCard';
import CharacterCard from './CharacterCard';

const MyInfoPage = () => {
	const [clover, setClover] = useState<CloverResponse | null>(null);
	const [characters, setCharacters] = useState<CharacterPersonalInfoResponse[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);
	const navigate = useNavigate();

	useEffect(() => {
		const accessToken = localStorage.getItem('accessToken');
		if (!accessToken) {
			setError('로그인 정보가 없습니다.');
			setLoading(false);
			return;
		}

		const fetchClover = fetch('http://localhost:8080/clover/me', {
			headers: { Authorization: `Bearer ${accessToken}` },
		}).then(res => res.json());

		const fetchCharacters = fetch('http://localhost:8080/characters', {
			headers: { Authorization: `Bearer ${accessToken}` },
		}).then(res => res.json());

		Promise.all([fetchClover, fetchCharacters])
			.then(([cloverRes, charRes]) => {
				if (cloverRes.result) setClover(cloverRes.result);
				if (Array.isArray(charRes.result)) setCharacters(charRes.result);
			})
			.catch(err => {
				console.error('데이터 불러오기 실패:', err);
				setError('유저 정보를 불러오는 데 실패했습니다.');
			})
			.finally(() => setLoading(false));
	}, []);

	if (loading) return <p>로딩 중...</p>;
	if (error) return <p style={{ color: 'red' }}>⚠️ {error}</p>;

	return (
		<Container>
			<h1>내 정보</h1>

			{clover && (
				<Section>
					<CloverCard clover={clover} />
				</Section>
			)}

			<Section>
				<h2>내 캐릭터 목록</h2>
				<CharacterList>
					{characters.map(char => (
						<CharacterCard
							key={char.characterName}
							character={char}
							onClick={() => navigate(`/me/${char.characterName}`)}
						/>
					))}
				</CharacterList>
			</Section>
		</Container>
	);
};

export default MyInfoPage;
