import React, { useRef, useEffect, useState } from 'react';
import { useDrag } from 'react-dnd';
import {
	mapSlotToFolder,
	rarityBorderColors,
	getSlotLabel,
	fixSlotFromItemType,
} from '../../../util/itemUtils';
import { getFolderBySlot } from '../../../util/slotUtils';
import { useDragLayerContext } from './DragLayerContext';
import { DND_ITEM_TYPE } from './dndTypes'

type DraggableItemProps = {
	item: any;
	isEquipped?: boolean;
};

const DraggableItem = ({ item, isEquipped = false }: DraggableItemProps) => {
	const imgRef = useRef<HTMLImageElement>(null);
	const [hovered, setHovered] = useState(false);

	const { setDragging, setDraggedItem } = useDragLayerContext();

	useEffect(() => {
		if (imgRef.current) {
			drag(imgRef.current);
			preview(imgRef.current);
		}
	}, []);

	const [{ isDragging }, drag, preview] = useDrag(() => ({
		type: DND_ITEM_TYPE,
		collect: (monitor) => ({
			isDragging: monitor.isDragging(),
		}),
		item: () => {
			const dragItem = {
				inventoryItemId: item.inventoryItemId,
				slot: item.slot ?? fixSlotFromItemType(item.itemType),
			};
			console.log('드래그 시작됨', { dragItem, isEquipped });
			if (isEquipped) {
				setDraggedItem(dragItem);
			}
			return dragItem;
		},
		end: () => {
			setDragging(false);
			setDraggedItem(null);
		},
	}));


	useEffect(() => {
		setDragging(isDragging);
	}, [isDragging]);

	if (!item || !item.itemId || (!item.itemType && !item.slot)) return null;

	const folder = item.itemType
		? mapSlotToFolder(item.itemType)
		: getFolderBySlot(item.slot);

	const borderColor = rarityBorderColors[item.rarity] || '#ccc';

	const slotLabel = getSlotLabel(item.slot ?? fixSlotFromItemType(item.itemType));

	return (
		<div
			onMouseEnter={() => setHovered(true)}
			onMouseLeave={() => setHovered(false)}
			style={{
				width: 64,
				display: 'flex',
				flexDirection: 'column',
				alignItems: 'center',
				margin: 8,
				position: 'relative',
			}}
		>
			<img
				ref={imgRef}
				src={`/assets/items/${folder}/${item.itemId}.png`}
				alt={item.itemName}
				title={item.itemName}
				style={{
					width: 48,
					height: 48,
					opacity: isDragging ? 0.4 : 1,
					transform: isDragging ? 'scale(1.1)' : 'scale(1)',
					transition: 'all 0.2s ease',
					border: `2px solid ${borderColor}`,
					borderRadius: 8,
				}}
			/>
			<span
				style={{
					fontSize: 12,
					marginTop: 4,
					textAlign: 'center',
					wordBreak: 'keep-all',
				}}
			>
				{item.itemName}
			</span>

			{/* 툴팁 */}
			{hovered && !isDragging && (
				<div
					style={{
						position: 'absolute',
						top: '50%',
						left: '210%',
						transform: 'translateX(-50%)',
						marginTop: 8,
						backgroundColor: '#fff',
						border: '1px solid #ccc',
						borderRadius: 8,
						boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
						padding: 10,
						zIndex: 10,
						width: 180,
						textAlign: 'left',
						fontSize: 13,
					}}
				>
					<strong>{item.itemName}</strong>
					{item.rarity && <p>등급: {item.rarity}</p>}
					<p>슬롯: {slotLabel}</p>
					<p>내구도: {item.durability}</p>
					<p>설명: {item.description || '설명이 없습니다.'}</p>
				</div>
			)}
		</div>
	);
};

export default DraggableItem;
