package AangFighter;

import AngUtilFunc.UtilFunc;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

@Script.Manifest(name = "AangFighter", description = "Fights shit", properties="client=4")
public class AangFighter extends PollingScript<ClientContext> implements PaintListener, MessageListener {

    private enum State{
        attacking, burying, eating
    }

    private enum CombatType{
        melee, ranged, magic
    }

    private UtilFunc UF = UtilFunc.getInstance();
    private int prayerLevels = 0, attackLevels = 0, strengthLevels = 0, defenseLevels = 0, hpLevels = 0, rangeLevels = 0;

    private final int[] monsterIDs = Monster.cows.ids;
    private int[] itemsToLoot;
    private final int bones = 526;
    private Npc target;
    private GroundItem targetBone;
    private State state = State.attacking;
    private boolean buryBones = true;
    private boolean clickedGroundItem = false, clickedMonster = false;
    private String stateText = "";

    private static int bronzeArrow, ironArrow, steelArrow, mithArrow, addyArrow, runeArrow;
    private int arrowsUsed;

    private int[] inventory = new int[28];

    private boolean isAttacking() {
        return ctx.players.local().interacting().valid() || (ctx.players.local().interacting().valid() && target != null && target.valid() && target.health() != 0);
    }

    private boolean inventoryFull() {
        return ctx.inventory.select().count() == 28;
    }

    private void checkIfBeingAttacked() {
        if(ctx.players.local().inCombat() && !ctx.players.local().interacting().valid()) {
            for (Npc npc : ctx.npcs.get() ) {
                if( npc.interacting().valid()) {
                    if( npc.interacting().name().equals(ctx.players.local().name()) ) {
                        if( target != npc )
                            npc.interact("Attack", npc.name());
                        target = npc;
                        break;
                    }
                }
            }
        }
    }

    private Npc getNextTarget() {
        final Filter<Npc> filter = new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                return npc.health() != 0 && !npc.inCombat();
            }
        };
        BasicQuery<Npc> list = ctx.npcs.select().id(monsterIDs).select(filter).nearest();
        Npc ret = list.poll();
        while(ret.valid())
        {
            if( ret.tile().matrix(ctx).reachable() )
                return ret;
            ret = list.poll();
        }
        return ret;
    }

    private GroundItem getNextGroundBone() {
        return ctx.groundItems.select().id(bones).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return UtilFunc.instance.pointOnScreen(groundItem.centerPoint());
            }
        }).nearest().poll();
    }

    private void ItemPickup(Item item, int slot, int amount){

    }

    @Override
    public void repaint(Graphics g) {

        if(target != null && target.valid()) {
            target.draw(g, 100);
            for( java.awt.Polygon t : target.triangles() ){
                g.drawPolygon(t);
            }
        }

        g.setColor(Color.white);
        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );

        if( targetBone != null && targetBone.valid())
            g.drawString("boneScreenLocation: " + targetBone.centerPoint(), 5, 144);

        g.drawString("State: " + stateText, 5, 300);
        if( ctx.menu.opened())
            g.drawString("Menulocation: " + ctx.menu.bounds().getLocation(), 5, 120);
        g.drawString("Mousepos: " + ctx.input.getLocation(), 5, 132);
    }

    @Override
    public void start() {
        System.out.println("Script Started!");
        if( ctx.skills.realLevel(5) >= 43 ) {
            buryBones = false;
            System.out.println("Pray level 43");
        }
        UF.init(ctx);

        while( ctx.game.tab() != Game.Tab.INVENTORY ) {
            UF.openInventory();
        }

    }

    /*

    if underattack and not attacking
        attack attacker
        set state attacking

    if state attacking
        if attacking
            prayflick / pickup bones / bury bones
        else if not attacking
            if inventory is full and inventory contains bones and bury bones
                set state bury bones
            else if bones on screen and bury bones
                pick up bones
            else if monster on screen
                attack monster
            else
                walk to nearest monster
     else if state burying
        if inventory contains bones
            bury first bone in inventory
        else
            set state attacking

     */


    @Override
    public void poll() { //poll time is ~180 ms
        if( state == State.attacking ) {
            if( !ctx.movement.running() && ctx.movement.energyLevel() >= 75) {
                stateText = "Setting running";
                ctx.movement.running(true);
            }
            stateText = "";
            if( isAttacking() ) {
                stateText = "Attacking";
            } else {
                if (!inventoryFull() && buryBones && (targetBone == null || !targetBone.valid() || !UF.pointOnScreen(targetBone.centerPoint())) ) {
                    targetBone = getNextGroundBone();
                    clickedGroundItem = false;
                }
                if (!inventoryFull() && targetBone != null && targetBone.valid() && !inventoryFull() && UF.pointOnScreen(targetBone.centerPoint())) {
                    stateText = "Picking up bones";
                    if( !ctx.players.local().inMotion() )
                        clickedGroundItem = false;
                    if (!clickedGroundItem ) {
                        clickedGroundItem = UF.interact.pickupGroundItem(targetBone);
                    }
                } else  if( inventoryFull() && buryBones && ctx.inventory.select().id(bones).first().poll().valid() ) {
                    state = State.burying;
                } else {
                    if( !clickedMonster || !target.valid() || target.health() <= 0 || target.interacting() != ctx.players.local() ) {
                        target = getNextTarget();
                        clickedMonster = false;
                    }
                    if (!UF.pointOnScreen(target.centerPoint())) {
                        if (target.tile().matrix(ctx).onMap()) {
                            stateText = "Walking to monster";
                            ctx.input.click(target.tile().matrix(ctx).mapPoint(), true);
                        }
                    } else {
                        stateText = "Attacking monster";
                        if( !clickedMonster )
                            clickedMonster = UF.interact.attackNPC(target);
                    }
                }
            }
        }else if( state == State.burying){
            Item invBones = ctx.inventory.select().id(bones).first().poll();
            if( invBones.valid() && buryBones) {
                stateText = "Clicking bones";
                UF.interact.clickInvItem(invBones);
            }else {
                state = State.attacking;
            }
        }
        checkIfBeingAttacked();
    }

    @Override
    public void stop() {
        System.out.println("Script Stopped!");
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.type() == 0) {
            if (messageEvent.text().equals("Congratulations, you just advanced a Prayer level."))
                prayerLevels++;
            else if (messageEvent.text().equals("Congratulations, you just advanced a Attack level."))
                attackLevels++;
            else if (messageEvent.text().equals("Congratulations, you just advanced a Strength level."))
                strengthLevels++;
            else if (messageEvent.text().equals("Congratulations, you just advanced a Defense level."))
                defenseLevels++;
            else if (messageEvent.text().equals("Congratulations, you just advanced a Hitpoints level."))
                hpLevels++;
            else if (messageEvent.text().equals("Congratulations, you just advanced a Ranged level."))
                rangeLevels++;
        }

        if( ctx.skills.realLevel(5) >= 43 )
            buryBones = false;
    }

}