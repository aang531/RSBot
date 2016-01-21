package AangTest;

import Util.AangScript;
import org.powerbot.bot.rt4.client.CollisionMap;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

@Script.Manifest(name = "AangTest", description = "test", properties="client=4;author=aang521")
public class AangTest extends AangScript implements KeyListener {

    BufferedImage img;

    private static final int blockingObject = 1 << 17;//1 << 8 aswell? 256 is more objects it seems

    private static final int ALL = 16777215;

    private static final int BLOCKED = (1 << 8)|(1<<21)|(1<<24);
    private static final int WALL_NORTH = (1 << 1)|BLOCKED;//1 << 10
    private static final int WALL_EAST = (1 << 3)|BLOCKED;//1 << 12
    private static final int WALL_SOUTH = (1 << 5)|BLOCKED;//1 << 14
    private static final int WALL_WEST = (1 << 7)|BLOCKED;//1 << 16
    private static final int WALL_NORTHWEST = 1|BLOCKED|WALL_NORTH|WALL_WEST;//1 << 9 as well
    private static final int WALL_NORTHEAST = (1 << 2)|BLOCKED|WALL_NORTH|WALL_EAST;//1 << 11
    private static final int WALL_SOUTHEAST = (1 << 4)|BLOCKED|WALL_SOUTH|WALL_EAST;//1 << 13
    private static final int WALL_SOUTHWEST = (1 << 6)|BLOCKED|WALL_SOUTH|WALL_WEST;//1 << 15

    //1 << 18 trapdoor and grapple point
    //1 << 21 water
    //1 << 24 null?
    //(1 << 24) - 1 not loaded tiles part of map

    private static final int TEST = 1 << 18;

    private int blockColor = 0x3fff0000;
    private int emptyColor = 0x3f00ff00;
    private int playerColor = 0xff0000ff;
    private int notFoundColor = 0xff000000;

    private boolean followMap = true;

    public int getExponent(int val){
        int ret = 0;
        while(val != 0){
            val >>= 1;
            ret++;
        }
        return ret-1;
    }

    void updateImage(){
        CollisionMap map = ctx.client().getCollisionMaps()[ctx.game.floor()];
        int[][] flags = map.getFlags();

        final int size = flags.length - 6;

        for( int y = 0; y < size; y++ )
            for( int x = 0; x < size; x++ ){
                final Tile offset = ctx.game.mapOffset();
                final boolean same = ctx.players.local().tile().x() == offset.x()+x+1 && ctx.players.local().tile().y() == offset.y()+size-y;

                final int flag = flags[x+1][size-y];
                boolean notFound = false;
                if( (1 << 24) - 1 == flag || (flag & (1 << 18 )) != 0 ) {
                    notFound = true;
                    notFoundColor = 0xffff00ff;
                }else if( (flag & 1<<19) != 0 || (flag & 1<<20) != 0 || (flag & 1<<22) != 0 || (flag & 1<<23) != 0 ){
                    notFound = true;
                    notFoundColor = 0xff000000;
                    log("Found new flag: " + getExponent(flag) + " at " + (offset.x()+x+1) + ", " + (offset.y()+size-y) + ", " + ctx.game.floor());
                }

                img.setRGB(x*4  ,y*4  ,notFound ? notFoundColor : same ? playerColor : (flag & WALL_NORTHWEST) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+1,y*4  ,notFound ? notFoundColor : same ? playerColor : (flag & WALL_NORTH) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+2,y*4  ,notFound ? notFoundColor : same ? playerColor : (flag & WALL_NORTH) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+3,y*4  ,notFound ? notFoundColor : same ? playerColor : (flag & WALL_NORTHEAST) != 0 ? blockColor : emptyColor);

                img.setRGB(x*4  ,y*4+1,notFound ? notFoundColor : same ? playerColor : (flag & WALL_WEST) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+1,y*4+1,notFound ? notFoundColor : same ? playerColor : (flag & BLOCKED) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+2,y*4+1,notFound ? notFoundColor : same ? playerColor : (flag & BLOCKED) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+3,y*4+1,notFound ? notFoundColor : same ? playerColor : (flag & WALL_EAST) != 0 ? blockColor : emptyColor);

                img.setRGB(x*4  ,y*4+2,notFound ? notFoundColor : same ? playerColor : (flag & WALL_WEST) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+1,y*4+2,notFound ? notFoundColor : same ? playerColor : (flag & BLOCKED) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+2,y*4+2,notFound ? notFoundColor : same ? playerColor : (flag & BLOCKED) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+3,y*4+2,notFound ? notFoundColor : same ? playerColor : (flag & WALL_EAST) != 0 ? blockColor : emptyColor);

                img.setRGB(x*4  ,y*4+3,notFound ? notFoundColor : same ? playerColor : (flag & WALL_SOUTHWEST) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+1,y*4+3,notFound ? notFoundColor : same ? playerColor : (flag & WALL_SOUTH) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+2,y*4+3,notFound ? notFoundColor : same ? playerColor : (flag & WALL_SOUTH) != 0 ? blockColor : emptyColor);
                img.setRGB(x*4+3,y*4+3,notFound ? notFoundColor : same ? playerColor : (flag & WALL_SOUTHEAST) != 0 ? blockColor : emptyColor);
            }
    }

    @Override
    public void start(){
        CollisionMap map = ctx.client().getCollisionMaps()[ctx.game.floor()];

        int[][] flags = map.getFlags();

        log(map.getOffsetX() + ", " + map.getOffsetY());
        log(""+ctx.game.mapOffset());
        log(flags.length + ", " + flags[0].length);

        final int size = flags.length - 6;

        img = new BufferedImage(size*4,size*4,BufferedImage.TYPE_INT_ARGB);

        updateImage();

        ctx.input.blocking(false);
    }

    @Override
    public void poll() {
        if( !game.loading() )
            updateImage();
        else
            sleep(500);
        sleep(100);
    }

    @Override
    public void repaint(Graphics g) {
        if (followMap) {
            final Tile offset = ctx.game.mapOffset();
            int xoff = offset.x() - ctx.players.local().tile().x();
            int yoff = offset.y() - ctx.players.local().tile().y();
            g.drawImage(img, xoff * 4 + 3 + 642, -(yoff * 4 + 99 * 4) + 3 + 83, null);
        }else{
            g.drawImage(img, 0, 0, null);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if( e.getKeyCode() == KeyEvent.VK_TAB ){
            e.consume();
            followMap = !followMap;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
