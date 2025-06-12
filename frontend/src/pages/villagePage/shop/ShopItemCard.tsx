const ShopItemCard = ({ item }: { item: any }) => {
	const handleBuy = async () => {
		const token = localStorage.getItem('accessToken');
		await fetch(`http://localhost:8080/shop/buy`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${token}`,
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ itemId: item.itemId }),
		});
		alert(`${item.itemName}을(를) 구매했습니다.`);
	};

	return (
		<div style={{ border: '1px solid gray', margin: 10, padding: 10, width: 220 }}>
			<h3>{item.itemName}</h3>
			<p>{item.description}</p>
			<p>등급: {item.rarity}</p>
			<p>공격력: {item.minPower} ~ {item.maxPower}</p>
			<p>무게: {item.minWeight} ~ {item.maxWeight}</p>
			<p>내구도: {item.maxDurability}</p>
			<p>가격: {item.price}G</p>
			<button onClick={handleBuy}>구매</button>
		</div>
	);
};

export default ShopItemCard;
