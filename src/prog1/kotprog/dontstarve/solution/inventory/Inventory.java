package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.*;

import java.util.Objects;

/**
 * Egy egyszerű Inventory-t leíró interface implementációja.
 */
public class Inventory implements BaseInventory {
    AbstractItem[] inventorySlots = new AbstractItem[10];
    EquippableItem equipped;
    final int slotSize;


    /**
     * A class default konstruktora.
     */
    public Inventory() {
        slotSize = 10;

        for (int i = 0; i < slotSize; i++) {
            inventorySlots[i] = new ItemEmpty();
        }
        equipped = new ItemEmptyEquipped();
    }

    /**
     * Egy item hozzáadása az inventorySlots-hoz.<br>
     * Ha a hozzáadni kívánt tárgy halmozható, akkor a meglévő stack-be kerül (ha még fér, különben új stacket kezd),
     * egyébként a legelső új helyre kerül.<br>
     * Ha egy itemből van több megkezdett stack, akkor az inventorySlots-ban hamarabb következőhöz adjuk hozzá
     * (ha esetleg ott nem fér el mind, akkor azt feltöltjük, és utána folytatjuk a többivel).<br>
     * Ha az adott itemből nem fér el mind az inventorySlots-ban, akkor ami elfér azt adjuk hozzá, a többit pedig nem
     * (ebben az esetben a hívó félnek tudnia kell, hogy mennyit nem sikerült hozzáadni).
     * @param item a hozzáadni kívánt tárgy
     * @return igaz, ha sikerült hozzáadni a tárgyat teljes egészében; hamis egyébként
     */
    @Override
    public boolean addItem(AbstractItem item) {
        boolean hasStackInInventory = false;

        //If stackable
        if (item.isStackable()) {
            //Check if inventorySlots has an existing stack
            for (int i = 0; i < inventorySlots.length; i++) {
                if (!Objects.isNull(getItem(i)) && getItem(i).getType() == item.getType()) {
                    //If the items can fit in the stack
                    if (getItem(i).getAmount() + item.getAmount() <= item.getMaxStack()) {
                        getItem(i).setAmount(item.getAmount() + getItem(i).getAmount());
                        item.setAmount(0);
                        hasStackInInventory = true;
                        break;
                    } else {
                        int fillStack = item.getMaxStack() - getItem(i).getAmount();
                        item.setAmount(item.getAmount() - fillStack);
                        getItem(i).setAmount(item.getMaxStack());
                    }
                }
            }

            //If doesn't have a stack or there are leftover items
            if (!hasStackInInventory) {
                for (int i = 0; i < inventorySlots.length; i++) {
                    if (Objects.isNull(getItem(i))) {
                        //If items fit in a stack
                        if (item.getAmount() <= item.getMaxStack()) {
                            addItemTypeToInventorySlot(item, i, item.getAmount());
                            item.setAmount(0);
                            break;
                        } else {
                            addItemTypeToInventorySlot(item, i, item.getMaxStack());
                            item.setAmount(item.getAmount() - item.getMaxStack());
                        }
                    }
                }
            }
        }
        //If not stackable
        if (!item.isStackable()) {
            for (int i = 0; i < inventorySlots.length; i++) {
                if (Objects.isNull(getItem(i))) {
                    addItemTypeToInventorySlot(item, i, 1);
                    item.setAmount(0);
                    break;
                }
            }
        }

        return item.getAmount() <= 0;
    }

    //Adding items to the inventorySlots slot based on the item's type
    private void addItemTypeToInventorySlot(AbstractItem item, int i, int amount) {
        switch (item.getType()) {
            case LOG -> inventorySlots[i] = new ItemLog(amount);
            case STONE -> inventorySlots[i] = new ItemStone(amount);
            case TWIG -> inventorySlots[i] = new ItemTwig(amount);
            case RAW_BERRY -> inventorySlots[i] = new ItemRawBerry(amount);
            case RAW_CARROT -> inventorySlots[i] = new ItemRawCarrot(amount);
            case COOKED_BERRY -> inventorySlots[i] = new ItemCookedBerry(amount);
            case COOKED_CARROT -> inventorySlots[i] = new ItemCookedCarrot(amount);
            case AXE -> inventorySlots[i] = new ItemAxe();
            case PICKAXE -> inventorySlots[i] = new ItemPickaxe();
            case TORCH -> inventorySlots[i] = new ItemTorch();
            case SPEAR -> inventorySlots[i] = new ItemSpear();
        }
    }

