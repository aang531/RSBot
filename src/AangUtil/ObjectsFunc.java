package AangUtil;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.List;

public class ObjectsFunc {
    private ClientContext ctx;
    private static ObjectsFunc ourInstance = new ObjectsFunc();

    public static ObjectsFunc getInstance() {
        return ourInstance;
    }

    public void init(ClientContext ctx)
    {
        this.ctx = ctx;
    }

    public GameObject getNearest( int id ) {
        List<GameObject> objects =  ctx.objects.get();
        int closest = -1;
        int closestDist = Integer.MAX_VALUE;
        for(int i = 0; i < objects.size(); i++ ) {
            if( objects.get(i).id() == id ) {
                Tile pt = ctx.players.local().tile(), tt = objects.get(i).tile();
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

    public boolean interact( GameObject obj, String action ) {
        while( obj.valid() && MiscFunc.getInstance().pointOnScreen(obj.centerPoint())){
            int index = MiscFunc.getInstance().getMenuOptionIndex(action,obj.name());
            if( !ctx.menu.opened()) {
                ctx.input.move(obj.centerPoint());
                Condition.sleep(30);
                index = MiscFunc.getInstance().getMenuOptionIndex(action,obj.name());
                if( index == 0 ){
                    return ctx.input.click(true);
                }else{
                    ctx.input.click(false);
                }
            }else{
                if( index != -1)
                    return AangUtil.interact.clickMenuOption(index);
                else
                    ctx.menu.close();
                return false;
            }
        }
        return false;
    }

}
