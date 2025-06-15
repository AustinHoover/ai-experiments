package io.github.austinhoover.rpg.game.intent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IntentParserTest {
    @Test
    public void testParseLook() {
        String input = "look";
        IntentParser.Intent intent = new IntentParser(null).parse(input);
        assertEquals(IntentParser.Type.LOOK, intent.type);
        assertNull(intent.target);
        assertNull(intent.message);
    }

    @Test
    public void testParseUnknown() {
        String input = "jump";
        IntentParser.Intent intent = new IntentParser(null).parse(input);
        assertEquals(IntentParser.Type.UNKNOWN, intent.type);
        assertNull(intent.target);
        assertNull(intent.message);
    }
} 