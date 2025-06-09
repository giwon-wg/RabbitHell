import React from 'react';
import { CloverResponse } from '../../types/types';
import { Card, Text, Title } from './MyInfoPage.styles';

type Props = {
	clover: CloverResponse;
};

const CloverCard = ({ clover }: Props) => {
	return (
		<>
			<Title>내 클로버</Title>
			<Card>
				<Text>이름: {clover.name}</Text>
				<Text>왕국: {clover.kingdomName}</Text>
				<Text>종족: {clover.specieName}</Text>
				<Text>스테미너: {clover.stamina}</Text>
				<Text>골드: {clover.cash}</Text>
				<Text>저축: {clover.saving}</Text>
			</Card>
		</>
	);
};

export default CloverCard;
