import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CloverResponse, CharacterPersonalInfoResponse } from '../../types/types';
import { sendQuitAndDisconnect } from '../../util/socketUtils';
import { stompClient } from '../../util/stompRef';

const ROOM_ID = '1'; // 추후 동적으로 바꿀 수 있음

const MainPage = () => {
	const navigate = useNavigate();
	const [clover, setClover] = useState<CloverResponse | null>(null);
	const [characters, setCharacters] = useState<CharacterPersonalInfoResponse[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token) {
			setError('로그인 정보가 없습니다.');
			setLoading(false);
			return;
		}

		const fetchClover = fetch('http://localhost:8080/clover/me', {
			headers: { Authorization: `Bearer ${token}` },
		}).then(res => res.json());

		const fetchCharacters = fetch('http://localhost:8080/characters', {
			headers: { Authorization: `Bearer ${token}` },
		}).then(res => res.json());

		Promise.all([fetchClover, fetchCharacters])
			.then(([cloverRes, characterRes]) => {
				if (cloverRes.result) setClover(cloverRes.result);
				if (Array.isArray(characterRes.result)) setCharacters(characterRes.result);
			})
			.catch(() => {
				setError('데이터를 불러오는 데 실패했습니다.');
			})
			.finally(() => setLoading(false));
	}, []);

	const handleLogout = () => {
		if (stompClient.current?.connected) {
			sendQuitAndDisconnect(stompClient); // ✅ 퇴장 메시지 없이 disconnect만 -> chatting 관련 웹소켓 종료
		}
		localStorage.removeItem('accessToken');
		navigate('/');
	};


	if (loading) return <p>로딩 중...</p>;
	if (error) return <p style={{ color: 'red' }}>⚠️ {error}</p>;

	return (
		<div style={{ padding: '24px' }}>
			<h1>메인 페이지</h1>
			{clover && (
				<div>
					<p>클로버 이름: <strong>{clover.name}</strong></p>
					<p>스태미나: {clover.stamina}</p>
				</div>
			)}

			<button onClick={() => navigate('/me')} style={{ marginRight: '12px' }}>
				내 정보 보기
			</button>
			<button onClick={handleLogout}>
				로그아웃
			</button>
		</div>
	);
};

export default MainPage;
