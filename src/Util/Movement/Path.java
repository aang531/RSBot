package Util.Movement;

import Util.AangUtil;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;

public class Path {

    Tile[] tilePath;
    Obstacle[] obstacles;

    int lastIndex = Integer.MAX_VALUE;

    public Path(Tile[] tilePath, Obstacle[] obstacles){
        this.tilePath = tilePath;
        this.obstacles = obstacles;
    }

    public Path(Tile[] tilePath ){
        this.tilePath = tilePath;
        this.obstacles = new Obstacle[]{};
    }

    public boolean traverse(){
        for (Obstacle obstacle : obstacles) {
            if (obstacle.check())
                return true;
        }
        for( int i = Math.min(lastIndex + 2,tilePath.length - 1); i >= 0; i-- ){
            if( AangUtil.movement.tileOnMap(tilePath[i]) && tilePath[i].matrix(AangUtil.ctx).reachable()){
                lastIndex = i;
                if( (AangUtil.ctx.movement.destination().distanceTo(tilePath[i]) > 2 || !AangUtil.ctx.players.local().inMotion()) && AangUtil.ctx.players.local().tile().distanceTo(tilePath[i]) > 0 ) {
                    boolean ret = AangUtil.movement.walkTile(tilePath[i]);
                    Condition.sleep(200);
                    return ret;
                }else{
                    return true;
                }
            }
        }
        for( int i = tilePath.length-1; i >= Math.min(lastIndex + 2,tilePath.length - 1); i-- ){
            if( AangUtil.movement.tileOnMap(tilePath[i]) && tilePath[i].matrix(AangUtil.ctx).reachable()){
                lastIndex = i;
                if( (AangUtil.ctx.movement.destination().distanceTo(tilePath[i]) > 2 || !AangUtil.ctx.players.local().inMotion()) && AangUtil.ctx.players.local().tile().distanceTo(tilePath[i]) > 0 ) {
                    boolean ret = AangUtil.movement.walkTile(tilePath[i]);
                    Condition.sleep(200);
                    return ret;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

}