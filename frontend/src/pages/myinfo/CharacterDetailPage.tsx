import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CharacterPersonalInfoResponse } from '../../types/types';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer } from 'recharts';

const CharacterDetailPage = () => {
	const { characterName } = useParams();
	const navigate = useNavigate();
	const [character, setCharacter] = useState<CharacterPersonalInfoResponse | null>(null);
	const [loading, setLoading] = useState(true);
	const [selectedItem, setSelectedItem] = useState<any | null>(null);

	const sectionStyle: React.CSSProperties = {
		backgroundColor: '#fff',
		border: '1px solid #ddd',
		borderRadius: 12,
		boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
		padding: 16,
	};
	const [equippedItems, setEquippedItems] = useState<any[]>([]);


	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token || !characterName) return;

		fetch('http://localhost:8080/characters', {
			headers: { Authorization: `Bearer ${token}` },
		})
			.then(res => res.json())
			.then(data => {
				const found = data.result.find(
					(c: CharacterPersonalInfoResponse) => c.characterName === characterName
				);
				setCharacter(found || null);
				if (found) {
					fetch(`http://localhost:8080/inventory/equipped?characterId=${found.characterId}`, {
						headers: { Authorization: `Bearer ${token}` },
					})
						.then(res => res.json())
						.then(data => {
							if (data.success) {
								setEquippedItems(data.result.equippedItems || []);
							}
						})
						.catch(err => {
							console.error('장착 아이템 불러오기 실패', err);
						});
				}
			})
			.catch(err => {
				console.error('캐릭터 불러오기 실패', err);
			})
			.finally(() => setLoading(false));
	}, [characterName]);

	if (loading) return <p>로딩 중...</p>;
	if (!character) return <p>캐릭터를 찾을 수 없습니다</p>;

	const statData = [
		{ stat: '힘', value: character.strength },
		{ stat: '민첩', value: character.agility },
		{ stat: '지력', value: character.intelligence },
		{ stat: '집중력', value: character.focus },
		{ stat: '행운', value: character.luck },
	];

	return (
		<div style={{ display: 'flex', padding: 24, gap: 16 }}>
			{/* 왼쪽: 기본 정보 */}
			<div style={{ width: '25%', ...sectionStyle }}>
				<img src="/Character.png" alt="bunny" width={100} />
				<h2>{character.characterName}</h2>
				<p>{character.job}</p>
				<p>Lv. {character.level}</p>
				<p>HP: {character.hp} / {character.maxHp}</p>
				<p>MP: {character.mp} / {character.maxMp}</p>
				<h4>스탯</h4>
				<ResponsiveContainer width="100%" height={200}>
					<RadarChart cx="50%" cy="50%" outerRadius="80%" data={statData}>
						<PolarGrid />
						<PolarAngleAxis dataKey="stat" />
						<PolarRadiusAxis angle={30} domain={[0, 100]} />
						<Radar name="스탯" dataKey="value" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
					</RadarChart>
				</ResponsiveContainer>
			</div>

			{/* 중간: 성장/장비 */}
			<div style={{ width: '30%', ...sectionStyle }}>
				<h3>장착 아이템</h3>
				<ul>
					{equippedItems.length > 0 ? (
						equippedItems.map(item => (
							<li key={item.slot}>
								{item.slot}: {item.itemId !== null ? `아이템 ID ${item.itemId} (내구도: ${item.durability})` : '없음'}
							</li>
						))
					) : (
						<li>장착 아이템 없음</li>
					)}
				</ul>

				<div style={{ marginTop: 16 }}>
					<h4>성장치</h4>
					<div style={{ background: '#eee', height: 10, width: '100%', marginBottom: 8 }}>
						<div style={{ width: '80%', height: '100%', background: '#4caf50' }} />
					</div>

					<h4>속도</h4>
					<div style={{ background: '#eee', height: 10, width: '100%', marginBottom: 8 }}>
						<div style={{ width: `${character.agility}%`, height: '100%', background: '#03a9f4' }} />
					</div>

					<h4>직업 숙련도</h4>
					<div style={{ background: '#eee', height: 10, width: '100%', marginBottom: 8 }}>
						<div style={{ width: `${Math.min(character.skillPoint, 100)}%`, height: '100%', background: '#ff9800' }} />
					</div>
				</div>

				<button style={{ marginTop: 12 }} onClick={() => alert('강화!')}>강화</button>
			</div>

			{/* 오른쪽: 인벤토리 */}
			<div style={{ flex: 1, ...sectionStyle }}>
				<h3>착용가능 장비 (무기)</h3>
				<div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
					{Array(8).fill(null).map((_, i) => (
						<img
							key={i}
							src="/assets/item.png"
							alt="item"
							style={{ width: 48, height: 48, cursor: 'pointer' }}
							onClick={() => setSelectedItem({ name: '전사의 방패', desc: '방어력 +10', price: 500 })}
						/>
					))}
				</div>
				{selectedItem && (
					<div style={{ border: '1px solid #ccc', padding: 12, marginTop: 12 }}>
						<h4>{selectedItem.name}</h4>
						<p>{selectedItem.desc}</p>
						<p>{selectedItem.price} 골드</p>
						<button onClick={() => alert('장착!')}>장착</button>
					</div>
				)}
			</div>
		</div>
	);
};

export default CharacterDetailPage;
