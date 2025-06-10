import React, {useEffect, useState} from 'react';

type BattleField = {
	code: string;
	name: string;
};

const BattlePage = () => {
	const [battleFields, setBattleFields] = useState<BattleField[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		loadBattleFields();
	}, []);

	const loadBattleFields = async () => {
		try {
			const token = localStorage.getItem("accessToken");
			const res = await fetch('http://localhost:8080/battles', {
				headers: {Authorization: `Bearer ${token}`},
			});
			const data = await res.json();
			setBattleFields(data.result.unlockedRareMaps);
		} catch {
			alert("전투 필드를 불러오는 데 실패했습니다.");
		} finally {
			setLoading(false);
		}
	};

	const startBattle = async (fieldCode: string) => {
		try {
			const token = localStorage.getItem("accessToken");
			const res = await fetch('http://localhost:8080/battles', {
				method: 'PATCH',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${token}`,
				},
				body: JSON.stringify({battleFieldType: fieldCode}),
			});
			const data = await res.json();
			alert(`전투 시작: ${data.message}`);
		} catch (err: any) {
			alert(err.message);
		}
	};

	return (
		<div style={{padding: '24px'}}>
			<h1>전투 페이지</h1>
			{loading ? (
				<p>로딩 중...</p>
			) : (
				<>
					<p>전투 가능한 필드 목록:</p>
					<div style={{display: 'flex', gap: '8px', flexWrap: 'wrap'}}>
						{battleFields.map((field) => (
							<button key={field.code} onClick={() => startBattle(field.code)}>
								{field.name}
							</button>
						))}
					</div>
				</>
			)}
		</div>
	);
};

export default BattlePage;
