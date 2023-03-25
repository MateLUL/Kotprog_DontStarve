package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

import java.util.ArrayList;
import java.util.Objects;

public class Inventory implements BaseInventory {
    ArrayList<AbstractItem> inventory = new ArrayList<>();
    final int slotSize;


    public Inventory() {
        slotSize = 10;
    }

    @Override
    public boolean addItem(AbstractItem item) {
        boolean addToEmptySlot = false;
        boolean hasSameType = false;
        int remainingItemAmount = item.getAmount();

        //If stackable
        if (item.isStackable()) {
            //Check for already existing item types
            for (int i = 0; i < inventory.size(); i++) {
                //If equal
                if (getItem(i).getType() == item.getType()) {
                    //If inventory + item amount is less or equal to max
                    if ((getItem(i).getAmount() + remainingItemAmount) <= item.getMaxStack()) {
                        //Add the amount of items to the inventory
                        getItem(i).setAmount(item.getAmount());

                        hasSameType = true;
                        remainingItemAmount = 0;
                        break;
                    }
                    //If more, then add enough items to fill the stack and continue
                    else {
                        remainingItemAmount -= item.getMaxStack() - getItem(i).getAmount();
                        getItem(i).setAmount(remainingItemAmount);
                        hasSameType = true;
                    }
                }
            }
        }
        //If not stackable
        else
            addToEmptySlot = true;


        if (addToEmptySlot || !hasSameType) {
            //If inventory has at least 1 open slot, add item
            if (inventory.size() < slotSize) {
                inventory.add(item);
                remainingItemAmount = 0;
            }
        }

        if (remainingItemAmount != 0)
            return false;
        else
            return true;
    }

    @Override
    public AbstractItem dropItem(int index) {
        return null;
    }

    @Override
    public boolean removeItem(ItemType type, int amount) {
        return false;
    }

    @Override
    public boolean swapItems(int index1, int index2) {
        return false;
    }

    @Override
    public boolean moveItem(int index, int newIndex) {
        return false;
    }

    @Override
    public boolean combineItems(int index1, int index2) {
        return false;
    }

    @Override
    public boolean equipItem(int index) {
        return false;
    }

    @Override
    public EquippableItem unequipItem() {
        return null;
    }

    @Override
    public ItemType cookItem(int index) {
        return null;
    }

    @Override
    public ItemType eatItem(int index) {
        return null;
    }

    @Override
    public int emptySlots() {
        return 0;
    }

    @Override
    public EquippableItem equippedItem() {
        return null;
    }

    @Override
    public AbstractItem getItem(int index) {
        if (Objects.isNull(inventory.get(index)))
            return null;
        else
            return inventory.get(index);
    }
}
