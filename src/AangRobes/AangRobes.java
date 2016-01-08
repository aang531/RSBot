package AangRobes;

import Util.AangScript;
import Util.Movement.Obstacle;
import Util.Movement.Path;
import Util.Movement.TileArea;
import org.powerbot.script.PaintListener;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.GroundItem;

import java.awt.*;

@Script.Manifest(name = "AangRobes", description = "Collets Monk robes in monastery and banks them.", properties="client=4;author=aang521")
public class AangRobes extends AangScript implements PaintListener {

    private static final int robeTopID = 544, robeBottomID = 542;
    private int robeTopPrice,robeBottomPrice;
    private static final int pickupAnim = 832;

    private static final Path toRobes = new Path( new Tile[] {  new Tile(3094,3494), new Tile(3093,3498), new Tile(3089,3501),
            new Tile(3083,3504), new Tile(3077,3504), new Tile(3075,3510), new Tile(3075,3515),
            new Tile(3072,3520), new Tile(3061,3518), new Tile(3054,3511), new Tile(3054,3505),
            new Tile(3053,3500), new Tile(3053,3493), new Tile(3058,3489), new Tile(3058,3484),
            new Tile(3058,3484,1), new Tile(3058,3487,1)},
            new Obstacle[]{  new Obstacle(2641, new Tile( 3057, 3483), new TileArea( 3056, 3482, 3, 5 ), "Climb-up", new int[]{ 0, 10, -158, 0, -32, 32 } ),
            new Obstacle(7122, new Tile( 3058, 3485, 1), new TileArea( 3055, 3482, 4, 3, 1 ), "Open", new int[]{ 30, 112, -210, -19, 120, 123 } )});

    private static final Path toBank = new Path( new Tile[] {new Tile(3058,3483,1), new Tile(3058,3483), new Tile(3058,3488),
            new Tile(3053,3494), new Tile(3053,3501), new Tile(3054,3507), new Tile(3056,3510),
            new Tile(3060,3517), new Tile(3062,3520), new Tile(3068,3520), new Tile(3072,3516),
            new Tile(3073,3508), new Tile(3080,3506), new Tile(3085,3505), new Tile(3092,3500), new Tile(3095,3495)},
            new Obstacle[]{ new Obstacle(7122, new Tile(3058, 3485, 1), new TileArea( 3056, 3486, 3, 6, 1 ), "Open" , new int[]{30, 112, -210, -19, 120, 123} ),
            new Obstacle(16679, new Tile(3057, 3483, 1), new TileArea( 3054, 3482, 5, 7, 1 ), "Climb-down" , new int[]{-5, 3, -64, 0, -32, 32} )});

    private static final TileArea robeArea = new TileArea(3056, 3486, 3, 5, 1);
    private static final TileArea bankArea = new TileArea(3091,3488, 7, 11);

    private static final int bankID = 11744;

    private int robeTopInInv = 0, robeTopGained = 0;
    private int robeBottomInInv = 0, robeBottomGained = 0;
    private long startTime;

    private static final int hopDelay = 24000;
    private long lastHopTime = 0;

    private boolean clickedExsistingUser = false;
    private boolean enteredUsername = false;
    private boolean enteredPassword = false;

    private LogingThread loginThread;

    private class LogingThread extends Thread {
        public boolean running = true;
        public int loggedOut = 0;

        public void run() {
            while(!isInterrupted() && running ) {
                if (game.playing() || game.loading() ) {
                    loggedOut = 0;
                    clickedExsistingUser = false;
                    enteredUsername = false;
                    enteredPassword = false;
                } else {
                    lastHopTime = System.currentTimeMillis();
                    if(loggedOut < 5) {
                        loggedOut++;
                        try {
                            sleep(500);
                        } catch (Exception ignored) {
                            return;
                        }
                        continue;
                    }
                    if (!clickedExsistingUser) {
                        ctx.input.hop(430, 296);
                        ctx.input.click(true);
                        clickedExsistingUser = true;
                    } else if (!enteredUsername) {
                        ctx.input.send("pureiviage@mail.com");
                        ctx.input.send("{VK_ENTER}");
                        enteredUsername = true;
                    } else if (!enteredPassword) {
                        ctx.input.send("abc1233");
                        ctx.input.send("{VK_ENTER}");
                        enteredPassword = true;
                    }
                }

                if(widgets.component(378,6).visible()) {
                    widgets.clickComponent(378, 6);
                }

                try {
                    sleep(500);
                } catch (Exception ignored) {
                    return;
                }
            }
        }
    }

