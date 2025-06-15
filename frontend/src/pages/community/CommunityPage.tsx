import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import PixelMoonsetBackground from './PixelMoonsetBackground';

interface Post {
	postId: number;
	userId: number;
	userName: string;
	title: string;
	content: string;
	commentCount: number;
	createdAt: string;
	modifiedAt: string;
}

const CommunityPage = () => {
	const [posts, setPosts] = useState<Post[]>([]);
	const [page, setPage] = useState(0);
	const [hasNext, setHasNext] = useState(false);
	const [loading, setLoading] = useState(true);
	const navigate = useNavigate(); // ✅ 컴포넌트 안에서 호출해야 함

	useEffect(() => {
		const token = localStorage.getItem("accessToken");

		fetch(`http://localhost:8080/posts?category=GENERAL&page=${page}&size=10`, {
			headers: {
				Authorization: `Bearer ${token}`,
			},
		})
			.then((res) => res.json())
			.then((data) => {
				setPosts(data.result.content);
				setHasNext(data.result.hasNext);
				setLoading(false);
			})
			.catch(() => {
				alert("게시글을 불러오는 데 실패했습니다.");
				setLoading(false);
			});
	}, [page]);

	if (loading) {
		return (
			<div className="w-full h-screen flex items-center justify-center">
				<div className="text-white text-xl">불러오는 중...</div>
			</div>
		);
	}

	return (
		<div className="w-full min-h-screen">
			<PixelMoonsetBackground
				showOverlay={true}
				overlayOpacity={0.2}
				className="min-h-screen"
			>
				<div className="container mx-auto px-4 py-8">
					<header className="text-center mb-12">
						<h1 className="text-4xl md:text-6xl font-bold text-white mb-4 drop-shadow-lg">
							RabbitHell Community
						</h1>
						<p className="text-xl text-gray-200 drop-shadow-md">
							토끼굴에 오신 것을 환영합니다
						</p>
					</header>

					<div className="flex justify-end mb-6">
						<button
							onClick={() => navigate("/community/write")}
							className="bg-white bg-opacity-20 hover:bg-opacity-30 text-white px-6 py-2 rounded-lg border border-white border-opacity-30 backdrop-blur-sm transition-all duration-300 font-medium"
						>
							글 작성하기
						</button>
					</div>

					<div className="bg-black bg-opacity-30 backdrop-blur-sm rounded-lg p-6 border border-white border-opacity-20 mb-8">
						<h2 className="text-2xl font-bold text-white mb-6">커뮤니티 게시글</h2>

						{posts.length > 0 ? (
							<div className="space-y-4">
								{posts.map((post) => (
									<div
										key={post.postId}
										className="bg-white bg-opacity-10 rounded-lg p-4 hover:bg-opacity-20 transition-all duration-300 cursor-pointer"
										onClick={() => navigate(`/community/${post.postId}`)}
									>
										<div className="flex justify-between items-start mb-2">
											<h3 className="text-lg font-semibold text-white hover:text-blue-300 transition-colors">
												{post.title}
											</h3>
											<span className="text-xs text-gray-300 whitespace-nowrap ml-4">
                        {new Date(post.createdAt).toLocaleString()}
                      </span>
										</div>

										<p className="text-gray-200 text-sm mb-3 line-clamp-2">
											{post.content}
										</p>

										<div className="flex justify-between items-center text-xs text-gray-300">
											<span>작성자: {post.userName}</span>
											<span>💬 {post.commentCount}</span>
										</div>
									</div>
								))}
							</div>
						) : (
							<div className="text-center text-gray-300 py-8">
								게시글이 없습니다.
							</div>
						)}
					</div>

					<div className="flex justify-center space-x-4 mb-8">
						{page > 0 && (
							<button
								onClick={() => setPage(page - 1)}
								className="bg-white bg-opacity-20 hover:bg-opacity-30 text-white px-4 py-2 rounded-lg border border-white border-opacity-30 backdrop-blur-sm transition-all duration-300"
							>
								이전
							</button>
						)}

						<span className="text-white px-4 py-2">페이지 {page + 1}</span>

						{hasNext && (
							<button
								onClick={() => setPage(page + 1)}
								className="bg-white bg-opacity-20 hover:bg-opacity-30 text-white px-4 py-2 rounded-lg border border-white border-opacity-30 backdrop-blur-sm transition-all duration-300"
							>
								다음
							</button>
						)}
					</div>

					<div className="text-center space-y-4">
						<button
							onClick={() => navigate("/community/write")}
							className="bg-white bg-opacity-20 hover:bg-opacity-30 text-white px-8 py-3 rounded-lg border border-white border-opacity-30 backdrop-blur-sm transition-all duration-300 font-medium"
						>
							새 글 작성
						</button>

						<div>
							<button
								className="bg-transparent border border-white border-opacity-50 text-white px-6 py-2 rounded-md hover:bg-white hover:bg-opacity-10 transition-all duration-300 text-sm"
								onClick={() => window.location.reload()}
							>
								배경 애니메이션 리셋 (5분 자동 반복)
							</button>
						</div>
					</div>
				</div>
			</PixelMoonsetBackground>
		</div>
	);
};

export default CommunityPage;
