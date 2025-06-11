import React, { useEffect, useState } from 'react';

type InventoryItem = {
	inventoryItemId: number;
	itemName: string;
	itemType: string;
	slot: string;
	durability: number;
	imageUrl: string; // 없으면 매핑해서 넣어야 함
};

const InventoryPage = () => {
	const [items, setItems] = useState<InventoryItem[]>([]);
	const [selectedSlot, setSelectedSlot] = useState<string | null>(null);

	useEffect(() => {
		const fetchItems = async () => {
			const token = localStorage.getItem('accessToken');
			if (!token) return;

			const query = new URLSearchParams({
				page: '0',
				size: '50',
				...(selectedSlot ? { slot: selectedSlot } : {}),
			}).toString();

			const res = await fetch(`http://localhost:8080/inventory/inventory-items?${query}`, {
				headers: { Authorization: `Bearer ${token}` },
			});
			const data = await res.json();

			if (data.success) {
				setItems(data.result.content);
			}
		};

		fetchItems();
	}, [selectedSlot]);

	const slotFilter = ['ALL', 'HAND', 'BODY', 'HEAD'];

	return (
		<div style={{ padding: 24 }}>
			<h2>전체 인벤토리</h2>

			<div style={{ marginBottom: 16 }}>
				{slotFilter.map((slot) => (
					<button
						key={slot}
						onClick={() => setSelectedSlot(slot === 'ALL' ? null : slot)}
						style={{
							marginRight: 8,
							padding: '6px 12px',
							border: '1px solid #aaa',
							backgroundColor: selectedSlot === slot ? '#ddd' : '#fff',
						}}
					>
						{slot}
					</button>
				))}
			</div>

			<div
				style={{
					display: 'grid',
					gridTemplateColumns: 'repeat(auto-fill, 64px)',
					gap: 12,
				}}
			>
				{items.map((item) => (
					<div
						key={item.inventoryItemId}
						style={{
							width: 64,
							height: 64,
							border: '1px solid black',
							borderRadius: 8,
							padding: 4,
							backgroundColor: 'white',
							display: 'flex',
							justifyContent: 'center',
							alignItems: 'center',
						}}
					>
						<img
							src={item.imageUrl ?? '/default-item.png'}
							alt={item.itemName}
							style={{ width: '100%', height: '100%', objectFit: 'contain' }}
						/>
					</div>
				))}
			</div>
		</div>
	);
};

export default InventoryPage;
