export const authFetch = async (url: string, options: RequestInit = {}) => {
	const token = localStorage.getItem("accessToken");

	const authHeaders: HeadersInit = {
		...(options.headers || {}),
		...(token ? { Authorization: `Bearer ${token}` } : {}),
	};

	return fetch(url, {
		...options,
		headers: authHeaders,
	});
};

export const miniFetch = async (url: string, options: RequestInit = {}) => {
	const token = localStorage.getItem("miniToken");

	const authHeaders: HeadersInit = {
		...(options.headers || {}),
		...(token ? { Authorization: `Bearer ${token}` } : {}),
	};

	return fetch(url, {
		...options,
		headers: authHeaders,
	});
};
