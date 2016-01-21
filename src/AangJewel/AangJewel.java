package AangJewel;

import Util.AangScript;
import Util.AangUtil;
import Util.Movement.Obstacle;
import Util.Movement.Path;
import Util.Movement.TileArea;
import Util.SkillsFunc;
import org.powerbot.script.*;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name = "AangJewel", description = "Crafts amulets at al kharid.", properties="client=4;author=aang521")
public class AangJewel extends AangScript implements MessageListener{

    private static final int goldID = 2357;
    private static final int amuletMouldID = 1595, ringMouldID = 1592;
    private static final int[] gemIDs = new int[]{1607,1605,1603,1601,1615};
    private static final int[] amuletIDs = new int[]{1675,1677,1679,1681,1683};
    private static final int[] ringIDs = new int[]{1637,1639,1641,1643,1645};
    private static final int furnaceID = 24009;
    private static final int bankID = 11744;
    private static final Tile bankTile = new Tile(3268,3167);

    private static final int furnaceInterface = 446;
    private int jewelComponent;

    private int goldPrice, gemPrice, jewelPrice, profit;

    private static final Path toBank = new Path( new Tile[] {new Tile(3274,3186), new Tile(3277,3186), new Tile(3280,3185),
            new Tile(3280,3182), new Tile(3280,3178), new Tile(3276,3172), new Tile(3275,3169),
            new Tile(3273,3167), new Tile(3269,3167)},
            new Obstacle[]{ new Obstacle(7122, new Tile(3280, 3185), new TileArea( 3274, 3184, 5, 4 ), "Open" , new int[]{9, 17, -235, 0, 20, 130} )});

    private static final Path toFurnace = new Path( new Tile[] {new Tile(3269,3167), new Tile(3272,3167), new Tile(3273,3169),
            new Tile(3275,3174), new Tile(3276,3178), new Tile(3278,3181), new Tile(3280,3185),
            new Tile(3278,3185), new Tile(3274,3186)},
            new Obstacle[]{ new Obstacle(7122, new Tile(3280, 3185), new TileArea( 3280, 3181, 3, 6 ), "Open" , new int[]{9, 17, -235, 0, 20, 130} )} );


    private static final TileArea bankArea = new TileArea( 3269, 3164, 3, 6 );
    private static final TileArea furnaceArea = new TileArea( 3274, 3184, 4, 4 );

    private int amuletsMade = 0;
    private long startTime;
    private int startExp;

    private int amuletsInInv = 0;

    private GameObject furnace = null;
    private long lastCraftingTime;

    private int jewelID, gemID, mouldID;

    private String status = "NULL";

    private GUI gui;

    private boolean started = false;

    @Override
    public void start(){
        gui = new GUI(this);
    }

    @Override
    public void stop(){
        gui.dispose();
    }

    public void startButtonPress(){
        final int gemIndex = gui.gemType.getSelectedIndex();
        final int jewelIndex = gui.jewelType.getSelectedIndex();
        gemID = gemIDs[gemIndex];
        if( jewelIndex == 0 ) {
            mouldID = amuletMouldID;
            jewelID = amuletIDs[gemIndex];
            jewelComponent = 33 + gemIndex;
        }else if ( jewelIndex == 1 ) {
            mouldID = ringMouldID;
            jewelID = ringIDs[gemIndex];
            jewelComponent = 8 + gemIndex;
        }else {
            log("error could not find jewelIndex: " + jewelIndex);
            exit();
            return;
        }

        startExp = skills.exp(SkillsFunc.CRAFTING);
        startTime = System.currentTimeMillis();
        Thread tmp = new Thread(){
            public void run(){
                goldPrice = misc.getGEPrice(goldID);
                gemPrice = misc.getGEPrice(gemID);
                jewelPrice = misc.getGEPrice(jewelID);
                profit = jewelPrice - (gemPrice+goldPrice);
            }
        };
        tmp.start();
        amuletsInInv = inventory.count(jewelID);

        started = true;

        gui.dispose();
    }

    public void exit(){
        ctx.controller.stop();
    }

    private boolean atBank() {
        return bankArea.contains(ctx.players.local().tile());
    }

    private boolean atFurnace() {
        return furnaceArea.contains(ctx.players.local().tile());
    }

    private boolean crafting() {
        return lastCraftingTime + 1200 > System.currentTimeMillis();
    }

