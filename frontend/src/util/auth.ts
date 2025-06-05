export async function refreshTokens() {
	const refreshToken = localStorage.getItem("refreshToken");
	if (!refreshToken) {
		throw new Error("리프레쉬 토큰 없음");
	}

	const res = await fetch("http://localhost:8080/auth/reissue", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ refreshToken }),
	});

	if (!res.ok) {
		throw new Error("토큰 리프레쉬 실패");
	}

	const data = await res.json();
	localStorage.setItem("accessToken", data.result.accessToken);
	localStorage.setItem("refreshToken", data.result.refreshToken);

	console.log("[🔄] 새 accessToken:", data.result.accessToken);

	return data.result.accessToken;
}
