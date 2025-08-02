import React, { useState, useEffect, useRef } from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import { useLocationCache } from '../hooks/useLocationCache';
import './Map.css';

interface Location {
    id: number;
    type: string;
    description: string;
    neighborIds: number[];
}

interface GraphNode {
    id: number;
    name: string;
    val: number;
    displayName: boolean;
}

interface GraphData {
    nodes: GraphNode[];
    links: Array<{
        source: number;
        target: number;
    }>;
}

interface MapProps {
    currentLocationId: number | null;
}

const API_BASE_URL = 'http://localhost:8080';
const MAX_DEPTH = 5;
const NODE_SIZE = 6;
const GLOW_SIZE = 2.0; // Multiplier for glow radius

const Map: React.FC<MapProps> = ({ currentLocationId }) => {
    const [graphData, setGraphData] = useState<GraphData>({ nodes: [], links: [] });
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);
    const animationFrameRef = useRef<number>();
    const startTimeRef = useRef<number>(Date.now());
    const { getLocation, setLocation } = useLocationCache();

    const fetchLocation = async (id: number): Promise<Location | null> => {
        // Check cache first
        const cachedLocation = getLocation(id);
        if (cachedLocation) {
            return cachedLocation;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/location/${id}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const location = await response.json();
            // Cache the location
            setLocation(location);
            return location;
        } catch (err) {
            console.error(`Error fetching location ${id}:`, err);
            return null;
        }
    };

    const buildGraph = async (startId: number, depth: number = 0, visited: Set<number> = new Set()): Promise<GraphData> => {
        if (depth > MAX_DEPTH || visited.has(startId)) {
            return { nodes: [], links: [] };
        }

        visited.add(startId);
        const location = await fetchLocation(startId);
        if (!location) {
            return { nodes: [], links: [] };
        }

        const nodes: GraphNode[] = [{
            id: location.id,
            name: location.type,
            val: 1,
            displayName: true // Default to showing names
        }];

        const links: Array<{ source: number; target: number }> = [];
        if(depth < MAX_DEPTH) {
            const neighborPromises = location.neighborIds
                .filter(id => !visited.has(id))
                .map(async (neighborId) => {
                    const neighborGraph = await buildGraph(neighborId, depth + 1, visited);
                    nodes.push(...neighborGraph.nodes);
                    links.push(
                        { source: location.id, target: neighborId },
                        ...neighborGraph.links
                    );
                });
            await Promise.all(neighborPromises);
        }
        return { nodes, links };
    };

    const validateGraph = (graph: GraphData) => {
        // Check that all nodes have a valid id
        for (const link of graph.links) {
            if (graph.nodes.find(node => node.id === link.source) === undefined || graph.nodes.find(node => node.id === link.target) === undefined) {
                return false;
            }
        }
        return true;
    }

    const fetchMap = async (id: number) => {
        setIsLoading(true);
        setError(null);
        try {
            const data = await buildGraph(id);
            if (!validateGraph(data)) {
                throw new Error('Invalid graph');
            }
            setGraphData(data);
        } catch (err) {
            console.error('Error fetching map:', err);
            setError('Failed to fetch map');
        } finally {
            setIsLoading(false);
        }
    };

    // Effect to refresh current location data when it changes
    useEffect(() => {
        if (currentLocationId !== null) {
            fetchLocation(currentLocationId);
        }
    }, [currentLocationId]);

    // Effect to build the graph
    useEffect(() => {
        if (currentLocationId !== null) {
            fetchMap(currentLocationId);
        }
    }, [currentLocationId]);

    useEffect(() => {
        return () => {
            if (animationFrameRef.current) {
                cancelAnimationFrame(animationFrameRef.current);
            }
        };
    }, []);

    const getGlowAnimation = () => {
        const elapsed = Date.now() - startTimeRef.current;
        const pulse = Math.sin(elapsed / 1000) * 0.5 + 0.5; // Oscillate between 0 and 1
        return {
            opacity: 0.15 + pulse * 0.1, // Pulse between 0.15 and 0.25
            size: GLOW_SIZE + pulse * 0.2 // Pulse between 2.0 and 2.2
        };
    };

    return (
        <div className="map-container">
            {error && <div className="map-error">{error}</div>}
            {isLoading && <div className="map-loading">Loading map...</div>}
            <ForceGraph2D
                graphData={graphData}
                nodeColor={() => '#2a2a4a'}
                nodeRelSize={NODE_SIZE}
                linkColor={() => '#4a4a8a'}
                backgroundColor="#1a1a1a"
                width={300}
                height={400}
                nodeCanvasObject={(node, ctx, globalScale) => {
                    const graphNode = node as GraphNode;
                    const label = graphNode.name;
                    const fontSize = 12/globalScale;
                    ctx.font = `${fontSize}px Sans-Serif`;
                    const textWidth = ctx.measureText(label).width;
                    const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2);

                    const isCurrentLocation = node.id === currentLocationId;
                    const nodeColor = isCurrentLocation ? '#4a4a8a' : '#2a2a4a';
                    const borderColor = isCurrentLocation ? '#6a6aaa' : '#4a4a8a';
                    const labelBgColor = isCurrentLocation ? 'rgba(74, 74, 138, 0.9)' : 'rgba(42, 42, 74, 0.9)';

                    if (isCurrentLocation && node.x !== undefined && node.y !== undefined && 
                        Number.isFinite(node.x) && Number.isFinite(node.y)) {
                        // Draw animated glow effect
                        const glow = getGlowAnimation();
                        const gradient = ctx.createRadialGradient(
                            node.x, node.y, NODE_SIZE,
                            node.x, node.y, NODE_SIZE * glow.size
                        );
                        gradient.addColorStop(0, `rgba(106, 106, 170, ${glow.opacity})`);
                        gradient.addColorStop(1, 'rgba(106, 106, 170, 0)');
                        
                        ctx.beginPath();
                        ctx.arc(node.x, node.y, NODE_SIZE * glow.size, 0, 2 * Math.PI);
                        ctx.fillStyle = gradient;
                        ctx.fill();
                    }

                    // Draw node circle with border
                    ctx.beginPath();
                    ctx.arc(node.x!, node.y!, NODE_SIZE, 0, 2 * Math.PI);
                    ctx.fillStyle = nodeColor;
                    ctx.fill();
                    ctx.strokeStyle = borderColor;
                    ctx.lineWidth = isCurrentLocation ? 3 : 2;
                    ctx.stroke();

                    // Only draw label if displayName is true
                    if (graphNode.displayName) {
                        // Draw label background
                        ctx.fillStyle = labelBgColor;
                        ctx.fillRect(
                            node.x! - bckgDimensions[0] / 2,
                            node.y! + NODE_SIZE + 2,
                            bckgDimensions[0],
                            bckgDimensions[1]
                        );

                        // Draw label text
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';
                        ctx.fillStyle = '#e0e0e0';
                        ctx.fillText(
                            label,
                            node.x!,
                            node.y! + NODE_SIZE + 2 + bckgDimensions[1] / 2
                        );
                    }
                }}
            />
            {currentLocationId && (
                <div className="location-description-container">
                    <div className={`location-description ${isDescriptionExpanded ? 'expanded' : 'collapsed'}`}>
                        {getLocation(currentLocationId)?.description || 'Loading description...'}
                    </div>
                    <button 
                        className="description-toggle"
                        onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
                    >
                        {isDescriptionExpanded ? 'Show Less' : 'Show More'}
                    </button>
                </div>
            )}
        </div>
    );
};

export default Map; 