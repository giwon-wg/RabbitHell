import React, { useEffect, useState } from 'react';

interface Clover {
	cloverId: number;
	name: string;
	cash: number;
	saving: number;
}

const BankPage = () => {
	const [clover, setClover] = useState<Clover | null>(null);
	const [amount, setAmount] = useState(0);
	const [error, setError] = useState<string | null>(null);

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

	const handleBankAction = async (type: 'deposit' | 'withdraw', value: number) => {
		if (!clover || value <= 0) return;

		if (type === 'deposit' && value > clover.cash) {
			setError('소지금이 부족합니다.');
			return;
		}
		if (type === 'withdraw' && value > clover.saving) {
			setError('은행 잔고가 부족합니다.');
			return;
		}

		setError(null);

		const token = localStorage.getItem('accessToken');
		const endpoint = type === 'deposit' ? '/villages/banks/save' : '/villages/banks/withdraw';

		try {
			const res = await fetch(`http://localhost:8080${endpoint}`, {
				method: 'PATCH',
				headers: {
					Authorization: `Bearer ${token}`,
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ money: value }),
			});

			const data = await res.json();
			console.log('[서버 응답]', data);

			if (data.result) {
				setClover(data.result);
				alert(data.message || '처리 완료');
			} else {
				alert(data.message || '알 수 없는 오류');
			}
			setAmount(0);
		} catch (e) {
			console.error(e);
			alert('요청 중 오류가 발생했습니다.');
		}
	};

	if (!clover || typeof clover.cash !== 'number') return <p>로딩 중...</p>;

	return (
		<div style={{ maxWidth: 480, margin: '2rem auto', padding: '2rem', border: '1px solid #ccc', borderRadius: 8 }}>
			<h2 style={{ textAlign: 'center' }}>은행</h2>
			<p style={{ textAlign: 'center', color: '#888' }}>대륙 제일 은행</p>

			<div style={{ margin: '1rem 0', background: '#000000', padding: '1rem', borderRadius: 4 }}>
				<div style={{ display: 'flex', justifyContent: 'space-between' }}>
					<span>소지금:</span>
					<strong>{clover.cash.toLocaleString()} Gold</strong>
				</div>
				<div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 8 }}>
					<span>은행 잔고:</span>
					<strong>{clover.saving.toLocaleString()} Gold</strong>
				</div>
			</div>

			{error && (
				<div style={{ background: '#fdd', color: '#900', padding: '0.5rem', marginBottom: '1rem', borderRadius: 4 }}>
					{error}
				</div>
			)}

			<input
				type="number"
				value={amount === 0 ? '' : amount}
				onChange={e => setAmount(Number(e.target.value))}
				placeholder="금액 입력"
				style={{ width: '100%', padding: '0.5rem', marginBottom: '1rem' }}
			/>

			<div style={{ display: 'flex', justifyContent: 'space-between', gap: '0.5rem' }}>
				<button onClick={() => handleBankAction('deposit', amount)} style={{ flex: 1 }}>
					입금
				</button>
				<button onClick={() => handleBankAction('withdraw', amount)} style={{ flex: 1 }}>
					출금
				</button>
			</div>

			<div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '0.5rem', gap: '0.5rem' }}>
				<button onClick={() => handleBankAction('deposit', clover.cash)} style={{ flex: 1 }}>
					전액 입금
				</button>
				<button onClick={() => handleBankAction('withdraw', clover.saving)} style={{ flex: 1 }}>
					전액 출금
				</button>
			</div>
		</div>
	);
};

export default BankPage;
