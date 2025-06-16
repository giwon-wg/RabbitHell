import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface VillageInfo {
	name: string;
	img: string;
}

const VillageMap: Record<number, VillageInfo> = {
	1: { name: '롭이어 마을', img: '/assets/lopeared.png' },
	2: { name: '앙고라 마을', img: '/assets/angora.png' },
	3: { name: '드워프 마을', img: '/assets/Dwarf.png' },
	4: { name: '중립 마을', img: '/assets/Neutral.png' },
};

interface Clover {
	cloverId: number;
	name: string;
	currentVillageId: number;
}

const actions = [
	{ label: '상점', route: (id: number) => `/village/shop/${id}` },
	{ label: '은행', route: () => '/village/bank' },
	// { label: '사냥터', route: () => '/battlefield' },
	{ label: '병원', route: () => '/hospital' },
	// { label: '창고', route: () => '/storage' },
	// { label: '투자', route: () => '/invest' },
];

const VillagePage = () => {
	const [clover, setClover] = useState<Clover | null>(null);
	const navigate = useNavigate();

	useEffect(() => {
		const token = localStorage.getItem('accessToken');
		if (!token) return;

		fetch('http://localhost:8080/clover/me', {
			headers: { Authorization: `Bearer ${token}` },
		})
			.then(res => res.json())
			.then(data => setClover(data.result))
			.catch(console.error);
	}, []);

	if (!clover) return <p>로딩 중...</p>;

	const villageInfo = VillageMap[clover.currentVillageId];
	if (!villageInfo) return <p>존재하지 않는 마을입니다.</p>;

	return (
		<div style={{ textAlign: 'center', padding: '2rem' }}>
			<h1>{villageInfo.name}</h1>
			<img src={villageInfo.img} alt={villageInfo.name} style={{ width: '300px', marginBottom: '1rem' }} />

			<div
				style={{
					display: 'flex',
					flexDirection: 'column',
					gap: '1rem',
					alignItems: 'center',
					marginTop: '2rem',
				}}
			>
				{actions.map(action => (
					<button
						key={action.label}
						onClick={() => navigate(action.route(clover.currentVillageId))}
						style={{
							width: '300px',
							padding: '1rem',
							fontSize: '1.1rem',
							borderRadius: '10px',
							border: '1px solid #ccc',
							background: '#000000',
							boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
							cursor: 'pointer',
						}}
					>
						{action.label}
					</button>
				))}
			</div>
		</div>
	);
};

export default VillagePage;
