package AangAmuletCrafter;

import Util.AangScript;
import Util.Movement.Path;
import Util.Movement.TileArea;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;

@Script.Manifest(name = "AangAmuletCrafter", description = "Crafts amulets at al kharid.", properties="client=4;author=aang521")
public class AangAmuletCrafter extends AangScript {

    private static final int goldID = 2357;
    private static final int mouldID = 1595;
    private static final int sapphireID = 1607;
    private static final int sapphireAmuletID = 1675;
    private static final int furnaceID = 24009;
    private static final int bankID = 11744;
    private static final Tile bankTile = new Tile(3268,3167);

    private static final int furnaceInterface = 446;
    private static final int sapphireAmuletComponent = 33;

    private int goldPrice, sapphirePrice, sapphireAmuletPrice, profit;

    private static final Path toBank = new Path( new Tile[] {new Tile(3274,3186), new Tile(3277,3186), new Tile(3280,3185),
            new Tile(3280,3182), new Tile(3280,3178), new Tile(3276,3172), new Tile(3275,3169),
            new Tile(3273,3167), new Tile(3269,3167)});

    private static final Path toFurnace = new Path( new Tile[] {new Tile(3269,3167), new Tile(3272,3167), new Tile(3273,3169),
            new Tile(3275,3174), new Tile(3276,3178), new Tile(3278,3181), new Tile(3280,3185),
            new Tile(3278,3185), new Tile(3274,3186)});

    private static final TileArea bankArea = new TileArea( 3269, 3164, 3, 6 );
    private static final TileArea furnaceArea = new TileArea( 3274, 3184, 5, 4 );

    private int amuletsMade = 0;
    private long startTime;

    private int amuletsInInv = 0;

    private GameObject furnace = null;
    private long lastCraftingTime;

    private String status = "NULL";

    @Override
    public void start(){
        startTime = System.currentTimeMillis();
        Thread tmp = new Thread(){
            public void run(){
                goldPrice = misc.getGEPrice(goldID);
                sapphirePrice = misc.getGEPrice(sapphireID);
                sapphireAmuletPrice = misc.getGEPrice(sapphireAmuletID);
                profit = sapphireAmuletPrice - (sapphirePrice+goldPrice);
            }
        };
        tmp.start();
        amuletsInInv = inventory.count(sapphireAmuletID);
    }

    private boolean atBank() {
        return bankArea.contains(ctx.players.local().tile());
    }

    private boolean atFurnace() {
        return furnaceArea.contains(ctx.players.local().tile());
    }

    private boolean crafting() {
        return lastCraftingTime + 1000 > System.currentTimeMillis();
    }

    @Override
    public void poll() {

        if( ctx.players.local().animation() == 899 )
            lastCraftingTime = System.currentTimeMillis();

        int tmp  = inventory.count(sapphireAmuletID);
        amuletsMade += Math.max(0,tmp - amuletsInInv);
        amuletsInInv = tmp;

        if( inventory.contains(sapphireID) && inventory.count(sapphireID) <= inventory.count(goldID) ){
            if( atFurnace() ){
                if( chat.pendingInput() ) {
                    status = "sending amount";
                    ctx.input.sendln(""+inventory.count(sapphireID));
                    sleep(400);
                }else if( widgets.widget(furnaceInterface).componentCount() > 0 ){
                    status = "Clicking make-x";
                    widgets.clickComponentItem(furnaceInterface,sapphireAmuletComponent,"Make-X");
                    sleep(200);
                }else if(!crafting()) {
                    if (inventory.itemSelected() && inventory.selectedItem().id() == goldID) {
                        status = "using gold with furnace";
                        if (furnace == null || !furnace.valid())
                            furnace = objects.get(furnaceID);
                        if (furnace != null && furnace.valid()) {
                            objects.useItem(furnace);
                            sleep(200);
                        }
                    }else{
                        status = "selecting gold";
                        inventory.clickItem(inventory.getFirst(goldID));
                        sleep(100);
                    }
                }
            }else{
                status = "walking to furnace";
                if( !movement.running() &&  movement.energy() >= 75 )
                    movement.setRunning();
                toFurnace.traverse();
            }
        }else{
            if( atBank() ){
                if( bank.opened() ){
                    if( inventory.contains(sapphireAmuletID)){
                        status = "depositing amulets";
                        bank.depositAll(sapphireAmuletID);
                        sleep(200);
                    }else{
                        if( !inventory.contains(sapphireID) ) {
                            status = "withdrawing sapphire";
                            bank.withdrawX(sapphireID, 13);
                            sleep(500);
                        }else if( inventory.count(goldID) < 13 ) {
                            status = "withdrawing gold";
                            bank.withdrawAll(goldID);
                            sleep(500);
                        }
                    }
                }else{
                    status = "opening bank";
                    GameObject bank = objects.get(bankID,bankTile);
                    objects.click(bank,"Bank");
                    sleep(200);
                }
            }else{
                status = "walking to bank";
                if( !movement.running() &&  movement.energy() >= 75 )
                    movement.setRunning();
                toBank.traverse();
            }
        }
    }

    public void repaint(Graphics g) {
        super.repaint(g);

        g.setColor(Color.black);
        g.fillRect(0, 264, 150, 72 + 2);

        g.setColor(Color.white);
        g.drawRect(0, 264, 150, 72 + 2);
        int totalProfit = profit * amuletsMade;

        long timeRunning = System.currentTimeMillis() - startTime;
        g.drawString("Profit/h: " + (int) (totalProfit / (timeRunning / 3600000.0f)), 5, 324);
        g.drawString("Amulets/h: " + (int) (amuletsMade / (timeRunning / 3600000.0f)), 5, 336);
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        g.drawString("Time Running " + hours + ":" + mins + ":" + seconds, 5, 276);
        g.drawString("Profit: " + totalProfit, 5, 288);
        g.drawString("Profit per amulet: " + profit, 5, 300);
        g.drawString("Amulets made: " + amuletsMade, 5, 312);

        g.drawString(status,5,100);
    }
}
