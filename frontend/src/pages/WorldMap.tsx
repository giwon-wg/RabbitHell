import React, { useEffect, useRef, useState } from 'react';

const villages = [
	{
		name: '롭이어 마을',
		img: '/assets/lopeared.png',
		xRatio: 0.255,
		yRatio: 0.36,
		route: '/village/lopeared',
		villageId: 1
	},
	{
		name: '앙고라 마을',
		img: '/assets/angora.png',
		xRatio: 0.565,
		yRatio: 0.32,
		route: '/village/angora',
		villageId: 2
	},
	{
		name: '드워프 마을',
		img: '/assets/Dwarf.png',
		xRatio: 0.53,
		yRatio: 0.72,
		route: '/village/dwarf',
		villageId: 3
	},
];

const moveVillage = async (targetVillageId: number) => {

	try {
		const accessToken = localStorage.getItem('accessToken');
		if (!accessToken) {
			alert('로그인이 필요합니다.');
			return;
		}

		const response = await fetch('http://localhost:8080/villages/move', {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${accessToken}`,
			},
			body: JSON.stringify({ targetVillageId }),
		});

		if (response.ok) {
			alert('이동 성공!');
			// window.location.href = `/village/${targetVillageId}`;
		} else {
			const data = await response.json();
			alert(`이동 실패: ${data.message || response.statusText}`);
		}
	} catch (error) {
		console.error('API 호출 실패:', error);
		alert('서버 요청 실패');
	}
};

const WorldMap = () => {
	const mapRef = useRef<HTMLImageElement | null>(null);
	const containerRef = useRef<HTMLDivElement | null>(null);
	const [mapSize, setMapSize] = useState({ width: 0, height: 0 });

	const updateMapSize = () => {
		if (mapRef.current) {
			const { width, height } = mapRef.current.getBoundingClientRect();
			setMapSize({ width, height });
		}
	};

	useEffect(() => {
		window.addEventListener('resize', updateMapSize);
		return () => window.removeEventListener('resize', updateMapSize);
	}, []);

	const handleMove = (route: string) => {
		window.location.href = route;
	};

	// 지도 기준 1500px일 때 기준 크기
	const scale = mapSize.width / 1500;

	return (
		<div
			ref={containerRef}
			style={{
				position: 'relative',
				width: '100%',
				maxWidth: '1500px',
				margin: '0 auto',
			}}
		>
			<img
				ref={mapRef}
				src="/assets/map.png"
				alt="월드맵"
				style={{
					width: '100%',
					height: 'auto',
					display: 'block',
				}}
				onLoad={updateMapSize}
			/>

			{mapSize.width > 0 &&
				villages.map((village, i) => (
					<div
						key={i}
						style={{
							position: 'absolute',
							top: `${village.yRatio * mapSize.height}px`,
							left: `${village.xRatio * mapSize.width}px`,
							transform: 'translate(-50%, -50%)',
							cursor: 'pointer',
							textAlign: 'center',
							zIndex: 10,
						}}
						onClick={() => moveVillage(village.villageId)}
					>
						<img
							src={village.img}
							alt={village.name}
							style={{
								width: `${60 * scale}px`,
								transition: 'width 0.2s ease',
							}}
						/>
						<div
							style={{
								background: 'rgba(255,255,255,0.85)',
								borderRadius: `${6 * scale}px`,
								padding: `${4 * scale}px ${8 * scale}px`,
								marginTop: `${4 * scale}px`,
								fontSize: `${14 * scale}px`,
								whiteSpace: 'nowrap',
								boxShadow: '0 0 4px rgba(0,0,0,0.1)',
							}}
						>
							{village.name}
						</div>
					</div>
				))}
		</div>
	);
};

export default WorldMap;
