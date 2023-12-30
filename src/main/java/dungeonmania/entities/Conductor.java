package dungeonmania.entities;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Conductor extends Activatable {
    public Conductor(Position position, boolean isActivated) {
        super(position, isActivated);
    }

    public Conductor(Position position) {
        this(position, false);
    }

    public List<Wire> getCardinallyAdjacentWires(GameMap map) {
        return map.getEntities(Wire.class).stream()
                .filter(w -> getPosition().getCardinallyAdjacentPositions().contains(w.getPosition()))
                .collect(Collectors.toList());
    }

    public void activateCardinallyAdjacentWires(GameMap map) {
        getCardinallyAdjacentWires(map).stream().filter(w -> !w.isActivated()).forEach(w -> {
            w.activate(map);
        });
    }

    @Override
    public void onDestroy(GameMap map) {
        map.updateCircuit();
    }

    @Override
    public void onActivate(GameMap map) {
        activateCardinallyAdjacentWires(map);
    }
}
