package AangUtil.Movement;

import org.powerbot.script.Tile;

public class RectTileArea extends TileArea {

    private int x, y, width, height;

    public RectTileArea( int x, int y, int width, int height ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean contains(Tile t) {
        return t.x() >= x && t.x() <= x + width && t.y() >= y && t.y() <= y + height;
    }
}
