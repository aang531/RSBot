package Util.Movement;

import Util.AangUtil;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

public class Obstacle {

    public enum ObstacleType{
        OBJECT,
        NPC
    }

    public int id;
    public Tile obstacleTile;
    public TileArea area;
    public String action;
    public int[] bounds;
    public ObstacleType type;

    public Obstacle(){
        id = -1;
        obstacleTile = Tile.NIL;
        area = new TileArea(-1,-1,0,0);
        action = "NULL";
        bounds = new int[]{};
        type = ObstacleType.OBJECT;
    }

    public Obstacle(int id, Tile obstacleTile, TileArea area, String action ) {
        this.id = id;
        this.obstacleTile = obstacleTile;
        this.area = area;
        this.action = action;
        this.bounds = new int[]{};
        type = ObstacleType.OBJECT;
    }

    public Obstacle(int id, Tile obstacleTile, TileArea area, String action, int[] bounds ) {
        this.id = id;
        this.obstacleTile = obstacleTile;
        this.area = area;
        this.action = action;
        this.bounds = bounds;
        type = ObstacleType.OBJECT;
    }

    public Obstacle(int id, TileArea area, String action ) {
        this.id = id;
        this.area = area;
        this.action = action;
        this.bounds = new int[]{};
        type = ObstacleType.NPC;
    }

    public Obstacle(int id, TileArea area, String action, int[] bounds ) {
        this.id = id;
        this.area = area;
        this.action = action;
        this.bounds = bounds;
        type = ObstacleType.NPC;
    }

    public boolean check() {
        if( area.contains(AangUtil.ctx.players.local().tile())){
            if( type == ObstacleType.OBJECT )
                return checkObject();
            else if( type == ObstacleType.NPC )
                return checkNPC();
        }
        return false;
    }

    private boolean checkObject(){
        GameObject o = AangUtil.objects.get(id, obstacleTile);
        if( o != null ) {
            if( bounds.length != 0)
                o.bounds(bounds);
            if( !AangUtil.misc.pointOnScreen(o.centerPoint()))
                return false;
            if(AangUtil.ctx.inventory.selectedItem().valid() ){
                AangUtil.ctx.input.hop(AangUtil.ctx.inventory.selectedItem().centerPoint());
                Condition.sleep(50);
                AangUtil.ctx.input.click(true);
            }
            AangUtil.objects.click(o,action);
            Condition.sleep(200);
            return true;
        }
        return false;
    }

    private boolean checkNPC(){
        Npc npc = AangUtil.npcs.getNearest(id);
        if( npc != null ) {
            if( bounds.length != 0)
                npc.bounds(bounds);
            if( !AangUtil.misc.pointOnScreen(npc.centerPoint()))
                return false;
            if(AangUtil.inventory.itemSelected() ){
                AangUtil.inventory.unselectItem();
            }
            AangUtil.npcs.click(npc,action);
            Condition.sleep(200);
            return true;
        }
        return false;
    }
}
