package Util;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Npc;

import java.util.List;

public class NpcsFunc extends AangUtil {
    private static NpcsFunc ourInstance = new NpcsFunc();

    public static NpcsFunc getInstance() {
        return ourInstance;
    }

    public Npc getNearest(int id ) {
        List<Npc> npcs = ctx.npcs.get();
        int closest = -1;
        int closestDist = Integer.MAX_VALUE;
        for(int i = 0; i < npcs.size(); i++ ) {
            if( npcs.get(i).id() == id ) {
                Tile pt = ctx.players.local().tile(), tt = npcs.get(i).tile();
                int dx = pt.x() - tt.x();
                int dy = pt.y() - tt.y();
                int dist = dx*dx+dy*dy;
                if( dist < closestDist ){
                    closest = i;
                    closestDist = dist;
                }
            }
        }
        if( closest != -1 )
            return npcs.get(closest);
        return null;
    }

    public boolean attackMonster( final Npc npc ) {
        return interact.clickInteractableCC(npc, "Attack",npc.name() + "  (level-" + npc.combatLevel() + ")");
    }

    public boolean click( final Npc npc, String action ) {
        return interact.clickInteractableCC(npc, action,npc.name() + (npc.combatLevel() != 0 ? "  (level-" + npc.combatLevel() + ")" : ""));
    }

    public boolean useItem( final Npc npc) {
        return interact.clickInteractableCC(npc, "Use",inventory.selectedItem().name() + " -> " + npc.name() + (npc.combatLevel() != 0 ? "  (level-" + npc.combatLevel() + ")" : ""));
    }
}
