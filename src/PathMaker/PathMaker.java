package PathMaker;

import org.powerbot.script.Tile;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Script.Manifest(name = "PathMaker", description = "Tool to make paths", properties="client=4")
public class PathMaker extends PollingScript<ClientContext> implements PaintListener {

    private List<Tile> path = new ArrayList<Tile>();
    private GUI gui;

    @Override
    public void start(){
        gui = new GUI(this);
        ctx.input.blocking(false);
    }

    @Override
    public void stop(){
        gui.dispose();
    }

    @Override
    public void repaint(Graphics g) {
       for( int i = 0; i < path.size(); i++ ){
           if( gui.tileList.getSelectedIndex() == i )
               g.setColor(Color.green);
           else
               g.setColor(Color.cyan);
           final Point p = path.get(i).matrix(ctx).mapPoint();
           g.fillRect(p.x-1,p.y-1,3,3);
           g.setColor(Color.white);
           if( i != 0 ){
               final Point pp = path.get(i-1).matrix(ctx).mapPoint();
               g.drawLine(pp.x, pp.y,p.x,p.y);
           }
           if(i != path.size() - 1 ){
               final Point pp = path.get(i+1).matrix(ctx).mapPoint();
               g.drawLine(pp.x, pp.y,p.x,p.y);
           }
       }
    }

    @Override
    public void poll() {
    }

    public void exit()
    {
        ctx.controller.stop();
    }

    public void addTileButtonPressed(){
        DefaultListModel lm = (DefaultListModel) gui.tileList.getModel();
        if( gui.tileList.getSelectedIndex() == -1 ) {
            path.add(ctx.players.local().tile());
            lm.addElement(ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y());
        }else{
            path.add(gui.tileList.getSelectedIndex()+1, ctx.players.local().tile());
            lm.add(gui.tileList.getSelectedIndex()+1,ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y());
        }
        gui.tileList.setModel(lm);
    }

    public void removeTileButtonPressed(int index) {
        path.remove(index);
        DefaultListModel lm = (DefaultListModel)gui.tileList.getModel();
        lm.remove(index);
        gui.tileList.setModel(lm);
    }

    public void resetButton(){
        path.clear();
    }

    public String getOutputText(){
        String ret = "private TilePath path = ctx.movement.newTilePath(";
        for( int i = 0; i < path.size(); i++ ){
            if( i ==  0)
                ret += " new Tile(" + path.get(i).x() + "," + path.get(i).y() + ")";
            else
                ret += ","+ ( i % 4 == 3 && i != path.size() - 1 ? "\n\t" :"" ) +" new Tile(" + path.get(i).x() + "," + path.get(i).y() + ")";
        }
        ret += " );";
        return ret;
    }
}
