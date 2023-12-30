package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.SunStone;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunstones = count(SunStone.class);
        int swords = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1 || sunstones >= 1)) {
            result.add("shield");
        }
        if ((wood >= 1 || arrows >= 2) && (keys >= 1 || treasure >= 1) && sunstones >= 1) {
            result.add("sceptre");
        }
        if (swords >= 1 && sunstones >= 1) {
            result.add("midnight_armour");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player player, boolean shouldRemove, boolean requireShield,
            EntityFactory entityCreator, boolean areZombiesPresent) {

        // Use helper methods to retrieve the entities for each item type
        List<Wood> woods = getEntities(Wood.class);
        List<Arrow> arrowList = getEntities(Arrow.class);
        List<Treasure> treasures = getEntities(Treasure.class);
        List<Key> keyList = getEntities(Key.class);
        List<SunStone> sunStones = getEntities(SunStone.class);
        List<Sword> swordList = getEntities(Sword.class);

        // Check for each buildable item and return the corresponding buildable
        if (!requireShield && canBuildBow(woods, arrowList, shouldRemove)) {
            return entityCreator.buildBow();
        } else if (canBuildShield(woods, treasures, keyList, sunStones, shouldRemove)) {
            return entityCreator.buildShield();
        } else if (canBuildSceptre(woods, arrowList, keyList, treasures, sunStones, shouldRemove)) {
            return entityCreator.buildSceptre();
        } else if (canBuildMidnightArmour(swordList, sunStones, areZombiesPresent, shouldRemove)) {
            return entityCreator.buildMidnightArmour();
        }

        return null;
    }

    // Helper methods to check buildable criteria
    private boolean canBuildBow(List<Wood> woods, List<Arrow> arrows, boolean shouldRemove) {
        if (woods.size() >= 1 && arrows.size() >= 3) {
            if (shouldRemove) {
                consumeItems(woods, 1);
                consumeItems(arrows, 3);
            }
            return true;
        }
        return false;
    }

    private boolean canBuildShield(List<Wood> woods, List<Treasure> treasures, List<Key> keys, List<SunStone> sunstones,
            boolean shouldRemove) {
        if (woods.size() >= 2 && (treasures.size() >= 1 || keys.size() >= 1 || sunstones.size() >= 1)) {
            if (shouldRemove) {
                consumeItems(woods, 2);
                consumeOneItem(treasures, keys, sunstones);
            }
            return true;
        }
        return false;
    }

    private boolean canBuildSceptre(List<Wood> woods, List<Arrow> arrows, List<Key> keys, List<Treasure> treasures,
            List<SunStone> sunstones, boolean shouldRemove) {
        if ((woods.size() >= 1 || arrows.size() >= 2) && (keys.size() >= 1 || treasures.size() >= 1)
                && sunstones.size() >= 1) {
            if (shouldRemove) {
                consumeItems(woods, 1);
                consumeItems(arrows, arrows.size() >= 2 ? 2 : 0);
                consumeOneItem(treasures, keys, null);
                consumeItems(sunstones, 1);
            }
            return true;
        }
        return false;
    }

    private boolean canBuildMidnightArmour(List<Sword> swords, List<SunStone> sunstones, boolean areZombiesPresent,
            boolean shouldRemove) {
        if (swords.size() >= 1 && sunstones.size() >= 1 && !areZombiesPresent) {
            if (shouldRemove) {
                consumeItems(swords, 1);
                consumeItems(sunstones, 1);
            }
            return true;
        }
        return false;
    }

    // Helper method to remove items from inventory
    private <T> void consumeItems(List<T> itemsList, int quantity) {
        for (int i = 0; i < quantity && !itemsList.isEmpty(); i++) {
            items.remove(itemsList.remove(0));
        }
    }

    private void consumeOneItem(List<Treasure> treasures, List<Key> keys, List<SunStone> sunstones) {
        if (!treasures.isEmpty()) {
            items.remove(treasures.remove(0));
        } else if (!keys.isEmpty()) {
            items.remove(keys.remove(0));
        }
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

}
