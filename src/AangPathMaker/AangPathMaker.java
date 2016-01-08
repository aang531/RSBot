package AangPathMaker;

import Util.AangScript;
import Util.Movement.Obstacle;
import org.powerbot.script.Filter;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Npc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

@Script.Manifest(name = "AangPathMaker", description = "Tool to make paths with obstacles.", properties="client=4;author=aang521")
public class AangPathMaker extends AangScript implements MouseListener {

    private List<Tile> path = new ArrayList<Tile>();
    private List<Obstacle> obstacles = new ArrayList<Obstacle>();

    private boolean selectingObstacle = false;
    private boolean selectingFirstTile = false;
    private boolean selectingSecondTile = false;

    private Interactive obstacle;

    private GUI gui;

    @Override
    public void start(){
        gui = new GUI(this);
        ctx.input.blocking(false);
    }

    @Override
    public void stop() {
        gui.dispose();
    }

    public void exit()
    {
        ctx.controller.stop();
    }

    public void addTileButtonPressed(){
        DefaultListModel lm = (DefaultListModel) gui.pathList.getModel();
        if( gui.pathList.getSelectedIndex() == -1 ) {
            path.add(ctx.players.local().tile());
            lm.addElement(ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y() + (ctx.players.local().tile().floor()!=0?", " + ctx.players.local().tile().floor():""));
        }else{
            int index = gui.pathList.getSelectedIndex();
            path.add(gui.pathList.getSelectedIndex(), ctx.players.local().tile());
            lm.add(gui.pathList.getSelectedIndex(),ctx.players.local().tile().x() + ", " + ctx.players.local().tile().y());
            gui.pathList.setSelectedIndex(index+1);
        }
    }

    public void removeTileButtonPressed() {
        int index = gui.pathList.getSelectedIndex();
        path.remove(index);
        DefaultListModel lm = (DefaultListModel)gui.pathList.getModel();
        lm.remove(index);
    }

    public void pathListSelectedChanged(){
        if( gui.pathList.getSelectedIndex() == -1 ) {
            gui.tileX.setText("");
            gui.tileY.setText("");
        }else{
            gui.tileX.setText(path.get(gui.pathList.getSelectedIndex()).x()+"");
            gui.tileY.setText(path.get(gui.pathList.getSelectedIndex()).y()+"");
        }
    }

    public void tileXChanged(){
        try {
            int val = Integer.parseInt(gui.tileX.getText());
            Tile t = path.get(gui.pathList.getSelectedIndex());
            path.set(gui.pathList.getSelectedIndex(),new Tile(val,t.y(),t.floor()));
            DefaultListModel lm = (DefaultListModel) gui.pathList.getModel();
            lm.set(gui.pathList.getSelectedIndex(),val + ", " + t.y() + (t.floor()!=0?", " + t.floor():""));
        }catch(Exception e){
            gui.tileX.setText(path.get(gui.pathList.getSelectedIndex()).x()+"");
        }
    }

    public void tileYChanged(){
        try {
            int val = Integer.parseInt(gui.tileY.getText());
            Tile t = path.get(gui.pathList.getSelectedIndex());
            path.set(gui.pathList.getSelectedIndex(),new Tile(t.x(),val,t.floor()));
            DefaultListModel lm = (DefaultListModel) gui.pathList.getModel();
            lm.set(gui.pathList.getSelectedIndex(),t.x() + ", " + val + (t.floor()!=0?", " + t.floor():""));
        }catch(Exception e){
            gui.tileY.setText(path.get(gui.pathList.getSelectedIndex()).y()+"");
        }
    }

    public void removeObstaclePressed(){
        int index = gui.obstacleList.getSelectedIndex();
        obstacles.remove(index);
        DefaultListModel lm = (DefaultListModel)gui.obstacleList.getModel();
        lm.remove(index);
        gui.obstacleList.setModel(lm);
    }

    public void newObstaclePressed(){
        DefaultListModel lm = (DefaultListModel) gui.obstacleList.getModel();
        obstacles.add(new Obstacle());
        lm.addElement("Name: NULL ID: 0");
        gui.obstacleList.setSelectedIndex(obstacles.size()-1);
    }

