import React, { useRef, useEffect } from 'react';
import { useDrop } from 'react-dnd';

type UnequipAreaProps = {
	onDropUnequip: (item: { inventoryItemId: number; slot: string }) => void;
};

const UnequipArea = ({ onDropUnequip }: UnequipAreaProps) => {
	const ref = useRef<HTMLDivElement>(null);

	const [{ isOver }, drop] = useDrop({
		accept: 'item',
		drop: (item: any) => onDropUnequip(item),
		collect: monitor => ({
			isOver: monitor.isOver(),
		}),
	});

	useEffect(() => {
		if (ref.current) drop(ref.current);
	}, []);

	return (
		<div
			ref={ref}
			style={{
				width: 120,
				height: 120,
				border: isOver ? '3px solid red' : '2px solid red',
				backgroundColor: isOver ? '#ffcccc' : '#ffe6e6',
				display: 'flex',
				alignItems: 'center',
				justifyContent: 'center',
				margin: 10,
				transition: 'all 0.2s ease',
			}}
		>
			해제
		</div>
	);
};

export default UnequipArea;
