package dungeonmania.goals;

import java.util.List;
import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class ExitGoal implements Goal {
    @Override
    public boolean achieved(Game game) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> exits = game.getMap().getEntities(Exit.class);
        return exits.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }

    @Override
    public String toString(Game game) {
        return this.achieved(game) ? "" : ":exit";
    }
}
