package AangUtil.Movement;

import AangUtil.AangUtil;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;

public class Path {

    Tile[] tilePath;
    Obstacle[] obstacles;

    int lastIndex = 0;

    public Path(Tile[] tilePath, Obstacle[] obstacles){
        this.tilePath = tilePath;
        this.obstacles = obstacles;
    }

    public boolean traverse(){
        for( int i = 0; i < obstacles.length; i++ ) {
            if( obstacles[i].check() )
                return true;
        }
        for( int i = Math.min(lastIndex + 2,tilePath.length - 1); i >= 0; i-- ){
            if( AangUtil.movement.tileOnMap(tilePath[i]) && tilePath[i].matrix(AangUtil.ctx).reachable()){
                lastIndex = i;
                boolean ret = AangUtil.movement.walkTile(tilePath[i]);
                Condition.sleep(200);
                return ret;
            }
        }
        for( int i = tilePath.length-1; i >= Math.min(lastIndex + 2,tilePath.length - 1); i-- ){
            if( AangUtil.movement.tileOnMap(tilePath[i]) && tilePath[i].matrix(AangUtil.ctx).reachable()){
                lastIndex = i;
                boolean ret = AangUtil.movement.walkTile(tilePath[i]);
                Condition.sleep(200);
                return ret;
            }
        }
        return false;
    }

}
