import React from 'react';
import { CharacterPersonalInfoResponse } from '../../types/types';
import { Card, Text } from './MyInfoPage.styles';

type Props = {
	character: CharacterPersonalInfoResponse;
	onClick?: () => void;
};

const CharacterCard = ({ character, onClick }: Props) => {
	return (
		<Card onClick={onClick} style={{ cursor: onClick ? 'pointer' : 'default' }}>
			<Text>이름: {character.characterName}</Text>
			<Text>직업: {character.job}</Text>
			<Text>레벨: {character.level}</Text>
		</Card>
	);
};

export default CharacterCard;
