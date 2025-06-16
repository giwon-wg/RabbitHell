// util/socketUtils.ts
import { Client } from "@stomp/stompjs";
export const sendQuitAndDisconnect = (
	stompClient: React.MutableRefObject<Client | null>
) => {
	if (stompClient.current?.connected) {
		stompClient.current.deactivate(); // ✅ 메시지 생략, 바로 끊음
	}
};
