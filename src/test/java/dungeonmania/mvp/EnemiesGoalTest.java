package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemiesGoalTest {
    @Test
    @Tag("v2: 1-1")
    @DisplayName("Test with one enemy goal and no spawners")
    public void enemyGoalOneEnemy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemyGoalNoSpawnersOneEnemy",
                "c_basicGoalsTest_enemyGoalNoSpawnersOneEnemy");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to Right
        res = dmc.tick(Direction.LEFT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to Left to kill a spider
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
    }

    @Test
    @Tag("v2: 1-2")
    @DisplayName("Test with enemy goal and exit goal")
    public void enemyGoalwithExitGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemyGoalAndExit", "c_basicGoalsTest_enemyGoalAndExit");

        // assert goals not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // Player moves down to anticipate spider movement
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // assert enemy goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // Player moves right to engage with spider
        res = dmc.tick(Direction.RIGHT);

        // assert enemy goal still not met (1 spider left)
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // Player moves left and then down to encounter the second spider
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // assert enemy goal met, exit goal not met
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // Player moves to the exit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        // assert all goals met
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertFalse(TestUtils.getGoals(res).contains(":exit"));
    }

    @Test
    @Tag("v2: 1-3")
    @DisplayName("Test for a scenario where the player fails")
    public void failingEnemyGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemyGoalNoSpawnersOneEnemy",
                "c_basicGoalsTest_enemyGoalNoSpawnersOneEnemy");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to Right
        res = dmc.tick(Direction.LEFT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // // move player to Left to kill a spider
        // res = dmc.tick(Direction.LEFT);

        // assert goal does not met
        assertEquals(":enemies", TestUtils.getGoals(res));
    }
}
