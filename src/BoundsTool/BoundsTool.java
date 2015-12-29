package BoundsTool;

import AangUtil.AangScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Script.Manifest(name = "BoundsTool", description = "Tool to edit bounds of objects and such.", properties="client=4")
public class BoundsTool extends AangScript implements MouseListener {

    public GameObject selectedModel;

    @Override
    public void repaint(Graphics g) {
        if( selectedModel != null && selectedModel.valid() ) {
            g.drawString(""+selectedModel, 5, 100);
            selectedModel.draw(g,255);
        }
    }

    @Override
    public void poll() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final Point mousePos =  e.getLocationOnScreen();
        System.out.println(mousePos );
        /*selectedModel = ctx.objects.select().within(100.0).select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.contains(mousePos);
            }
        }).poll();
        System.out.println("" + selectedModel);*/
        //Area a = new Area(ctx.players.local().tile().derive(-10,-10), ctx.players.local().tile().derive(10,10));
        Tile playerTile = ctx.players.local().tile();
        loops:
        for( int y = -10; y <= 10 ; y++ )
            for( int x = -10; x <= 10; x++ ){
                if( playerTile.derive(x,y).matrix(ctx).getBounds().contains(mousePos)) {//TODO finds wrong tile
                    final Tile foundTile = playerTile.derive(x,y);
                    System.out.println("found tile " + foundTile);
                    selectedModel = ctx.objects.select().select(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            return gameObject.tile().x() == foundTile.x() && gameObject.tile().y() == foundTile.y();
                        }
                    }).poll();
                    break loops;
                }
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