    @Override
    public void poll() {
        if( started ) {
            if (ctx.players.local().animation() == 899)
                lastCraftingTime = System.currentTimeMillis();

            int tmp = inventory.count(jewelID);
            amuletsMade += Math.max(0, tmp - amuletsInInv);
            amuletsInInv = tmp;

            if (inventory.contains(gemID) && inventory.count(gemID) <= inventory.count(goldID) && inventory.contains(mouldID) ) {
                if (atFurnace()) {
                    if (chat.pendingInput()) {
                        status = "sending amount";
                        ctx.input.sendln("13");
                        sleep(600);
                    } else if (widgets.widget(furnaceInterface).componentCount() > 0) {
                        status = "Clicking make-x";
                        int count = Math.min(inventory.count(gemID), inventory.count(goldID));
                        if( count <= 10 )
                            widgets.clickComponentItem(furnaceInterface, jewelComponent, "Make-10");
                        else
                            widgets.clickComponentItem(furnaceInterface, jewelComponent, "Make-X");
                        sleep(600);
                    } else if (!crafting()) {
                        if (inventory.itemSelected() && inventory.selectedItem().id() == goldID) {
                            status = "using gold with furnace";
                            if (furnace == null || !furnace.valid())
                                furnace = objects.get(furnaceID);
                            if (furnace != null && furnace.valid()) {
                                if( objects.useItem(furnace) ){
                                    Condition.wait(new Callable<Boolean>() {
                                        @Override
                                        public Boolean call() throws Exception {
                                            return widgets.widget(furnaceInterface).componentCount() > 0;
                                        }
                                    }, 100, 10);
                                }
                                sleep(100);
                            }
                        } else {
                            status = "selecting gold";
                            inventory.clickItem(inventory.getFirst(goldID));
                            sleep(100);
                        }
                    }else{
                        status = "crafting";
                        sleep(200);
                    }
                } else {
                    status = "walking to furnace";
                    toFurnace.traverse();
                }
            } else {
                if (atBank()) {
                    if (bank.opened()) {
                        boolean clicked = true;
                        if (!inventory.contains(mouldID)) {
                            status = "withdrawing mould";
                            clicked = bank.withdraw(mouldID,1);
                            sleep(200);
                        }
                        if(!clicked) return;
                        if (inventory.contains(jewelID)) {
                            status = "depositing amulets";
                            clicked = bank.depositAll(jewelID);
                            sleep(200);
                        }
                        if(!clicked) return;
                        if (!inventory.contains(gemID)) {
                            status = "withdrawing sapphire";
                            clicked = bank.withdraw(gemID, 13);
                            sleep(100);
                        }
                        if(!clicked) return;
                        if (inventory.count(goldID) < 13) {
                            status = "withdrawing gold";
                            clicked = bank.withdrawAll(goldID);
                            sleep(100);
                        }
                        if(!clicked) return;
                        sleep(200);
                        if (!inventory.contains(jewelID) && inventory.contains(gemID) && inventory.contains(goldID) )
                            toFurnace.traverse();
                    } else {
                        status = "opening bank";
                        GameObject bank = objects.get(bankID, bankTile);
                        if( objects.click(bank, "Bank") ) {
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return AangUtil.bank.opened();
                                }
                            }, 100, 10);
                        }
                        //sleep(200);
                    }
                } else {
                    status = "walking to bank";
                    toBank.traverse();
                }
            }
        }
    }

    public void repaint(Graphics g) {
        super.repaint(g);

        g.setColor(Color.black);
        g.fillRect(0, 240, 150, 96 + 2);

        g.setColor(Color.white);
        g.drawRect(0, 240, 150, 96 + 2);
        int totalProfit = profit * amuletsMade;

        int expGained = skills.exp(SkillsFunc.CRAFTING) - startExp;

        long timeRunning = System.currentTimeMillis() - startTime;
        g.drawString("Profit/h: " + (int) (totalProfit / (timeRunning / 3600000.0f)), 5, 312);
        g.drawString("Amulets/h: " + (int) (amuletsMade / (timeRunning / 3600000.0f)), 5, 324);
        g.drawString("Exp/h: " + (int) (expGained / (timeRunning / 3600000.0f)), 5, 336);
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        g.drawString("Time Running " + hours + ":" + mins + ":" + seconds, 5, 252);
        g.drawString("Profit: " + totalProfit, 5, 264);
        g.drawString("Profit each: " + profit, 5, 276);
        g.drawString("Jewels made: " + amuletsMade, 5, 288);
        g.drawString("Exp gained: " + expGained, 5, 300);

        g.drawString(status,5,100);
        g.drawString( "Crafting: "+crafting(),5,112);
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.type() == 0) {
            if (messageEvent.text().equals("Congratulations, you just advanced a Crafting level."))
                lastCraftingTime = 0;
        }
    }
}
