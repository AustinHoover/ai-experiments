import React from 'react';
import './MessageLog.css';

interface MessageLogProps {
    messages: string[];
}

const MessageLog: React.FC<MessageLogProps> = ({ messages }) => {
    return (
        <div className="message-log">
            <div className="message-log-content">
                {messages.map((message, index) => (
                    <div key={index} className="message">
                        {message}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MessageLog; 