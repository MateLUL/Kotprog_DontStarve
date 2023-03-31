package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * Egy általános itemet leíró osztály.
 */
public abstract class AbstractItem {
    /**
     * Az item típusa.
     * @see ItemType
     */
    private final ItemType type;

    /**
     * Az item mennyisége.
     */
    private int amount;

    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     * @param type az item típusa
     * @param amount az item mennyisége
     */
    public AbstractItem(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }


    /**
     * A type gettere.
     * @return a tárgy típusa
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Az amount gettere.
     * @return a tárgy mennyisége
     */
    public int getAmount() {
        return amount;
    }

    public void setAmount(int newAmount) {
        amount = newAmount;
    }

    public boolean isStackable() {
        return getType() != ItemType.AXE && getType() != ItemType.PICKAXE && getType() != ItemType.SPEAR && getType() != ItemType.TORCH;
    }

    public int getMaxStack() {
        return switch (getType()) {
            case LOG -> 15;
            case STONE, RAW_CARROT, RAW_BERRY, COOKED_CARROT, COOKED_BERRY -> 10;
            case TWIG -> 20;
            case SPEAR, AXE, PICKAXE, TORCH -> 1;
            default -> 0;
        };
    }
}
