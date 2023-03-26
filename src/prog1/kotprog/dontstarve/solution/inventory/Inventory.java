package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.*;

import java.util.ArrayList;
import java.util.Objects;

public class Inventory implements BaseInventory {
    AbstractItem[] inventory = new AbstractItem[10];
    EquippableItem equipped;
    final int slotSize;


    public Inventory() {
        slotSize = 10;

        for (int i = 0; i < slotSize; i++) {
            inventory[i] = new ItemEmpty();
        }
        equipped = new ItemEmptyEquipped();
    }

    @Override
    public boolean addItem(AbstractItem item) {
        int remainingItemAmount = item.getAmount();
        boolean hasStackInInventory = false;

        //If stackable
        if (item.isStackable()) {
            //Check if inventory has an existing stack
            for (int i = 0; i < inventory.length; i++) {
                if (!Objects.isNull(getItem(i)) && getItem(i).getType() == item.getType()) {
                    //If the items can fit in the stack
                    if (getItem(i).getAmount() + remainingItemAmount <= item.getMaxStack()) {
                        getItem(i).setAmount(remainingItemAmount + getItem(i).getAmount());
                        remainingItemAmount = 0;
                        hasStackInInventory = true;
                        break;
                    }
                    //If items don't fit
                    else {
                        int fillStack = item.getMaxStack() - getItem(i).getAmount();
                        remainingItemAmount -= fillStack;
                        getItem(i).setAmount(fillStack);
                    }
                }
            }

            //If doesn't have a stack or there are leftover items
            if (!hasStackInInventory) {
                for (int i = 0; i < inventory.length; i++) {
                    if (Objects.isNull(getItem(i))) {
                        //If items fit in a stack
                        if (remainingItemAmount <= item.getMaxStack()) {
                            switch (item.getType()) {
                                case LOG -> inventory[i] = new ItemLog(remainingItemAmount);
                                case STONE -> inventory[i] = new ItemStone(remainingItemAmount);
                                case TWIG -> inventory[i] = new ItemTwig(remainingItemAmount);
                                case RAW_BERRY -> inventory[i] = new ItemRawBerry(remainingItemAmount);
                                case RAW_CARROT -> inventory[i] = new ItemRawCarrot(remainingItemAmount);
                                case COOKED_BERRY -> inventory[i] = new ItemCookedBerry(remainingItemAmount);
                                case COOKED_CARROT -> inventory[i] = new ItemCookedCarrot(remainingItemAmount);
                            }
                            remainingItemAmount = 0;
                            break;
                        }
                        //If items don't fit
                        else {
                            switch (item.getType()) {
                                case LOG -> inventory[i] = new ItemLog(item.getMaxStack());
                                case STONE -> inventory[i] = new ItemStone(item.getMaxStack());
                                case TWIG -> inventory[i] = new ItemTwig(item.getMaxStack());
                                case RAW_BERRY -> inventory[i] = new ItemRawBerry(item.getMaxStack());
                                case RAW_CARROT -> inventory[i] = new ItemRawCarrot(item.getMaxStack());
                                case COOKED_BERRY -> inventory[i] = new ItemCookedBerry(item.getMaxStack());
                                case COOKED_CARROT -> inventory[i] = new ItemCookedCarrot(item.getMaxStack());
                            }
                            remainingItemAmount -= item.getMaxStack();
                        }
                    }
                }
            }
        }
        //If not stackable
        else
            for (int i = 0; i < inventory.length; i++) {
                if (Objects.isNull(getItem(i))) {
                    switch (item.getType()) {
                        case AXE -> inventory[i] = new ItemAxe();
                        case PICKAXE -> inventory[i] = new ItemPickaxe();
                        case TORCH -> inventory[i] = new ItemTorch();
                        case SPEAR -> inventory[i] = new ItemSpear();
                    }
                    remainingItemAmount = 0;
                    break;
                }
            }

        if (remainingItemAmount > 0)
            return false;
        else
            return true;
    }

