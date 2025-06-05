import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CharacterPersonalInfoResponse } from '../../types/types';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer } from 'recharts';

const CharacterDetailPage = () => {
	const { characterName } = useParams();
	const navigate = useNavigate();
	const [character, setCharacter] = useState<CharacterPersonalInfoResponse | null>(null);
	const [loading, setLoading] = useState(true);
	const [inventoryItems, setInventoryItems] = useState<any[]>([]);
	const [hoveredItemKey, setHoveredItemKey] = useState<string | null>(null);
	const [equippedItems, setEquippedItems] = useState<any[]>([]);

	const sectionStyle: React.CSSProperties = {
		backgroundColor: '#fff',
		border: '1px solid #ddd',
		borderRadius: 12,
		boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
		padding: 16,
	};

	const itemImageStyle: React.CSSProperties = {
		width: 48,
		height: 48,
		cursor: 'pointer',
		border: '2px solid #888',
		borderRadius: 8,
		boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
	};

	const getSlotLabel = (slot: string): string => {
		switch (slot) {
			case 'HAND': return '무기';
			case 'BODY': return '갑옷';
			case 'HEAD': return '악세사리';
			default: return '기타';
		}
	};

	const getFolderBySlot = (slot: string): string => {
		switch (slot) {
			case 'HAND': return 'weapon';
			case 'BODY': return 'armor';
			case 'HEAD': return 'earring';
			default: return 'etc';
		}
	};

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token || !characterName) return;

		fetch('http://localhost:8080/characters', {
			headers: { Authorization: `Bearer ${token}` },
		})
			.then(res => res.json())
			.then(data => {
				const found = data.result.find((c: CharacterPersonalInfoResponse) => c.characterName === characterName);
				setCharacter(found || null);
				if (found) {
					fetch(`http://localhost:8080/inventory/inventory-items/equipped?characterId=${found.characterId}`, {
						headers: { Authorization: `Bearer ${token}` },
					})
						.then(res => res.json())
						.then(data => {
							if (data.success) setEquippedItems(data.result.equippedItems || []);
						});
				}
				fetch('http://localhost:8080/inventory/inventory-items?page=0&size=30', {
					headers: { Authorization: `Bearer ${token}` },
				})
					.then(res => res.json())
					.then(data => {
						if (data.success) setInventoryItems(data.result.content);
					});
			})
			.finally(() => setLoading(false));
	}, [characterName]);

	const weapons = inventoryItems.filter(item => item.slot === 'HAND');
	const armors = inventoryItems.filter(item => item.slot === 'BODY');
	const accessories = inventoryItems.filter(item => item.slot === 'HEAD');

	if (loading) return <p>로딩 중...</p>;
	if (!character) return <p>캐릭터를 찾을 수 없습니다</p>;

	const statData = [
		{ stat: '행운', value: character.luck },
		{ stat: '힘', value: character.strength },
		{ stat: '민첩', value: character.agility },
		{ stat: '지력', value: character.intelligence },
		{ stat: '집중력', value: character.focus },
	];

	return (
		<div style={{ display: 'flex', padding: 24, gap: 16 }}>
			{/* 기본 정보 */}
			<div style={{ width: '25%', ...sectionStyle }}>
				<img src="/Character.png" alt="bunny" width={100} />
				<h2>{character.characterName}</h2>
				<p>{character.job}</p>
				<p>Lv. {character.level}</p>
				<p>HP: {character.hp} / {character.maxHp}</p>
				<p>MP: {character.mp} / {character.maxMp}</p>
				<h4>스탯</h4>
				<div
					style={{
						display: 'flex',
						flexDirection: 'row',
						alignItems: 'center',
						justifyContent: 'space-between',
						flexWrap: 'wrap', // 반응형 처리
						gap: 12,
					}}
				>
					{/* 왼쪽: 스탯 텍스트 */}
					<div style={{ minWidth: 120, transform: 'translateY(-25px)' }}>
						{statData.map(({ stat, value }) => (
							<div key={stat} style={{ fontSize: 14 }}>
								<strong>{stat}</strong>: {value}
							</div>
						))}
					</div>

					{/* 오른쪽: 차트 */}
					<div style={{ flexShrink: 0, marginLeft: 'auto', marginRight: 'auto' }}>
						<ResponsiveContainer width={160} height={160}>
							<RadarChart cx="50%" cy="50%" outerRadius="80%" data={statData}>
								<PolarGrid radialLines={false} />
								<PolarAngleAxis
									dataKey="stat"
									tick={({ payload, x, y }) => (
										<text x={x} y={y} textAnchor="middle" fontSize={10} fill="#999">
											{payload.value}
										</text>
									)}
								/>
								<PolarRadiusAxis tick={false} axisLine={false} />
								<Radar dataKey="value" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
							</RadarChart>
						</ResponsiveContainer>
					</div>
				</div>
			</div>

			{/* 장착 아이템 */}
			<div style={{ width: '30%', ...sectionStyle }}>
				<h3>장착 아이템</h3>
				<ul style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '16px', listStyle: 'none', padding: 0 }}>
					{equippedItems.map(item => {
						const key = `${item.itemId}-${item.slot}`;
						return (
							<li
								key={item.slot}
								onMouseEnter={() => setHoveredItemKey(key)}
								onMouseLeave={() => setHoveredItemKey(null)}
								style={{ position: 'relative', border: '1px solid #ccc', borderRadius: '10px', padding: '12px', textAlign: 'center', backgroundColor: '#f9f9f9' }}>
								{hoveredItemKey === key && (
									<div style={{ position: 'absolute', top: '100%', left: '50%', transform: 'translateX(-50%)', marginTop: 8, backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: 8, boxShadow: '0 2px 6px rgba(0,0,0,0.2)', padding: 12, zIndex: 10, width: 200, textAlign: 'left' }}>
										<strong>{item.itemName}</strong>
										<p>슬롯: {getSlotLabel(item.slot)}</p>
										<p>내구도: {item.durability}</p>
										<button onClick={() => alert("장착!")}>장착</button>
									</div>
								)}
								<img src={`/assets/items/${getFolderBySlot(item.slot)}/${item.itemId}.png`} alt={item.itemName} style={{ width: 64, height: 64, objectFit: 'cover', marginBottom: 8 }} />
								<div>{item.itemName}</div>
								<div>내구도: {item.durability}</div>
							</li>
						);
					})}
				</ul>
			</div>

			{/* 착용 가능 장비 */}
			<div style={{ flex: 1, ...sectionStyle }}>
				<h3>착용 가능 장비</h3>
				{[['무기', weapons, 'weapon'], ['갑옷', armors, 'armor'], ['악세사리', accessories, 'earring']].map(([label, items, folder]) => (
					<div key={label as string} style={{ marginBottom: 16 }}>
						<h4>{label}</h4>
						<div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
							{(items as any[]).map((item, i) => {
								const key = `${item.itemId}-${item.slot}`;
								return (
									<div
										key={`${folder}-${item.itemId}-${i}`}
										onMouseEnter={() => setHoveredItemKey(key)}
										onMouseLeave={() => setHoveredItemKey(null)}
										style={{ position: 'relative' }}>
										<img src={`/assets/items/${folder}/${item.itemId}.png`} alt={item.itemName} style={itemImageStyle} />
										{hoveredItemKey === key && (
											<div style={{ position: 'absolute', top: '100%', left: '50%', transform: 'translateX(-50%)', marginTop: 8, backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: 8, boxShadow: '0 2px 6px rgba(0,0,0,0.2)', padding: 12, zIndex: 20, width: 180, textAlign: 'left' }}>
												<strong>{item.itemName}</strong>
												<p>설명: {item.description}</p>
												<p>슬롯: {getSlotLabel(item.slot)}</p>
												<p>내구도: {item.durability}</p>
											</div>
										)}
									</div>
								);
							})}
						</div>
					</div>
				))}
			</div>
		</div>
	);
};

export default CharacterDetailPage;
