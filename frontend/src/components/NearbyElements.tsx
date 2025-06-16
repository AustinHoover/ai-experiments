import React from 'react';
import './NearbyElements.css';

interface Character {
    id: number;
    name: string;
    role: string;
    gender: string;
}

interface NearbyElementsProps {
    characters: Character[];
}

const NearbyElements: React.FC<NearbyElementsProps> = ({ characters }) => {
    return (
        <div className="nearby-elements">
            <h2>Nearby Characters</h2>
            <div className="nearby-table-container">
                <table className="nearby-table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        {characters.map((character) => (
                            <tr key={character.id}>
                                <td>{character.name}</td>
                                <td>{character.role}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default NearbyElements; 