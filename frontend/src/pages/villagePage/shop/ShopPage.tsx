// ShopPage.tsx

import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {getMyCash, getMySaving, getShopItems} from './shopApi';
import ShopItemCard from './ShopItemCard';

const ShopPage = () => {
	const {villageId} = useParams();
	const [items, setItems] = useState<any[]>([]);
	const [cash, setCash] = useState<number>(0);
	const [saving, setSaving] = useState<number>(0);
	const shopId = parseInt(villageId ?? '0', 10);

	useEffect(() => {
		if (!shopId) return;
		getShopItems(shopId).then(setItems).catch(console.error);
		getMyCash().then(setCash).catch(console.error);
		;
		getMySaving().then(setSaving).catch(console.error);
	}, [shopId]);

	// cash와 saving을 포함한 객체를 받아 두 상태를 모두 업데이트합니다.
	const handlePurchaseSuccess = (updatedBalance: { cash: number; saving: number }) => {
		setCash(updatedBalance.cash);
		setSaving(updatedBalance.saving);
	};

	const styles = {
		page: {
			fontFamily: "'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif",
			padding: '2rem',
		},
		header: {
			maxWidth: '1200px',
			margin: '0 auto',
			paddingBottom: '1rem',
			borderBottom: '1px solid #e0e0e0',
		},
		title: {
			color: '#1a202c',
			fontSize: '2.5rem',
			marginBottom: '0.5rem',
		},
		cashDisplay: {
			color: '#4a5568',
			fontSize: '1.25rem',
			fontWeight: '600',
		},
		itemGrid: {
			display: 'flex',
			flexWrap: 'wrap' as 'wrap',
			gap: '1.5rem',
			maxWidth: '1200px',
			margin: '2rem auto',
			justifyContent: 'center',
		},
	};

	return (
		<div style={styles.page}>
			<header style={styles.header}>
				<h1 style={styles.title}>상점</h1>
				<p style={styles.cashDisplay}>소지금: {cash.toLocaleString()}G</p>
				<p style={styles.cashDisplay}>저축금: {saving.toLocaleString()}G</p>
			</header>
			<main style={styles.itemGrid}>
				{items.map((item) => (
					<ShopItemCard
						key={item.itemId}
						item={item}
						onBuy={handlePurchaseSuccess}
					/>
				))}
			</main>
		</div>
	);
};

export default ShopPage;
