package dungeonmania.entities.logicals.logicStrategies;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public interface LogicStrategy {
    public boolean canActivate(GameMap map, Position position);
}
