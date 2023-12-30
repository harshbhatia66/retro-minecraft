package dungeonmania.task_2f;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.*;

public class SwitchDoorTest {
    @Test
    @DisplayName("SwitchDoor Basic 'Or' Logic")
    public void basicOrLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoorTest_basicOrLogic", "c_switchDoorTest_basicOrLogic");

        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // activate switch and move next to switch door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // successfully move into open switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 2));
    }

    @Test
    @DisplayName("SwitchDoor Basic 'And' Logic")
    public void basicAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoorTest_basicAndLogic", "c_switchDoorTest_basicAndLogic");

        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // activate 1st switch and move next to switch door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // activate 2nd switch and move next to switch door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // successfully move into open switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 2));
    }

    @Test
    @DisplayName("SwitchDoor Basic 'Xor' Logic")
    public void basicXorLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoorTest_basicXorLogic", "c_switchDoorTest_basicXorLogic");

        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // activate 1st switch and move next to switch door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // successfully move into open switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 2));

        // activate 2nd switch and move next to switch door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(2, 2));
    }

    @Test
    @DisplayName("SwitchDoor Basic 'CoAnd' Logic")
    public void basicCoAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoorTest_basicCoAndLogic", "c_switchDoorTest_basicCoAndLogic");

        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // activate 1st switch and move next to switch door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // activate 2nd switch (on different tick) and move next to switch door
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // deactivate 1st switch and move next to switch door
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // deactivate 2nd switch and move next to switch door
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // attempt to move into closed switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // activate both conductors on the same tick and move next to switch door
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 3));

        // successfully move into open switch door
        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getPlayerPos(res), new Position(3, 2));
    }
}
