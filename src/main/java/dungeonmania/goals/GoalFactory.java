package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        String goalType = jsonGoal.getString("goal");
        switch (goalType) {
        case "AND":
            JSONArray andSubgoals = jsonGoal.getJSONArray("subgoals");
            return new AndGoal(createGoal(andSubgoals.getJSONObject(0), config),
                    createGoal(andSubgoals.getJSONObject(1), config));
        case "OR":
            JSONArray orSubgoals = jsonGoal.getJSONArray("subgoals");
            return new OrGoal(createGoal(orSubgoals.getJSONObject(0), config),
                    createGoal(orSubgoals.getJSONObject(1), config));
        case "exit":
            return new ExitGoal();
        case "boulders":
            return new BouldersGoal();
        case "treasure":
            int treasureGoal = config.optInt("treasure_goal", 1);
            return new TreasureGoal(treasureGoal);
        case "enemies":
            int enemyGoal = config.optInt("enemy_goal");
            return new EnemiesGoal(enemyGoal);
        default:
            throw new IllegalArgumentException("Unknown goal type: " + goalType);
        }
    }
}
