package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Activatable extends Entity {
    // stores the tick of activation if the Conductor is activated, and -1 otherwise
    private int tickActivated;

    public Activatable(Position position, boolean isActivated) {
        super(position);
        if (isActivated) {
            tickActivated = 0;
        } else {
            tickActivated = -1;
        }
    }

    public Activatable(Position position) {
        this(position, false);
    }

    public boolean isActivated() {
        return tickActivated >= 0;
    }

    public void activate(GameMap map) {
        if (!isActivated()) {
            tickActivated = map.getTick();
            onActivate(map);
        }
    }

    public void deactivate(GameMap map) {
        if (isActivated()) {
            tickActivated = -1;
            onDeactivate(map);
        }
    }

    public int getTickActivated() {
        return tickActivated;
    }

    public void onActivate(GameMap map) {
        return;
    }

    public void onDeactivate(GameMap map) {
        return;
    }
}
