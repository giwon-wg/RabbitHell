import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CharacterPersonalInfoResponse } from '../../types/types';
import {
	Radar, RadarChart, PolarGrid, PolarAngleAxis,
	PolarRadiusAxis, ResponsiveContainer
} from 'recharts';

const CharacterDetailPage = () => {
	const { characterName } = useParams();
	const navigate = useNavigate();
	const [character, setCharacter] = useState<CharacterPersonalInfoResponse | null>(null);
	const [loading, setLoading] = useState(true);
	const [hoveredItemKey, setHoveredItemKey] = useState<string | null>(null);
	const [equippedItems, setEquippedItems] = useState<any[]>([]);

	// 슬롯별 상태
	const [weapons, setWeapons] = useState<any[]>([]);
	const [armors, setArmors] = useState<any[]>([]);
	const [accessories, setAccessories] = useState<any[]>([]);

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

	const fixSlotFromItemType = (itemType: string): string => {
		switch (itemType) {
			case 'SWORD':
			case 'BOW':
			case 'DAGGER':
			case 'WAND':
				return 'HAND';
			case 'ARMOR':
			case 'SHIELD':
				return 'BODY';
			case 'ACCESSORY':
				return 'HEAD';
			default:
				return 'ETC';
		}
	}

	const fetchEquipableItems = async (
		slot: 'HAND' | 'BODY' | 'HEAD',
		setter: React.Dispatch<React.SetStateAction<any[]>>
	) => {
		const token = localStorage.getItem('accessToken');
		const res = await fetch(
			`http://localhost:8080/inventory/inventory-items/equipable?page=0&size=10&slot=${slot}`,
			{ headers: { Authorization: `Bearer ${token}` } }
		);
		const data = await res.json();
		if (data.success) setter(data.result.content);
	};

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token || !characterName) return;

		// 캐릭터 정보 + 장착 아이템
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

				// 슬롯별 장착 가능 아이템 fetch
				fetchEquipableItems('HAND', setWeapons);
				fetchEquipableItems('BODY', setArmors);
				fetchEquipableItems('HEAD', setAccessories);
			})
			.finally(() => setLoading(false));
	}, [characterName]);

	if (loading) return <p>로딩 중...</p>;
	if (!character) return <p>캐릭터를 찾을 수 없습니다</p>;

	const statData = [
		{ stat: '행운', value: character.luck },
		{ stat: '힘', value: character.strength },
		{ stat: '민첩', value: character.agility },
		{ stat: '지력', value: character.intelligence },
		{ stat: '집중력', value: character.focus },
	];

	const handleUnequip = async (inventoryItemId: number) => {
		const token = localStorage.getItem("accessToken");
		if (!token || !characterName) {
			alert("로그인이 필요합니다.");
			return;
		}

		try {
			// 장착 해제 요청
			const response = await fetch(`http://localhost:8080/inventory/inventory-items/${inventoryItemId}/unequip`, {
				method: "POST",
				headers: {
					Authorization: `Bearer ${token}`,
				},
			});
			if (!response.ok) throw new Error("해제 실패");

			setHoveredItemKey(null);

			// 최신 캐릭터 정보 다시 조회
			const characterRes = await fetch(`http://localhost:8080/characters`, {
				headers: { Authorization: `Bearer ${token}` },
			});
			const characterData = await characterRes.json();
			const found = characterData.result.find((c: CharacterPersonalInfoResponse) => c.characterName === characterName);

			if (found) {
				setCharacter(found);

				// 장착 아이템 목록 다시 불러오기
				const equippedRes = await fetch(
					`http://localhost:8080/inventory/inventory-items/equipped?characterId=${(found as any).characterId}`,
					{
						headers: { Authorization: `Bearer ${token}` },
					}
				);
				const equippedData = await equippedRes.json();
				if (equippedData.success) {
					setEquippedItems(equippedData.result.equippedItems || []);
					await fetchEquipableItems('HAND', setWeapons);
					await fetchEquipableItems('BODY', setArmors);
					await fetchEquipableItems('HEAD', setAccessories);
				}
			}
		} catch (error) {
			console.error(error);
			alert("해제 중 오류 발생");
		}
	};


	const handleEquip = async (inventoryItemId: number, slot: string) => {
		const token = localStorage.getItem("accessToken");
		if (!token || !character) return;

		try {
			const characterId = (character as any).characterId;

			const response = await fetch(
				`http://localhost:8080/inventory/inventory-items/${inventoryItemId}/equip?characterId=${characterId}&slot=${slot}`,
				{
					method: "POST",
					headers: {
						Authorization: `Bearer ${token}`,
					},
				}
			);

			if (!response.ok) throw new Error("장착 실패");

			setHoveredItemKey(null);

			const res = await fetch(
				`http://localhost:8080/inventory/inventory-items/equipped?characterId=${characterId}`,
				{
					headers: { Authorization: `Bearer ${token}` },
				}
			);
			const data = await res.json();
			if (data.success) {
				setEquippedItems(data.result.equippedItems || []);
				await fetchEquipableItems('HAND', setWeapons);
				await fetchEquipableItems('BODY', setArmors);
				await fetchEquipableItems('HEAD', setAccessories);
			}
		} catch (err) {
			alert("장착 중 오류 발생");
		}
	};

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
				<div style={{
					display: 'flex',
					flexDirection: 'row',
					alignItems: 'center',
					justifyContent: 'space-between',
					flexWrap: 'wrap',
					gap: 12,
				}}>
					<div style={{ minWidth: 120, transform: 'translateY(-25px)' }}>
						{statData.map(({ stat, value }) => (
							<div key={stat} style={{ fontSize: 14 }}>
								<strong>{stat}</strong>: {value}
							</div>
						))}
					</div>
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
			<div style={{ flex: 1, ...sectionStyle }}>
				<h3>착용 가능 장비</h3>
				{[['무기', weapons, 'weapon'], ['갑옷', armors, 'armor'], ['악세사리', accessories, 'earring']].map(([label, items]) => (
					<div key={label as string} style={{ marginBottom: 16 }}>
						<h4>{label}</h4>
						<div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
							{(items as any[]).map((item, i) => {
								const slot = fixSlotFromItemType(item.itemType); // ✅ 타입 기반으로 슬롯 보정
								const key = `inv-${item.inventoryItemId}`;
								const folder = getFolderBySlot(slot); // ✅ 슬롯에서 폴더 추출

								return (
									<div key={item.inventoryItemId}
										 onMouseEnter={() => setHoveredItemKey(key)}
										 onMouseLeave={() => setHoveredItemKey(null)}
										 style={{ position: 'relative' }}>
										<img
											src={`/assets/items/${folder}/${item.itemId}.png`}
											alt={item.itemName}
											style={itemImageStyle}
										/>
										{hoveredItemKey === key && (
											<div style={{
												position: 'absolute', top: '100%', left: '50%',
												transform: 'translateX(-50%)',
												marginTop: -15,
												marginLeft: 100,
												backgroundColor: '#fff', border: '1px solid #ccc',
												borderRadius: 8, boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
												padding: 12, zIndex: 20, width: 180, textAlign: 'left'
											}}>
												<strong>{item.itemName}</strong>
												<p>설명: {item.description}</p>
												<p>슬롯: {getSlotLabel(slot)}</p>
												<p>내구도: {item.durability}</p>
												<button onClick={() => handleEquip(item.inventoryItemId, slot)}>장착</button>
											</div>
										)}
									</div>
								);
							})}
						</div>
					</div>
				))}
			</div>

			{/* 착용 가능 장비 */}
			<div style={{ flex: 1, ...sectionStyle }}>
				<h3>착용 가능 장비</h3>
				{[['무기', weapons, 'weapon'], ['갑옷', armors, 'armor'], ['악세사리', accessories, 'earring']].map(([label, items, folder]) => (
					<div key={label as string} style={{ marginBottom: 16 }}>
						<h4>{label}</h4>
						<div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
							{(items as any[]).map((item, i) => {
								const key = `inv-${item.inventoryItemId}`;
								return (
									<div key={item.inventoryItemId}
										 onMouseEnter={() => setHoveredItemKey(key)}
										 onMouseLeave={() => setHoveredItemKey(null)}
										 style={{ position: 'relative' }}>
										<img
											src={`/assets/items/${getFolderBySlot(fixSlotFromItemType(item.itemType))}/${item.itemId}.png`}
											alt={item.itemName}
											style={itemImageStyle}
										/>
										{hoveredItemKey === key && (
											<div style={{
												position: 'absolute', top: '100%', left: '50%',
												transform: 'translateX(-50%)',
												marginTop: -15,
												marginLeft: 100,
												backgroundColor: '#fff', border: '1px solid #ccc',
												borderRadius: 8, boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
												padding: 12, zIndex: 20, width: 180, textAlign: 'left'
											}}>
												<strong>{item.itemName}</strong>
												<p>설명: {item.description}</p>
												<p>슬롯: {getSlotLabel(item.slot)}</p>
												<p>내구도: {item.durability}</p>
												<button onClick={() => handleEquip(item.inventoryItemId, item.slot)}>장착</button>
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
