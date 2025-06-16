import React, { useEffect, useRef, ReactNode } from 'react';

interface Props {
	children?: ReactNode;
	showOverlay?: boolean;
	overlayOpacity?: number;
	className?: string;
	style?: React.CSSProperties;
}

const PixelMoonsetBackground: React.FC<Props> = ({
													 children,
													 showOverlay = true,
													 overlayOpacity = 0.3,
													 className = "",
													 style = {},
												 }) => {
	const canvasRef = useRef<HTMLCanvasElement | null>(null);
	const animationRef = useRef<number | null>(null);

	const resetAnimation = () => {
		// 원하는 초기화 로직
	};

	const setupCanvas = (): void => {
		const canvas = canvasRef.current;
		if (!canvas) return;
		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;
		canvas.style.width = '100%';
		canvas.style.height = '100%';
	};

	const draw = () => {
		const canvas = canvasRef.current;
		if (!canvas) return;
		const ctx = canvas.getContext('2d');
		if (!ctx) return;

		ctx.fillStyle = 'black';
		ctx.fillRect(0, 0, canvas.width, canvas.height);

		animationRef.current = requestAnimationFrame(draw);
	};

	useEffect(() => {
		setupCanvas();
		draw();

		const handleResize = () => {
			setupCanvas();
		};

		window.addEventListener('resize', handleResize);
		return () => {
			if (animationRef.current) cancelAnimationFrame(animationRef.current);
			window.removeEventListener('resize', handleResize);
		};
	}, []);

	return (
		<div className={`relative w-full min-h-screen ${className}`} style={style}>
			<canvas
				ref={canvasRef}
				className="absolute inset-0 w-full h-full"
				style={{ zIndex: -1, imageRendering: 'pixelated' }}
				onClick={resetAnimation}
			/>
			{showOverlay && (
				<div
					className="absolute inset-0 bg-black pointer-events-none"
					style={{ opacity: overlayOpacity, zIndex: 0 }}
				/>
			)}
			<div className="relative z-10 w-full h-full">{children}</div>
		</div>
	);
};

export default PixelMoonsetBackground;
