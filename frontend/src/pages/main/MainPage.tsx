import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CloverResponse, CharacterPersonalInfoResponse } from '../../types/types';
import { sendQuitAndDisconnect } from '../../util/socketUtils';
import { stompClient } from '../../util/stompRef';

import {
	MainContainer,
	ContentBox,
	Title,
	StatText,
	GameButton,
	ErrorText, CardColumn, QuestBox, MyInfoBox,
} from './MainPage.styles';

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
	if (loading) return <MainContainer><Title>로딩 중...</Title></MainContainer>;
	if (error) return <MainContainer><ErrorText>⚠️ {error}</ErrorText></MainContainer>;

	return (
		<MainContainer>
			<CardColumn>
			<ContentBox>
				<Title>
					{clover && (
						<>
							{clover.name}
						</>
					)}
				</Title>
			</ContentBox>
				<MyInfoBox>
				{clover && (
					<>
						<StatText>⚡ 스태미나: {clover.stamina}</StatText>
					</>
				)}
				</MyInfoBox>
			<QuestBox>
				<text>
					일일 퀘스트
				</text>
			</QuestBox>
			</CardColumn>
		</MainContainer>
	);
};

export default MainPage;
