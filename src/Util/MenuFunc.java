package Util;

import org.powerbot.bot.rt4.client.Client;

import java.awt.*;

public class MenuFunc extends AangUtil {
    private static MenuFunc ourInstance = new MenuFunc();

    public static MenuFunc getInstance() {
        return ourInstance;
    }

    public boolean clickMenuOption( int index ){
        if( ctx.menu.opened() ) {
            Point loc = ctx.menu.bounds().getLocation();
            ctx.input.hop(loc.x + 40, loc.y + 27+ index * 15);
            sleep(80);
            return ctx.input.click(true);
        }
        return false;
    }

    public Rectangle bounds(){
        Client client = ctx.client();
        return new Rectangle(client.getMenuX(), client.getMenuY(), client.getMenuWidth(), client.getMenuHeight());
    }

    public boolean opened(){
        return ctx.client().isMenuOpen();
    }

    public void close(){
        final Rectangle b = bounds();
        b.x -= 10;
        b.y -= 10;
        b.width += 20;
        b.height += 20;
        boolean left = true;
        boolean top = true;
        if( b.x <= 5 )
            left = false;
        if( b.y <= 5 )
            top = false;
        ctx.input.hop(left ? b.x - 3 : b.x + b.width + 3, top ? b.y - 3 : b.y + b.height + 3 );
        sleep(50);
    }
}