    @Override
    public void start(){
        startTime = System.currentTimeMillis();

        Thread tmp = new Thread() {
            public void run() {
                robeTopPrice = misc.getGEPrice(robeTopID);
                robeBottomPrice = misc.getGEPrice(robeBottomID);
            }
        };
        tmp.start();

        loginThread = new LogingThread();
        loginThread.start();

        robeTopInInv = inventory.count(robeTopID);
        robeBottomInInv = inventory.count(robeTopID);

        worlds.openWorldHop();
        if(!worlds.sortedOnMembers())
            worlds.sortOnMembers();
    }

    public void stop(){
        loginThread.interrupt();
        loginThread.running = false;
    }

    private boolean atBank(){
        return bankArea.contains(ctx.players.local().tile());
    }

    private boolean atRobes(){
        return robeArea.contains(ctx.players.local().tile());
    }

    @Override
    public void poll() {
        if( game.playing() && !widgets.component(378,6).visible() ) {
            int tmp = inventory.count(robeTopID);
            robeTopGained += Math.max(0, tmp - robeTopInInv);
            robeTopInInv = tmp;
            tmp = inventory.count(robeBottomID);
            robeBottomGained += Math.max(0, tmp - robeBottomInInv);
            robeBottomInInv = tmp;

            if( !camera.pitchedUp())
                camera.pitchUp();

            //TODO fix bounds for ground items on table and for stairs down

            if (inventory.full()) {
                if (atBank()) {
                    if (bank.opened()) {
                        bank.depositAll();
                    } else {
                        GameObject bank = objects.getNearest(bankID);
                        if (bank != null)
                            objects.click(bank, "Bank");
                    }
                } else {
                    if( movement.energy() >= 75)
                        movement.setRunning();
                    toBank.traverse();
                }
            } else {
                if (atRobes()) {
                    GroundItem robeTop = groundItems.get(robeTopID);
                    GroundItem robeBottom = groundItems.get(robeBottomID);
                    if (robeTop != null && robeTop.valid() && ctx.players.local().animation() == -1) {
                        groundItems.pickup(robeTop);
                        sleep(400);
                    }else if (robeBottom != null && robeBottom.valid() && ctx.players.local().animation() == -1) {
                        groundItems.pickup(robeBottom);
                        sleep(400);
                    }else if(System.currentTimeMillis() > hopDelay + lastHopTime ) {
                        int currentWorld = worlds.getCurrentWorld();
                        int currentIndex = -1;
                        for (int i = 0; i < worlds.freeWorlds.length; i++) {
                            if (worlds.freeWorlds[i] == currentWorld) {
                                currentIndex = i;
                                break;
                            }
                        }
                        if (currentIndex == worlds.freeWorlds.length - 1) {
                            worlds.hop(worlds.freeWorlds[0]);
                            lastHopTime = System.currentTimeMillis();
                            sleep(200);
                        }else {
                            worlds.hop(worlds.freeWorlds[currentIndex + 1]);
                            lastHopTime = System.currentTimeMillis();
                            sleep(200);
                        }
                    }
                } else {
                    if( movement.energy() >= 75)
                        movement.setRunning();
                    toRobes.traverse();
                }
            }
        }
    }

    @Override
    public void repaint(Graphics g){
        super.repaint(g);

        g.setColor(Color.black);
        g.fillRect(0, 276, 150, 60 + 2);

        g.setColor(Color.white);
        g.drawRect(0, 276, 150, 60 + 2);
        int profit = robeTopPrice * robeTopGained + robeBottomPrice * robeBottomGained;

        long timeRunning = System.currentTimeMillis() - startTime;
        g.drawString("Profit/h: " + (int) (profit / (timeRunning / 3600000.0f)), 5, 324);
        g.drawString("Robes/h: " + (int) ((robeTopGained+robeBottomGained) / (timeRunning / 3600000.0f)), 5, 336);
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        g.drawString("Time Running " + hours + ":" + mins + ":" + seconds, 5, 288);
        g.drawString("Profit: " + profit, 5, 300);
        g.drawString("Robes gained: " + (robeTopGained+robeBottomGained), 5, 312);
    }
}
