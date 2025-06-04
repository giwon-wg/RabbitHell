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
import CharacterDetailPage from './pages/myinfo/CharacterDetailPage';
import MainPage from './pages/main/MainPage';
import BattleMainPage from "./pages/battlePage/BattlePage";
import VillageMainPage from "./pages/villagePage/VillagePage";
import CommunityPage from './pages/community/CommunityPage'

function App() {
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

			{/* Layout 적용 대상 경로들 */}
			<Route element={<LayoutWrapper hide={shouldHideLayout} />}>
				<Route path="/me" element={<MyInfoPage />} />
				<Route path="/me/:characterName" element={<CharacterDetailPage />} />
				<Route path="/main" element={<MainPage />} />
				<Route path="/battle" element={<BattleMainPage />} />
				<Route path="/Village" element={<VillageMainPage />} />
				<Route path="/community" element={<CommunityPage />} />
			</Route>
		</Routes>
	);
};

const LayoutWrapper = ({ hide }: { hide: boolean }) => {
	return hide ? <Outlet /> : <Layout><Outlet /></Layout>;
};

export default App;
