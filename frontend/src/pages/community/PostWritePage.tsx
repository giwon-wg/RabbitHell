import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const PostWritePage: React.FC = () => {
	const [title, setTitle] = useState("");
	const [content, setContent] = useState("");
	const [category, setCategory] = useState("GENERAL");
	const navigate = useNavigate();

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		const token = localStorage.getItem("accessToken");

		const response = await fetch("http://localhost:8080/posts", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify({ title, content, postCategory: category }),
		});

		const data = await response.json();

		if (response.ok) {
			alert("게시글이 작성되었습니다!");
			navigate("/community");
		} else {
			alert(`작성 실패: ${data.message}`);
		}
	};

	return (
		<div>
			<h2>게시글 작성</h2>
			<form onSubmit={handleSubmit}>
				<div>
					<label>카테고리: </label>
					<select value={category} onChange={(e) => setCategory(e.target.value)}>
						<option value="GENERAL">자유</option>
						<option value="QUESTION">질문</option>
					</select>
				</div>
				<div>
					<label>제목: </label>
					<input
						type="text"
						value={title}
						onChange={(e) => setTitle(e.target.value)}
						required
					/>
				</div>
				<div>
					<label>내용: </label>
					<textarea
						value={content}
						onChange={(e) => setContent(e.target.value)}
						rows={10}
						required
					/>
				</div>
				<button type="submit">작성</button>
			</form>
		</div>
	);
};

export default PostWritePage;
