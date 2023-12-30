package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface CircuitObserver extends CircuitComponent {
    public void subscribe(CircuitSubject subject);

    public void notify(GameMap map, int tick);
}
