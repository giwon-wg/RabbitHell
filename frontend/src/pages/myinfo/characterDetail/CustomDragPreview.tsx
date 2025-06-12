import { useDragLayer } from 'react-dnd';

const layerStyles: React.CSSProperties = {
	position: 'fixed',
	pointerEvents: 'none',
	zIndex: 1000,
	left: 0,
	top: 0,
};

function getItemStyles(currentOffset: any) {
	if (!currentOffset) {
		return { display: 'none' };
	}
	const { x, y } = currentOffset;
	return {
		transform: `translate(${x}px, ${y}px)`,
	};
}

const CustomDragPreview = () => {
	const { isDragging, item, currentOffset } = useDragLayer((monitor) => ({
		item: monitor.getItem(),
		currentOffset: monitor.getSourceClientOffset(),
		isDragging: monitor.isDragging(),
	}));

	if (!isDragging || !item) return null;

	return (
		<div style={layerStyles}>
			<div style={getItemStyles(currentOffset)}>
				<div
					style={{
						width: 48,
						height: 48,
						backgroundColor: 'rgba(0,0,0,0.2)',
						border: '2px solid red',
						borderRadius: 8,
					}}
				>
					{/* 미리보기 대체 이미지 */}
				</div>
			</div>
		</div>
	);
};

export default CustomDragPreview;
