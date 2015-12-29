package AangUtil;

import AangUtil.Movement.MovementFunc;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

public abstract class AangScript extends PollingScript<ClientContext> implements PaintListener {
    public InteractFunc interact;
    public ChatFunc chat;
    public MiscFunc misc;
    public ObjectsFunc objects;
    public MovementFunc movement;

    public AangScript() {
        super();
        interact = InteractFunc.getInstance();
        interact.init(ctx);

        chat = ChatFunc.getInstance();
        chat.init(ctx);

        misc = MiscFunc.getInstance();
        misc.init(ctx);

        objects = ObjectsFunc.getInstance();
        objects.init(ctx);

        movement = MovementFunc.getInstance();
        movement.init(ctx);

        AangUtil.init(ctx);
    }

    @Override
    public void repaint(Graphics g) {
        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );
    }
}
