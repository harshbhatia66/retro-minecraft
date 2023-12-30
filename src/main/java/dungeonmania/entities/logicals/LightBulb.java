package dungeonmania.entities.logicals;

import dungeonmania.util.Position;

public class LightBulb extends Logical {
    public LightBulb(Position position, String logic, boolean isActivated) {
        super(position, logic, isActivated);
    }

    public LightBulb(Position position, String logic) {
        super(position, logic);
    }
}
