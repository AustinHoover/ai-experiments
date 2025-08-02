import React, { useState, useEffect } from 'react';
import MessageLog from '../components/MessageLog';
import TextInput from '../components/TextInput';
import Map from '../components/Map';
import NearbyElements from '../components/NearbyElements';
import SceneView from '../components/SceneView';
import './ExplorePage.css';

const API_BASE_URL = 'http://localhost:8080';

interface Character {
    id: number;
    name: string;
    role: string;
    gender: string;
}

const ExplorePage: React.FC = () => {
    const [messages, setMessages] = useState<string[]>([]);
    const [isSimulating, setIsSimulating] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [currentLocationId, setCurrentLocationId] = useState<number | null>(null);
    const [characters, setCharacters] = useState<Character[]>([]);

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

    const fetchCharacters = async () => {
        if(currentLocationId === null) {
            return;
        }
        try {
            const response = await fetch(`${API_BASE_URL}/location/${currentLocationId}/characters`);
            if (!response.ok) {
                throw new Error('Failed to fetch characters');
            }
            const data = await response.json();
            setCharacters(data);
        } catch (err) {
            console.error('Error fetching characters:', err);
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
                fetchCurrentLocation(),
            ]);
        } catch (err) {
            console.error('Error during simulation:', err);
            setError('Failed to process command. Please try again.');
        } finally {
            setIsSimulating(false);
        }
    };

    const handleRefresh = () => {
        fetchMessages();
        fetchCurrentLocation();
    };

    useEffect(() => {
        fetchMessages();
        fetchCurrentLocation();
    }, []);

    useEffect(() => {
        fetchCharacters();
    }, [currentLocationId]);

    return (
        <div className="explore-page">
            <div className="explore-content">
                <div className="explore-main">
                    <Map currentLocationId={currentLocationId} />
                    <div className="explore-scene-view">
                        <SceneView currentLocationId={currentLocationId} />
                        <MessageLog 
                            messages={messages} 
                            onRefresh={handleRefresh}
                            error={error}
                        />
                    </div>
                    {/* <MessageLog 
                        messages={messages} 
                        onRefresh={handleRefresh}
                        error={error}
                    />
                    <TextInput 
                        onSubmit={(input) => setMessages(prev => [...prev, '> ' + input])}
                        onSimulate={handleSimulate}
                        isSimulating={isSimulating}
                    /> */}
                    <NearbyElements characters={characters} />
                </div>
                <div className="explore-bottom-section">
                    <TextInput 
                        onSubmit={(input) => setMessages(prev => [...prev, '> ' + input])}
                        onSimulate={handleSimulate}
                        isSimulating={isSimulating}
                    />
                </div>
            </div>
        </div>
    );
};

export default ExplorePage; 