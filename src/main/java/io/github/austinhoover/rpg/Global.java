package io.github.austinhoover.rpg;

import io.github.austinhoover.kobold.Kobold;
import io.github.austinhoover.rpg.intent.ConversationHandler;
import io.github.austinhoover.rpg.intent.GameLog;
import io.github.austinhoover.rpg.intent.IntentParser;
import io.github.austinhoover.rpg.intent.MovementHandler;
import io.github.austinhoover.rpg.intent.StoryHandler;
import io.github.austinhoover.rpg.player.PlayerState;
import io.github.austinhoover.rpg.world.World;

public class Global {
    
    public static Kobold kobold = new Kobold();
    public static World world = World.loadWorld("data/testworld1.json");
    public static PlayerState player = new PlayerState();
    public static IntentParser parser = new IntentParser(kobold);
    public static ConversationHandler conversation = new ConversationHandler(world, player, kobold);
    public static MovementHandler mover = new MovementHandler(world, player, kobold, conversation);
    public static GameLog gameLog = new GameLog();
    public static StoryHandler story = new StoryHandler(world, player, kobold, conversation, gameLog);

}
