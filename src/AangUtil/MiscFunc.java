package AangUtil;

import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MiscFunc {
    private ClientContext ctx;
    private static MiscFunc ourInstance = new MiscFunc();

    public static MiscFunc getInstance() {
        return ourInstance;
    }

    public void init(ClientContext ctx) {
        this.ctx = ctx;
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
        return p.x > 0 && p.y > 0 && p.x < 516 && p.y < 338;
    }

    public boolean inventoryFull() {
        return ctx.inventory.select().count() == 28;
    }

    public int getMenuOptionIndex(String action, String option){
        for( int i = 0; ctx.menu.commands().length > i; i++ ){
            if( ctx.menu.commands()[i].action.equals(action) && ctx.menu.commands()[i].option.equals(option) ){
                return i;
            }
        }
        return -1;
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
}
