package dungeonmania.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeBody extends Enemy {
    public static final double DEFAULT_HEALTH = 5;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_ARROW_BUFF = 2;
    public static final double DEFAULT_TREASURE_BUFF = 3;
    public static final double DEFAULT_KEY_BUFF = 4;

    private double arrowBuff;
    private double treasureBuff;
    private double keyBuff;

    private Boolean isInvisible = false;
    private Boolean isInvincible = false;
    private SnakeBody subSnake = null;

    public SnakeBody(Position position, double health, double attack, double arrowBuff, double treasureBuff,
            double keyBuff) {
        super(position, health, attack);
        this.arrowBuff = arrowBuff;
        this.treasureBuff = treasureBuff;
        this.keyBuff = keyBuff;
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();

        //check if invincibility potion eaten
        if (this.getIsInvincible()) {
            //check if has subsnake
            if (this.subSnake == null) {
                g.unsubscribe(getId());
            } else {

                // get all of subSnakes stats
                BattleStatistics subBS = this.subSnake.getBattleStatistics();

                Position subPos = this.subSnake.getPosition();
                double subHealth = subBS.getHealth();
                double subAttack = subBS.getAttack();
                SnakeBody subSub = this.subSnake.getSubSnake();
                // create snake head where subsnake was
                SnakeHead newSnakeHead = new SnakeHead(subPos, subHealth, subAttack, getArrowBuff(), getTreasureBuff(),
                        getKeyBuff());
                // destroy subsnake and replace with snake head
                newSnakeHead.setSubSnake(subSub);
                g.unsubscribe(this.getId());
                map.addEntity(newSnakeHead);

            }
        } else {
            //destroys all subsnakes aswell

            destroySub(map);

            g.unsubscribe(getId());
            map.removeNode(this);

        }
    }

    public void destroySub(GameMap map) {
        if (subSnake != null) {
            subSnake.onDestroy(map);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {

        if (this.getIsInvisible()) {

            // cycle through subsnakes to see if moving onto self
            Boolean isPartOfSnake = false;
            for (Entity snakePart : this.getSubSnakes()) {
                if (snakePart == entity) {
                    isPartOfSnake = true;
                }
            }

            return ((entity instanceof Player)
                    || (((entity instanceof SnakeBody) || (entity instanceof SnakeHead)) && !isPartOfSnake));
        } else {

            return entity instanceof Player;
        }
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }

    }

    // only moves when parent snake does
    @Override
    public void move(Game game) {
        return;
    }

    public void subMove(Game game, Position pos) {

        // sets new prePov

        if (this.subSnake != null) {

            game.getMap().moveTo(this, pos);

            // moves Subsnake's Subsnake if exsists
            subSnake.subMove(game, this.getPreviousDistinctPosition());

        }

    }

    public SnakeBody getSubSnake() {
        return this.subSnake;
    }

    //recursively add body
    public void addBody(GameMap map, Position pos) {

        if (this.subSnake == null) {
            this.subSnake = new SnakeBody(pos, DEFAULT_HEALTH, DEFAULT_ATTACK, this.arrowBuff, this.treasureBuff,
                    this.keyBuff);
            //set for is invincible and is invisible
            if (this.getIsInvisible()) {
                this.subSnake.setIsInvisible(true);
            }
            //set for is invincible and is invisible
            if (this.getIsInvincible()) {
                this.subSnake.setIsInvincible(true);
            }
            map.addEntity(this.subSnake);

        } else {
            this.subSnake.addBody(map, pos);
        }
    }

    public void treasureEaten() {
        BattleStatistics snakeBS = this.getBattleStatistics();
        double currHealth = snakeBS.getHealth();
        double newHealth = currHealth + this.getTreasureBuff();
        snakeBS.setHealth(newHealth);

        // do same for subSnakes
        if (this.subSnake != null) {
            this.subSnake.treasureEaten();
        }
    }

    public void keyEaten() {
        BattleStatistics snakeBS = this.getBattleStatistics();
        double currHealth = snakeBS.getHealth();
        double newHealth = currHealth * getKeyBuff();
        snakeBS.setHealth(newHealth);

        if (this.subSnake != null) {
            this.subSnake.keyEaten();
        }
    }

    public void arrowEaten() {

        BattleStatistics snakeBS = this.getBattleStatistics();
        double currAttack = snakeBS.getAttack();
        double newAttack = currAttack + getArrowBuff();

        snakeBS.setAttack(newAttack);

        if (this.subSnake != null) {
            this.subSnake.arrowEaten();
        }

    }

    public void invisibilityPotionEaten() {
        setIsInvisible(true);

        if (this.getSubSnake() != null) {
            this.getSubSnake().invisibilityPotionEaten();
        }
    }

    public void invincibilityPotionEaten() {
        this.setIsInvincible(true);

        if (this.getSubSnake() != null) {
            this.getSubSnake().invincibilityPotionEaten();
        }
    }

    public double getArrowBuff() {
        return arrowBuff;
    }

    public double getTreasureBuff() {
        return treasureBuff;
    }

    public double getKeyBuff() {
        return keyBuff;
    }

    public Boolean getIsInvisible() {
        return isInvisible;
    }

    public void setIsInvisible(Boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public Boolean getIsInvincible() {
        return isInvincible;
    }

    public void setIsInvincible(Boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    //method that recursives provides list of all subsnakes
    public List<Entity> getSubSnakes() {
        if (this.subSnake == null) {
            List<Entity> subSnakes = new ArrayList<Entity>();
            subSnakes.add(this);
            return subSnakes;
        } else {
            List<Entity> subSnakes = this.subSnake.getSubSnakes();
            subSnakes.add(this);
            return subSnakes;
        }
    }

    public void setSubSnake(SnakeBody subSnake) {
        this.subSnake = subSnake;
    }

}
