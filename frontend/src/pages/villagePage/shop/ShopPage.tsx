import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getShopItems } from './shopApi';
import ShopItemCard from './ShopItemCard';

const ShopPage = () => {
	const { villageId } = useParams();
	const [items, setItems] = useState<any[]>([]);
	const shopId = parseInt(villageId ?? '0', 10);

	useEffect(() => {
		if (!shopId) return;
		getShopItems(shopId).then(setItems).catch(console.error);
	}, [shopId]);

	return (
		<div>
			<h1>상점</h1>
			<div style={{ display: 'flex', flexWrap: 'wrap' }}>
				{items.map((item) => (
					<ShopItemCard key={item.itemId} item={item} />
				))}
			</div>
		</div>
	);
};

export default ShopPage;
