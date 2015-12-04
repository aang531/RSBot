package AangFighter;

import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;

import java.awt.*;

@Script.Manifest(name = "AangFighter", description = "Fights shit", properties="client=4")
public class AangFighter extends PollingScript<ClientContext> implements PaintListener {

    public enum State{
        attacking, burying, eating;
    }

    //TODO set running if has energy

    private final int[] monsterIDs = Monster.cows.ids;
    private final int bones = 526;
    private Npc target;
    private GroundItem targetBone;
    private State state = State.attacking;

    private boolean isAttacking()
    {
        return ctx.players.local().interacting().valid() || (ctx.players.local().interacting().valid() && target != null && target.valid() && target.health() != 0);
    }

    private boolean inventoryFull()
    {
        return ctx.inventory.select().count() == 28;
    }

    private void checkIfBeingAttacked()
    {
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

    private Npc getNextTarget()
    {
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

    private GroundItem getNextGroundBone()
    {
        return ctx.groundItems.select().id(bones).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.inViewport();
            }
        }).nearest().poll();
    }

    @Override
    public void repaint(Graphics g) {

        if(target != null && target.valid())
            target.draw(g,100);
        g.drawString(""+inventoryFull(),0,100);
        g.drawString(""+ctx.inventory.select().count(),0,110);

        Point p = target.tile().matrix(ctx).mapPoint();
        g.fillRect(p.x-2,p.y-2,4,4);

        g.setColor(Color.white);
        g.drawLine(0,ctx.input.getLocation().y, ctx.input.getLocation().x - 5,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x + 5,ctx.input.getLocation().y, 765,ctx.input.getLocation().y );
        g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x, ctx.input.getLocation().y - 5 );
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y + 5, ctx.input.getLocation().x, 504 );
    }

    @Override
    public void start() {
        System.out.println("Script Started!");
    }

    @Override
    public void poll() { //TODO add check if target is under attack by someone else
        //if( ctx.players.local().interacting().valid() )
        //    target = (Npc)ctx.players.local().interacting();
        if( !ctx.movement.running() && ctx.movement.energyLevel() >= 75) {
            ctx.movement.running(true);
        }
        if( state == State.attacking ) {
            if (!isAttacking()) {

                if (!inventoryFull() && (targetBone == null || !targetBone.valid())) {
                    targetBone = getNextGroundBone();
                }
                if (!inventoryFull() && targetBone != null && targetBone.valid() && !inventoryFull()) {
                    if (ctx.movement.destination() != targetBone.tile()) {
                        targetBone.interact("Take", "Bones");
                    }
                } else {
                    target = getNextTarget();
                    if( !target.inViewport()) {
                        if( target.tile().matrix(ctx).onMap() )
                            ctx.input.click(target.tile().matrix(ctx).mapPoint(),true);
                    }else
                        target.interact("Attack", target.name());
                }
            }
        }else if( state == State.burying){
            Item invBones = ctx.inventory.select().id(bones).first().poll();
        }
        checkIfBeingAttacked();
    }

    @Override
    public void stop() {
        System.out.println("Script Stopped!");
    }
}