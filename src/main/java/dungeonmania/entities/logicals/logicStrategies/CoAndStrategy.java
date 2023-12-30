package dungeonmania.entities.logicals.logicStrategies;

import java.util.List;

import dungeonmania.entities.Conductor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class CoAndStrategy extends LogicStrategyClass {
    @Override
    public boolean canActivate(GameMap map, Position position) {
        if (!(numActivatedConductors(map, position) >= 2 && numInactiveConductors(map, position) == 0)) {
            return false;
        }

        List<Conductor> conductors = getCardinallyAdjacentConductors(map, position);
        return conductors.stream().allMatch(c -> c.getTickActivated() == conductors.get(0).getTickActivated());
    }
}
