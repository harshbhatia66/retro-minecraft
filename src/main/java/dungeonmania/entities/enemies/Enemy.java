package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;
    private Random randGen = new Random();

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    public abstract void move(Game game);

    public void moveRandom(Game game) {
        Position nextPosition;
        GameMap gameMap = game.getMap();
        List<Position> validPositions = getPosition().getCardinallyAdjacentPositions();
        validPositions = validPositions.stream().filter(p -> gameMap.canMoveTo(this, p)).collect(Collectors.toList());
        if (validPositions.isEmpty()) {
            nextPosition = getPosition();
        } else {
            nextPosition = validPositions.get(randGen.nextInt(validPositions.size()));
        }
        gameMap.moveTo(this, nextPosition);
    }

    public void moveWhenInvincible(Game game) {
        GameMap map = game.getMap();
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(getPosition(), Direction.RIGHT)
                : Position.translateBy(getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(getPosition(), Direction.UP)
                : Position.translateBy(getPosition(), Direction.DOWN);
        Position offset = getPosition();

        if (plrDiff.getY() == 0 && map.canMoveTo(this, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(this, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            offset = chooseOffsetBasedOnPriority(moveX, moveY, map);
        } else {
            offset = chooseOffsetBasedOnPriority(moveY, moveX, map);
        }

        map.moveTo(this, offset);
    }

    private Position chooseOffsetBasedOnPriority(Position primary, Position secondary, GameMap map) {
        if (map.canMoveTo(this, primary))
            return primary;
        else if (map.canMoveTo(this, secondary))
            return secondary;
        else
            return getPosition();
    }
}