    public void obstacleListSelectedChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        if( index != -1 ) {
            Obstacle o = obstacles.get(index);
            gui.idLabel.setText("ID: " + o.id);
            if( obstacles.get(index).bounds.length == 0 ) {
                gui.xStart.setValue(-32);
                gui.yStart.setValue(-64);
                gui.zStart.setValue(-32);
                gui.xEnd.setValue(32);
                gui.yEnd.setValue(0);
                gui.zEnd.setValue(32);
            }else{
                gui.xStart.setValue(obstacles.get(index).bounds[0]);
                gui.yStart.setValue(obstacles.get(index).bounds[2]);
                gui.zStart.setValue(obstacles.get(index).bounds[4]);
                gui.xEnd.setValue(obstacles.get(index).bounds[1]);
                gui.yEnd.setValue(obstacles.get(index).bounds[3]);
                gui.zEnd.setValue(obstacles.get(index).bounds[5]);
            }
            gui.xPos.setText(""+o.area.x);
            gui.yPos.setText(""+o.area.y);
            gui.width.setText(""+o.area.width);
            gui.height.setText(""+o.area.height);
            gui.action.setText(o.action);
            gui.obstacleType.setSelectedIndex(o.type == Obstacle.ObstacleType.OBJECT ? 0 : 1);
        }else{
            gui.idLabel.setText("ID: ");
            gui.xStart.setValue(-32);
            gui.yStart.setValue(-64);
            gui.zStart.setValue(-32);
            gui.xEnd.setValue(32);
            gui.yEnd.setValue(0);
            gui.zEnd.setValue(32);
            gui.xPos.setText("");
            gui.yPos.setText("");
            gui.width.setText("");
            gui.height.setText("");
            gui.action.setText("");
            gui.obstacleType.setSelectedIndex(0);
        }
    }

    public void inputPressed(){
        //TODO
    }

    public void outputPressed(){
        String out = "private Path path = new Path( new Tile[] {";
        for( int i = 0; i < path.size(); i++ ) {
            if (i == 0)
                out += "new Tile(" + path.get(i).x() + "," + path.get(i).y() + (path.get(i).floor() != 0 ? "," + path.get(i).floor() : "" ) + ")";
            else
                out += "," + (i % 4 == 3 && i != path.size() - 1 ? "\n\t" : " ") + "new Tile(" + path.get(i).x() + "," + path.get(i).y() + (path.get(i).floor() != 0 ? "," + path.get(i).floor() : "" ) + ")";
        }

        out += "},\n\tnew Obstacle[]{";
        for( int i = 0; i < obstacles.size(); i++ ) {
            final Obstacle o = obstacles.get(i);
            if (i != 0)
                out += ",\n\t";
            else
                out += " ";
            out += "new Obstacle(" + o.id + ", " +
                    (o.type == Obstacle.ObstacleType.OBJECT ? "new Tile(" + o.obstacleTile.x() + ", " + o.obstacleTile.y() + (o.obstacleTile.floor() != 0 ? ", "+o.obstacleTile.floor() : ""): "" ) + "), " +
                    "new TileArea( " + o.area.x + ", " + o.area.y + ", " + o.area.width + ", " + o.area.height + (o.area.floor != 0 ? ", " + o.area.floor : "" ) + " ), " +
                    "\"" + o.action + "\" " +
                    ( o.bounds.length != 0 ? ", new int[]{" + o.bounds[0] + ", " + o.bounds[1] + ", " + o.bounds[2] + ", " + o.bounds[3] + ", " + o.bounds[4] + ", " + o.bounds[5] + "} ": "") +
                    ")";
        }
        out += "});";
        new OutputWindow(out);
    }

    public void selectFirstTilePressed(){
        ctx.input.blocking(true);
        selectingObstacle = false;
        gui.selectButton.setEnabled(true);
        selectingFirstTile = true;
        gui.selectFirstTileButton.setEnabled(false);
        selectingSecondTile = false;
        gui.selectSecondTileButton.setEnabled(true);
    }

    public void selectSecondTilePressed(){
        ctx.input.blocking(true);
        selectingObstacle = false;
        gui.selectButton.setEnabled(true);
        selectingFirstTile = false;
        gui.selectFirstTileButton.setEnabled(true);
        selectingSecondTile = true;
        gui.selectSecondTileButton.setEnabled(false);
    }

    public void xStartChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[0] = (Integer)gui.xStart.getValue();
        boundsUpdated();
    }

    public void yStartChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[2] = (Integer)gui.yStart.getValue();
        boundsUpdated();
    }

    public void zStartChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[4] = (Integer)gui.zStart.getValue();
        boundsUpdated();
    }

    public void xEndChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[1] = (Integer)gui.xEnd.getValue();
        boundsUpdated();
    }

    public void yEndChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[3] = (Integer)gui.yEnd.getValue();
        boundsUpdated();
    }

    public void zEndChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( o.bounds.length == 0 ){
            o.bounds = new int[]{-32,32,-64,0,-32,32};
        }
        o.bounds[5] = (Integer)gui.zEnd.getValue();
        boundsUpdated();
    }

    public void xPosChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        try {
            o.area.x = Integer.parseInt(gui.xPos.getText());
        }catch(Exception e){
            gui.xPos.setText(o.area.x+"");
        }
    }

    public void yPosChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        try {
            o.area.y = Integer.parseInt(gui.yPos.getText());
        }catch(Exception e){
            gui.yPos.setText(o.area.y+"");
        }
    }

    public void widthChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        try {
            o.area.width = Integer.parseInt(gui.width.getText());
        }catch(Exception e){
            gui.width.setText(o.area.width+"");
        }
    }

    public void heightChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        try {
            o.area.height = Integer.parseInt(gui.height.getText());
        }catch(Exception e){
            gui.height.setText(o.area.height+"");
        }
    }

    public void obstacleTypeChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        o.type = gui.obstacleType.getSelectedIndex() == 0 ? Obstacle.ObstacleType.OBJECT : Obstacle.ObstacleType.NPC;
    }

    public void selectObstaclePressed(){
        ctx.input.blocking(true);

        selectingObstacle = true;
        gui.selectButton.setEnabled(false);

        selectingFirstTile = false;
        gui.selectFirstTileButton.setEnabled(true);
        selectingSecondTile = false;
        gui.selectSecondTileButton.setEnabled(true);
    }

    public void actionChanged(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        o.action = gui.action.getText();
    }

    private void boundsUpdated(){
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        if( obstacle != null && obstacle.valid() ){
            obstacle.bounds(o.bounds);
        }
        gui.xStart.setValue(o.bounds[0]);
        gui.xEnd.setValue(o.bounds[1]);
        gui.yStart.setValue(o.bounds[2]);
        gui.yEnd.setValue(o.bounds[3]);
        gui.zStart.setValue(o.bounds[4]);
        gui.zEnd.setValue(o.bounds[5]);
    }

    @Override
    public void poll() {

    }

    private static final Color notMyFloorWhite = new Color(Color.white.getRed(),Color.white.getGreen(),Color.white.getBlue(),100);
    private static final Color notMyFloorCyan = new Color(Color.cyan.getRed(),Color.cyan.getGreen(),Color.cyan.getBlue(),100);
    private static final Color notMyFloorGreen = new Color(Color.green.getRed(),Color.green.getGreen(),Color.green.getBlue(),100);

    @Override
    public void repaint(Graphics g) {
        for( int i = 0; i < path.size(); i++ ){
            boolean myFloor = true;
            if( path.get(i).floor() != ctx.players.local().tile().floor())
                myFloor = false;
            if( gui.pathList.getSelectedIndex() == i ) {
                if (myFloor) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(notMyFloorGreen);
                }
            }else {
                if (myFloor) {
                    g.setColor(Color.cyan);
                } else {
                    g.setColor(notMyFloorCyan);
                }
            }
            final Point p = path.get(i).matrix(ctx).mapPoint();
            g.fillRect(p.x-1,p.y-1,3,3);
            if (myFloor) {
                g.setColor(Color.white);
            } else {
                g.setColor(notMyFloorWhite);
            }
            if( i != 0 ){
                final Point pp = path.get(i-1).matrix(ctx).mapPoint();
                g.drawLine(pp.x, pp.y,p.x,p.y);
            }
            if(i != path.size() - 1 ){
                final Point pp = path.get(i+1).matrix(ctx).mapPoint();
                g.drawLine(pp.x, pp.y,p.x,p.y);
            }
        }

        if( gui!=null){
            int index = gui.obstacleList.getSelectedIndex();
            if( index != -1 ) {
                Obstacle o = obstacles.get(index);
                if( obstacle != null && obstacle.valid() ){
                    g.setColor(Color.green);
                    for (Polygon p : obstacle.triangles())
                        g.drawPolygon(p);
                }else if(o.id != -1){
                    if( o.type == Obstacle.ObstacleType.OBJECT )
                        obstacle = objects.get(o.id,o.obstacleTile);
                    else
                        obstacle = npcs.getNearest(o.id);
                }
                if( o.area.floor == ctx.players.local().tile().floor()) {
                    g.setColor(new Color(255,200,0,100));

                    Tile t0, t1, t2, t3;
                    t0 = new Tile(o.area.x,o.area.y,o.area.floor);
                    t1 = new Tile(o.area.x+o.area.width,o.area.y,o.area.floor);
                    t2 = new Tile(o.area.x+o.area.width,o.area.y+o.area.height,o.area.floor);
                    t3 = new Tile(o.area.x,o.area.y+o.area.height,o.area.floor);

                    Polygon p = new Polygon();
                    p.addPoint(t0.matrix(ctx).bounds().xpoints[0],t0.matrix(ctx).bounds().ypoints[0]);
                    p.addPoint(t1.matrix(ctx).bounds().xpoints[1],t1.matrix(ctx).bounds().ypoints[1]);
                    p.addPoint(t2.matrix(ctx).bounds().xpoints[2],t2.matrix(ctx).bounds().ypoints[2]);
                    p.addPoint(t3.matrix(ctx).bounds().xpoints[3],t3.matrix(ctx).bounds().ypoints[3]);

                    g.fillPolygon(p);
                }
            }
        }
    }
