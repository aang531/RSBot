package Util;

import Util.Movement.MovementFunc;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

public abstract class AangScript extends PollingScript<ClientContext> implements PaintListener {
    public final ChatFunc chat;
    public final MiscFunc misc;
    public final ObjectsFunc objects;
    public final MovementFunc movement;
    public final InventoryFunc inventory;
    public final NpcsFunc npcs;
    public final PrayerFunc prayer;
    public final WorldFunc worlds;
    public final WidgetFunc widgets;
    public final BankFunc bank;
    public final GroundItemFunc groundItems;
    public final MenuFunc menu;
    public final GameFunc game;
    public final CameraFunc camera;
    public final InteractFunc interact;

    public AangScript() {
        super();

        chat = ChatFunc.getInstance();
        misc = MiscFunc.getInstance();
        objects = ObjectsFunc.getInstance();
        movement = MovementFunc.getInstance();
        inventory = InventoryFunc.getInstance();
        npcs = NpcsFunc.getInstance();
        prayer = PrayerFunc.getInstance();
        worlds = WorldFunc.getInstance();
        widgets = WidgetFunc.getInstance();
        bank = BankFunc.getInstance();
        groundItems = GroundItemFunc.getInstance();
        menu = MenuFunc.getInstance();
        game = GameFunc.getInstance();
        camera = CameraFunc.getInstance();
        interact = InteractFunc.getInstance();

        AangUtil.init(ctx);
    }

    public static void sleep( int millis ){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void log(String message){
        System.out.println(message);
    }

    public static void logError(String message){
        System.err.println(message);
    }

    @Override
    public void repaint(Graphics g) {
        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );
    }
}
