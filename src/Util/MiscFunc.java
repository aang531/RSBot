package Util;

import org.powerbot.script.MenuCommand;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Component;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MiscFunc extends AangUtil {
    private static MiscFunc ourInstance = new MiscFunc();

    public static MiscFunc getInstance() {
        return ourInstance;
    }


    public int getGEPrice(int id){
        try {
            String price;
            URL url = new URL("http://services.runescape.com/m=itemdb_oldschool/Wine_of_zamorak/viewitem?obj=" + id);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if( line.contains("Current Guide Price") ) {
                    price = line.substring(line.indexOf("<span title='")+13,line.indexOf("'>")).replace(",","");
                    return Integer.parseInt(price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean pointOnScreen(Point p){
        return p.x > 3 && p.y > 3 && p.x < 516 && p.y < 338;
    }

    public Tile getTileUnderPoint(Point p){
        Tile playerTile = ctx.players.local().tile();
        for (int y = -10; y <= 10; y++)
            for (int x = -10; x <= 10; x++) {
                if (playerTile.derive(x, y).matrix(ctx).bounds().contains(p)) {
                    return playerTile.derive(x, y);
                }
            }
        return null;
    }

    public void openInventory(){
        ctx.input.send("{VK_ESCAPE down}");
        ctx.input.send("{VK_ESCAPE up}");
    }
    public void openAttackTab() {
        ctx.input.send("{VK_F1 down}");
        ctx.input.send("{VK_F1 up}");
    }

    public void openLevelsTab() {
        ctx.input.send("{VK_F2 down}");
        ctx.input.send("{VK_F2 up}");
    }

    public void openQuestTab() {
        ctx.input.send("{VK_F3 down}");
        ctx.input.send("{VK_F3 up}");
    }

    public void openEquipmentTab() {
        ctx.input.send("{VK_F4 down}");
        ctx.input.send("{VK_F4 up}");
    }

    public void openPrayerTab() {
        ctx.input.send("{VK_F5 down}");
        ctx.input.send("{VK_F5 up}");
    }

    public void openMagicTab() {
        ctx.input.send("{VK_F6 down}");
        ctx.input.send("{VK_F6 up}");
    }

    public void openClanTab() {
        ctx.input.send("{VK_F7 down}");
        ctx.input.send("{VK_F7 up}");
    }

    public void openFriendTab() {
        ctx.input.send("{VK_F8 down}");
        ctx.input.send("{VK_F8 up}");
    }

    public void openIgnoreTab() {
        ctx.input.send("{VK_F9 down}");
        ctx.input.send("{VK_F9 up}");
    }

    public void openLogoutTab() {
        Component c = ctx.widgets.widget(548).component(31);
        ctx.input.hop(c.centerPoint());
        AangUtil.sleep(80);
        ctx.input.click(true);
    }

    public void openOptionsTab() {
        ctx.input.send("{VK_F10 down}");
        ctx.input.send("{VK_F10 up}");
    }

    public void openEmoteTab() {
        ctx.input.send("{VK_F11 down}");
        ctx.input.send("{VK_F11 up}");
    }

    public void openMusicTab() {
        ctx.input.send("{VK_F12 down}");
        ctx.input.send("{VK_F12 up}");
    }
}
