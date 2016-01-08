package Util;

import org.powerbot.script.rt4.GroundItem;

public class GroundItemFunc extends AangUtil {
    private static GroundItemFunc ourInstance = new GroundItemFunc();

    public static GroundItemFunc getInstance() {
        return ourInstance;
    }

    public GroundItem get(int id){
        for(GroundItem gi : ctx.groundItems.get() )
            if( gi.id() == id )
                return gi;
        return null;
    }

    public boolean pickup( final GroundItem gi ) {
        while( gi.valid() && misc.pointOnScreen(gi.centerPoint()) ) {
            int index = misc.getMenuOptionIndex("Take", gi.name());
            if( !ctx.menu.opened() ) {
                ctx.input.hop(gi.centerPoint());
                sleep(100);
                index = misc.getMenuOptionIndex("Take", gi.name());
                if (index == 0) {
                    ctx.input.click(true);
                    return ctx.menu.commands()[0].action.equals("Take") && ctx.menu.commands()[0].option.equals(gi.name());
                } else {
                    ctx.input.click(false);
                }
            }else{
                if( index == -1 ) {
                    ctx.menu.close();
                    return false;
                }
                return menu.clickMenuOption(index);
            }
        }
        return false;
    }

    public boolean click( GroundItem gi, String action, String option) {
        while( gi.valid() && misc.pointOnScreen(gi.centerPoint()) ) {
            int index = misc.getMenuOptionIndex(action, option);
            if( !ctx.menu.opened() ) {
                ctx.input.hop(gi.centerPoint());
                sleep(100);
                index = misc.getMenuOptionIndex(action, option);
                if (index == 0) {
                    ctx.input.click(true);
                    return ctx.menu.commands()[0].action.equals(action) && ctx.menu.commands()[0].option.equals(option);
                } else {
                    ctx.input.click(false);
                }
            }else{
                if( index == -1 ) {
                    ctx.menu.close();
                    return false;
                }
                return menu.clickMenuOption(index);
            }
        }
        return false;
    }
}
