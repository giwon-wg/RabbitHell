import {
	BrowserRouter as Router,
	Routes,
	Route,
	useLocation,
	Outlet,
} from 'react-router-dom';

import Layout from './layout/Layout';
import LoginPage from './pages/LoginPage';
import OAuthSuccessPage from './pages/OAuthSuccessPage';
import CloverCreatePage from './pages/CloverCreatePage';
import MyInfoPage from './pages/myinfo/MyInfoPage';
import CharacterDetailPage from './pages/myinfo/characterDetail/CharacterDetailPage';
import MainPage from './pages/main/MainPage';
import BattleMainPage from "./pages/battlePage/BattlePage";
import VillageMainPage from "./pages/villagePage/VillagePage";
import CommunityPage from './pages/community/CommunityPage';
import {refreshTokens} from "./util/auth";
import React, { useEffect } from 'react';
import { jwtDecode } from "jwt-decode";
import PostDetailPage from "./pages/community/PostDetailPage";
import PostWritePage from "./pages/community/PostWritePage";
import WordMap from "./pages/WorldMap";
import Bank from "./pages/villagePage/bank/Bank";
import HospitalPage from './pages/villagePage/hospital/Hospital';
import ChatMessageToall from "./pages/ChatMessagePage/ChatMessageToall";
import CharacterCreatePage from './pages/CharacterCreatePage';
import InventoryPage from "./pages/myinfo/inventory/InventoryPage";

type DecodedToken = {
	exp: number;
}

function scheduleRefresh(accessToken: string) {
	const decoded: DecodedToken = jwtDecode(accessToken);
	const expiresAt = decoded.exp * 1000;
	const now = Date.now();
	const delay = expiresAt - now - 60_000; // 만료 1분 전

	console.log("[🔁] accessToken 만료까지:", (expiresAt - now) / 1000, "초");
	console.log("[⏳] refresh 예약 in", delay / 1000, "초");

	if (delay <= 0) {
		alert("세션이 만료되었습니다. 다시 로그인해주세요.");
		localStorage.removeItem("accessToken");
		localStorage.removeItem("refreshToken");
		window.location.href = "/";
		return;
	}

	setTimeout(() => {
		console.log("[🔄] refreshTokens 실행 시작");
		refreshTokens()
			.then(newAccessToken => {
				scheduleRefresh(newAccessToken);
			})
			.catch(() => {
				alert("세션이 만료되었습니다.");
				window.location.href = "/";
			});
	}, delay);
}

function App() {

	useEffect(() => {
		const accessToken = localStorage.getItem("accessToken");

		try {
			if (accessToken) {
				const decoded = jwtDecode(accessToken);
				if (decoded?.exp && decoded.exp * 1000 > Date.now()) {
					scheduleRefresh(accessToken);
				}
			}
		} catch (e) {
			console.warn("Invalid access token, skipping refresh scheduler");
		}
	}, []);

	return (
		<Router>
			<RoutesWithLayout />
		</Router>
	);
}

const RoutesWithLayout = () => {
	const location = useLocation();
	const hideLayoutRoutes = ['/', '/create-clover', '/oauth/success'];
	const shouldHideLayout = hideLayoutRoutes.includes(location.pathname);

	return (
		<Routes>
			{/* Layout 없이 표시할 페이지들 */}
			<Route path="/" element={<LoginPage />} />
			<Route path="/oauth/success" element={<OAuthSuccessPage />} />
			<Route path="/create-clover" element={<CloverCreatePage />} />
			<Route path="/chat" element={<ChatMessageToall/>}/>
			<Route path="/character/create" element={<CharacterCreatePage />} />

			{/* Layout 적용 대상 경로들 */}
			<Route element={<LayoutWrapper hide={shouldHideLayout} />}>
				<Route path="/me" element={<MyInfoPage />} />
				<Route path="/me/inventory" element={<InventoryPage />} />
				<Route path="/me/:characterName" element={<CharacterDetailPage />} />
				<Route path="/main" element={<MainPage />} />
				<Route path="/battle" element={<BattleMainPage />} />
				<Route path="/village" element={<VillageMainPage />} />
				<Route path="/village/bank" element={<Bank />} />
				<Route path="/hospital" element={<HospitalPage />} />
				<Route path="/community" element={<CommunityPage />} />
				<Route path="/community/:postId" element={<PostDetailPage />} />
				<Route path="/community/write" element={<PostWritePage />} />
				<Route path="/map" element={<WordMap />} />
			</Route>
		</Routes>
	);
};

const LayoutWrapper = ({ hide }: { hide: boolean }) => {
	return hide ? <Outlet /> : <Layout><Outlet /></Layout>;
};

export default App;
