package dungeonmania.entities.enemies;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import java.util.HashMap;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeHead extends SnakeBody {
    private SnakeBody subSnake;

    // getting config parameters
    public SnakeHead(Position position, double health, double attack, double arrowBuff, double treasureBuff,
            double keyBuff) {
        super(position, health, attack, arrowBuff, treasureBuff, keyBuff);
    }

    @Override
    public void move(Game game) {

        GameMap map = game.getMap();

        List<Entity> inventoryItems = getAllInventoryItems(map);
        // If not inventory items on map do nothing
        if (inventoryItems.size() == 0) {
            return;
        } else {

            // Find closest Inventory Item
            Map<Entity, Integer> distances = new HashMap<>();

            // base case for selected inventory item and distance
            int minDistance = -1;
            Entity entToGoto = null;

            // cycle through all inventory items and ditances
            for (Entity item : inventoryItems) {
                if (minDistance == -1) {
                    minDistance = map.dijkstraPathDist(this.getPosition(), item.getPosition(), this);
                    entToGoto = item;
                } else {
                    if (map.dijkstraPathDist(this.getPosition(), item.getPosition(), this) < minDistance) {
                        minDistance = map.dijkstraPathDist(this.getPosition(), item.getPosition(), this);
                        entToGoto = item;
                    }
                }
            }

            Position nextPos = map.dijkstraPathFind(this.getPosition(), entToGoto.getPosition(), this);
            // make prev pos into current pos just before move
            this.subMove(game, this.getPreviousDistinctPosition());
            map.moveTo(this, nextPos);
            this.checkConsume(map, inventoryItems);

        }

    }

    // gets each type individually and then casts as Entity to combine
    public List<Entity> getAllInventoryItems(GameMap map) {
        List<Entity> inventoryItems = map.getEntities(Treasure.class).stream().map((entity) -> (Entity) entity)
                .collect(Collectors.toList());
        inventoryItems.addAll(
                map.getEntities(Key.class).stream().map((entity) -> (Entity) entity).collect(Collectors.toList()));
        inventoryItems.addAll(
                map.getEntities(Arrow.class).stream().map((entity) -> (Entity) entity).collect(Collectors.toList()));
        inventoryItems.addAll(map.getEntities(InvincibilityPotion.class).stream().map((entity) -> (Entity) entity)
                .collect(Collectors.toList()));
        inventoryItems.addAll(map.getEntities(InvisibilityPotion.class).stream().map((entity) -> (Entity) entity)
                .collect(Collectors.toList()));
        return inventoryItems;
    }

    public void checkConsume(GameMap map, List<Entity> inventoryItems) {

        // check for overlap
        inventoryItems.forEach((entity) -> {
            if (entity.getPosition().equals(this.getPosition())) {
                consume(map, entity);
                return;
            }
        });
    }

    public void consume(GameMap map, Entity entity) {

        processConsumable(entity);
        map.removeNode(entity);
        this.addBody(map, this.getPreviousDistinctPosition());
    }

    public void processConsumable(Entity entity) {
        String className = entity.getClass().getSimpleName();

        switch (className) {
        case "Treasure":
            this.treasureEaten();
            break;
        case "Key":
            this.keyEaten();
            break;
        case "Arrow":
            this.arrowEaten();
            break;
        case "InvisibilityPotion":
            this.invisibilityPotionEaten();
            break;
        case "InvincibilityPotion":

            this.invincibilityPotionEaten();

            break;
        default:
            break;
        }
    }

}
