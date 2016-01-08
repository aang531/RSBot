package Util.Movement;

import Util.AangUtil;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

public class MovementFunc extends AangUtil {
    private static MovementFunc ourInstance = new MovementFunc();

    public static MovementFunc getInstance() {
        return ourInstance;
    }

    public int energy() {
        return ctx.movement.energyLevel();
    }

    public boolean running() {
        return ctx.movement.running();
    }

    public void setRunning() {
        if( !running() ) {
            ctx.input.hop(ctx.widgets.component(160, 22).centerPoint());
            Condition.sleep(50);
            ctx.input.click(true);
        }
    }

    public void setRunning(boolean running) {
        if( running() != running ) {
            ctx.input.hop(ctx.widgets.component(160, 22).centerPoint());
            Condition.sleep(50);
            ctx.input.click(true);
        }
    }

    public boolean tileOnMap(Tile t){
        Point p = t.matrix(ctx).mapPoint();
        return p.x > 570 && p.x < 570 + 145 && p.y > 9 && p.y < 9 + 151 &&
                p.distanceSq(568+13,125+13) > 13*13 && p.distanceSq(545+16,4+16) > 19*19;
    }

    public boolean tileOnScreen(Tile t){
        return misc.pointOnScreen(t.matrix(ctx).centerPoint());
    }

    public boolean traversePath(Path path){
        return path.traverse();
    }

    public boolean walkTile(Tile t){
        if( tileOnScreen(t) && !ctx.bank.opened()){
            ctx.input.hop(t.matrix(ctx).centerPoint());
            Condition.sleep(50);
            //t.matrix(ctx).click("Walk here");
            int index = misc.getMenuOptionIndex("Walk here","");
            if( index == 0 )
                ctx.input.click(true);
            else{
                ctx.input.click(false);
                Condition.sleep(50);
                menu.clickMenuOption(index);
            }
            return true;
        }else if( tileOnMap(t)){
            ctx.input.hop(t.matrix(ctx).mapPoint());
            Condition.sleep(50);
            ctx.input.click(true);
            return true;
        }else return false;
    }
}
