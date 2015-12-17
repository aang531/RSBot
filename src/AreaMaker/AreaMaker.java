package AreaMaker;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "AreaMaker", description = "Tool to areas", properties="client=4")
public class AreaMaker extends PollingScript<ClientContext> implements PaintListener {
    List<Tile> tiles = new ArrayList<Tile>();

    GUI gui;

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
        if( tiles.size() >= 2) {
            g.setColor(new Color(255,255,255,255));
            Tile[] t = new Tile[tiles.size()];
            tiles.toArray(t);
            Area a = new Area(t);
            for(int i = 0; i < a.getPolygon().npoints; i++ ){
                Point p = new Point(tiles.get(i).matrix(ctx).getBounds().xpoints[0],tiles.get(i).matrix(ctx).getBounds().ypoints[0]);
                Point pp;
                if( i == a.getPolygon().npoints - 1)
                    pp = new Point(tiles.get(0).matrix(ctx).getBounds().xpoints[0],tiles.get(0).matrix(ctx).getBounds().ypoints[0]);
                else
                    pp = new Point(tiles.get(i+1).matrix(ctx).getBounds().xpoints[0],tiles.get(i+1).matrix(ctx).getBounds().ypoints[0]);
                g.drawLine(p.x,p.y,pp.x,pp.y);
            }
            g.drawPolygon(a.getPolygon());

            if( a.contains(ctx.players.local())) {
                g.setColor(new Color(0, 255, 0, 100));
            }else {
                g.setColor(new Color(255, 0, 0, 100));
            }
            g.fillPolygon(ctx.players.local().tile().matrix(ctx).getBounds());
        }else{
            g.setColor(new Color(255,0,0,100));
            g.fillPolygon(ctx.players.local().tile().matrix(ctx).getBounds());
        }
        for( int i = 0; i < tiles.size(); i++ ) {
            if( i == gui.tileList.getSelectedIndex() )
                g.setColor(Color.yellow);
            else
                g.setColor(new Color(0,0,255,150));
            g.fillPolygon(tiles.get(i).matrix(ctx).getBounds());
        }
    }

    @Override
    public void poll() {

    }

    public void addTileButton(){
        DefaultListModel lm = (DefaultListModel)gui.tileList.getModel();
        if( gui.tileList.getSelectedIndex() == -1 ) {
            tiles.add(ctx.players.local().tile());
            lm.addElement(ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y());
        }else{
            tiles.add(gui.tileList.getSelectedIndex()+1, ctx.players.local().tile());
            lm.add(gui.tileList.getSelectedIndex()+1,ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y());
        }
        gui.tileList.setModel(lm);
    }

    public void removeTileButton(int index){
        tiles.remove(index);
        DefaultListModel lm = (DefaultListModel)gui.tileList.getModel();
        lm.remove(index);
        gui.tileList.setModel(lm);
    }

    public void resetButton(){
        tiles.clear();
    }

    public void toClipBoardButton(){
        String text = "private Area area = new Area(";
        for( int i = 0; i < tiles.size(); i ++ ) {
            if( i ==  0)
                text += " new Tile(" + tiles.get(i).x() + "," + tiles.get(i).y() + ")";
            else
                text += ","+ ( i % 4 == 3 && i != tiles.size() - 1 ? "\n\t" :"" ) +" new Tile(" + tiles.get(i).x() + "," + tiles.get(i).y() + ")";
        }
        text += " );";

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);

        Tile[] t = new Tile[tiles.size()];
        tiles.toArray(t);
        Area a = new Area(t);
        System.out.println(a.getPolygon().npoints);
        for( int i = 0; i < a.getPolygon().npoints; i++ )
            System.out.println(a.getPolygon().xpoints[i] + ", " + a.getPolygon().ypoints[i]);
    }

    public void exit(){
        ctx.controller.stop();
    }
}
