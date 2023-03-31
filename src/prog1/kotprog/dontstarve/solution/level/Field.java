package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.inventory.items.*;

/**
 * A pálya egy általános mezőjét leíró interface implementációja.
 */
public class Field implements BaseField {
    AbstractItem[] itemSlots = new AbstractItem[10];
    int fieldColor;

    /**
     * A class default konstruktora.
     * Minden boolean értéke alapértelmezetten hamis.
     * Az itemSlots[] array ItemEmpty értékkel lesz feltöltve.
     */
    public Field(int fieldColor) {
        this.fieldColor = fieldColor;

        for (int i = 0; i < 10; i++) {
            itemSlots[i] = new ItemEmpty();
        }
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mező járható-e.
     * @return igaz, amennyiben a mező járható; hamis egyébként
     */
    @Override
    public boolean isWalkable() {
        return fieldColor != MapColors.getWater();
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e fa.
     * @return igaz, amennyiben van fa; hamis egyébként
     */
    @Override
    public boolean hasTree() {
        return fieldColor == MapColors.getTree();
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e kő.
     * @return igaz, amennyiben van kő; hamis egyébként
     */
    @Override
    public boolean hasStone() {
        return fieldColor == MapColors.getStone();
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e gally.
     * @return igaz, amennyiben van gally; hamis egyébként
     */
    @Override
    public boolean hasTwig() {
        return fieldColor == MapColors.getTwig();
    }

    /**
     * Ezen metódus segítségével lekérdezheő, hogy a mezőn van-e bogyó.
     * @return igaz, amennyiben van bogyó; hamis egyébként
     */
    @Override
    public boolean hasBerry() {
        return fieldColor == MapColors.getBerry();
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e répa.
     * @return igaz, amennyiben van répa; hamis egyébként
     */
    @Override
    public boolean hasCarrot() {
        return fieldColor == MapColors.getCarrot();
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e tűz rakva.
     * @return igaz, amennyiben van tűz; hamis egyébként
     */
    @Override
    public boolean hasFire() {
        return false;
    }

    /**
     * Ezen metódus segítségével a mezőn lévő tárgyak lekérdezhetők.<br>
     * A tömbben az a tárgy jön hamarabb, amelyik korábban került az adott mezőre.<br>
     * A karakter ha felvesz egy tárgyat, akkor a legkorábban a mezőre kerülő tárgyat fogja felvenni.<br>
     * Ha nem sikerül felvenni, akkor a (nem) felvett tárgy a tömb végére kerül.
     * @return a mezőn lévő tárgyak
     */
    @Override
    public AbstractItem[] items() {
        return null;
    }

    /**
     * Az itemSlots[] tömbbe új elem helyezése.<br>
     * Az új elem mindig egy üres helyre kerül.
     * @param item az elhelyezendő értek
     */
    @Override
    public void addItems(AbstractItem item) {
        for (int i = 0; i < itemSlots.length; i++) {
            if (itemSlots[i].getType() == ItemType.EMPTY) {
                addItemSwitchCase(item, i);
                break;
            }
        }
    }

    private void addItemSwitchCase(AbstractItem item, int i) {
        switch (item.getType()) {
            case LOG -> itemSlots[i] = new ItemLog(item.getAmount());
            case STONE -> itemSlots[i] = new ItemStone(item.getAmount());
            case TWIG -> itemSlots[i] = new ItemTwig(item.getAmount());
            case RAW_BERRY -> itemSlots[i] = new ItemRawBerry(item.getAmount());
            case RAW_CARROT -> itemSlots[i] = new ItemRawCarrot(item.getAmount());
            case COOKED_BERRY -> itemSlots[i] = new ItemCookedBerry(item.getAmount());
            case COOKED_CARROT -> itemSlots[i] = new ItemCookedCarrot(item.getAmount());
            case AXE -> itemSlots[i] = new ItemAxe();
            case PICKAXE -> itemSlots[i] = new ItemPickaxe();
            case TORCH -> itemSlots[i] = new ItemTorch();
            case SPEAR -> itemSlots[i] = new ItemSpear();
        }
    }
}
