import React from 'react';
import NavBar from '../components/NavBar';
import ChatBox from '../components/ChatBox';
import styled from 'styled-components';

const LayoutContainer = styled.div`
	display: flex;
	flex-direction: column;
	height: 100vh;
`;

const ContentArea = styled.div`
	display: flex;
	flex: 1;
`;

const MainContent = styled.main`
	flex: 1;
	padding: 24px;
	overflow-y: auto;
`;

const Layout = ({ children }: { children: React.ReactNode }) => {
	return (
		<LayoutContainer>
			<NavBar />
			<ContentArea>
				<MainContent>{children}</MainContent>
				<ChatBox />
			</ContentArea>
		</LayoutContainer>
	);
};

export default Layout;
