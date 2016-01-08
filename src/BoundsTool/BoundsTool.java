package BoundsTool;

import Util.AangScript;
import org.powerbot.script.*;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Script.Manifest(name = "BoundsTool", description = "Tool to edit bounds of objects and such.", properties="client=4;author=aang521")
public class BoundsTool extends AangScript implements MouseListener {

    public GameObject selectedModel;

    private GUI gui;

    public int minx,miny,minz,maxx,maxy,maxz;

    public boolean selecting = false;

    @Override
    public void start(){
        gui = new GUI(this);
    }

    @Override
    public void stop(){
        gui.dispose();
    }

    @Override
    public void repaint(Graphics g) {
        if( selectedModel != null && selectedModel.valid() ) {
            g.drawString(""+selectedModel, 5, 100);
            selectedModel.draw(g,255);
        }else{
            g.drawString("NONE", 5, 100);
        }
    }

    @Override
    public void poll() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( selecting ) {
            final Point mousePos = e.getPoint();
            System.out.println(mousePos);

            Tile playerTile = ctx.players.local().tile();
            loops:
            for (int y = -10; y <= 10; y++)
                for (int x = -10; x <= 10; x++) {
                    if (playerTile.derive(x, y).matrix(ctx).bounds().contains(mousePos)) {
                        final Tile foundTile = playerTile.derive(x, y);
                        System.out.println("found tile " + x + " " + y);
                        selectedModel = ctx.objects.select().select(new Filter<GameObject>() {
                            @Override
                            public boolean accept(GameObject gameObject) {
                                return gameObject.type() != GameObject.Type.FLOOR_DECORATION && gameObject.type() != GameObject.Type.WALL_DECORATION &&
                                        gameObject.tile().x() == foundTile.x() && gameObject.tile().y() == foundTile.y();
                            }
                        }).poll();

                        minx = -32;
                        miny = -64;
                        minz = -32;
                        maxx = 32;
                        maxy = 0;
                        maxz = 32;
                        updateBounds();

                        selecting = false;
                        break loops;
                    }
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

    public void exit()
    {
        ctx.controller.stop();
    }

    public void changeMinx(int newValue){
        minx = newValue;
        updateBounds();
    }
    public void changeMiny(int newValue){
        miny = newValue;
        updateBounds();
    }
    public void changeMinz(int newValue){
        minz = newValue;
        updateBounds();
    }
    public void changeMaxx(int newValue){
        maxx = newValue;
        updateBounds();
    }
    public void changeMaxy(int newValue){
        maxy = newValue;
        updateBounds();
    }
    public void changeMaxz(int newValue){
        maxz = newValue;
        updateBounds();
    }
    private void updateBounds(){
        selectedModel.bounds(minx,maxx,miny,maxy,minz,maxz);
        gui.updateValues();
    }
}
