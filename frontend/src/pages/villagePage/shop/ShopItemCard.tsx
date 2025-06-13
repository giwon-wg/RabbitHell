// ShopItemCard.tsx

import React, {useState} from 'react';

const ShopItemCard = ({item, onBuy}: {
	item: any,
	onBuy?: (updatedBalance: { cash: number, saving: number }) => void
}) => {
	const [isHovered, setIsHovered] = useState(false);

	const handleBuy = async () => {
		const token = localStorage.getItem('accessToken');
		const shopId = item.shopId;
		const itemId = item.itemId;
		const quantity = 1;

		try {
			const res = await fetch(`http://localhost:8080/villages/shops/${shopId}/items/${itemId}/buy?quantity=${quantity}`, {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${token}`,
				},
			});

			if (!res.ok) {
				const errText = await res.text();
				throw new Error(errText || `구매에 실패했습니다.`);
			}

			const json = await res.json();
			alert(`'${item.itemName}' 구매 완료!`);

			if (onBuy) {
				// 서버 응답에 cash와 saving이 모두 포함되어 있다고 가정하고 객체로 전달합니다.
				// onBuy(json.result) 와 같이 서버 응답 구조에 따라 더 간단하게 작성할 수도 있습니다.
				onBuy({cash: json.result.cash, saving: json.result.saving});
			}
		} catch (error) {
			console.error("Purchase error:", error);
			alert(`구매 실패: ${error instanceof Error ? error.message : String(error)}`);
		}
	};

	const rarityColor: { [key: string]: string } = {
		'COMMON': '#A0A0A0',
		'UNCOMMON': '#2ECC71',
		'RARE': '#3498DB',
		'EPIC': '#9B59B6',
		'LEGENDARY': '#F1C40F',
	};

	const styles = {
		card: {
			width: '280px',
			backgroundColor: '#ffffff',
			borderRadius: '12px',
			boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
			overflow: 'hidden',
			display: 'flex',
			flexDirection: 'column' as 'column',
			justifyContent: 'space-between',
			transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
			transform: isHovered ? 'translateY(-5px)' : 'translateY(0)',
		},
		content: {
			padding: '1.5rem',
		},
		itemName: {
			margin: '0 0 0.5rem 0',
			color: '#2d3748',
			fontSize: '1.25rem',
		},
		rarity: {
			display: 'inline-block',
			padding: '0.25rem 0.75rem',
			backgroundColor: rarityColor[item.rarity] || '#cccccc',
			color: '#ffffff',
			borderRadius: '12px',
			fontSize: '0.75rem',
			fontWeight: 'bold',
			marginBottom: '1rem',
		},
		description: {
			fontSize: '0.9rem',
			color: '#718096',
			marginBottom: '1rem',
			minHeight: '3.2em', // 2-3줄 높이 확보
		},
		stats: {
			fontSize: '0.85rem',
			color: '#4a5568',
			listStyle: 'none',
			padding: 0,
			margin: 0,
		},
		statItem: {
			display: 'flex',
			justifyContent: 'space-between',
			padding: '0.25rem 0',
		},
		footer: {
			backgroundColor: '#f7fafc',
			padding: '1rem 1.5rem',
			borderTop: '1px solid #e2e8f0',
			display: 'flex',
			justifyContent: 'space-between',
			alignItems: 'center',
		},
		price: {
			margin: 0,
			fontSize: '1.2rem',
			fontWeight: 'bold',
			color: '#1a202c',
		},
		buyButton: {
			backgroundColor: '#3182ce',
			color: 'white',
			border: 'none',
			borderRadius: '8px',
			padding: '0.75rem 1.5rem',
			fontSize: '1rem',
			fontWeight: 'bold',
			cursor: 'pointer',
			transition: 'background-color 0.2s',
		},
	};

	return (
		<div
			style={styles.card}
			onMouseEnter={() => setIsHovered(true)}
			onMouseLeave={() => setIsHovered(false)}
		>
			<div style={styles.content}>
				<h3 style={styles.itemName}>{item.itemName}</h3>
				<span style={styles.rarity}>{item.rarity}</span>
				<p style={styles.description}>{item.description}</p>
				<ul style={styles.stats}>
					<li style={styles.statItem}><span>공격력</span> <span>{item.minPower} ~ {item.maxPower}</span></li>
					<li style={styles.statItem}><span>무게</span> <span>{item.minWeight} ~ {item.maxWeight}</span></li>
					<li style={styles.statItem}><span>내구도</span> <span>{item.maxDurability}</span></li>
				</ul>
			</div>
			<div style={styles.footer}>
				<p style={styles.price}>{item.price.toLocaleString()}G</p>
				<button
					style={styles.buyButton}
					onClick={handleBuy}
					onMouseOver={e => (e.currentTarget.style.backgroundColor = '#2b6cb0')}
					onMouseOut={e => (e.currentTarget.style.backgroundColor = '#3182ce')}
				>
					구매
				</button>
			</div>
		</div>
	);
};

export default ShopItemCard;
