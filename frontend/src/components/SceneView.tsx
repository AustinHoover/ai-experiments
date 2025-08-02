import React, { useState, useEffect, useRef } from 'react';
import './SceneView.css';

interface SceneViewProps {
    currentLocationId: number | null;
}

const API_BASE_URL = 'http://localhost:8080';

const SceneView: React.FC<SceneViewProps> = ({ currentLocationId }) => {
    const [imageUrl, setImageUrl] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);

    const fetchSceneImage = async (locationId: number) => {
        setIsLoading(true);
        setError(null);
        
        try {
            const response = await fetch(`${API_BASE_URL}/location/${locationId}/scene`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setImageUrl(data.imageUrl || null);
        } catch (err) {
            console.error('Error fetching scene image:', err);
            setError('Failed to load scene image');
            setImageUrl(null);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (currentLocationId !== null) {
            fetchSceneImage(currentLocationId);
        } else {
            setImageUrl(null);
        }
    }, [currentLocationId]);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas || !imageUrl) return;

        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        const img = new Image();
        img.crossOrigin = 'anonymous';
        
        img.onload = () => {
            // Set canvas size to match container
            const container = canvas.parentElement;
            if (container) {
                canvas.width = container.clientWidth;
                canvas.height = container.clientHeight;
            }

            // Clear canvas
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // Calculate scaling to cover the entire canvas while maintaining aspect ratio
            const scaleX = canvas.width / img.width;
            const scaleY = canvas.height / img.height;
            const scale = Math.max(scaleX, scaleY);

            // Calculate position to center the image
            const scaledWidth = img.width * scale;
            const scaledHeight = img.height * scale;
            const x = (canvas.width - scaledWidth) / 2;
            const y = (canvas.height - scaledHeight) / 2;

            // Draw the image
            ctx.drawImage(img, x, y, scaledWidth, scaledHeight);
        };

        img.onerror = () => {
            setError('Failed to load image');
            setImageUrl(null);
        };

        img.src = imageUrl;
    }, [imageUrl]);

    return (
        <div className="scene-view">
            {isLoading && <div className="scene-loading">Loading scene...</div>}
            {error && <div className="scene-error">{error}</div>}
            {!imageUrl && !isLoading && !error && (
                <div className="scene-placeholder">
                    <p>No scene image available</p>
                </div>
            )}
            <canvas
                ref={canvasRef}
                className="scene-canvas"
                style={{ display: imageUrl && !isLoading && !error ? 'block' : 'none' }}
            />
        </div>
    );
};

export default SceneView; 