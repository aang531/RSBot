package AangBlueDye;

import AangUtil.AangScript;
import AangUtil.Movement.Obstacle;
import AangUtil.Movement.Path;
import AangUtil.Movement.RectTileArea;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;

import java.awt.*;

@Script.Manifest(name = "AangBlueDye", description = "Converts woad leaves into blue dye.", properties="client=4")
public class AangBlueDye extends AangScript implements PaintListener {
    private long startTime;

    private int dyePrice;
    private int dyesGained;

    private Area bankArea = new Area( new Tile(3092,3246), new Tile(3092,3243), new Tile(3094,3243), new Tile(3094,3246) );
    private Area witchArea = new Area( new Tile(3083,3261), new Tile(3083,3256), new Tile(3089,3256), new Tile(3089,3261) );

    private Area doorArea = new Area( new Tile(3087,3260), new Tile(3087,3257), new Tile(3091,3257), new Tile(3091,3260) );
    private TilePath toBank = ctx.movement.newTilePath( new Tile(3096,3262), new Tile(3102,3262), new Tile(3103,3258),
            new Tile(3101,3255), new Tile(3097,3252), new Tile(3094,3248), new Tile(3093,3244) );
    private TilePath toWitch = ctx.movement.newTilePath( new Tile(3092,3244), new Tile(3092,3247), new Tile(3096,3251),
            new Tile(3101,3254), new Tile(3102,3258), new Tile(3103,3262), new Tile(3099,3262),
            new Tile(3095,3262), new Tile(3091,3260), new Tile(3087,3259) );

    private Path pathToBank = new Path(new Tile[]{new Tile( 3088,3258), new Tile(3096,3262), new Tile(3102,3262), new Tile(3103,3258),
            new Tile(3101,3255), new Tile(3097,3252), new Tile(3094,3248), new Tile(3093,3244)},
            new Obstacle[]{new Obstacle(7122,new Tile(3088,3258,0),new RectTileArea(3086, 3256, 2, 5),"Open")});

    private Npc witch;
    private int bankID = 11744, witchID = 4284, leaveID = 1793, dyeID = 1767;
    private int closedDoorID = 7122, opendDoorID = 7123;

    @Override
    public void start(){
        startTime = System.currentTimeMillis();

        Thread tmp = new Thread() {
            public void run() {
                dyePrice = misc.getGEPrice(dyeID);
            }
        };
        tmp.start();
    }

    String status = "NONE";

    @Override
    public void stop(){
        long timeRunning = System.currentTimeMillis() - startTime;
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        int profit = (dyePrice-5) * dyesGained;

        System.out.println("Script stopped after "+hours+":"+mins+":"+seconds);
        System.out.println("Profit/h: " + (int)(profit/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Leaves/h: " + (int)(dyesGained/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Gained " + profit);
        System.out.println("Collected " + dyesGained + " leaves.");
    }

    @Override
    public void repaint(Graphics g) {
        super.repaint(g);

        g.drawString( "inv full: " + misc.inventoryFull(), 5, 100);
        g.drawString("Status: " + status, 5, 112);
    }

    private boolean atBank(){
        return bankArea.contains(ctx.players.local());
    }

    private boolean atWitch(){
        return witchArea.contains(ctx.players.local());
    }

    @Override
    public void poll() {
        while(ctx.game.tab() != Game.Tab.INVENTORY)
            misc.openInventory();
        if( movement.energy() > 75 && !movement.running() ) {
            movement.setRunning();
        }
        if( misc.inventoryFull() ){
            if(atBank() || ctx.bank.opened()){
                if( ctx.bank.opened() ){
                    status = "Depositing items";
                    ctx.bank.deposit(dyeID, Bank.Amount.ALL);
                }else{
                    status = "Opening bank";
                    ctx.objects.select().id(bankID).nearest().poll().click("Bank", "Bank booth");
                }
            }else{
                status = "Walking to bank";
                pathToBank.traverse();
                //toBank.traverse();
            }
        }else{
            if( atWitch() ){
                if( chat.chatting()){
                    if( chat.canContinue()) {
                        status = "Talking to witch";
                        chat.continueChat();
                    }
                }else {
                    if (ctx.inventory.selectedItem().id() == leaveID) {
                        if (witch == null || !witch.valid())
                            witch = ctx.npcs.select().id(witchID).nearest().poll();
                        if (witch != null && witch.valid()) {
                            status = "Using leaves on witch";
                            interact.useItemOnNpc(witch);
                            Condition.sleep(200);
                        }
                    }else{
                        status = "Selecting leaves";
                        interact.clickInvItem(ctx.inventory.select().id(leaveID).poll());
                    }
                }
            }else{
                status = "Walking to witch";
                toWitch.traverse();
            }
        }
    }
}
