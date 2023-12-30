package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.util.Position;

public class MidnightArmour extends Buildable {
    private double attack;
    private double defence;

    public MidnightArmour(double attack, double defence, Position position) {
        super(position, Integer.MAX_VALUE);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence, 1, 1));
    }
}
