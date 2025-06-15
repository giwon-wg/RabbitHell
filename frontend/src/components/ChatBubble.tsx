import React from "react";/*

const ChatBubble = ({ isMe, username, message, timestamp }) => {
	return (
		<div
			style={{
				display: "flex",
				justifyContent: isMe ? "flex-end" : "flex-start",
				marginBottom: "1rem",
			}}
		>
			<div>
				{!isMe && (
					<div style={{ fontSize: "0.75rem", marginBottom: "0.2rem", color: "#666" }}>
						{username}
					</div>
				)}
				<div
					style={{
						backgroundColor: isMe ? "#FF6F00" : "#E0E0E0",
						color: isMe ? "white" : "black",
						padding: "0.75rem 1rem",
						borderRadius: "1.5rem",
						maxWidth: "70%",
						fontSize: "0.95rem",
						animation: "bubble-up 0.3s ease-out",
					}}
				>
					{message}
				</div>
				<div style={{ fontSize: "0.65rem", color: "#999", marginTop: "0.25rem", textAlign: isMe ? "right" : "left" }}>
					{timestamp}
				</div>
			</div>

			<style>
				{`
          @keyframes bubble-up {
            0% {
              transform: translateY(20px);
              opacity: 0;
            }
            100% {
              transform: translateY(0);
              opacity: 1;
            }
          }
        `}
			</style>
		</div>
	);
};

export default ChatBubble;
*/
