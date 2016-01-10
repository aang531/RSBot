package Util;

import org.powerbot.script.rt4.Actor;

import java.awt.*;

public class InteractFunc extends AangUtil {
    private static InteractFunc ourInstance = new InteractFunc();

    public static InteractFunc getInstance() {
        return ourInstance;
    }

    public boolean clickInteractable( Actor obj, String action, String option ){
        int tries = 0;
        Point p = obj.nextPoint();
        while( tries < 5 && obj.valid()){
            if( !misc.pointOnScreen(p) ){
                tries++;
                p = obj.nextPoint();
                continue;
            }
            if( !menu.opened()) {
                ctx.input.hop(p);
                sleep(80);
                final int index = misc.getMenuOptionIndex(action,option);
                if( index == 0 ){
                    ctx.input.click(true);
                    sleep(80);
                    return game.getCrosshair() == GameFunc.Crosshair.ACTION;
                }else if( index != -1 ){
                    ctx.input.click(false);
                    sleep(80);
                }
            }else{
                final int index = misc.getMenuOptionIndex(action,option);
                if( index != -1) {
                    menu.clickMenuOption(index);
                    sleep(80);
                    return game.getCrosshair() == GameFunc.Crosshair.ACTION;
                }else {
                    menu.close();
                    sleep(80);
                }
            }
            tries++;
            p = obj.nextPoint();
        }
        return false;
    }

    public boolean clickInteractableCenter( Actor obj, String action, String option ){
        int tries = 0;
        while( tries < 5 && obj.valid() && misc.pointOnScreen(obj.centerPoint())){
            if( !menu.opened()) {
                ctx.input.hop(obj.centerPoint());
                sleep(80);
                final int index = misc.getMenuOptionIndex(action,option);
                if( index == 0 ){
                    ctx.input.click(true);
                    sleep(80);
                    return game.getCrosshair() == GameFunc.Crosshair.ACTION;
                }else{
                    ctx.input.click(false);
                    sleep(80);
                }
            }else{
                final int index = misc.getMenuOptionIndex(action,option);
                if( index != -1) {
                    menu.clickMenuOption(index);
                    sleep(80);
                    return game.getCrosshair() == GameFunc.Crosshair.ACTION;
                }else {
                    menu.close();
                    sleep(80);
                }
            }
            tries++;
        }
        return false;
    }
}
