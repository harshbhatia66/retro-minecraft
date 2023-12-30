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

public class LogicalBombTest {
    @Test
    @DisplayName("Logical Bomb Basic 'Or' Logic")
    public void basicOrLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalBombTest_basicOrLogic", "c_logicalBombTest_basicOrLogic");

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, getLogicalBombs(res).size());
    }

    @Test
    @DisplayName("Logical Bomb Basic 'And' Logic")
    public void basicAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalBombTest_basicAndLogic", "c_logicalBombTest_basicAndLogic");

        assertEquals(1, getLogicalBombs(res).size());

        // 1 activated conductor
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors
        res = dmc.tick(Direction.LEFT);

        assertEquals(0, getLogicalBombs(res).size());
    }

    @Test
    @DisplayName("Logical Bomb Basic 'Xor' Logic")
    public void basicXorLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalBombTest_basicXorLogic", "c_logicalBombTest_basicXorLogic");

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors (does not detonate)
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 1 activate conductor (detonates)
        res = dmc.tick(Direction.UP);

        assertEquals(1, getLogicalBombs(res).size());
    }

    @Test
    @DisplayName("Logical Bomb Basic 'CoAnd' Logic")
    public void basicCoAndLogicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalBombTest_basicCoAndLogic", "c_logicalBombTest_basicCoAndLogic");

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors on different ticks
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 1 activated conductor (again)
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 0 activated conductors
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getLogicalBombs(res).size());
        assertEquals(getLogicalBombs(res).get(0).getPosition(), new Position(3, 2));

        // 2 activated conductors on the same tick
        res = dmc.tick(Direction.DOWN);

        assertEquals(0, getLogicalBombs(res).size());
    }

    public List<EntityResponse> getLogicalBombs(DungeonResponse res) {
        return TestUtils.getEntities(res, "bomb");
    }
}
