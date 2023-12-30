package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.buildables.BuildableFactory;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.*;
import dungeonmania.entities.logicals.LightBulb;
import dungeonmania.entities.logicals.SwitchDoor;
import dungeonmania.map.GameMap;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class EntityFactory {
    private JSONObject config;
    private Random ranGen = new Random();
    private BuildableFactory buildableFactory;

    public EntityFactory(JSONObject config) {
        this.config = config;
        this.buildableFactory = new BuildableFactory();
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0)
            return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();

        Spider dummySpider = buildSpider(new Position(0, 0)); // for checking possible positions

        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius)
                    continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np) || np.equals(player))
                    continue;
                if (map.getEntities(np).stream().anyMatch(e -> e instanceof Enemy))
                    continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = buildSpider(initPosition);
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0)
            return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> !map.getEntities(p).stream().anyMatch(e -> (e instanceof Wall)))
                .collect(Collectors.toList());
        if (pos.size() == 0)
            return;
        ZombieToast zt = buildZombieToast(pos.get(randGen.nextInt(pos.size())));
        map.addEntity(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }

    public Spider buildSpider(Position pos) {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(pos, spiderHealth, spiderAttack);
    }

    public SnakeHead buildSnakeHead(Position pos) {
        double snakeHeadHealth = config.optDouble("snake_health", SnakeHead.DEFAULT_HEALTH);
        double snakeHeadAttack = config.optDouble("snake_attack", SnakeHead.DEFAULT_ATTACK);
        double arrowBuff = config.optDouble("snake_attack_arrow_buff", SnakeHead.DEFAULT_ARROW_BUFF);
        double treasureBuff = config.optDouble("snake_health_treasure_buff", SnakeHead.DEFAULT_TREASURE_BUFF);
        double keyBuff = config.optDouble("snake_health_key_buff", SnakeHead.DEFAULT_KEY_BUFF);
        pos = pos.asLayer(0);
        return new SnakeHead(pos, snakeHeadHealth, snakeHeadAttack, arrowBuff, treasureBuff, keyBuff);
    }

    public Player buildPlayer(Position pos) {
        double playerHealth = config.optDouble("player_health", Player.DEFAULT_HEALTH);
        double playerAttack = config.optDouble("player_attack", Player.DEFAULT_ATTACK);
        return new Player(pos, playerHealth, playerAttack);
    }

    public ZombieToast buildZombieToast(Position pos) {
        double zombieHealth = config.optDouble("zombie_health", ZombieToast.DEFAULT_HEALTH);
        double zombieAttack = config.optDouble("zombie_attack", ZombieToast.DEFAULT_ATTACK);
        return new ZombieToast(pos, zombieHealth, zombieAttack);
    }

    public ZombieToastSpawner buildZombieToastSpawner(Position pos) {
        int zombieSpawnRate = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        return new ZombieToastSpawner(pos, zombieSpawnRate);
    }

    public Mercenary buildMercenary(Position pos) {
        double mercenaryHealth = config.optDouble("mercenary_health", Mercenary.DEFAULT_HEALTH);
        double mercenaryAttack = config.optDouble("mercenary_attack", Mercenary.DEFAULT_ATTACK);
        double allyAttack = config.optDouble("ally_attack", Mercenary.DEFAULT_HEALTH);
        double allyDefence = config.optDouble("ally_defence", Mercenary.DEFAULT_ATTACK);
        int mercenaryBribeAmount = config.optInt("bribe_amount", Mercenary.DEFAULT_BRIBE_AMOUNT);
        int mercenaryBribeRadius = config.optInt("bribe_radius", Mercenary.DEFAULT_BRIBE_RADIUS);
        return new Mercenary(pos, mercenaryHealth, mercenaryAttack, mercenaryBribeAmount, mercenaryBribeRadius,
                allyAttack, allyDefence);
    }

    public Bow buildBow() {
        int bowDurability = config.optInt("bow_durability");
        Map<String, Object> properties = new HashMap<>();
        properties.put("durability", bowDurability);
        return (Bow) buildableFactory.createBuildable("Bow", null, properties);
    }

    public Shield buildShield() {
        int shieldDurability = config.optInt("shield_durability");
        double shieldDefence = config.optInt("shield_defence");
        Map<String, Object> properties = new HashMap<>();
        properties.put("durability", shieldDurability);
        properties.put("defence", shieldDefence);
        return (Shield) buildableFactory.createBuildable("Shield", null, properties);
    }

    public Sceptre buildSceptre() {
        int sceptreDurability = config.optInt("sceptre_durability");
        int mindControlDuration = config.optInt("mind_control_duration");
        Map<String, Object> properties = new HashMap<>();
        properties.put("durability", sceptreDurability);
        properties.put("mindControlDuration", mindControlDuration);
        return (Sceptre) buildableFactory.createBuildable("Sceptre", null, properties);
    }

    public MidnightArmour buildMidnightArmour() {
        double attackBonus = config.optDouble("midnight_armour_attack");
        double defenseBonus = config.optDouble("midnight_armour_defence");
        Map<String, Object> properties = new HashMap<>();
        properties.put("attack", attackBonus);
        properties.put("defence", defenseBonus);
        return (MidnightArmour) buildableFactory.createBuildable("MidnightArmour", null, properties);
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));

        switch (jsonEntity.getString("type")) {
        case "player":
            return buildPlayer(pos);
        case "zombie_toast":
            return buildZombieToast(pos);
        case "zombie_toast_spawner":
            return buildZombieToastSpawner(pos);
        case "mercenary":
            return buildMercenary(pos);
        case "wall":
            return new Wall(pos);
        case "boulder":
            return new Boulder(pos);
        case "switch":
            return new Switch(pos);
        case "exit":
            return new Exit(pos);
        case "treasure":
            return new Treasure(pos);
        case "wood":
            return new Wood(pos);
        case "arrow":
            return new Arrow(pos);
        case "sun_stone":
            return new SunStone(pos);
        case "bomb":
            int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
            try {
                return new Bomb(pos, bombRadius, jsonEntity.getString("logic"));
            } catch (JSONException e) {
                return new Bomb(pos, bombRadius);
            }
        case "invisibility_potion":
            int invisibilityPotionDuration = config.optInt("invisibility_potion_duration",
                    InvisibilityPotion.DEFAULT_DURATION);
            return new InvisibilityPotion(pos, invisibilityPotionDuration);
        case "invincibility_potion":
            int invincibilityPotionDuration = config.optInt("invincibility_potion_duration",
                    InvincibilityPotion.DEFAULT_DURATION);
            return new InvincibilityPotion(pos, invincibilityPotionDuration);
        case "portal":
            return new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour")));
        case "sword":
            double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
            int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
            return new Sword(pos, swordAttack, swordDurability);
        case "spider":
            return buildSpider(pos);
        case "door":
            return new Door(pos, jsonEntity.getInt("key"));
        case "key":
            return new Key(pos, jsonEntity.getInt("key"));
        case "light_bulb_off":
            return new LightBulb(pos, jsonEntity.getString("logic"));
        case "light_bulb_on":
            return new LightBulb(pos, jsonEntity.getString("logic"), true);
        case "wire":
            return new Wire(pos);
        case "switch_door":
            return new SwitchDoor(pos, jsonEntity.getString("logic"));
        case "snake_head":
            return buildSnakeHead(pos);
        default:
            return null;
        }
    }
}
