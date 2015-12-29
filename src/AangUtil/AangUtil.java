package AangUtil;

import AangUtil.Movement.MovementFunc;
import org.powerbot.script.rt4.ClientContext;

public class AangUtil {
    public static void init(ClientContext ctx)
    {
        AangUtil.ctx = ctx;
    }

    public static ClientContext ctx;

    public final static InteractFunc interact = InteractFunc.getInstance();
    public final static ChatFunc chat = ChatFunc.getInstance();
    public final static MiscFunc misc = MiscFunc.getInstance();
    public final static ObjectsFunc objects = ObjectsFunc.getInstance();
    public final static MovementFunc movement = MovementFunc.getInstance();
}