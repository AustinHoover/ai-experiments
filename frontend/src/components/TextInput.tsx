import React, { useState } from 'react';
import './TextInput.css';
import LoadingSpinner from './LoadingSpinner';

interface TextInputProps {
    onSubmit: (input: string) => void;
    onSimulate: (input: string) => Promise<void>;
    isSimulating: boolean;
}

const TextInput: React.FC<TextInputProps> = ({ onSubmit, onSimulate, isSimulating }) => {
    const [input, setInput] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!input.trim() || isSimulating) return;

        const trimmedInput = input.trim();
        setInput('');
        onSubmit(trimmedInput);
        
        try {
            await onSimulate(trimmedInput);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <form className="text-input-container" onSubmit={handleSubmit}>
            <input
                type="text"
                className="text-input"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="Enter your command..."
                disabled={isSimulating}
            />
            <button 
                type="submit" 
                className="submit-button"
                disabled={isSimulating || !input.trim()}
            >
                {isSimulating ? <LoadingSpinner /> : 'Submit'}
            </button>
        </form>
    );
};

export default TextInput; 