import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import OAuthSuccessPage from './pages/OAuthSuccessPage';
import CloverCreatePage from './pages/CloverCreatePage';
import MainPage from './pages/MainPage';

function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<LoginPage />} />
				<Route path="/oauth/success" element={<OAuthSuccessPage />} />
				<Route path="/create-clover" element={<CloverCreatePage />} />
				<Route path="/main" element={<MainPage />} />
			</Routes>
		</Router>
	);
}

export default App;
