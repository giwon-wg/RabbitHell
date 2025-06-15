import React, { useState } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

const jobOptions = [
	{ name: '전사 1차', value: 'WARRIOR_TIER1', image: '/images/warrior.png' },
	{ name: '전사 2차', value: 'WARRIOR_TIER2', image: '/images/warrior.png' },
	{ name: '전사 3차', value: 'WARRIOR_TIER3', image: '/images/warrior.png' },
	{ name: '전사 4차', value: 'WARRIOR_TIER4', image: '/images/warrior.png' },

	{ name: '마법사 1차', value: 'WIZARD_TIER1', image: '/images/mage.png' },
	{ name: '마법사 2차', value: 'WIZARD_TIER2', image: '/images/mage.png' },
	{ name: '마법사 3차', value: 'WIZARD_TIER3', image: '/images/mage.png' },
	{ name: '마법사 4차', value: 'WIZARD_TIER4', image: '/images/mage.png' },

	{ name: '도적 1차', value: 'THIEF_TIER1', image: '/images/rogue.png' },
	{ name: '도적 2차', value: 'THIEF_TIER2', image: '/images/rogue.png' },
	{ name: '도적 3차', value: 'THIEF_TIER3', image: '/images/rogue.png' },
	{ name: '도적 4차', value: 'THIEF_TIER4', image: '/images/rogue.png' },


	{ name: '궁수 1차', value: 'ARCHER_TIER1', image: '/images/archer.png' },
	{ name: '궁수 2차', value: 'ARCHER_TIER2', image: '/images/archer.png' },
	{ name: '궁수 3차', value: 'ARCHER_TIER3', image: '/images/archer.png' },
	{ name: '궁수 4차', value: 'ARCHER_TIER4', image: '/images/archer.png' },
];

const JobSelectPage: React.FC = () => {
	const navigate = useNavigate();
	const { characterName } = useParams();
	const location = useLocation();
	const characterId: number | undefined = location.state?.characterId;

	const [selectedJob, setSelectedJob] = useState<string | null>(null);

	const handleChangeJob = async () => {
		if (!selectedJob || characterId == null) {
			alert('캐릭터 정보가 없습니다.');
			return;
		}

		try {
			const token = localStorage.getItem('accessToken');
			const res = await fetch(`http://localhost:8080/characters/${characterId}/job`, {
				method: 'PATCH',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${token}`,
				},
				body: JSON.stringify({ job: selectedJob }),
			});

			const data = await res.json();
			if (data.success) {
				alert('전직 성공!');
				navigate(`/me/${characterName}`);
			} else {
				alert('전직 실패: ' + data.message);
			}
		} catch (err) {
			alert('전직 중 오류 발생');
		}
	};

	return (
		<div style={{ padding: 24 }}>
			<h2 style={{ textAlign: 'center', marginBottom: 24 }}>전직</h2>

			<div style={{ display: 'flex', justifyContent: 'space-between', gap: 16 }}>
				{jobOptions.map((job) => (
					<div
						key={job.value}
						onClick={() => setSelectedJob(job.value)}
						style={{
							border: selectedJob === job.value ? '2px solid #4a90e2' : '1px solid #ccc',
							borderRadius: 8,
							padding: 12,
							textAlign: 'center',
							cursor: 'pointer',
							backgroundColor: selectedJob === job.value ? '#eef6ff' : '#fff',
							flex: 1,
						}}
					>
						<img
							src={job.image}
							alt={job.name}
							style={{ width: 80, height: 80, objectFit: 'cover', marginBottom: 8 }}
						/>
						<div style={{ fontWeight: 'bold' }}>{job.name}</div>
					</div>
				))}
			</div>

			<div style={{ textAlign: 'center', marginTop: 32 }}>
				<button
					onClick={handleChangeJob}
					disabled={!selectedJob}
					style={{
						padding: '12px 24px',
						fontSize: 16,
						borderRadius: 8,
						border: 'none',
						backgroundColor: selectedJob ? '#4a90e2' : '#ccc',
						color: '#fff',
						cursor: selectedJob ? 'pointer' : 'not-allowed',
					}}
				>
					전직하기
				</button>
			</div>
		</div>
	);
};

export default JobSelectPage;
