import styled from 'styled-components';

export const Container = styled.div`
	padding: 32px;
	max-width: 800px;
	margin: 0 auto;
	font-family: 'Segoe UI', sans-serif;
`;

export const CharacterList = styled.div`
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
`;

export const Section = styled.div`
	margin-bottom: 48px;
`;

export const Title = styled.h2`
	font-size: 24px;
	margin-bottom: 16px;
	color: #333;
`;

export const Card = styled.div`
	background-color: #ffffff;
	border-radius: 12px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
	padding: 20px;
	margin-bottom: 16px;
	transition: transform 0.2s ease;

	&:hover {
		transform: translateY(-3px);
	}
`;

export const Text = styled.p`
	margin: 6px 0;
	font-size: 16px;
	color: #444;
`;
