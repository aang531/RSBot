package Util;

public class PrayerFunc extends AangUtil {
    private static PrayerFunc ourInstance = new PrayerFunc();

    public static PrayerFunc getInstance() {
        return ourInstance;
    }

    public static final int PRAYER_THICK_SKIN               = 0x1;
    public static final int PRAYER_BURST_OF_STRENGTH        = 0x2;
    public static final int PRAYER_CLARITY_OF_THOUGHT       = 0x4;
    public static final int PRAYER_SHARP_EYE                = 0x40000;
    public static final int PRAYER_MYSTIC_WILL              = 0x80000;
    public static final int PRAYER_ROCK_SKIN                = 0x8;
    public static final int PRAYER_SUPERHUMAN_STRENGTH      = 0x10;
    public static final int PRAYER_IMPROVED_REFLEXES        = 0x20;
    public static final int PRAYER_RAPID_RESTORE            = 0x40;
    public static final int PRAYER_RAPID_HEAL               = 0x80;
    public static final int PRAYER_PROTECT_ITEM             = 0x100;
    public static final int PRAYER_HAWK_EYE                 = 0x100000;
    public static final int PRAYER_MYSTIC_LORE              = 0x200000;
    public static final int PRAYER_STEEL_SKIN               = 0x200;
    public static final int PRAYER_ULTIMATE_STRENGTH        = 0x400;
    public static final int PRAYER_INCREDIBLE_REFLEXES      = 0x800;
    public static final int PRAYER_PROTECT_FROM_MAGIC       = 0x1000;
    public static final int PRAYER_PROTECT_FROM_MISSILES    = 0x2000;
    public static final int PRAYER_PROTECT_FROM_RANGE       = PRAYER_PROTECT_FROM_MISSILES;
    public static final int PRAYER_PROTECT_FROM_MELEE       = 0x4000;
    public static final int PRAYER_EAGLE_EYE                = 0x400000;
    public static final int PRAYER_MYSTIC_MIGHT             = 0x800000;
    public static final int PRAYER_RETRIBUTION              = 0x8000;
    public static final int PRAYER_REDEMPTION               = 0x10000;
    public static final int PRAYER_SMITE                    = 0x20000;
    public static final int PRAYER_CHIVALRY                 = 0x40000;
    public static final int PRAYER_PIETY                    = 0x80000;

    public boolean quickPrayActive() {
        return ctx.client().getVarpbits()[375] == 1;
    }

    public boolean praying() {
        return ctx.client().getVarpbits()[83] > 0;
    }

    public int points() {
        return ctx.prayer.points();
    }

    public boolean quickPrayerHas(int pray) {
        return (ctx.client().getVarpbits()[84] & pray) != 0;
    }

    public boolean prayerActive(int pray){
        return (ctx.client().getVarpbits()[83] & pray) != 0;
    }
}
