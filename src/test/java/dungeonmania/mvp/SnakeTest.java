package dungeonmania.mvp;

import java.util.stream.Collectors;
import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.reflections.vfs.Vfs.Dir;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {
    @Test
    @Tag("16-1")
    @DisplayName("Basic Snake test")
    // basic test. Tests snake doesn't move
    public void snakeTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicSnakeTest", "c_basicSnakeTest");

        assertEquals(1, getSnakeHeads(res).size());

        Position firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        Position secondPos = getSnakeHeads(res).get(0).getPosition();
        assertTrue(firstPos.equals(secondPos));

    }

    @Test
    @Tag("16-2")
    @DisplayName("Snake movement")
    // basic test. Tests snake doesn't move
    public void snakeMovementTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        // I tried making a key instead of an arrow and it didn't work and I don't kow why
        DungeonResponse res = dmc.newGame("d_snakeMovementTest", "c_snakeMovementTest");
        assertEquals(1, getSnakeHeads(res).size());

        Position firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        Position secondPos = getSnakeHeads(res).get(0).getPosition();
        assertFalse(firstPos.equals(secondPos));
    }

    @Test
    @Tag("16-3")
    @DisplayName("Snake grow")
    // basic test. Tests snake doesn't move
    public void snakeGrowTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_snakeGrowTest", "c_snakeGrowTest");
        assertEquals(1, getSnakeHeads(res).size());

        Position firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        Position secondPos = getSnakeHeads(res).get(0).getPosition();
        assertFalse(firstPos.equals(secondPos));

        // check snakehead is where arrow was
        int secondPosX = secondPos.getX();
        int secondPosY = secondPos.getY();
        assertEquals(secondPosX, 2);
        assertEquals(secondPosY, 3);

        //tick again
        res = dmc.tick(Direction.UP);
        //check there is a snake body entity
        assertEquals(getSnakeBodies(res).size(), 1);
        // check body is at original position of head
        Position bodyPos = getSnakeBodies(res).get(0).getPosition();
        assertTrue(firstPos.equals(bodyPos));

    }

    @Test
    @Tag("16-4")
    @DisplayName("Snake two consumables")
    // basic test. Tests snake doesn't move
    public void twoConsumablesTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_twoConsumablesTest", "c_twoConumablesTest");

        assertEquals(1, getSnakeHeads(res).size());

        Position firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        Position secondPos = getSnakeHeads(res).get(0).getPosition();
        assertFalse(firstPos.equals(secondPos));
        res = dmc.tick(Direction.UP);
        // check at least one snake body
        assertTrue(getSnakeBodies(res).size() == 1);
        // after moving to arrow test again that snake moves
        firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        secondPos = getSnakeHeads(res).get(0).getPosition();

        assertFalse(firstPos.equals(secondPos));
        // more ticks until snake reaches second arrow
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);

        assertTrue(getSnakeBodies(res).size() == 2);

    }

    @Test
    @Tag("16-5")
    @DisplayName("Many Consumables")
    // basic test. Tests snake doesn't move
    public void manyConsumablesTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_manyConsumablesTest", "c_manyConsumablesTest");

        // list of all types in map
        List<String> typesPresent = res.getEntities().stream().map(entity -> entity.getType())
                .collect(Collectors.toList());
        // check none are present after consumption
        assertTrue(typesPresent.contains("treasure"));
        assertTrue(typesPresent.contains("arrow"));
        assertTrue(typesPresent.contains("invisibility_potion"));
        assertEquals(1, getSnakeHeads(res).size());
        Position firstPos = getSnakeHeads(res).get(0).getPosition();
        res = dmc.tick(Direction.DOWN);
        Position secondPos = getSnakeHeads(res).get(0).getPosition();
        assertFalse(firstPos.equals(secondPos));
        res = dmc.tick(Direction.UP);
        // check at least one snake body
        assertTrue(getSnakeBodies(res).size() == 1);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        // check two snake bodies
        assertTrue(getSnakeBodies(res).size() == 2);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        assertTrue(getSnakeBodies(res).size() == 3);

        // check again after consumption
        typesPresent = res.getEntities().stream().map(entity -> entity.getType()).collect(Collectors.toList());
        // check none are present after consumption
        assertTrue(!typesPresent.contains("tresure"));
        assertTrue(!typesPresent.contains("arrow"));
        assertTrue(!typesPresent.contains("invisibility_potion"));

    }

    @Test
    @Tag("16-6")
    @DisplayName("Simple Battle")
    public void simpleBattleTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_simpleBattleTest", "c_simpleBattleTest");

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertTrue(res.getBattles().size() == 1);

    }

    @Test
    @Tag("16-7")
    @DisplayName("Battle body")
    public void battleBodyTest() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleBodyTest", "c_battleBodyTest");

        // tick until snake is fully grown
        int distToLastConsumable = 8;
        for (int i = 0; i < distToLastConsumable; i++) {
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.LEFT);

        }
        assertTrue(getSnakeBodies(res).size() == 3);
        // move down into last snake body
        res = dmc.tick(Direction.UP);
        assertTrue(!res.getBattles().isEmpty());
        assertTrue(getSnakeBodies(res).size() == 0);

    }

    @Test
    @Tag("16-8")
    @DisplayName("Invincibility Test")
    public void invincibilityTest() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_invinciblityTest", "c_battleBodyTest");

        // tick until snake is fully grown
        int distToLastConsumable = 8;
        for (int i = 0; i < distToLastConsumable; i++) {
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.LEFT);

        }
        assertTrue(getSnakeBodies(res).size() == 3);
        // move down into last snake body
        res = dmc.tick(Direction.UP);
        assertTrue(!res.getBattles().isEmpty());

        assertEquals(getSnakeBodies(res).size(), 2);

    }

    // gets all snake head entities
    private List<EntityResponse> getSnakeHeads(DungeonResponse res) {
        return TestUtils.getEntities(res, "snake_head");
    }

    private List<EntityResponse> getSnakeBodies(DungeonResponse res) {
        return TestUtils.getEntities(res, "snake_body");
    }

}
