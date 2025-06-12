import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import {
	Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer
} from 'recharts';

import { CharacterPersonalInfoResponse } from '../../../types/types';
import DraggableItem from './DraggableItem';
import EquipSlot from './EquipSlot';
import UnequipArea from './UnequipArea';
import { DragLayerProvider } from './DragLayerContext';
import CustomDragPreview from './CustomDragPreview';

type ExtendedCharacter = CharacterPersonalInfoResponse & { characterId: number };

const CharacterDetailPage = () => {
	const { characterName } = useParams();
	const [character, setCharacter] = useState<ExtendedCharacter | null>(null);
	const [equippedItems, setEquippedItems] = useState<any[]>([]);
	const [weapons, setWeapons] = useState<any[]>([]);
	const [armors, setArmors] = useState<any[]>([]);
	const [accessories, setAccessories] = useState<any[]>([]);
	const [hoveredItem, setHoveredItem] = useState<any | null>(null);

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token || !characterName) return;

		fetch('http://localhost:8080/characters', {
			headers: { Authorization: `Bearer ${token}` },
		})
			.then(res => res.json())
			.then(data => {
				const found = data.result.find((c: any) => c.characterName === characterName) as ExtendedCharacter;
				if (found) {
					setCharacter(found);
					refreshEquipped(found.characterId);
				}
			});
	}, [characterName]);

	useEffect(() => {
		if (!character) return;

		fetchEquipableItems('HAND', setWeapons);
		fetchEquipableItems('BODY', setArmors);
		fetchEquipableItems('HEAD', setAccessories);
	}, [character]);



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
	};

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
		if (data.success) {
			const fixed = data.result.content.map((item: any) => ({
				...item,
				slot: item.slot ?? fixSlotFromItemType(item.itemType)
			}));
			setter(fixed);
		}
	};

	const handleEquip = async (item: any) => {
		const token = localStorage.getItem('accessToken');
		if (!token || !character) return;

		await fetch(
			`http://localhost:8080/inventory/inventory-items/${item.inventoryItemId}/equip?characterId=${character.characterId}&slot=${item.slot}`,
			{ method: 'POST', headers: { Authorization: `Bearer ${token}` } }
		);

		refreshEquipped(character.characterId);

		switch (item.slot) {
			case 'HAND':
				fetchEquipableItems('HAND', setWeapons);
				break;
			case 'BODY':
				fetchEquipableItems('BODY', setArmors);
				break;
			case 'HEAD':
				fetchEquipableItems('HEAD', setAccessories);
				break;
		}
	};

	const handleUnequip = async (item: any) => {
		const token = localStorage.getItem('accessToken');
		if (!token || !character) return;

		console.log('🪓 해제 요청:', item);

		await fetch(
			`http://localhost:8080/inventory/inventory-items/${item.inventoryItemId}/unequip`,
			{ method: 'POST', headers: { Authorization: `Bearer ${token}` } }
		);

		refreshEquipped(character.characterId);

		switch (item.slot) {
			case 'HAND':
				fetchEquipableItems('HAND', setWeapons);
				break;
			case 'BODY':
				fetchEquipableItems('BODY', setArmors);
				break;
			case 'HEAD':
				fetchEquipableItems('HEAD', setAccessories);
				break;
		}
	};

	const refreshEquipped = async (characterId: number) => {
		const token = localStorage.getItem('accessToken');
		const res = await fetch(`http://localhost:8080/inventory/inventory-items/equipped?characterId=${characterId}`, {
			headers: { Authorization: `Bearer ${token}` },
		});
		const data = await res.json();
		if (data.success) setEquippedItems(data.result.equippedItems || []);
	};

	if (!character) return <p>캐릭터 정보를 불러오는 중입니다...</p>;

	const statData = [
		{ stat: '행운', value: character.luck },
		{ stat: '힘', value: character.strength },
		{ stat: '민첩', value: character.agility },
		{ stat: '지력', value: character.intelligence },
		{ stat: '집중력', value: character.focus },
	];

	return (
		<DndProvider backend={HTML5Backend}>
			<DragLayerProvider>
				<div style={{ display: 'flex', gap: 24, padding: 24 }}>
					{/* 캐릭터 정보 */}
					<div style={{ width: 240, backgroundColor: '#fff', borderRadius: 8, padding: 16, boxShadow: '0 2px 6px rgba(0,0,0,0.1)' }}>
						<img src="/Character.png" alt="캐릭터" width={100} style={{ marginBottom: 12 }} />
						<h2>{character.characterName}</h2>
						<p>{character.job}</p>
						<p>Lv. {character.level}</p>
						<p>HP: {character.hp} / {character.maxHp}</p>
						<p>MP: {character.mp} / {character.maxMp}</p>
						<h4 style={{ marginTop: 16 }}>스탯</h4>
						<ResponsiveContainer width="100%" height={180}>
							<RadarChart cx="50%" cy="50%" outerRadius="80%" data={statData}>
								<PolarGrid radialLines={false} />
								<PolarAngleAxis dataKey="stat" tick={{ fontSize: 10 }} />
								<PolarRadiusAxis tick={false} axisLine={false} />
								<Radar dataKey="value" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
							</RadarChart>
						</ResponsiveContainer>
					</div>

					{/* 오른쪽 래퍼: 장착 + 보유를 위아래로 */}
					<div style={{ display: 'flex', flexDirection: 'column', gap: 24, flex: 1 }}>
						{/* 장착 슬롯 */}
						<div style={{
							backgroundColor: '#fff',
							borderRadius: 8,
							boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
							padding: 16
						}}>
							<h3 style={{ marginBottom: 12 }}>장착 슬롯</h3>
							<div style={{ display: 'flex', flexDirection: 'row', gap: 16 }}>
							{['HAND', 'BODY', 'HEAD'].map(slot => (
								<EquipSlot
									key={slot}
									slotType={slot}
									onDropItem={handleEquip}
									equippedItem={equippedItems.find(i => i.slot === slot)}
								/>
							))}
							</div>
						</div>

						<CustomDragPreview />
						{/* 보유 장비 */}
						<div style={{ position: 'relative' }}>

							{/* 해제 슬롯 */}
							<UnequipArea onDropUnequip={handleUnequip} />

							{/* 착용 가능 장비 */}
							<div style={{
								backgroundColor: '#fff',
								borderRadius: 8,
								boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
								padding: 16,
								display: 'flex',
								flexDirection: 'column',
								gap: 24,
								flex: 1
								}}>

								<div>
									<h3>착용 가능 장비</h3>
									{[
										['무기', weapons],
										['갑옷', armors],
										['악세사리', accessories],
									].map(([label, items]) => (
										<div key={label as string} style={{ marginBottom: 16 }}>
											<p style={{ fontWeight: 'bold', marginBottom: 4 }}>{label}</p>
											<div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
												{(items as any[]).map(item => (
													<DraggableItem key={item.inventoryItemId} item={item} />
												))}
											</div>
										</div>
									))}
								</div>
							</div>
						</div>
					</div>
				</div>
			</DragLayerProvider>
		</DndProvider>
	);
};

export default CharacterDetailPage;