    /**
     * Egy tárgy kidobása az inventorySlots-ból.
     * Hatására a tárgy eltűnik az inventorySlots-ból.
     * @param index a slot indexe, amelyen lévő itemet szeretnénk eldobni
     * @return az eldobott item
     */
    @Override
    public AbstractItem dropItem(int index) {
        if (index < slotSize && index >= 0 && !Objects.isNull(getItem(index))) {
            AbstractItem droppedItem = inventorySlots[index];
            inventorySlots[index] = new ItemEmpty();

            return droppedItem;
        }

        return null;
    }

    /**
     * Bizonyos mennyiségű, adott típusú item törlése az inventorySlots-ból. A törölt itemek véglegesen eltűnnek.<br>
     * Ha nincs elegendő mennyiség, akkor nem történik semmi.<br>
     * Az item törlése a legkorábban lévő stackből (stackekből) történik, akkor is, ha van másik megkezdett stack.<br>
     * @param type a törlendő item típusa
     * @param amount a törlendő item mennyisége
     * @return igaz, amennyiben a törlés sikeres volt
     */
    @Override
    public boolean removeItem(ItemType type, int amount) {
        //Multiple stacks + complex problem ---> if nests causing the test to skip to the main return false statement
        //So either amountToBeDeleted != 0 || doesn't reach

        int amountInInventory = 0;
        int amountToBeDeleted = amount;

        //Counting the amount of items of that type in inventorySlots
        amountInInventory = countItemAmountInInventory(type, amountInInventory);

        //If inventorySlots has enough or more items than the amount to be deleted
        if (amountInInventory >= amountToBeDeleted) {
            for (int i = 0; i < inventorySlots.length; i++) {
                if (!Objects.isNull(getItem(i)) && getItem(i).getType() == type) {
                    //If item is stackable
                    if (getItem(i).isStackable()) {
                        //If a whole stack can be deleted
                        if (amountToBeDeleted >= getItem(i).getMaxStack()) {
                            if (getItem(i).getAmount() == getItem(i).getMaxStack()) {
                                amountToBeDeleted -= getItem(i).getMaxStack();
                            } else {
                                amountToBeDeleted -= getItem(i).getAmount();
                            }

                            inventorySlots[i] = new ItemEmpty();
                        } else if (amountToBeDeleted <= getItem(i).getAmount()) {
                            getItem(i).setAmount(getItem(i).getAmount() - amountToBeDeleted);

                            if (getItem(i).getAmount() == 0) {
                                inventorySlots[i] = new ItemEmpty();
                            }

                            amountToBeDeleted = 0;
                        } else {
                            amountToBeDeleted -= getItem(i).getAmount();
                            inventorySlots[i] = new ItemEmpty();
                        }
                    } else {
                        inventorySlots[i] = new ItemEmpty();

                        amountToBeDeleted--;
                    }
                }

                if (amountToBeDeleted == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private int countItemAmountInInventory(ItemType type, int amountInInventory) {
        for (int i = 0; i < inventorySlots.length; i++) {
            if (!Objects.isNull(getItem(i)) && getItem(i).getType() == type) {
                amountInInventory += getItem(i).getAmount();
            }
        }
        return amountInInventory;
    }

    /**
     * Két item pozíciójának megcserélése az inventorySlots-ban.<br>
     * Csak akkor használható, ha mind a két pozíción már van item.
     * @param index1 a slot indexe, amelyen az első item található
     * @param index2 a slot indexe, amelyen a második item található
     * @return igaz, ha sikerült megcserélni a két tárgyat; hamis egyébként
     */
    @Override
    public boolean swapItems(int index1, int index2) {
        if (!Objects.isNull(getItem(index1)) && !Objects.isNull(getItem(index2))) {
            AbstractItem temp = null;

            switch (getItem(index1).getType()) {
                case LOG -> temp = new ItemLog(inventorySlots[index1].getAmount());
                case STONE -> temp = new ItemStone(inventorySlots[index1].getAmount());
                case TWIG -> temp = new ItemTwig(inventorySlots[index1].getAmount());
                case RAW_BERRY -> temp = new ItemRawBerry(inventorySlots[index1].getAmount());
                case RAW_CARROT -> temp = new ItemRawCarrot(inventorySlots[index1].getAmount());
                case COOKED_BERRY -> temp = new ItemCookedBerry(inventorySlots[index1].getAmount());
                case COOKED_CARROT -> temp = new ItemCookedCarrot(inventorySlots[index1].getAmount());
                case AXE -> temp = new ItemAxe();
                case PICKAXE -> temp = new ItemPickaxe();
                case TORCH -> temp = new ItemTorch();
                case SPEAR -> temp = new ItemSpear();
            }

            //swapItemsSwitch(index1, getItem(index2).getType(), getItem(index2).getAmount());
            addItemTypeToInventorySlot(getItem(index2), index1, getItem(index2).getAmount());

            assert temp != null;
            //swapItemsSwitch(index2, temp.getType(), temp.getAmount());
            addItemTypeToInventorySlot(temp, index2, temp.getAmount());

            return true;
        }

        return false;
    }

    /**
     * Egy item átmozgatása az inventorySlots egy másik pozíciójára.<br>
     * Csak akkor használható, ha az eredeti indexen van tárgy, az új indexen viszont nincs.
     * @param index a mozgatni kívánt item pozíciója az inventorySlots-ban
     * @param newIndex az új pozíció, ahova mozgatni szeretnénk az itemet
     * @return igaz, ha sikerült a mozgatás; hamis egyébként
     */
    @Override
    public boolean moveItem(int index, int newIndex) {
        if (index >= 0 && index < slotSize && newIndex >= 0 && newIndex < slotSize && !Objects.isNull(getItem(index)) && Objects.isNull(getItem(newIndex))) {
            //swapItemsSwitch(newIndex, getItem(index).getType(), getItem(index).getAmount());
            addItemTypeToInventorySlot(getItem(index), newIndex, getItem(index).getAmount());

            inventorySlots[index] = new ItemEmpty();

            return true;
        }

        return false;
    }

    /**
     * Két azonos típusú tárgy egyesítése.<br>
     * Csak stackelhető tárgyakra használható. Ha a két stack méretének összege a maximális stack méreten belül van,
     * akkor az egyesítés az első pozíción fog megtörténni. Ha nem, akkor az első pozíción lévő stack maximálisra
     * töltődik, a másikon pedig a maradék marad.<br>
     * @param index1 első item pozíciója az inventorySlots-ban
     * @param index2 második item pozíciója az inventorySlots-ban
     * @return igaz, ha sikerült az egyesítés (változott valami a művelet hatására)
     */
    @Override
    public boolean combineItems(int index1, int index2) {
        if (!Objects.isNull(getItem(index1)) && !Objects.isNull(getItem(index2)) && getItem(index1).getType() == getItem(index2).getType() && getItem(index1).isStackable()) {
            int remainingItemAmount = getItem(index1).getAmount() + getItem(index2).getAmount();
            int index1Amount = getItem(index1).getAmount();
            int index2Amount = getItem(index2).getAmount();

            if (remainingItemAmount <= getItem(index1).getMaxStack()) {
                getItem(index1).setAmount(remainingItemAmount);
                inventorySlots[index2] = new ItemEmpty();

            } else {
                remainingItemAmount -= getItem(index1).getMaxStack();

                getItem(index1).setAmount(getItem(index1).getMaxStack());
                getItem(index2).setAmount(remainingItemAmount);

            }

            return Objects.isNull(getItem(index2)) || (index1Amount != getItem(index1).getAmount() && index2Amount != getItem(index2).getAmount());
        }

        return false;
    }

    /**
     * Egy item felvétele a karakter kezébe.<br>
     * Csak felvehető tárgyra használható. Ilyenkor az adott item a karakter kezébe kerül.
     * Ha a karakternek már tele volt a keze, akkor a kezében lévő item a most felvett item helyére kerül
     * (tehát gyakorlatilag helyet cserélnek).
     * @param index a kézbe venni kívánt tárgy pozíciója az inventorySlots-ban
     * @return igaz, amennyiben az itemet sikerült a kezünkbe venni
     */
    @Override
    public boolean equipItem(int index) {
        if (!Objects.isNull(getItem(index))) {
            if (!getItem(index).isStackable()) {
                if (equipped.getType() == ItemType.EMPTY) {
                    switch (getItem(index).getType()) {
                        case AXE -> equipped = new ItemAxe();
                        case TORCH -> equipped = new ItemTorch();
                        case SPEAR -> equipped = new ItemSpear();
                        case PICKAXE -> equipped = new ItemPickaxe();
                    }

                    inventorySlots[index] = new ItemEmpty();

                } else {
                    AbstractItem temp = new ItemEmpty();
                    switch (equipped.getType()) {
                        case AXE -> temp = new ItemAxe();
                        case TORCH -> temp = new ItemTorch();
                        case SPEAR -> temp = new ItemSpear();
                        case PICKAXE -> temp = new ItemPickaxe();
                    }

                    switch (inventorySlots[index].getType()) {
                        case AXE -> equipped = new ItemAxe();
                        case TORCH -> equipped = new ItemTorch();
                        case SPEAR -> equipped = new ItemSpear();
                        case PICKAXE -> equipped = new ItemPickaxe();
                    }

                    equipUnequipItemSwitch(index, temp);

                }
                return true;
            }
        }

        return false;
    }

    /**
     * A karakter kezében lévő tárgy inventorySlots-ba helyezése.<br>
     * A karakter kezében lévő item az inventorySlots első szabad pozíciójára kerül.
     * Ha a karakternek üres volt a keze, akkor nem történik semmi.<br>
     * Ha nincs az inventorySlots-ban hely, akkor a levett item a pálya azon mezőjére kerül, ahol a karakter állt.
     * @return a levetett item, amennyiben az nem fért el az inventorySlots-ban; null egyébként
     */
    @Override
    public EquippableItem unequipItem() {
        EquippableItem unequippedItem;

        if (emptySlots() != 0) {
            if (equipped.getType() != ItemType.EMPTY) {
                for (int i = 0; i < inventorySlots.length; i++) {
                    if (Objects.isNull(getItem(i))) {
                        equipUnequipItemSwitch(i, equipped);

                        equipped = new ItemEmptyEquipped();

                        return null;
                    }
                }
            }
        } else {
            unequippedItem = equipped;
            equipped = new ItemEmptyEquipped();
            //GameManager.getInstance().getCharacter().getCurrentPosition()

            return unequippedItem;
        }

        return null;
    }

    private void equipUnequipItemSwitch(int i, AbstractItem equipped) {
        switch (equipped.getType()) {
            case AXE -> inventorySlots[i] = new ItemAxe();
            case TORCH -> inventorySlots[i] = new ItemTorch();
            case SPEAR -> inventorySlots[i] = new ItemSpear();
            case PICKAXE -> inventorySlots[i] = new ItemPickaxe();
        }
    }

    /**
     * Egy item megfőzése.<br>
     * Csak nyers étel főzhető meg. Hatására az inventorySlots adott pozíciójáról 1 egységnyi eltűnik.
     * @param index A megfőzni kívánt item pozíciója az inventorySlots-ban
     * @return A megfőzni kívánt item típusa
     */
    @Override
    public ItemType cookItem(int index) {
        if (index >= 0 && index < slotSize && !Objects.isNull(getItem(index)) && (getItem(index).getType() == ItemType.RAW_BERRY || getItem(index).getType() == ItemType.RAW_CARROT)) {
            return eatOrCookItem(index);
        }

        return null;
    }

    /**
     * Egy item elfogyasztása.<br>
     * Csak ételek ehetők meg. Hatására az inventorySlots adott pozíciójáról 1 egységnyi eltűnik.
     * @param index A megenni kívánt item pozíciója az inventorySlots-ban
     * @return A megenni kívánt item típusa
     */
    @Override
    public ItemType eatItem(int index) {
        if (index >= 0 && index < slotSize && !Objects.isNull(getItem(index)) && (getItem(index).getType() == ItemType.COOKED_BERRY || getItem(index).getType() == ItemType.COOKED_CARROT || getItem(index).getType() == ItemType.RAW_CARROT || getItem(index).getType() == ItemType.RAW_BERRY)) {
            return eatOrCookItem(index);
        }

        return null;
    }

    private ItemType eatOrCookItem(int index) {
        if (getItem(index).getAmount() >= 2) {
            getItem(index).setAmount(getItem(index).getAmount() - 1);

            return getItem(index).getType();
        } else {
            ItemType item = getItem(index).getType();

            inventorySlots[index] = new ItemEmpty();

            return item;
        }
    }

    /**
     * A rendelkezésre álló üres inventorySlots slotok számának lekérdezése.
     * @return az üres inventorySlots slotok száma
     */
    @Override
    public int emptySlots() {
        int emptySlotCounter = 0;

        for (int i = 0; i < inventorySlots.length; i++) {
            if (Objects.isNull(getItem(i))) {
                emptySlotCounter++;
            }
        }

        return emptySlotCounter;
    }

    /**
     * Az aktuálisan viselt tárgy lekérdezése.<br>
     * Ha a karakter jelenleg egy tárgyat sem visel, akkor null.<br>
     * @return Az aktuálisan viselt tárgy
     */
    @Override
    public EquippableItem equippedItem() {
        if (equipped.getType() != ItemType.EMPTY) {
            return equipped;
        }

        return null;
    }

    /**
     * Adott inventorySlots sloton lévő tárgy lekérdezése.<br>
     * Az inventorySlots-ban lévő legelső item indexe 0, a következőé 1, és így tovább.<br>
     * Ha az adott pozíció üres, akkor null.<br>
     * @param index a lekérdezni kívánt pozíció
     * @return az adott sloton lévő tárgy
     */
    @Override
    public AbstractItem getItem(int index) {
        if (index < slotSize && index >= 0 && inventorySlots[index].getType() != ItemType.EMPTY) {
            return inventorySlots[index];
        }

        return null;
    }
}