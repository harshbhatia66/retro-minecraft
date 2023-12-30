package dungeonmania.goals;

import dungeonmania.Game;

public interface Goal {
    boolean achieved(Game game);

    String toString(Game game);
}
