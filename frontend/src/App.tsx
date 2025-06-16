import React, { useState, useEffect } from 'react';
import './App.css';
import MessageLog from './components/MessageLog';
import TextInput from './components/TextInput';
import Map from './components/Map';

const API_BASE_URL = 'http://localhost:8080';

const App: React.FC = () => {
    const [messages, setMessages] = useState<string[]>([]);
    const [isSimulating, setIsSimulating] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [currentLocationId, setCurrentLocationId] = useState<number | null>(null);

    const fetchMessages = async (retryCount = 0) => {
        try {
            const response = await fetch(`${API_BASE_URL}/messages`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setMessages(data);
            setError(null);
        } catch (err) {
            console.error('Error fetching messages:', err);
            if (retryCount < 1) {
                console.log('Retrying message fetch...');
                setTimeout(() => fetchMessages(retryCount + 1), 1000);
            } else {
                setError('Failed to fetch messages. Please try refreshing.');
                setMessages(['Error: Unable to load messages. Please try again.']);
            }
        }
    };

    const fetchCurrentLocation = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/location`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            setCurrentLocationId(data.id);
        } catch (err) {
            console.error('Error fetching current location:', err);
        }
    };

    const handleSimulate = async (input: string) => {
        setIsSimulating(true);
        try {
            const response = await fetch(`${API_BASE_URL}/simulate`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: input,
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // After simulation completes, fetch both messages and current location
            await Promise.all([
                fetchMessages(),
                fetchCurrentLocation()
            ]);
        } catch (err) {
            console.error('Error during simulation:', err);
            setError('Failed to process command. Please try again.');
        } finally {
            setIsSimulating(false);
        }
    };

    useEffect(() => {
        fetchMessages();
        fetchCurrentLocation();
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <h1>RPG Game</h1>
            </header>
            <main className="App-main">
                <div className="App-content">
                    <Map currentLocationId={currentLocationId} />
                    <div className="App-right-panel">
                        <MessageLog 
                            messages={messages} 
                            onRefresh={() => fetchMessages()}
                            error={error}
                        />
                        <TextInput 
                            onSubmit={(input) => setMessages(prev => [...prev, '> ' + input])}
                            onSimulate={handleSimulate}
                            isSimulating={isSimulating}
                        />
                    </div>
                </div>
            </main>
        </div>
    );
};

export default App; 