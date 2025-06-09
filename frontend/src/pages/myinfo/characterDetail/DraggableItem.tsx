import React, { useRef, useEffect } from 'react';
import { useDrag } from 'react-dnd';

const DraggableItem = ({ item }: { item: any }) => {
	const imgRef = useRef<HTMLImageElement>(null);

	const [{ isDragging }, drag] = useDrag(() => ({
		type: 'item',
		item,
		collect: monitor => ({
			isDragging: monitor.isDragging(),
		}),
	}));

	useEffect(() => {
		if (imgRef.current) {
			drag(imgRef.current);
		}
	}, []);

	const mapSlotToFolder = (itemType: string): string => {
		switch (itemType) {
			case 'SWORD':
			case 'BOW':
			case 'DAGGER':
			case 'WAND':
				return 'weapon';
			case 'ARMOR':
			case 'SHIELD':
				return 'armor';
			case 'ACCESSORY':
				return 'earring';
			default:
				return 'etc';
		}
	};

	if (!item || !item.slot || !item.itemId) return null;

	return (
		<div style={{ width: 64, display: 'flex', flexDirection: 'column', alignItems: 'center', margin: 4 }}>
			<img
				ref={imgRef}
				src={`/assets/items/${mapSlotToFolder(item.itemType)}/${item.itemId}.png`}
				alt={item.itemName}
				title={item.itemName}
				style={{
					width: 48,
					height: 48,
					opacity: isDragging ? 0.4 : 1,
					transform: isDragging ? 'scale(1.1)' : 'scale(1)',
					transition: 'all 0.2s ease',
				}}
			/>
			<span style={{ fontSize: 12, marginTop: 4, textAlign: 'center', wordBreak: 'keep-all' }}>
		{item.itemName}
	</span>
		</div>
	);
};

export default DraggableItem;
