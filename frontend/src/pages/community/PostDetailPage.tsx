import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

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

const PostDetailPage = () => {
	const { postId } = useParams<{ postId: string }>();
	const [post, setPost] = useState<Post | null>(null);
	const navigate = useNavigate();

	useEffect(() => {
		const token = localStorage.getItem("accessToken");

		fetch(`http://localhost:8080/posts/${postId}`, {
			headers: {
				Authorization: `Bearer ${token}`,
			},
		})
			.then((res) => res.json())
			.then((data) => setPost(data.result))
			.catch(() => {
				alert("게시글을 불러오지 못했습니다.");
				navigate("/community");
			});
	}, [postId, navigate]);

	if (!post) return <p>불러오는 중...</p>;

	return (
		<div>
			<h2>{post.title}</h2>
			<p>
				작성자: <strong>{post.userName}</strong> | 작성일:{" "}
				{new Date(post.createdAt).toLocaleString()}
			</p>
			<hr />
			<div>{post.content}</div>
			<hr />
			<p>💬 댓글 수: {post.commentCount}</p>
		</div>
	);
};

export default PostDetailPage;
