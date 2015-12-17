package AngUtilFunc;

import org.powerbot.script.Filter;
import org.powerbot.script.MenuCommand;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.awt.*;

public class InteractFunc {
    private ClientContext ctx;
    private static InteractFunc ourInstance = new InteractFunc();

    public static InteractFunc getInstance() {
        return ourInstance;
    }

    private InteractFunc() {
    }

    public void init(ClientContext ctx)
    {
        this.ctx = ctx;
    }

    public boolean clickMenuOption( int index ){
        if( ctx.menu.opened() ) {
            Point loc = ctx.menu.bounds().getLocation();
            ctx.input.hop(loc.x + 40, loc.y + 27+ index * 15);
            return ctx.input.click(true);
        }
        return false;
    }

    public boolean pickupGroundItem( final GroundItem gi ) {
        while( gi.valid() && UtilFunc.instance.pointOnScreen(gi.centerPoint()) ) {
            int index = UtilFunc.instance.getMenuOptionIndex("Take", gi.name());
            if( !ctx.menu.opened() ) {
                ctx.input.move(gi.centerPoint());
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
                return clickMenuOption(index);
            }
        }
        return false;
    }

    public boolean clickGroundItem( GroundItem gi, String action, String option) {
        while( gi.valid() && UtilFunc.instance.pointOnScreen(gi.centerPoint()) ) {
            int index = UtilFunc.instance.getMenuOptionIndex(action, option);
            if( !ctx.menu.opened() ) {
                ctx.input.move(gi.centerPoint());
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
                return clickMenuOption(index);
            }
        }
        return false;
    }

    public boolean attackNPC( final Npc npc ) {
        while( npc.valid() && UtilFunc.instance.pointOnScreen(npc.centerPoint())){
            int index = UtilFunc.instance.getMenuOptionIndex("Attack",npc.name() + "  (level-" + npc.combatLevel() + ")");
            if( !ctx.menu.opened()) {
                ctx.input.move(npc.centerPoint());
                if( index == 0 ){
                    return ctx.input.click(true);
                }else{
                    ctx.input.click(false);
                }
            }else{
                if( index != -1)
                    return clickMenuOption(index);
                else
                    ctx.menu.close();
                return false;
            }
        }
        return false;
    }

    public boolean clickInvItem(final Item item, String action ){
        while( item.valid() ) {
            int index = UtilFunc.instance.getMenuOptionIndex(action, item.name());
            if( !ctx.menu.opened()) {
                ctx.input.move(item.centerPoint());
                if( index == 0)
                    return ctx.input.click(true);
                else
                    ctx.input.click(false);
            }else{
                if( index != -1 )
                    return clickMenuOption(index);
                else
                    ctx.menu.close();
                return false;
            }
        }
        return false;
    }

    public boolean clickInvItem( final Item item, boolean leftClick) {
        if( item.valid() ) {
            ctx.input.move(item.centerPoint());
            if( ctx.menu.commands()[0].option.equals(item.name()))
                return ctx.input.click(leftClick);
        }
        return false;
    }

    public boolean clickInvItem( final Item item) {
        if( item.valid() ) {
            ctx.input.move(item.centerPoint());
            if( ctx.menu.commands()[0].option.equals(item.name()))
                return ctx.input.click(true);
        }
        return false;
    }
}