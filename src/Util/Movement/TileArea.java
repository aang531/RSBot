package Util.Movement;


import org.powerbot.script.Tile;

public class TileArea {

    public int x, y, width, height,floor;

    public TileArea( int x, int y, int width, int height ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        floor = 0;
    }

    public TileArea( int x, int y, int width, int height, int floor ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.floor = floor;
    }

    public boolean contains(Tile t) {
        return floor == t.floor() && t.x() >= x && t.x() <= x + width && t.y() >= y && t.y() <= y + height;
    }
}
