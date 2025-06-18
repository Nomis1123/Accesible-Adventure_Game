
import java.io.IOException;

import AdventureModel.AdventureGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("DOWN, NORTH, IN, WEST, UP, SOUTH", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    void custom_getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.getRooms().get(6).getCommands();
        assertEquals("FORCED", commands);
    }

    @Test
    void custom_getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.player.takeObject("BIRD");
        game.movePlayer("IN");
        game.player.dropObject("BIRD");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a copy of an illuminated manuscript, a water bird", objects);
    }

}
