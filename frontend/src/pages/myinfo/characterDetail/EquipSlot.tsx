import React, { useRef, useEffect } from 'react';
import { useDrop } from 'react-dnd';
import { getFolderBySlot } from '../../../util/slotUtils';
import DraggableItem from './DraggableItem';


const getSlotLabel = (slot: string): string => {
	switch (slot) {
		case 'HAND': return '무기';
		case 'BODY': return '갑옷';
		case 'HEAD': return '악세사리';
		default: return '기타';
	}
};

const EquipSlot = ({ slotType, onDropItem, equippedItem }: any) => {
	const ref = useRef<HTMLDivElement>(null);

	const [{ isOver, canDrop }, drop] = useDrop({
		accept: 'item',
		drop: (item: any) => {
			if (item.slot === slotType) onDropItem(item);
		},
		collect: monitor => ({
			isOver: monitor.isOver(),
			canDrop: monitor.canDrop(),
		}),
	});

	useEffect(() => {
		if (ref.current) drop(ref.current);
	}, []);

	const highlight = isOver && canDrop;

	return (
		<div style={{ marginBottom: 16 }}>
			<p style={{ marginBottom: 4, fontWeight: 'bold', fontSize: 14 }}>
				{getSlotLabel(slotType)}
			</p>
			<div
				ref={ref}
				style={{
					width: 100,
					height: 100,
					border: highlight ? '3px solid green' : '2px dashed gray',
					backgroundColor: highlight ? '#e6ffe6' : '#f0f0f0',
					display: 'flex',
					alignItems: 'center',
					justifyContent: 'center',
					transition: 'all 0.2s ease',
				}}
			>
				{equippedItem ? (
					<DraggableItem item={equippedItem} isEquipped />
				) : (
					<span style={{ color: '#888' }}>슬롯 비어있음</span>
				)}
			</div>
		</div>
	);
};

export default EquipSlot;
