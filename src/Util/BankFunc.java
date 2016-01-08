package Util;

import org.powerbot.script.rt4.Component;

public class BankFunc extends AangUtil {
    private static BankFunc ourInstance = new BankFunc();

    public static BankFunc getInstance() {
        return ourInstance;
    }

    public static final int WIDGET_BANK = 12;
    public static final int COMPONENT_TABS = 10;
    public static final int COMPONENT_ITEMS = 12;
    public static final int COMPONENT_ITEM = 22;
    public static final int COMPONENT_NOTE = 23;
    public static final int COMPONENT_DEPOSIT_ALL = 27;
    public static final int COMPONENT_DEPOSIT_ARMOR = 29;

    public boolean opened(){
        return widgets.component(WIDGET_BANK,3).visible();
    }

    public boolean depositAll(){
        return widgets.clickComponent(WIDGET_BANK,COMPONENT_DEPOSIT_ALL);
    }

    public boolean depositAll(int id ){
        return inventory.clickItem(inventory.getFirst(id),"Deposit-All");
    }

    public boolean depositArmor(){
        return widgets.clickComponent(WIDGET_BANK,COMPONENT_DEPOSIT_ARMOR);
    }

    public boolean withdrawX(int id, int amount){
        Component c = getItemComponent(id);//TODO make sure item is on screen
        if( c == null )
            return false;
        while( !ChatFunc.getInstance().pendingInput() ) {
            widgets.clickComponentItem(c, "Withdraw-X");
            sleep(400);
        }
        if( ChatFunc.getInstance().pendingInput() ){
            ctx.input.sendln(""+amount);
            return true;
        }
        return false;
    }

    public boolean withdrawAll(int id){
        Component c = getItemComponent(id);
        return widgets.clickComponentItem(c,"Withdraw-All");
    }

    private Component getItemComponent(int id){
        Component c = widgets.component(WIDGET_BANK,COMPONENT_ITEMS);
        for( int i = 0; i < c.componentCount(); i++ ){
            if( c.components()[i].itemId() == id ) {
                return c.component(i);
            }
        }
        return null;
    }
}
