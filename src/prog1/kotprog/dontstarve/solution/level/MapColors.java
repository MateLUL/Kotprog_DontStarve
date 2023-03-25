package prog1.kotprog.dontstarve.solution.level;

import java.awt.*;

/**
 * A betöltendő mapen előforduló színek enumja.
 */
public class MapColors {
    /**
     * Üres mező.
     */
    static final int EMPTY = 0xFF32C832;

    /**
     * Vizet tartalmazó mező.
     */
    static final int WATER = 0xFF3264C8;

    /**
     * Fát tartalmazó mező.
     */
    static final int TREE = 0xFFC86432;

    /**
     * Követ tartalmazó mező.
     */
    static final int STONE = 0xFFC8C8C8;

    /**
     * Gallyat tartalmazó mező.
     */
    static final int TWIG = 0xFFF0B478;

    /**
     * Bogyót tartalmazó mező.
     */
    static final int BERRY = 0xFFFF0000;

    /**
     * Répát tartalmazó mező.
     */
    static final int CARROT = 0xFFFAC800;

    public static int getEmpty() {
        Color empty = new Color(EMPTY);
        return empty.getRGB();
    }
    public static int getWater() {
        Color water = new Color(WATER);
        return water.getRGB();
    }
    public static int getTree() {
        Color tree = new Color(TREE);
        return tree.getRGB();
    }
    public static int getStone() {
        Color stone = new Color(STONE);
        return stone.getRGB();
    }
    public static int getTwig() {
        Color twig = new Color(TWIG);
        return twig.getRGB();
    }
    public static int getBerry() {
        Color berry = new Color(BERRY);
        return berry.getRGB();
    }
    public static int getCarrot() {
        Color carrot = new Color(CARROT);
        return carrot.getRGB();
    }
}
