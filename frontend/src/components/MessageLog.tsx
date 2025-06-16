import React from 'react';
import './MessageLog.css';

interface MessageLogProps {
    messages: string[];
    onRefresh: () => void;
    error: string | null;
}

const MessageLog: React.FC<MessageLogProps> = ({ messages, onRefresh, error }) => {
    const isPlayerMessage = (message: string) => message.startsWith('> ');

    return (
        <div className="message-log">
            <div className="message-log-header">
                <div className="message-log-title">Game Log</div>
                <button 
                    className="refresh-button"
                    onClick={onRefresh}
                    title="Refresh messages"
                >
                    ↻
                </button>
            </div>
            <div className="message-log-content">
                {error && <div className="error-message">{error}</div>}
                {messages.map((message, index) => (
                    <div 
                        key={index} 
                        className={`message ${isPlayerMessage(message) ? 'player-message' : 'story-message'}`}
                    >
                        {message}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MessageLog; 