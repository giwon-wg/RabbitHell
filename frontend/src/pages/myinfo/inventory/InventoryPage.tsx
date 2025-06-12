import React, { useEffect, useState } from 'react';
import { rarityBorderColors, typeGroups, mapSlotToFolder } from '../../../util/itemUtils';


type InventoryItem = {
	inventoryItemId: number;
	itemId: number;
	itemName: string;
	itemType: string;
	slot: string | null;
	durability: number;
	maxDurability: number;
	power: number;
	weight: number;
	rarity: string;
};

const InventoryPage = () => {
	const [items, setItems] = useState<InventoryItem[]>([]);
	const [selectedGroup, setSelectedGroup] = useState<'ALL' | '무기' | '방어구' | '악세사리' | '소모품' | '기타'>('ALL');
	const [hoveredItemKey, setHoveredItemKey] = useState<string | null>(null);

	useEffect(() => {
		const fetchItems = async () => {
			const token = localStorage.getItem('accessToken');
			if (!token) return;

			const query = new URLSearchParams({ page: '0', size: '200' }).toString();
			const res = await fetch(`http://localhost:8080/inventory/inventory-items?${query}`, {
				headers: { Authorization: `Bearer ${token}` },
			});
			const data = await res.json();

			if (data.success) {
				setItems(data.result.content);
			}
		};

		fetchItems();
	}, []);

	const groupFilters: typeof selectedGroup[] = ['ALL', '무기', '방어구', '악세사리', '소모품', '기타'];

	const filteredItems = items; // 전체 다 보여주되 스타일로 회색 처리

	return (
		<div style={{
			maxWidth: 960,
			margin: '0 auto',
			padding: 24,
			border: '1px solid #ddd',
			borderRadius: 16,
			backgroundColor: '#fff',
			boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
		}}>
			<h2 style={{ marginBottom: 16 }}>전체 인벤토리</h2>

			{/* 필터 버튼 */}
			<div style={{ marginBottom: 20 }}>
				{groupFilters.map((group) => (
					<button
						key={group}
						onClick={() => setSelectedGroup(group)}
						style={{
							marginRight: 8,
							padding: '6px 12px',
							border: '1px solid #aaa',
							backgroundColor: selectedGroup === group ? '#ddd' : '#fff',
							borderRadius: 6,
							cursor: 'pointer',
						}}
					>
						{group}
					</button>
				))}
			</div>

			{/* 아이템 목록 */}
			<div
				style={{
					display: 'grid',
					gridTemplateColumns: 'repeat(auto-fill, 80px)',
					gap: 20,
				}}
			>
				{filteredItems.map((item) => {
					const key = `inv-${item.inventoryItemId}`;
					const borderColor = rarityBorderColors[item.rarity] ?? '#ccc';
					const folder = mapSlotToFolder(item.itemType);
					const imageUrl = `/assets/items/${folder}/${item.itemId}.png`;

					const isDimmed = selectedGroup !== 'ALL' && typeGroups[item.itemType] !== selectedGroup;

					return (
						<div
							key={key}
							onMouseEnter={() => setHoveredItemKey(key)}
							onMouseLeave={() => setHoveredItemKey(null)}
							style={{
								width: 80,
								height: 80,
								border: `2px solid ${borderColor}`,
								borderRadius: 12,
								backgroundColor: 'white',
								padding: 6,
								position: 'relative',
								boxShadow: hoveredItemKey === key
									? '0 4px 10px rgba(0,0,0,0.2)'
									: '0 1px 3px rgba(0,0,0,0.1)',
								opacity: isDimmed ? 0.3 : 1,
								filter: isDimmed ? 'grayscale(100%)' : 'none',
								cursor: isDimmed ? 'default' : 'pointer',
								pointerEvents: isDimmed ? 'none' : 'auto',
								transition: 'all 0.2s ease',
							}}
						>
							<img
								src={imageUrl}
								alt={item.itemName}
								onError={(e) => {
									e.currentTarget.src = '/default-item.png';
								}}
								style={{
									width: '100%',
									height: '100%',
									objectFit: 'contain',
								}}
							/>

							{hoveredItemKey === key && (
								<div
									style={{
										position: 'absolute',
										top: '80%',
										left: '180%',
										transform: 'translateX(-50%)',
										marginTop: 6,
										backgroundColor: '#fff',
										border: '1px solid #ccc',
										borderRadius: 8,
										boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
										padding: 8,
										zIndex: 100,
										width: 180,
										textAlign: 'left',
										fontSize: 13,
									}}
								>
									<strong>{item.itemName}</strong>
									<p>타입: {item.itemType}</p>
									<p>내구도: {item.durability} / {item.maxDurability}</p>
									<p>공격력: {item.power}</p>
									<p>무게: {item.weight}</p>
									<p>등급: {item.rarity}</p>
								</div>
							)}
						</div>
					);
				})}
			</div>
		</div>
	);
};

export default InventoryPage;
