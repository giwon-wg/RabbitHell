import React from 'react';
import NavBar from '../components/NavBar';
import ChatBox from '../components/ChatBox';
import styled from 'styled-components';

const LayoutContainer = styled.div`
	display: flex;
	flex-direction: column;
	height: 100vh;
	position: relative; /* ✅ canvas 기준점 */
`;

const ContentArea = styled.div`
	display: flex;
	flex: 1;
`;

const MainContent = styled.main`
	flex: 1;
	padding: 24px;
	overflow-y: auto;
	position: relative; /* ✅ canvas가 자식으로 배치될 수 있게 */
	min-height: 100vh;  /* ✅ 캔버스 높이 확보 */
`;

const Layout = ({ children }: { children: React.ReactNode }) => {
	return (
		<LayoutContainer>
			<NavBar />
			<ContentArea>
				<MainContent>
					{children}
				</MainContent>
				<ChatBox />
			</ContentArea>
		</LayoutContainer>
	);
};

export default Layout;
