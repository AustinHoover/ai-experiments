import React, { useState } from 'react';
import './App.css';
import MessageLog from './components/MessageLog';
import TextInput from './components/TextInput';

function App() {
  const [messages, setMessages] = useState<string[]>([
    "Welcome to the RPG Game!",
    "The world is being generated...",
    "Loading game data...",
    "Ready to begin your adventure!",
  ]);

  const handleNewMessage = (message: string) => {
    setMessages(prevMessages => [...prevMessages, message]);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>RPG Game</h1>
      </header>
      <main className="App-main">
        <MessageLog messages={messages} />
        <TextInput onNewMessage={handleNewMessage} />
      </main>
    </div>
  );
}

export default App; 