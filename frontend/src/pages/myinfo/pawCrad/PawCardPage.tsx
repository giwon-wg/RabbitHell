import React, { useEffect, useState } from "react";
import styled from "styled-components";

// 타입 정의
interface DeckItem {
	deckId: number;
	cloverId: number;
	pawCardId: number;
	cardNumber: number;
	cardEmblem: string;
	ratioPercent: number;
	description: string;
	pawCardSlot: string | null;
	createdAt: string;
	modifiedAt: string;
}

const pawCardSlots = ["PAW_CARD_SLOT1", "PAW_CARD_SLOT2", "PAW_CARD_SLOT3", "PAW_CARD_SLOT4"];

// 스타일 컴포넌트
const Section = styled.div`
	margin-bottom: 20px;
	text-align: center;
`;

const Container = styled.div`
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px;
`;

const Row = styled.div`
	display: flex;
	gap: 12px;
	justify-content: center;
	flex-wrap: wrap;
`;

const Slot = styled.div<{ filled?: boolean }>`
	width: 100px;
	height: 130px;
	background-color: ${(props) => (props.filled ? "#ffe28a" : "white")};
	border: ${(props) => (props.filled ? "2px solid green" : "2px dashed gray")};
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	cursor: ${(props) => (props.filled ? "default" : "pointer")};
`;

const Card = styled.div`
	width: 80px;
	height: 120px;
	background-color: lightblue;
	border: 1px solid #000;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	font-size: 14px;
	cursor: pointer;
`;

const Button = styled.button`
	margin: 20px;
	padding: 10px 20px;
	background-color: #ffd700;
	border: 1px solid #aaa;
	border-radius: 8px;
	font-weight: bold;
	cursor: pointer;
`;

const CardSlotInventory: React.FC = () => {
	const [equipped, setEquipped] = useState<DeckItem[]>([]);
	const [slots, setSlots] = useState<(DeckItem | null)[]>([null, null, null, null]);
	const [inventory, setInventory] = useState<DeckItem[]>([]);

	useEffect(() => {
		const token = localStorage.getItem("accessToken");
		if (!token) return;

		const fetchAll = async () => {
			const res = await fetch("http://localhost:8080/decks?page=1&size=100&sortBy=CREATED_AT&sortDirection=ASC", {
				headers: { Authorization: `Bearer ${token}` },
			});
			const data = await res.json();
			if (data.success) {
				const equippedList: DeckItem[] = [];
				const slotList: (DeckItem | null)[] = new Array(4).fill(null);
				const inventoryList: DeckItem[] = [];

				for (const deck of data.result.content) {
					if (deck.pawCardSlot) {
						equippedList.push(deck);
						const idx = pawCardSlots.indexOf(deck.pawCardSlot);
						if (idx !== -1) slotList[idx] = null; // 비워진 슬롯으로 시작
					} else {
						inventoryList.push(deck);
					}
				}

				setEquipped(equippedList);
				setSlots(slotList);
				setInventory(inventoryList);
			}
		};

		fetchAll();
	}, []);

	const handleCardClick = (card: DeckItem) => {
		const emptyIndex = slots.findIndex(slot => slot === null);
		if (emptyIndex !== -1) {
			setSlots(prev => prev.map((s, i) => (i === emptyIndex ? card : s)));
			setInventory(prev => prev.filter(item => item.deckId !== card.deckId));
		}
	};

	const handleEquip = async () => {
		const token = localStorage.getItem("accessToken");
		if (!token) return;

		const payload = slots
			.map((card, idx) =>
				card ? { deckId: card.deckId, pawCardSlot: pawCardSlots[idx] } : null
			)
			.filter((entry): entry is { deckId: number; pawCardSlot: string } => entry !== null);

		await fetch("http://localhost:8080/decks/assign-slots", {
			method: "POST",
			headers: {
				Authorization: `Bearer ${token}`,
				"Content-Type": "application/json",
			},
			body: JSON.stringify({ activePawCardRequestList: payload }),
		});

		await fetch("http://localhost:8080/decks/calculate-effect", {
			method: "POST",
			headers: {
				Authorization: `Bearer ${token}`,
				"Content-Type": "application/json",
			},
		});

		console.log("장착 및 효과 계산 완료");

	};

	return (
		<Container>
			<Section>
				<h3>장착 중인 카드</h3>
				<Row>
					{equipped.map((card) => (
						<Slot key={card.deckId} filled>
							<div>{card.description}</div>
							<div>{card.cardEmblem} {card.cardNumber}</div>
						</Slot>
					))}
				</Row>
			</Section>

			<Section>
				<h3>슬롯</h3>
				<Row>
					{slots.map((card, index) => (
						<Slot key={index}>
							{card ? (
								<>
									<div>{card.description}</div>
									<div>{card.cardEmblem} {card.cardNumber}</div>
								</>
							) : (
								"Empty"
							)}
						</Slot>
					))}
				</Row>
				<Button onClick={handleEquip}>장착</Button>
			</Section>

			<Section>
				<h3>인벤토리</h3>
				<Row>
					{inventory.map((card) => (
						<Card key={card.deckId} onClick={() => handleCardClick(card)}>
							<div>{card.description}</div>
							<div>{card.cardEmblem} {card.cardNumber}</div>
						</Card>
					))}
				</Row>
			</Section>
		</Container>
	);
};

export default CardSlotInventory;
