import React from 'react';
import './CommandGrid.css';

interface CommandGridProps {
    callbacks: Array<Array<(() => void) | null>>;
    labels?: Array<Array<string>>;
    keyMappings?: Array<Array<string>>;
}

const CommandGrid: React.FC<CommandGridProps> = ({ 
    callbacks, 
    labels = [], 
    keyMappings = [] 
}) => {
    const ROWS = 3;
    const COLS = 6;

    // Ensure we have the right dimensions
    const normalizedCallbacks: Array<Array<(() => void) | null>> = [];
    const normalizedLabels: Array<Array<string>> = [];
    const normalizedKeyMappings: Array<Array<string>> = [];

    for (let row = 0; row < ROWS; row++) {
        normalizedCallbacks[row] = [];
        normalizedLabels[row] = [];
        normalizedKeyMappings[row] = [];
        
        for (let col = 0; col < COLS; col++) {
            // Get callback if it exists
            normalizedCallbacks[row][col] = 
                callbacks[row] && callbacks[row][col] ? callbacks[row][col] : null;
            
            // Get label if it exists
            normalizedLabels[row][col] = 
                labels[row] && labels[row][col] ? labels[row][col] : '';
            
            // Get key mapping if it exists
            normalizedKeyMappings[row][col] = 
                keyMappings[row] && keyMappings[row][col] ? keyMappings[row][col] : '';
        }
    }

    const handleButtonClick = (row: number, col: number) => {
        const callback = normalizedCallbacks[row][col];
        if (callback) {
            callback();
        }
    };

    const handleKeyPress = (event: KeyboardEvent) => {
        for (let row = 0; row < ROWS; row++) {
            for (let col = 0; col < COLS; col++) {
                const keyMapping = normalizedKeyMappings[row][col];
                if (keyMapping && event.key.toLowerCase() === keyMapping.toLowerCase()) {
                    const callback = normalizedCallbacks[row][col];
                    if (callback) {
                        callback();
                    }
                    break;
                }
            }
        }
    };

    React.useEffect(() => {
        window.addEventListener('keydown', handleKeyPress);
        return () => {
            window.removeEventListener('keydown', handleKeyPress);
        };
    }, [callbacks, keyMappings]);

    return (
        <div className="command-grid">
            <div className="command-grid-container">
                {normalizedCallbacks.map((row, rowIndex) => (
                    <div key={rowIndex} className="command-grid-row">
                        {row.map((callback, colIndex) => {
                            const label = normalizedLabels[rowIndex][colIndex];
                            const keyMapping = normalizedKeyMappings[rowIndex][colIndex];
                            const isEnabled = callback !== null;
                            
                            return (
                                <button
                                    key={`${rowIndex}-${colIndex}`}
                                    className={`command-grid-button ${isEnabled ? 'enabled' : 'disabled'}`}
                                    onClick={() => handleButtonClick(rowIndex, colIndex)}
                                    disabled={!isEnabled}
                                    title={keyMapping ? `Press '${keyMapping}'` : undefined}
                                >
                                    <div className="button-content">
                                        {label && <span className="button-label">{label}</span>}
                                        {keyMapping && <span className="button-key">{keyMapping}</span>}
                                    </div>
                                </button>
                            );
                        })}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CommandGrid; 