package AangWine;

import Util.AangScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Interactive;

import java.awt.*;

@Script.Manifest(name = "AangWine", description = "Telegrabs zammy wine", properties="client=4")
public class AangWine extends AangScript implements PaintListener, MessageListener {

    private static final int wineID = 245, lawRune = 563, airStaff = 1381, bankID = 24101, waterRune = 555;

    private int winePrice = 0, lawPrice = 0, waterRunePrice = 0;

    private GameObject table;

    private boolean running = false;
    private Thread mainThread;

    private int itemsInInv;
    private int winesGained = 0;
    private int tooLateTimes = 0;

    private long startTime;

    private TilePath toAltar = ctx.movement.newTilePath( new Tile(2946,3368), new Tile(2946,3373), new Tile(2949,3375),
            new Tile(2956,3382), new Tile(2961,3384), new Tile(2961,3388), new Tile(2964,3393),
            new Tile(2964,3398), new Tile(2964,3401), new Tile(2967,3407), new Tile(2966,3411),
            new Tile(2962,3415), new Tile(2960,3418), new Tile(2958,3419), new Tile(2958,3424),
            new Tile(2956,3428), new Tile(2953,3431), new Tile(2952,3438), new Tile(2950,3441),
            new Tile(2949,3444), new Tile(2947,3451), new Tile(2946,3456), new Tile(2945,3463),
            new Tile(2945,3469), new Tile(2945,3476), new Tile(2945,3482), new Tile(2942,3490),
            new Tile(2942,3498), new Tile(2941,3503), new Tile(2941,3511), new Tile(2942,3516),
            new Tile(2931,3515) );

    private Area bankArea = new Area( new Tile(2948,3368), new Tile(2944,3368), new Tile(2944,3372), new Tile(2948,3372) );

    private Area altarArea = new Area( new Tile(2931,3517), new Tile(2933,3517), new Tile(2933,3514), new Tile(2931,3514) );

    private TilePath teleportToBank = ctx.movement.newTilePath( new Tile(2963,3379), new Tile(2959,3379), new Tile(2955,3379),
            new Tile(2951,3378), new Tile(2947,3375), new Tile(2946,3369) );

    private long lastTimeClick = 0;

    private void startGrabThread(){
        if( mainThread != null ) {
            running = false;
            mainThread.interrupt();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
            }
        }
        running = true;
        mainThread = new Thread() {
            public void run() {
                while(running && !isInterrupted()) {
                    if (ctx.magic.spell() != Magic.Spell.TELEKINETIC_GRAB)
                        ctx.magic.cast(Magic.Spell.TELEKINETIC_GRAB);
                    ctx.input.hop(table.centerPoint());
                    if( System.currentTimeMillis() - lastTimeClick > 2000 && itemsInInv == 28 ) {
                        running = false;
                        break;
                    }
                    if (ctx.menu.commands()[0].action.equals("Cast") && ctx.menu.commands()[0].option.equals("Telekinetic Grab -> Wine of zamorak")) {
                        if (System.currentTimeMillis() - lastTimeClick > 4000) {
                            ctx.input.click(true);
                            lastTimeClick = System.currentTimeMillis();
                            itemsInInv++;
                            winesGained++;
                            try {
                                sleep(20000);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
                System.out.println("thread stopped");
            }
        };
        mainThread.start();
    }

    private void stopGrabThread() {
        running = false;
        if( mainThread != null ) {
            mainThread.interrupt();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean atAltar(){
        return altarArea.contains(ctx.players.local());
    }

    public boolean atBank(){
        return bankArea.contains(ctx.players.local());
    }

    @Override
    public void start() {
        System.out.println("Script Started!");

        while(ctx.game.tab() != Game.Tab.INVENTORY)
            misc.openInventory();
        itemsInInv = ctx.inventory.select().count();
        System.out.println(itemsInInv);

        Thread tmp = new Thread() {
            public void run() {
                winePrice = misc.getGEPrice(wineID);
                lawPrice = misc.getGEPrice(lawRune);
                waterRunePrice = misc.getGEPrice(waterRune);
            }
        };

        tmp.start();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        System.out.println("Script Stopped!");
        stopGrabThread();

        int runeCost = (tooLateTimes + winesGained)*lawPrice;
        int wineCost = winesGained * winePrice;
        int profit = wineCost-runeCost;

        long timeRunning = System.currentTimeMillis() - startTime;
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        System.out.println("Script stopped after "+hours+":"+mins+":"+seconds);
        System.out.println("Profit/h: " + (int)(profit/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Gained " + profit);
        System.out.println("Collected " + winesGained + " wines, missed " + tooLateTimes + " times");
    }

    @Override
    public void suspend() {
        System.out.println("Script paused!");
        stopGrabThread();
    }

    @Override
    public void resume() {
        System.out.println("Script resumed!");
        startGrabThread();
    }

    @Override
    public void poll() {
        if( atAltar() ) {
            if (itemsInInv == 28) {
                stopGrabThread();
                if( !running ) {
                    misc.openMagicTab();
                    if( System.currentTimeMillis() - lastTimeClick > 4000 ) {
                        if (ctx.magic.spell() != null) {
                            ctx.input.hop(0, 0);
                            ctx.input.click(true);
                        }
                        ctx.magic.cast(Magic.Spell.FALADOR_TELEPORT);
                        lastTimeClick = System.currentTimeMillis();
                    }
                }
            }else{
                if( table == null || !table.valid())
                    table = ctx.objects.select().id(595).each(Interactive.doSetBounds(new int[]{-16,16,-124,-80,-16-84,16-84})).nearest().poll();
                if( !running && !ctx.players.local().inMotion() ){
                    startGrabThread();
                }
            }
        }else if( atBank() ){
            if( ctx.movement.energyLevel() >= 75 )
                ctx.movement.running(true);
            if (itemsInInv == 28) {
                if(ctx.bank.opened()){
                    ctx.bank.deposit(wineID, Bank.Amount.ALL);
                    if( ctx.bank.opened() )
                        itemsInInv = ctx.inventory.select().count();
                }else {
                    ctx.objects.select().id(bankID).nearest().poll().click("Bank", "Bank booth");
                }
            }else{
                toAltar.traverse();
            }
        }else{
            if (itemsInInv == 28) {
                teleportToBank.traverse();
            }else{
                toAltar.traverse();
            }
        }
    }

    @Override
    public void repaint(Graphics g) {

        g.setColor(Color.white);

        int runeCost = (tooLateTimes + winesGained)*lawPrice;
        int wineCost = winesGained * winePrice;
        int profit = wineCost-runeCost;

        long timeRunning = System.currentTimeMillis() - startTime;
        g.drawString("Profit/h: " + (int)(profit/(timeRunning/3600000.0f)),5,372);
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        g.drawString("Time Running "+hours+":"+mins+":"+seconds,5,288);
        g.drawString("Items in inv: " + itemsInInv, 5, 300);
        g.drawString("Times missed: " + tooLateTimes, 5, 312);
        g.drawString("Wines grabbed: " + winesGained, 5, 324);

        g.drawString("Rune Cost: " + runeCost,5,336);
        g.drawString("Price of Wines: " + wineCost,5,348);
        g.drawString("Profit: " + profit,5,360);

        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.text().equals("Too late - it's gone!")) {
            itemsInInv--;
            winesGained--;
            tooLateTimes++;
        }
    }
}
