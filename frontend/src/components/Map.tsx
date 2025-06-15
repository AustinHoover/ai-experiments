import React from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import './Map.css';

interface Location {
    id: number;
    type: string;
    description: string;
}

interface MapProps {
    currentLocation: Location | null;
}

interface GraphData {
    nodes: Array<{
        id: number;
        name: string;
        val: number;
    }>;
    links: Array<{
        source: number;
        target: number;
    }>;
}

const Map: React.FC<MapProps> = ({ currentLocation }) => {
    const graphData: GraphData = {
        nodes: currentLocation ? [{
            id: currentLocation.id,
            name: currentLocation.type,
            val: 1
        }] : [],
        links: []
    };

    return (
        <div className="map-container">
            <ForceGraph2D
                graphData={graphData}
                nodeLabel="name"
                nodeColor={() => '#1a1a2a'}
                nodeRelSize={6}
                linkColor={() => '#333'}
                backgroundColor="#1a1a1a"
                width={300}
                height={400}
            />
        </div>
    );
};

export default Map; 