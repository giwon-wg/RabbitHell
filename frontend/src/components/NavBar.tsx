import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';

const NavContainer = styled.nav`
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 12px 24px;
	background-color: #fff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	font-weight: bold;
`;

const Logo = styled.div`
	font-size: 20px;
`;

const Menu = styled.ul`
	display: flex;
	gap: 20px;
	list-style: none;
`;

const MenuItem = styled.li<{ $active: boolean }>`
	a {
		text-decoration: none;
		color: ${(props) => (props.$active ? '#007bff' : '#333')};
		font-weight: ${(props) => (props.$active ? 'bold' : 'normal')};
	}
`;

const NavBar = () => {
	const location = useLocation();
	const currentPath = location.pathname;

	return (
		<NavContainer>
			<Logo>RabbitHell</Logo>
			<Menu>
				<MenuItem $active={currentPath === '/main'}>
					<Link to="/main">메인</Link>
				</MenuItem>
				<MenuItem $active={currentPath === '/map'}>
					<Link to="/map">월드 맵</Link>
				</MenuItem>
				<MenuItem $active={currentPath === '/battle'}>
					<Link to="/battle">전투</Link>
				</MenuItem>
				<MenuItem $active={currentPath === '/village'}>
					<Link to="/village">마을</Link>
				</MenuItem>
				<MenuItem $active={currentPath === '/me'}>
					<Link to="/me">내 정보</Link>
				</MenuItem>
				<MenuItem $active={currentPath === '/community'}>
					<Link to="/community">커뮤니티</Link>
				</MenuItem>
			</Menu>
		</NavContainer>
	);
};

export default NavBar;
