package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean underControl = false;
    private int allianceDuration = 0;
    private boolean isAdjacentToPlayer = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        if (player.isSceptreHolder() && !allied) {
            Sceptre sceptre = player.getInventory().getFirst(Sceptre.class);
            this.allianceDuration = sceptre.getDuration();
            this.underControl = true;
            return;
        }
        bribe(player);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();

        if (allied) {
            if (underControl) {
                allianceDuration--;
                if (allianceDuration <= 0) {
                    allied = false;
                    underControl = false;
                }
            }
            nextPos = moveWhenAllied(game);
        } else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
            moveWhenInvisible(game);
            return;
        } else if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            moveWhenInvincible(game);
            return;

        } else {
            nextPos = moveNormally(game);
        }
        map.moveTo(this, nextPos);
    }

    private Position moveWhenAllied(Game game) {
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        Position nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
            isAdjacentToPlayer = true;
        return nextPos;
    }

    private void moveWhenInvisible(Game game) {
        moveRandom(game);
    }

    private Position moveNormally(Game game) {
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        return map.dijkstraPathFind(getPosition(), player.getPosition(), this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return ((!allied && canBeBribed(player)) || (player.isSceptreHolder() && !allied));
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }
}
