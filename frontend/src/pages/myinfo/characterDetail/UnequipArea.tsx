import React, { useRef, useEffect } from 'react';
import { useDrop } from 'react-dnd';
import { useDragLayerContext } from './DragLayerContext';
import { DND_ITEM_TYPE } from './dndTypes'

type UnequipAreaProps = {
	onDropUnequip: (item: { inventoryItemId: number; slot: string }) => void;
};

const UnequipArea = ({ onDropUnequip }: UnequipAreaProps) => {
	const ref = useRef<HTMLDivElement>(null);
	const { isDragging, draggedItem  } = useDragLayerContext();

	const [{ isOver }, drop] = useDrop({
		accept: DND_ITEM_TYPE,
		drop: (item: any) => {
			onDropUnequip(item)
		},
		collect: monitor => ({
			isOver: monitor.isOver(),
		}),
	});

	useEffect(() => {
		if (ref.current) {
			console.log('💡 drop 영역 크기:', ref.current.getBoundingClientRect());
			drop(ref.current);
		}
	}, [drop]);

	// if (!draggedItem?.slot) return null;

	console.log('UnequipArea isOver:', isOver, 'isDragging:', isDragging);

	return (
		<div
			ref={ref}
			style={{
				position: 'absolute',
				inset: 0,
				// backgroundColor: isOver ? 'rgba(255, 0, 0, 0.05)' : 'transparent',
				// backgroundColor: isOver ? 'rgba(255, 0, 0, 0.2)' : 'rgba(0, 255, 0, 0.05)',
				border: isOver ? '2px dashed red' : 'none',
				pointerEvents: isDragging ? 'auto' : 'none',
				zIndex: 99999,
				display: 'flex',
				alignItems: 'center',
				justifyContent: 'center',
			}}
		>
			{/*<span style={{ background: 'white', border: '1px solid red', padding: 12 }}>*/}
			{/*여기에 놓으면 장비가 해제됩니다*/}
			{/*</span>*/}
		</div>
	);
};
export default UnequipArea;
