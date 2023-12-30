package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    private final int enemyTarget;

    public EnemiesGoal(int enemyTarget) {
        this.enemyTarget = enemyTarget;
    }

    public boolean areAllSpawnersGone(Game game) {
        // Check zombie toast spawners
        long spawnerCount = game.getMap().getEntities(ZombieToastSpawner.class).stream().count();
        return spawnerCount == 0;
    }

    @Override
    public boolean achieved(Game game) {
        int enemiesDestroyed = game.getEnemiesDestroyedCount();
        return enemiesDestroyed >= enemyTarget && areAllSpawnersGone(game);
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":enemies";
    }
}
