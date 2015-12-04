package AangFighter;

/**
 * Created by Cas on 02-12-2015.
 */
public class Monster {
    public final int ids[];

    public Monster(int ids[])
    {
        this.ids = ids;
    }

    public static final Monster chickens = new Monster(new int[]{2692,2693});
    public static final Monster cows = new Monster(new int[]{2805,2806,2807,2808,2809});
}