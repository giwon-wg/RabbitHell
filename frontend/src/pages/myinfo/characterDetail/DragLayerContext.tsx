import { createContext, useContext, useState } from 'react';

const DragLayerContext = createContext<{
	isDragging: boolean;
	setDragging: (val: boolean) => void;
	draggedItem: any;
	setDraggedItem: (item: any) => void;
}>({
	isDragging: false,
	setDragging: () => {},
	draggedItem: null,
	setDraggedItem: () => {},
});

export const DragLayerProvider = ({ children }: { children: React.ReactNode }) => {
	const [isDragging, setDragging] = useState(false);
	const [draggedItem, setDraggedItem] = useState<any>(null);

	return (
		<DragLayerContext.Provider value={{ isDragging, setDragging, draggedItem, setDraggedItem }}>
			{children}
		</DragLayerContext.Provider>
	);
};

export const useDragLayerContext = () => useContext(DragLayerContext);