//TODO add reset button
    @Override
    public void mouseClicked(MouseEvent e) {//TODO add change listeners for text fields when they lose focus
        int index = gui.obstacleList.getSelectedIndex();
        Obstacle o = obstacles.get(index);
        final Tile t = misc.getTileUnderPoint(e.getPoint());
        if(selectingFirstTile){//todo adjust width and hight accordingly
            if( t != null ) {
                ctx.input.blocking(false);
                o.area.x = t.x();
                o.area.y = t.y();
                o.area.floor = t.floor();
                selectingFirstTile = false;
                gui.selectFirstTileButton.setEnabled(true);
                gui.xPos.setText(o.area.x+"");
                gui.yPos.setText(o.area.y+"");
            }
        }else if(selectingSecondTile){
            if( t != null ) {
                ctx.input.blocking(false);
                final int minx = Math.min(t.x(),o.area.x);
                final int maxx = Math.max(t.x(),o.area.x);
                final int miny = Math.min(t.y(),o.area.y);
                final int maxy = Math.max(t.y(),o.area.y);
                o.area.x = minx;
                o.area.y = miny;
                o.area.width = maxx-minx;
                o.area.height = maxy-miny;
                gui.xPos.setText(o.area.x+"");
                gui.yPos.setText(o.area.y+"");
                gui.width.setText(o.area.width+"");
                gui.height.setText(o.area.height+"");
                selectingSecondTile = false;
                gui.selectSecondTileButton.setEnabled(true);
            }
        }else if(selectingObstacle) {
            if (o.type == Obstacle.ObstacleType.OBJECT) {
                GameObject obj = ctx.objects.select().select(new Filter<GameObject>() {
                    @Override
                    public boolean accept(GameObject gameObject) {
                        return gameObject.type() != GameObject.Type.FLOOR_DECORATION && gameObject.type() != GameObject.Type.WALL_DECORATION &&
                                gameObject.tile().x() == t.x() && gameObject.tile().y() == t.y();
                    }
                }).poll();
                if (obj != null && obj.valid()) {
                    ctx.input.blocking(false);
                    selectingObstacle = false;
                    gui.selectButton.setEnabled(true);
                    o.id = obj.id();
                    o.obstacleTile = t;
                    obstacle = obj;
                    o.bounds = new int[]{-32, 32, -64, 0, -32, 32};
                    boundsUpdated();
                    gui.idLabel.setText("ID: " + o.id);

                    DefaultListModel lm = (DefaultListModel) gui.obstacleList.getModel();
                    lm.setElementAt("Name: " + obj.name() + " ID: " + obj.id() , index);
                }
            }else if(o.type == Obstacle.ObstacleType.NPC){
                Npc npc = ctx.npcs.select().select(new Filter<Npc>() {
                    @Override
                    public boolean accept(Npc npc) {
                        return npc.tile().x() == t.x() && npc.tile().y() == t.y() && npc.tile().floor() == ctx.players.local().tile().floor();
                    }
                }).poll();
                if (npc != null && npc.valid()) {
                    selectingObstacle = false;
                    gui.selectButton.setEnabled(true);
                    o.id = npc.id();
                    obstacle = npc;
                    o.bounds = new int[]{-32, 32, -128, 0, -32, 32};
                    boundsUpdated();
                    gui.idLabel.setText("ID: " + o.id);

                    DefaultListModel lm = (DefaultListModel) gui.obstacleList.getModel();
                    lm.setElementAt("Name: " + npc.name() + " ID: " + npc.id() , index);
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
}
