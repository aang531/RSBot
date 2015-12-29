package AangLeafs;

import AangUtil.AangScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Component;

import java.awt.*;

@Script.Manifest(name = "AangLeaves", description = "Buys woad leafs in fally park.", properties="client=4")
public class AangLeaves extends AangScript implements PaintListener, MessageListener {

    private static final int gardenerID = 3253;
    private static final int leafID = 1793;
    private Npc gardener;


    private int leafPrice;
    private long startTime;
    private int leavesGained = 0;

    @Override
    public void start() {

        Thread tmp = new Thread() {
            public void run() {
                leafPrice = misc.getGEPrice(leafID);
            }
        };
        tmp.start();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void stop(){
        long timeRunning = System.currentTimeMillis() - startTime;
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        int profit = (leafPrice-10) * leavesGained;

        System.out.println("Script stopped after "+hours+":"+mins+":"+seconds);
        System.out.println("Profit/h: " + (int)(profit/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Leaves/h: " + (int)(leavesGained/((System.currentTimeMillis() - startTime)/3600000.0f)));
        System.out.println("Gained " + profit);
        System.out.println("Collected " + leavesGained + " leaves.");
    }

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.white);
        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );

        g.setColor(Color.black);
        g.fillRect(0,264, 150, 72 + 2);

        g.setColor(Color.white);
        g.drawRect(0,264,150,72 + 2);
        int profit = (leafPrice-10) * leavesGained;

        long timeRunning = System.currentTimeMillis() - startTime;
        g.drawString("Profit/h: " + (int)(profit/(timeRunning/3600000.0f)),5,324);
        g.drawString("Leaves/h: " + (int)(leavesGained/(timeRunning/3600000.0f)),5,336);
        long hours = timeRunning / 3600000;
        timeRunning -= hours * 3600000;
        long mins = timeRunning / 60000;
        timeRunning -= mins * 60000;
        long seconds = timeRunning / 1000;

        g.drawString("Time Running "+hours+":"+mins+":"+seconds,5,276);
        g.drawString("Profit: " + profit,5,288);
        g.drawString("Coins spent: " + leavesGained * 10,5,300);
        g.drawString("Leaves gained: " + leavesGained,5,312);
    }

    @Override
    public void poll() {
        if( gardener == null || !gardener.valid()){
            gardener = ctx.npcs.select().id(gardenerID).poll();
        }
        if( chat.chatting() ) {
            if( chat.canContinue() ){
                chat.continueChat();
            } else if ( chat.isPendingOption()){
                Component[] components = chat.getPendingOptions();
                if( components != null ) {
                    if (components[0].text().equals("Yes please, I need woad leaves.")) {
                        chat.chooseOption(1);
                    } else if (components[0].text().equals("How about 5 coins?")) {
                        chat.chooseOption(4);
                    }
                }
            }
        }else{
            interact.interactNPC(gardener,"Talk-to");
            Condition.sleep(200);
        }
        Condition.sleep(150);
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.text().equals("Wyson gives you a pair of woad leaves.")) {
            leavesGained += 2;
            interact.interactNPC(gardener,"Talk-to");
        }
    }
}
