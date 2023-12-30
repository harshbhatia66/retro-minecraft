package dungeonmania.entities.buildables;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import dungeonmania.util.Position;

public class BuildableFactory {
    private final Map<String, BiFunction<Position, Map<String, Object>, Buildable>> buildableCreators;

    public BuildableFactory() {
        buildableCreators = new HashMap<>();
        buildableCreators.put("Shield", this::createShield);
        buildableCreators.put("Bow", this::createBow);
        buildableCreators.put("Sceptre", this::createSceptre);
        buildableCreators.put("MidnightArmour", this::createMidnightArmour);
    }

    public Buildable createBuildable(String type, Position position, Map<String, Object> properties) {
        if (!buildableCreators.containsKey(type)) {
            throw new IllegalArgumentException("Unrecognized buildable item requested: " + type);
        }
        return buildableCreators.get(type).apply(position, properties);
    }

    private Buildable createShield(Position position, Map<String, Object> properties) {
        int durability = getIntProperty(properties, "durability");
        double defence = getDoubleProperty(properties, "defence");
        return new Shield(durability, defence, position);
    }

    private Buildable createBow(Position position, Map<String, Object> properties) {
        int durability = getIntProperty(properties, "durability");
        return new Bow(durability, position);
    }

    private Buildable createSceptre(Position position, Map<String, Object> properties) {
        int durability = getIntProperty(properties, "durability");
        int mindControlDuration = getIntProperty(properties, "mindControlDuration");
        return new Sceptre(durability, mindControlDuration, position);
    }

    private Buildable createMidnightArmour(Position position, Map<String, Object> properties) {
        double attack = getDoubleProperty(properties, "attack");
        double defence = getDoubleProperty(properties, "defence");
        return new MidnightArmour(attack, defence, position);
    }

    private int getIntProperty(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new IllegalArgumentException("Expected integer for property: " + key);
    }

    private double getDoubleProperty(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value instanceof Double) {
            return (double) value;
        }
        throw new IllegalArgumentException("Expected double for property: " + key);
    }
}
