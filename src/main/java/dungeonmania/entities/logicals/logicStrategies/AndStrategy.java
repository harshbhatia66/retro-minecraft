package dungeonmania.entities.logicals.logicStrategies;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AndStrategy extends LogicStrategyClass {
    @Override
    public boolean canActivate(GameMap map, Position position) {
        return numActivatedConductors(map, position) >= 2 && numInactiveConductors(map, position) == 0;
    }
}
