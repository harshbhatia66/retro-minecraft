package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends Logical {
    public SwitchDoor(Position position, String logic) {
        super(position, logic);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return isActivated();
    }
}
