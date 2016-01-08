package AangBlueDye;

import Util.AangScript;
import Util.Movement.Obstacle;
import Util.Movement.Path;
import Util.Movement.TileArea;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;

import java.awt.*;

@Script.Manifest(name = "AangBlueDye", description = "Converts woad leaves into blue dye.", properties="client=4;author=aang521")
public class AangBlueDye extends AangScript implements PaintListener {
    private long startTime;

    private int dyePrice, leafPrice;
    private int dyesGained;

    private Area bankArea = new Area( new Tile(3092,3246), new Tile(3092,3243), new Tile(3094,3243), new Tile(3094,3246) );
    private Area witchArea = new Area( new Tile(3083,3261), new Tile(3083,3256), new Tile(3089,3256), new Tile(3089,3261) );

    private Npc witch;

    private static final int bankID = 11744, witchID = 4284, leafID = 1793, dyeID = 1767;
    private int closedDoorID = 7122;
    private int[] doorBounds = new int[] {118,128,-235,0,20,130};

    private Path pathToBank = new Path(new Tile[]{new Tile( 3088,3258), new Tile(3096,3262), new Tile(3102,3262), new Tile(3103,3258),
            new Tile(3101,3255), new Tile(3097,3252), new Tile(3094,3248), new Tile(3093,3244)},
            new Obstacle[]{new Obstacle(closedDoorID,new Tile(3088,3258,0),new TileArea(3086, 3256, 2, 5),"Open",doorBounds)});
    private Path pathToWitch = new Path(new Tile[]{new Tile(3092,3244), new Tile(3092,3247), new Tile(3096,3251),
            new Tile(3101,3254), new Tile(3102,3258), new Tile(3103,3262), new Tile(3099,3262),
            new Tile(3095,3262), new Tile(3091,3260), new Tile(3087,3259)},
            new Obstacle[]{new Obstacle(closedDoorID,new Tile(3088,3258,0),new TileArea(3089, 3257, 3, 3),"Open",doorBounds)});

    private int  itemsInInv;

    @Override
    public void start(){
        startTime = System.currentTimeMillis();

        Thread tmp = new Thread() {
            public void run() {
                dyePrice = misc.getGEPrice(dyeID);
                leafPrice = misc.getGEPrice(leafID);
            }
        };
        tmp.start();

        while(ctx.game.tab() != Game.Tab.INVENTORY) {
            misc.openInventory();
            Condition.sleep(50);
        }
        itemsInInv = ctx.inventory.select().count();
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

        int profit = (dyePrice - 5 - leafPrice*2) * dyesGained;

        System.out.println("Script stopped after "+hours+":"+mins+":"+seconds);
        System.out.println("Profit/h: " + (int)(profit/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Leaves/h: " + (int)(dyesGained/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Gained " + profit);
        System.out.println("Collected " + dyesGained + " leaves.");
    }

    @Override
    public void repaint(Graphics g) {
        try {
            super.repaint(g);

            g.drawString("Status: " + status, 5, 112);

            g.setColor(Color.black);
            g.fillRect(0, 264, 150, 72 + 2);

            g.setColor(Color.white);
            g.drawRect(0, 264, 150, 72 + 2);
            int profit = (dyePrice - 5 - leafPrice * 2) * dyesGained;

            long timeRunning = System.currentTimeMillis() - startTime;
            g.drawString("Profit/h: " + (int) (profit / (timeRunning / 3600000.0f)), 5, 324);
            g.drawString("Dyes/h: " + (int) (dyesGained / (timeRunning / 3600000.0f)), 5, 336);
            long hours = timeRunning / 3600000;
            timeRunning -= hours * 3600000;
            long mins = timeRunning / 60000;
            timeRunning -= mins * 60000;
            long seconds = timeRunning / 1000;

            g.drawString("Time Running " + hours + ":" + mins + ":" + seconds, 5, 276);
            g.drawString("Profit: " + profit, 5, 288);
            g.drawString("Coins spent: " + dyesGained * 5, 5, 300);
            g.drawString("Dyes gained: " + dyesGained, 5, 312);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean atBank(){
        return bankArea.contains(ctx.players.local());
    }

    private boolean atWitch(){
        return witchArea.contains(ctx.players.local());
    }

    @Override
    public void poll() {
        while(!inventory.opened()) {
            log("opening inv");
            misc.openInventory();
            Condition.sleep(50);
        }
        if( movement.energy() > 75 && !movement.running() ) {
            movement.setRunning();
        }
        if( atWitch() ){
            int prevInInv = itemsInInv;
            itemsInInv = ctx.inventory.select().count();
            if( itemsInInv > prevInInv ) {
                dyesGained += itemsInInv - prevInInv;
            }
        }
        if( inventory.full() ){
            if(atBank() || ctx.bank.opened()){
                if( ctx.bank.opened() ){
                    status = "Depositing items";
                    ctx.bank.deposit(dyeID, Bank.Amount.ALL);
                    Condition.sleep(100);
                    itemsInInv = inventory.count();
                }else{
                    status = "Opening bank";
                    objects.click(objects.getNearest(bankID),"Bank");
                }
            }else{
                status = "Walking to bank";
                if( inventory.itemSelected() ) {
                    ctx.input.hop(0,0);
                    sleep(50);
                    ctx.input.click(true);
                }
                pathToBank.traverse();
            }
        }else{
            if( atWitch() ){
                if( chat.chatting() ){
                    status = "Talking to witch";
                    if(chat.canContinue()) {
                        status = "Continuing chat";
                        chat.continueChat();
                    }
                }else {
                    if (inventory.itemSelected() && inventory.selectedItem().id() == leafID) {
                        status = "Using leaves on witch";
                        if (witch == null || !witch.valid())
                            witch = npcs.getNearest(witchID);
                        if (witch != null && witch.valid()) {
                            npcs.useItem(witch);
                            sleep(200);
                        }
                    }else{
                        status = "Selecting leaves";
                        inventory.clickItem(inventory.getFirst(leafID));
                    }
                }
            }else{
                status = "Walking to witch";
                pathToWitch.traverse();
            }
        }
    }
}
