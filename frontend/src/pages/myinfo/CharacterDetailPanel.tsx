import React from 'react';
import styled from 'styled-components';
import { CharacterPersonalInfoResponse } from '../../types/types';

const Panel = styled.div`
	padding: 24px;
	background: white;
	border-radius: 12px;
	box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
	margin-top: 24px;
`;

const Bar = styled.div<{ width: number; color?: string }>`
	height: 12px;
	width: 100%;
	background: #eee;
	border-radius: 6px;
	margin-bottom: 8px;
	position: relative;

	&::after {
		content: '';
		display: block;
		height: 100%;
		width: ${props => props.width}%;
		background: ${props => props.color || '#4caf50'};
		border-radius: 6px;
		position: absolute;
		left: 0;
		top: 0;
	}
`;

type Props = {
	character: CharacterPersonalInfoResponse;
	onClose: () => void;
};

const CharacterDetailPanel = ({ character, onClose }: Props) => {
	return (
		<Panel>
			<h2>{character.characterName}</h2>
			<p>Lv. {character.level} / 직업: {character.job}</p>
			<p>HP: {character.hp} / {character.maxHp}</p>
			<p>MP: {character.mp} / {character.maxMp}</p>
			<p>힘: {character.strength} / 민첩: {character.agility}</p>
			<p>지력: {character.intelligence} / 집중력: {character.focus}</p>

			<h4>성장치</h4>
			<Bar width={(character.exp % 100)} color="#007bff" />
			<h4>속도</h4>
			<Bar width={character.agility * 1} color="#03a9f4" />
			<h4>직업 숙련도</h4>
			<Bar width={character.skillPoint} color="#ff9800" />
		</Panel>
	);
};

export default CharacterDetailPanel;
