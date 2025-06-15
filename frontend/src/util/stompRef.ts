//stompClient 전역 관리
import { Client } from '@stomp/stompjs';

export const stompClient: { current: Client | null } = { current: null };
