package dungeonmania.entities.logicals;

import dungeonmania.entities.Activatable;
import dungeonmania.entities.logicals.logicStrategies.AndStrategy;
import dungeonmania.entities.logicals.logicStrategies.CoAndStrategy;
import dungeonmania.entities.logicals.logicStrategies.LogicStrategy;
import dungeonmania.entities.logicals.logicStrategies.OrStrategy;
import dungeonmania.entities.logicals.logicStrategies.VoidStrategy;
import dungeonmania.entities.logicals.logicStrategies.XorStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Logical extends Activatable {
    private LogicStrategy logicStrategy;

    public Logical(Position position, String logic, boolean isActivated) {
        super(position, isActivated);
        switch (logic) {
        case "and":
            logicStrategy = new AndStrategy();
            break;

        case "or":
            logicStrategy = new OrStrategy();
            break;

        case "xor":
            logicStrategy = new XorStrategy();
            break;

        case "co_and":
            logicStrategy = new CoAndStrategy();
            break;

        default:
            logicStrategy = new VoidStrategy();
        }
    }

    public Logical(Position position, String logic) {
        this(position, logic, false);
    }

    public void updateStatus(GameMap map) {
        if (logicStrategy.canActivate(map, getPosition())) {
            activate(map);
        } else {
            deactivate(map);
        }
    }
}
