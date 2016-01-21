package Util;

import Util.Movement.MovementFunc;
import org.powerbot.script.rt4.ClientContext;

public class AangUtil {
    public static void init(ClientContext ctx)
    {
        AangUtil.ctx = ctx;
    }

    public static ClientContext ctx;

    public static final MiscFunc misc = MiscFunc.getInstance();
    public static final ObjectsFunc objects = ObjectsFunc.getInstance();
    public static final MovementFunc movement = MovementFunc.getInstance();
    public static final InventoryFunc inventory = InventoryFunc.getInstance();
    public static final NpcsFunc npcs = NpcsFunc.getInstance();
    public static final PrayerFunc prayer = PrayerFunc.getInstance();
    public static final WorldFunc worlds = WorldFunc.getInstance();
    public static final WidgetFunc widgets = WidgetFunc.getInstance();
    public static final BankFunc bank = BankFunc.getInstance();
    public static final GroundItemFunc groundItems = GroundItemFunc.getInstance();
    public static final MenuFunc menu = MenuFunc.getInstance();
    public static final GameFunc game = GameFunc.getInstance();
    public static final CameraFunc camera = CameraFunc.getInstance();
    public static final ChatFunc chat = ChatFunc.getInstance();
    public static final InteractFunc interact = InteractFunc.getInstance();
    public static final SkillsFunc skills = SkillsFunc.getInstance();

    public static void sleep( int millis ){
        AangScript.sleep(millis);
    }

    public static void log(String message){
        AangScript.log(message);
    }

    public static void logError(String message){
        AangScript.logError(message);
    }
}