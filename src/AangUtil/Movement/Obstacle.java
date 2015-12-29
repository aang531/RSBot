package AangUtil.Movement;

import AangUtil.AangUtil;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;

public class Obstacle {
    private int id;
    private Tile obstacleTile;
    private TileArea beforeArea;
    private String action;
    private int[] bounds;

    public Obstacle(int id, Tile obstacleTile, TileArea beforeArea, String action ) {
        this.id = id;
        this.obstacleTile = obstacleTile;
        this.beforeArea = beforeArea;
        this.action = action;
        this.bounds = new int[]{};
    }

    public Obstacle(int id, Tile obstacleTile, TileArea beforeArea, String action, int[] bounds ) {
        this.id = id;
        this.obstacleTile = obstacleTile;
        this.beforeArea = beforeArea;
        this.action = action;
        this.bounds = bounds;
    }

    public boolean check() {
        if( beforeArea.contains(AangUtil.ctx.players.local().tile())){
            GameObject o = AangUtil.objects.get(id, obstacleTile);
            if( o != null ) {
                if( bounds.length != 0)
                    o.bounds(bounds);
                AangUtil.objects.interact(o,action);
                Condition.sleep(200);
                return true;
            }
        }
        return false;
    }
}
