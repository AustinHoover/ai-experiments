import React, { useState } from 'react';
import './TextInput.css';

interface TextInputProps {
    onNewMessage: (message: string) => void;
}

const TextInput: React.FC<TextInputProps> = ({ onNewMessage }) => {
    const [inputValue, setInputValue] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!inputValue.trim()) return;

        try {
            const response = await fetch('http://localhost:8080/simulate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: inputValue,
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.text();
            onNewMessage(data);
            setInputValue(''); // Clear input after successful submission
        } catch (error) {
            console.error('Error:', error);
            onNewMessage('Error: Failed to process input');
        }
    };

    return (
        <form className="text-input-container" onSubmit={handleSubmit}>
            <input
                type="text"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                placeholder="Type your command here..."
                className="text-input"
            />
            <button type="submit" className="submit-button">
                Submit
            </button>
        </form>
    );
};

export default TextInput; 