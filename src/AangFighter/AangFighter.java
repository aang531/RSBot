package AangFighter;

import Util.AangScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@Script.Manifest(name = "AangFighter", description = "Fights shit", properties="client=4")
public class AangFighter extends AangScript implements PaintListener, MessageListener {

    private enum State{
        attacking, burying, eating
    }

    private enum CombatType{
        melee, ranged, magic
    }

    private int prayerLevels = 0, attackLevels = 0, strengthLevels = 0, defenseLevels = 0, hpLevels = 0, rangeLevels = 0;

    private final int[] monsterIDs = Monster.chickens.ids;//Monster.cows.ids;
    private List<Integer> itemsToLoot = new ArrayList<Integer>();
    private int[] lootIDs;
    private final int bones = 526;
    private Npc target;
    private GroundItem targetBone;
    private State state = State.attacking;
    private boolean buryBones = true;
    private boolean clickedGroundItem = false, clickedMonster = false;
    private String stateText = "";
    private CombatType combatType = CombatType.ranged;

    private static int bronzeArrow, ironArrow, steelArrow, mithArrow = 888, addyArrow, runeArrow;
    private int ammoUsed = mithArrow;

    private boolean isAttacking() {
        return ctx.players.local().interacting().valid() || (ctx.players.local().interacting().valid() && target != null && target.valid() && target.health() != 0);
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
        return ctx.groundItems.select().id(lootIDs).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return misc.pointOnScreen(groundItem.centerPoint());
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

        g.drawString("bone: " + targetBone, 5, 144);

        g.drawString("State: " + stateText, 5, 300);
    }

    @Override
    public void start() {
        System.out.println("Script Started!");
        if( ctx.skills.realLevel(5) >= 43 ) {
            buryBones = false;
            System.out.println("Pray level 43");
        }

        lootIDs = new int[]{buryBones ? bones : 0,combatType == CombatType.ranged ? ammoUsed : 0};

        System.out.println("loot ID's: " + lootIDs[1]);

        while( ctx.game.tab() != Game.Tab.INVENTORY ) {
            misc.openInventory();
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
    public void poll() {
        if( state == State.attacking ) {
            if( !ctx.movement.running() && ctx.movement.energyLevel() >= 75) {
                stateText = "Setting running";
                ctx.movement.running(true);
            }
            stateText = "";
            if( isAttacking() ) {
                stateText = "Attacking";
                Item ammo = inventory.getFirst(ammoUsed);
                if( ammo != null && ammo.valid() ){
                    inventory.clickItem(ammo);
                    sleep(100);
                }
            } else {//TODO find out why it stops picking up arrows after some time
                if (!inventory.full() && (targetBone == null || !targetBone.valid() || !misc.pointOnScreen(targetBone.centerPoint())) ) {
                    targetBone = getNextGroundBone();
                    clickedGroundItem = false;
                }
                if (!inventory.full() && targetBone != null && targetBone.valid() && misc.pointOnScreen(targetBone.centerPoint())) {
                    stateText = "Picking up bones";
                    if( !ctx.players.local().inMotion() )
                        clickedGroundItem = false;
                    if (!clickedGroundItem ) {
                        clickedGroundItem = groundItems.pickup(targetBone);
                    }
                } else  if( inventory.full() && ctx.inventory.select().id(bones).first().poll().valid() ) {
                    state = State.burying;
                } else {
                    if( !clickedMonster || !target.valid() || target.health() <= 0 || target.interacting() != ctx.players.local() ) {
                        target = getNextTarget();
                        clickedMonster = false;
                    }
                    if (!misc.pointOnScreen(target.centerPoint())) {
                        if (target.tile().matrix(ctx).onMap()) {
                            stateText = "Walking to monster";
                            ctx.input.click(target.tile().matrix(ctx).mapPoint(), true);
                        }
                    } else {
                        stateText = "Attacking monster";
                        if( !clickedMonster )
                            clickedMonster = npcs.attackMonster(target);
                    }
                }
            }
        }else if( state == State.burying){
            Item invBones = inventory.getFirst(bones);
            if( invBones!= null && invBones.valid()) {
                stateText = "Clicking bones";
                inventory.clickItem(invBones);
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
            else if( messageEvent.text().equals("There is no ammo left in your quiver.")){

            }
        }

        if( ctx.skills.realLevel(5) >= 43 ) {
            buryBones = false;
            lootIDs[1] = 0;
        }
    }

}