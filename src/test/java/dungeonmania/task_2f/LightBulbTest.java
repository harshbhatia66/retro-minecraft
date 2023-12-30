package dungeonmania.task_2f;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class LightBulbTest {
    @Test
    @DisplayName("LightBulb Basic 'Or' Logic")
    public void basicOrLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_lightBulbTest_basicOrLogic", "c_lightBulbTest_basicOrLogic");

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLightBulbsOn(res).size());
        assertEquals(0, getLightBulbsOff(res).size());
        assertEquals(getLightBulbsOn(res).get(0).getPosition(), new Position(3, 2));
    }

    @Test
    @DisplayName("LightBulb Basic 'And' Logic")
    public void basicAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_lightBulbTest_basicAndLogic", "c_lightBulbTest_basicAndLogic");

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors
        res = dmc.tick(Direction.UP);

        assertEquals(1, getLightBulbsOn(res).size());
        assertEquals(0, getLightBulbsOff(res).size());
        assertEquals(getLightBulbsOn(res).get(0).getPosition(), new Position(3, 2));
    }

    @Test
    @DisplayName("LightBulb Basic 'Xor' Logic")
    public void basicXorLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_lightBulbTest_basicXorLogic", "c_lightBulbTest_basicXorLogic");

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLightBulbsOn(res).size());
        assertEquals(0, getLightBulbsOff(res).size());
        assertEquals(getLightBulbsOn(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors
        res = dmc.tick(Direction.UP);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));
    }

    @Test
    @DisplayName("LightBulb Basic 'CoAnd' Logic")
    public void basicCoAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_lightBulbTest_basicCoAndLogic", "c_lightBulbTest_basicCoAndLogic");

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor
        res = dmc.tick(Direction.UP);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors on different ticks
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor (again)
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));
        // 0 activated conductors
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, getLightBulbsOff(res).size());
        assertEquals(0, getLightBulbsOn(res).size());
        assertEquals(getLightBulbsOff(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors on the same tick
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        assertEquals(1, getLightBulbsOn(res).size());
        assertEquals(0, getLightBulbsOff(res).size());
        assertEquals(getLightBulbsOn(res).get(0).getPosition(), new Position(3, 2));
    }

    public List<EntityResponse> getLightBulbsOff(DungeonResponse res) {
        return TestUtils.getEntities(res, "light_bulb_off");
    }

    public List<EntityResponse> getLightBulbsOn(DungeonResponse res) {
        return TestUtils.getEntities(res, "light_bulb_on");
    }
}
