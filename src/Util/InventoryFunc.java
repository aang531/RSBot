package Util;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;

public class InventoryFunc extends AangUtil {
    private static InventoryFunc ourInstance = new InventoryFunc();

    public static InventoryFunc getInstance() {
        return ourInstance;
    }

    private static final int INVENTORY_WIDGET = 149;

    private Component component(){
        return ctx.widgets.widget(INVENTORY_WIDGET).component(0);
    }

    public boolean opened() {
        return ctx.game.tab() == Game.Tab.INVENTORY;
    }

    public int count(){
        int ret = 0;
        for( int i : component().itemIds() )
            if( i != -1 )
                ret++;
        return ret;
    }

    public int count(int id){
        int ret = 0;
        for( int i : component().itemIds() )
            if( i != -1 && i == id )
                ret++;
        return ret;
    }

    public boolean full(){
        return count() == 28;
    }

    public boolean contains(int id){
        for( int i : component().itemIds() )
        if( i == id )
            return true;
        return false;
    }

    public Item getFirst(int id){
        Component c = component();
        for( int i = 0; i < c.itemIds().length; i++ )
            if( c.itemIds()[i] == id )
                return new Item(ctx, c, i, c.itemIds()[i], c.itemStackSizes()[i]);
        return null;
    }

    public int selectedItemIndex(){
        return ctx.client().getSelectionType() == 1 ? ctx.client().getSelectionIndex() : -1;
    }

    public Item selectedItem() {
        Component c = component();
        int index = selectedItemIndex();
        if( index == -1 )
            return null;
        return new Item(ctx, c,index,c.itemIds()[index], c.itemStackSizes()[index]);
    }

    public boolean itemSelected() {
        return selectedItemIndex() != -1;
    }

    public void unselectItem() {
        if( opened() ) {
            clickItem(selectedItem());
        }else{
            ctx.input.hop(530,150);
            sleep(50);
            ctx.input.click(true);
        }
    }

    public boolean clickItem( Item item, String action ){
        while( item != null ) {
            int index = menu.getIndex(action, item.name());
            if( !ctx.menu.opened()) {
                ctx.input.hop(item.centerPoint());
                sleep(50);
                index = menu.getIndex(action, item.name());
                if( index == 0)
                    return ctx.input.click(true);
                else
                    ctx.input.click(false);
            }else{
                if( index != -1 )
                    return menu.clickMenuOption(index);
                else
                    ctx.menu.close();
                return false;
            }
        }
        return false;
    }

    public boolean selectItem(Item item){
        return clickItem(item,"Use");
    }

    public boolean clickItem( final Item item) {
        if( item != null ) {
            ctx.input.hop(item.centerPoint());
            sleep(50);
            if( ctx.menu.commands()[0].option.equals(item.name()))
                return ctx.input.click(true);
        }
        return false;
    }
}
