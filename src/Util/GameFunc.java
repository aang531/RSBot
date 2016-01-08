package Util;

import org.powerbot.script.rt4.Constants;

public class GameFunc extends AangUtil {
    private static GameFunc ourInstance = new GameFunc();

    public static GameFunc getInstance() {
        return ourInstance;
    }

    public boolean playing() {
        final int c = ctx.client().getClientState();
        return c == Constants.GAME_LOADED;
    }

    public boolean loading() {
        final int c = ctx.client().getClientState();
        return c == Constants.GAME_LOADING;
    }
}
