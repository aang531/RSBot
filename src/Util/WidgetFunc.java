package Util;

import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Widget;

import java.awt.*;

public class WidgetFunc extends AangUtil {
    private static WidgetFunc ourInstance = new WidgetFunc();

    public static WidgetFunc getInstance() {
        return ourInstance;
    }

    public Component component(int widget, int component){
        return ctx.widgets.component(widget,component);
    }

    public Component component(int widget, int component, int component2) {
        return ctx.widgets.component(widget,component).component(component2);
    }

    public Widget widget(int widget){
        return ctx.widgets.widget(widget);
    }

    public boolean clickComponent(Component c){
        if( !c.valid() || !c.visible() )
            return false;
        ctx.input.hop(c.centerPoint());
        AangUtil.sleep(60);
        ctx.input.click(true);
        return true;
    }

    public boolean clickComponent(Component c, int xoffset, int yoffset){
        if( !c.valid() || !c.visible() )
            return false;
        Point p = new Point(xoffset,yoffset);
        p.translate(c.screenPoint().x,c.screenPoint().y);
        ctx.input.hop(p);
        AangUtil.sleep(60);
        ctx.input.click(true);
        return true;
    }

    public boolean clickComponent(int widget, int component){
        return clickComponent(component(widget,component));
    }

    public boolean clickComponent(int widget, int component, int component2){
        return clickComponent(component(widget,component, component2));
    }

    public boolean clickComponent(int widget, int component, int xoffset, int yoffset){
        return clickComponent(component(widget,component),xoffset,yoffset);
    }

    public boolean clickComponent(int widget, int component, int component2, int xoffset, int yoffset){
        return clickComponent(component(widget,component,component2),xoffset,yoffset);
    }

    public boolean clickComponentItem(int widget, int component, String action ){
        Component c = component(widget, component);
        return clickComponentItem(c,action);
    }

    public boolean clickComponentItem(Component c, String action ){
        while( c != null && c.valid() && misc.pointOnScreen(c.centerPoint())){
            Item i = new Item(ctx, c, c.itemId(), c.itemStackSize());
            int index = menu.getIndex(action,i.name());
            if( !ctx.menu.opened()) {
                ctx.input.hop(c.centerPoint());
                sleep(100);
                index = menu.getIndex(action,i.name());
                if( index == 0 ){
                    return ctx.input.click(true);
                }else{
                    ctx.input.click(false);
                }
            }else{
                if( index != -1)
                    return menu.clickMenuOption(index);
                else
                    ctx.menu.close();
                return false;
            }
        }
        return false;
    }
}
