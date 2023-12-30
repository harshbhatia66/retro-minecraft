package dungeonmania.entities.logicals.logicStrategies;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class VoidStrategy extends LogicStrategyClass {
    @Override
    public boolean canActivate(GameMap map, Position position) {
        return false;
    }
}
