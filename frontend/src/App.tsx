import React from 'react';
import './App.css';
import ExplorePage from './pages/ExplorePage';

const App: React.FC = () => {
    return (
        <div className="App">
            <header className="App-header">
                <h1>RPG Game</h1>
            </header>
            <main className="App-main">
                <ExplorePage />
            </main>
        </div>
    );
};

export default App; 