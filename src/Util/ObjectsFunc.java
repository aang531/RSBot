package Util;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import java.util.List;

public class ObjectsFunc extends AangUtil {
    private static ObjectsFunc ourInstance = new ObjectsFunc();

    public static ObjectsFunc getInstance() {
        return ourInstance;
    }

    public GameObject getNearest( int id ) {
        List<GameObject> objects =  ctx.objects.get();
        Tile pt = ctx.players.local().tile();
        int closest = -1;
        int closestDist = Integer.MAX_VALUE;
        for(int i = 0; i < objects.size(); i++ ) {
            if( objects.get(i).id() == id ) {
                Tile tt = objects.get(i).tile();
                int dx = pt.x() - tt.x();
                int dy = pt.y() - tt.y();
                int dist = dx*dx+dy*dy;
                if( dist < closestDist ){
                    closest = i;
                    closestDist = dist;
                }
            }
        }
        if( closest != -1 )
            return objects.get(closest);
        return null;
    }

    public GameObject get(int id) {
        List<GameObject> objects =  ctx.objects.get();
        for(int i = 0; i < objects.size(); i++ ) {
            if( objects.get(i).id() == id ) {
                return objects.get(i);
            }
        }
        return null;
    }

    public GameObject get(int id, Tile t) {
        List<GameObject> objects =  ctx.objects.get();
        for(int i = 0; i < objects.size(); i++ ) {
            Tile ot = objects.get(i).tile();
            if( objects.get(i).id() == id && ot.x() == t.x() && ot.y() == t.y()) {
                return objects.get(i);
            }
        }
        return null;
    }

    public boolean click(GameObject obj, String action ) {
        while( obj != null && obj.valid() && misc.pointOnScreen(obj.centerPoint())){
            int index = misc.getMenuOptionIndex(action,obj.name());
            if( !ctx.menu.opened()) {
                ctx.input.hop(obj.centerPoint());
                sleep(100);
                index = misc.getMenuOptionIndex(action,obj.name());
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

    public boolean useItem( GameObject obj ) {
        while( obj.valid() && misc.pointOnScreen(obj.centerPoint())){
            int index = MiscFunc.getInstance().getMenuOptionIndex("Use", inventory.selectedItem().name() + " -> " + obj.name());
            if( !ctx.menu.opened()) {
                ctx.input.hop(obj.centerPoint());
                sleep(60);
                index = MiscFunc.getInstance().getMenuOptionIndex("Use", inventory.selectedItem().name() + " -> " + obj.name());
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
