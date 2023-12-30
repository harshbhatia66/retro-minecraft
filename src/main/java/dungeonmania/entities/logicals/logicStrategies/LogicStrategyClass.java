package dungeonmania.entities.logicals.logicStrategies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Conductor;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicStrategyClass implements LogicStrategy {
    @Override
    public abstract boolean canActivate(GameMap map, Position position);

    protected List<Conductor> getCardinallyAdjacentConductors(GameMap map, Position position) {
        List<Conductor> cardinallyAdjacentConductors = new ArrayList<Conductor>();
        for (Position cardinallyAdjacentPosition : position.getCardinallyAdjacentPositions()) {
            for (Entity entity : map.getEntities(cardinallyAdjacentPosition)) {
                if (entity instanceof Conductor) {
                    cardinallyAdjacentConductors.add((Conductor) entity);
                }
            }
        }
        return cardinallyAdjacentConductors;
    }

    protected int numConductors(GameMap map, Position position) {
        return getCardinallyAdjacentConductors(map, position).size();
    }

    protected int numActivatedConductors(GameMap map, Position position) {
        int numActivatedConductors = 0;
        for (Conductor conductor : getCardinallyAdjacentConductors(map, position)) {
            if (conductor.isActivated()) {
                numActivatedConductors++;
            }
        }
        return numActivatedConductors;
    }

    protected int numInactiveConductors(GameMap map, Position position) {
        return numConductors(map, position) - numActivatedConductors(map, position);
    }
}
