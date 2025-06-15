import React from 'react';
import './LoadingSpinner.css';

const LoadingSpinner: React.FC = () => {
    return (
        <div className="spinner">
            <div className="spinner-inner"></div>
        </div>
    );
};

export default LoadingSpinner; 