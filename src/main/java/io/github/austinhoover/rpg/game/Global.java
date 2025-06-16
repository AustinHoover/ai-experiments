package io.github.austinhoover.rpg.game;

import io.github.austinhoover.rpg.kobold.Kobold;
import io.github.austinhoover.rpg.game.intent.ConversationHandler;
import io.github.austinhoover.rpg.game.intent.GameLog;
import io.github.austinhoover.rpg.game.intent.IntentParser;
import io.github.austinhoover.rpg.game.intent.MovementHandler;
import io.github.austinhoover.rpg.game.intent.StoryHandler;
import io.github.austinhoover.rpg.game.player.PlayerState;
import io.github.austinhoover.rpg.game.world.World;

public class Global {
    
    public static Kobold kobold = new Kobold();
    public static World world = new World();
    public static PlayerState player = new PlayerState();
    public static IntentParser parser = new IntentParser(kobold);
    public static ConversationHandler conversation = new ConversationHandler(world, player, kobold);
    public static MovementHandler mover = new MovementHandler(world, player, kobold, conversation);
    public static GameLog gameLog = new GameLog();
    public static StoryHandler story = new StoryHandler(world, player, kobold, conversation, gameLog);

}
