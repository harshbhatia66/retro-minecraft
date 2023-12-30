package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for sunstone and more buildables

public class SunStoneAndBuildablesTest {
    @Test
    @Tag("v2: 2-1")
    @DisplayName("Test whether a player can pick up a SunStone")
    public void pickingUpSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_pickingUpSunStone", "c_sunstoneTest_pickingUpSunStone");

        // move player up
        res = dmc.tick(Direction.UP);

        // check if SunStone is in inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("v2: 2-2")
    @DisplayName("Test if the SunStone can be used to open a door")
    public void usingSunStoneToOpenDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_usingSunStoneToOpenDoor",
                "c_SunStoneTest_usingSunStoneToOpenDoor");

        // picking up the SunStone
        res = dmc.tick(Direction.DOWN);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Try walking through door
        res = dmc.tick(Direction.DOWN);

        // Check if door is still locked and SunStone is still in inventory
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("v2: 2-3")
    @DisplayName("Testing goal completion with SunStone")
    public void treasureGoalWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_TreasureGoalWithSunStone",
                "c_SunStoneTest_TreasureGoalWithSunStone");

        // move player down
        res = dmc.tick(Direction.DOWN);

        // check if the goal is met when the player does not have the SunStone
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up the SunStone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // check if the goal is met when the player has the SunStone
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("v2: 2-4")
    @DisplayName("Test if the SunStone can be used interchangeably with treasure or keys when building entities "
            + "- Sceptre")
    public void buildingASceptreWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_buildingASceptreWithSunStone",
                "c_BuildablesTest_buildingASceptreWithSunStone");
        // pick up the SunStone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // pick up the treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up the Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // build the sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // check if the sceptre is in the inventory
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // check if the inventory is empty
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("v2: 2-5")
    @DisplayName("Test if the SunStone can be used interchangeably with treasure or keys when building entities "
            + "- Shield")
    public void buildingAShieldWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_buildingAShieldWithSunStone",
                "c_SunStoneTest_buildingAShieldWithSunStone");

        // pick up the SunStone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // pick up the Wood
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // build the shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));

        // check if the shield is in the inventory
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
        // check if the sunstone is used
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // check if the wood is used up
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

    }

    @Test
    @Tag("v2: 6")
    @DisplayName("Confirm Midnight Armour Cannot Be Forged with Zombies Around")
    public void testNoMidnightArmourWithZombies() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_testNoMidnightArmourWithZombies",
                "c_BuildablesTest_testNoMidnightArmourWithZombies");
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Secure the SunStone first
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Next, collect the Sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Confirm the existence of ZombieToast
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());

        // Try to forge Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());

        // Ensure ingredients remain post-failed attempt
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("v2: 2-7")
    @DisplayName("Validate Midnight Armour Creation without Zombies")
    public void testMidnightArmourCreationNoZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_testMidnightArmourCreationNoZombies",
                "c_BuildablesTest_testMidnightArmourCreationNoZombies");
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Acquire SunStone first
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Then pick up the Sword
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Forge Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Check for disappearance of ingredients
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("v2: 2-8")
    @DisplayName("Verify Sceptre's Influence Over Mercenary")
    public void testSceptreMercenaryInfluence() throws InvalidActionException {
        // Initialize a dungeon with a sceptre and a mercenary
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_testSceptreMercenaryInfluence",
                "c_sceptreTest_testSceptreMercenaryInfluence");

        String mercenaryId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // Retrieve Wood
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Obtain SunStone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Collect Treasure
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Craft Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Influence the mercenary with the sceptre
        res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));

        // Verify mercenary is now allied
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, res.getBattles().size());
    }

}
