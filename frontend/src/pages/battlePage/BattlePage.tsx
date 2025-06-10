import React, {useEffect, useState} from 'react';

type BattleField = {
	code: string;
	name: string;
};

const BattlePage = () => {
	const [battleFields, setBattleFields] = useState<BattleField[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		const token = localStorage.getItem("accessToken");

		fetch('http://localhost:8080/battles', {
			headers: {
				Authorization: `Bearer ${token}`,
			},
		})
			.then((res) => res.json())
			.then((data) => {
				setBattleFields(data.result.unlockedRareMaps);
				setLoading(false);
			})
			.catch(() => {
				alert("전투 필드를 불러오는 데 실패했습니다.");
				setLoading(false);
			});
	}, []);

	const startBattle = (fieldCode: string) => {
		const token = localStorage.getItem("accessToken");

		fetch('http://localhost:8080/battles/start', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify({
				battleFieldType: fieldCode,
			}),
		})
			.then((res) => {
				if (!res.ok) throw new Error("전투 시작 실패");
				return res.json();
			})
			.then((data) => {
				alert(`전투 완료: ${data.message}`);
			})
			.catch((err) => {
				alert(err.message);
			});
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