    @Override
    public AbstractItem dropItem(int index) {
        if (index < slotSize && index >= 0) {
            if (!Objects.isNull(getItem(index))) {
                AbstractItem droppedItem = inventory[index];
                inventory[index] = new ItemEmpty();

                return droppedItem;
            }
            else
                return null;
        }
        else
            return null;
    }

    @Override
    public boolean removeItem(ItemType type, int amount) {
        int amountInInventory = 0;
        int amountToBeDeleted = amount;

        for (int i = 0; i < inventory.length; i++) {
            if (!Objects.isNull(getItem(i)) && getItem(i).getType() == type)
                amountInInventory += getItem(i).getAmount();
        }

        if (amountInInventory >= amountToBeDeleted) {
            for (int i = 0; i < inventory.length; i++) {
                if (amountToBeDeleted == 0) {
                    return true;
                }
                else {
                    if (!Objects.isNull(getItem(i)) && getItem(i).getType() == type) {
                        if (getItem(i).getAmount() == getItem(i).getMaxStack() && amountToBeDeleted >= getItem(i).getMaxStack()) {
                            inventory[i] = new ItemEmpty();
                            amountToBeDeleted -= getItem(i).getMaxStack();
                            amountInInventory -= getItem(i).getMaxStack();
                        }
                        else {
                            if (getItem(i).getAmount() >= amountToBeDeleted) {
                                getItem(i).setAmount(getItem(i).getAmount() - amountToBeDeleted);
                                amountToBeDeleted = 0;
                            }
                        }
                    }
                }
            }
        }
        else
            return false;

        return false;
    }

    @Override
    public boolean swapItems(int index1, int index2) {
        AbstractItem temp;

        if (!Objects.isNull(getItem(index1)) && !Objects.isNull(getItem(index2))) {
            switch (getItem(index1).getType()) {
                case LOG -> temp = new ItemLog(inventory[index1].getAmount());
                case STONE -> temp = new ItemStone(inventory[index1].getAmount());
                case TWIG -> temp = new ItemTwig(inventory[index1].getAmount());
                case RAW_BERRY -> temp = new ItemRawBerry(inventory[index1].getAmount());
                case RAW_CARROT -> temp = new ItemRawCarrot(inventory[index1].getAmount());
                case COOKED_BERRY -> temp = new ItemCookedBerry(inventory[index1].getAmount());
                case COOKED_CARROT -> temp = new ItemCookedCarrot(inventory[index1].getAmount());
                case AXE -> temp = new ItemAxe();
                case PICKAXE -> temp = new ItemPickaxe();
                case TORCH -> temp = new ItemTorch();
                case SPEAR -> temp = new ItemSpear();
            }
        }
        else
            return false;

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
        if (!Objects.isNull(getItem(index)) && !getItem(index).isStackable()) {
            if (equipped.getType() == ItemType.EMPTY) {
                switch (getItem(index).getType()) {
                    case AXE -> equipped = new ItemAxe();
                    case TORCH -> equipped = new ItemTorch();
                    case SPEAR -> equipped = new ItemSpear();
                    case PICKAXE -> equipped = new ItemPickaxe();
                }

                return true;
            }

        }
        else
            return false;
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
        int emptySlotCounter = 0;
        for (int i = 0; i < inventory.length; i++) {
            if (Objects.isNull(getItem(i)))
                emptySlotCounter++;
        }

        return emptySlotCounter;
    }

    @Override
    public EquippableItem equippedItem() {
        if (equipped.getType() == ItemType.EMPTY)
            return null;
        else
            return equipped;
    }

    @Override
    public AbstractItem getItem(int index) {
        if (index < slotSize && index >= 0) {
            if (inventory[index].getType() == ItemType.EMPTY)
                return null;
            else
                return inventory[index];
        }
        else
            return null;
    }
}
