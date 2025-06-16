import React, { useState } from 'react';
import MessageLog from '../components/MessageLog';
import './CombatPage.css';

const CombatPage: React.FC = () => {
    const [messages, setMessages] = useState<string[]>([]);
    const [error, setError] = useState<string | null>(null);

    const handleRefresh = () => {
        // TODO: Implement combat message fetching
        setMessages(['Combat system coming soon...']);
    };

    return (
        <div className="combat-page">
            <div className="combat-content">
                <MessageLog 
                    messages={messages} 
                    onRefresh={handleRefresh}
                    error={error}
                />
            </div>
        </div>
    );
};

export default CombatPage; 