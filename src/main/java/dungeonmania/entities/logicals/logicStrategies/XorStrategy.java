package dungeonmania.entities.logicals.logicStrategies;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class XorStrategy extends LogicStrategyClass {
    @Override
    public boolean canActivate(GameMap map, Position position) {
        return numActivatedConductors(map, position) == 1;
    }
}
