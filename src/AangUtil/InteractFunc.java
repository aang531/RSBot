package AangUtil;

import org.powerbot.script.Condition;
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
        while( gi.valid() && MiscFunc.getInstance().pointOnScreen(gi.centerPoint()) ) {
            int index = MiscFunc.getInstance().getMenuOptionIndex("Take", gi.name());
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
        while( gi.valid() && MiscFunc.getInstance().pointOnScreen(gi.centerPoint()) ) {
            int index = MiscFunc.getInstance().getMenuOptionIndex(action, option);
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

    public boolean attackMonster( final Npc npc ) {
        while( npc.valid() && MiscFunc.getInstance().pointOnScreen(npc.centerPoint())){
            int index = MiscFunc.getInstance().getMenuOptionIndex("Attack",npc.name() + "  (level-" + npc.combatLevel() + ")");
            if( !ctx.menu.opened()) {
                ctx.input.move(npc.centerPoint());
                Condition.sleep(30);
                index = MiscFunc.getInstance().getMenuOptionIndex("Attack",npc.name() + "  (level-" + npc.combatLevel() + ")");
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

    public boolean interactNPC( final Npc npc, String action ) {
        while( npc.valid() && MiscFunc.getInstance().pointOnScreen(npc.centerPoint())){
            int index = MiscFunc.getInstance().getMenuOptionIndex(action,npc.name() + (npc.combatLevel() != 0 ? "  (level-" + npc.combatLevel() + ")" : ""));
            if( !ctx.menu.opened()) {
                ctx.input.move(npc.centerPoint());
                Condition.sleep(30);
                index = MiscFunc.getInstance().getMenuOptionIndex(action,npc.name() + (npc.combatLevel() != 01 ? "  (level-" + npc.combatLevel() + ")" : ""));
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

    public boolean useItemOnNpc( final Npc npc) {
        while( npc.valid() && MiscFunc.getInstance().pointOnScreen(npc.centerPoint())){
            int index = MiscFunc.getInstance().getMenuOptionIndex("Use", ctx.inventory.selectedItem().name() + " -> " + npc.name() + (npc.combatLevel() != 0 ? "  (level-" + npc.combatLevel() + ")" : ""));
            if( !ctx.menu.opened()) {
                ctx.input.hop(npc.centerPoint());
                Condition.sleep(30);
                index = MiscFunc.getInstance().getMenuOptionIndex("Use",ctx.inventory.selectedItem().name() + " -> " + npc.name() + (npc.combatLevel() != 0 ? "  (level-" + npc.combatLevel() + ")" : ""));
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
            int index = MiscFunc.getInstance().getMenuOptionIndex(action, item.name());
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
            ctx.input.hop(item.centerPoint());
            Condition.sleep(50);
            if( ctx.menu.commands()[0].option.equals(item.name()))
                return ctx.input.click(true);
        }
        return false;
    }
}