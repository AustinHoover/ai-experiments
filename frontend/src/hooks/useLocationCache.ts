import { useState, useCallback } from 'react';

interface Location {
    id: number;
    type: string;
    description: string;
    neighborIds: number[];
}

export const useLocationCache = () => {
    const [cache, setCache] = useState<Map<number, Location>>(new Map());

    const getLocation = useCallback((id: number): Location | undefined => {
        return cache.get(id);
    }, [cache]);

    const setLocation = useCallback((location: Location) => {
        setCache(prevCache => {
            const newCache = new Map(prevCache);
            newCache.set(location.id, location);
            return newCache;
        });
    }, []);

    const clearCache = useCallback(() => {
        setCache(new Map());
    }, []);

    return {
        getLocation,
        setLocation,
        clearCache
    };
}; 