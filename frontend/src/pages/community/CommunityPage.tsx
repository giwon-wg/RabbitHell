import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // ← 추가

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
	const navigate = useNavigate(); // ← 추가

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

	if (loading) return <p>불러오는 중...</p>;

	return (
		<div>
			<h1>커뮤니티 게시판</h1>
			<div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "10px" }}>
				<button onClick={() => navigate("/community/write")}>글 작성하기</button>
			</div>
			<ul>
				{posts.map((post) => (
					<li key={post.postId}>
						<a href={`/community/${post.postId}`}>
							<strong>{post.title}</strong>
						</a>{" "}
						- {post.userName} / 💬 {post.commentCount} / 🕒{" "}
						{new Date(post.createdAt).toLocaleString()}
					</li>
				))}
			</ul>

			<div>
				{page > 0 && <button onClick={() => setPage(page - 1)}>이전</button>}
				{hasNext && <button onClick={() => setPage(page + 1)}>다음</button>}
			</div>
		</div>
	);
};

export default CommunityPage;
