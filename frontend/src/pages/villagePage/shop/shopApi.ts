export const getShopItems = async (shopId: number, page = 0, size = 10) => {
	const token = localStorage.getItem('accessToken');
	const res = await fetch(`http://localhost:8080/shops/${shopId}/items?page=${page}&size=${size}`, {
		headers: {
			Authorization: `Bearer ${token}`,
		},
	});

	if (!res.ok) {
		const text = await res.text();
		throw new Error(`상점 아이템 조회 실패: ${res.status} - ${text}`);
	}

	const json = await res.json();
	return json.result?.content || [];
};
