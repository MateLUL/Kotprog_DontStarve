package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;

public class Field implements BaseField {
    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public boolean hasTree() {
        return false;
    }

    @Override
    public boolean hasStone() {
        return false;
    }

    @Override
    public boolean hasTwig() {
        return false;
    }

    @Override
    public boolean hasBerry() {
        return false;
    }

    @Override
    public boolean hasCarrot() {
        return false;
    }

    @Override
    public boolean hasFire() {
        return false;
    }

    @Override
    public AbstractItem[] items() {
        return new AbstractItem[0];
    }
}
