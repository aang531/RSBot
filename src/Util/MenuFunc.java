package Util;

import org.powerbot.script.Condition;

import java.awt.*;

public class MenuFunc extends AangUtil {
    private static MenuFunc ourInstance = new MenuFunc();

    public static MenuFunc getInstance() {
        return ourInstance;
    }

    public boolean clickMenuOption( int index ){
        if( ctx.menu.opened() ) {
            Point loc = ctx.menu.bounds().getLocation();
            ctx.input.hop(loc.x + 40, loc.y + 27+ index * 15);
            Condition.sleep(50);
            return ctx.input.click(true);
        }
        return false;
    }

}
