package io.github.austinhoover.backend.rpg.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class LocationTest {
    
    @Test
    public void testParseNearbyLocationResponse_CommaSeparated() {
        String input = "warehouse, storage room";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("warehouse"));
        assertTrue(result.contains("storage room"));
    }

    @Test
    public void testParseNearbyLocationResponse_NumberedList() {
        String input = "1. hidden door\n2. back entrance to a mansion";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("hidden door"));
        assertTrue(result.contains("back entrance to a mansion"));
    }

    @Test
    public void testParseNearbyLocationResponse_NumberedWithParentheses() {
        String input = "1) warehouse\n2) cellar";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("warehouse"));
        assertTrue(result.contains("cellar"));
    }

    @Test
    public void testParseNearbyLocationResponse_DashList() {
        String input = "- alley\n- courtyard";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("alley"));
        assertTrue(result.contains("courtyard"));
    }

    @Test
    public void testParseNearbyLocationResponse_WithArticles() {
        String input = "A hidden door\nAn alley\nThe warehouse";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(3, result.size());
        assertTrue(result.contains("hidden door"));
        assertTrue(result.contains("alley"));
        assertTrue(result.contains("warehouse"));
    }

    @Test
    public void testParseNearbyLocationResponse_MixedFormat() {
        String input = "1. A hidden door\n- An alley\nThe warehouse";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(3, result.size());
        assertTrue(result.contains("hidden door"));
        assertTrue(result.contains("alley"));
        assertTrue(result.contains("warehouse"));
    }

    @Test
    public void testParseNearbyLocationResponse_WithQuotes() {
        String input = "\"warehouse\", \"storage room\"";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("warehouse"));
        assertTrue(result.contains("storage room"));
    }

    @Test
    public void testParseNearbyLocationResponse_ComplexCase() {
        String input = "1. A hidden door\n2. \"The warehouse\"\n- An alley\n3) A secret room.";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(4, result.size());
        assertTrue(result.contains("hidden door"));
        assertTrue(result.contains("warehouse"));
        assertTrue(result.contains("alley"));
        assertTrue(result.contains("secret room"));
    }

    @Test
    public void testParseNearbyLocationResponse_DashArticles() {
        String input = "- An underground passage\n - A dark apartment building";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("underground passage"));
        assertTrue(result.contains("dark apartment building"));
    }

    @Test
    public void testParseNearbyLocationResponse_UnnescessaryDetailParentheses() {
        String input = "1. Alleyway (if the alley branches out)\n2. Warehouse (as it could be located behind one of the tall buildings)";
        List<String> result = Location.parseNearbyLocationResponse(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("alleyway"));
        assertTrue(result.contains("warehouse"));
    }
} 