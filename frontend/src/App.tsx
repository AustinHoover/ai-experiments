import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import ExplorePage from './pages/ExplorePage';
import CombatPage from './pages/CombatPage';

const App: React.FC = () => {
    return (
        <Router>
            <div className="App">
                <header className="App-header">
                    <h1>RPG Game</h1>
                </header>
                <main className="App-main">
                    <Routes>
                        <Route path="/explore" element={<ExplorePage />} />
                        <Route path="/combat" element={<CombatPage />} />
                        <Route path="/" element={<Navigate to="/explore" replace />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
};

export default App; 