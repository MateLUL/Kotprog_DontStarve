package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.utility.Position;

public class Field implements BaseField {
    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean hasTree() {
        return true;
    }

    @Override
    public boolean hasStone() {
        return true;
    }

    @Override
    public boolean hasTwig() {
        return true;
    }

    @Override
    public boolean hasBerry() {
        return true;
    }

    @Override
    public boolean hasCarrot() {
        return true;
    }

    @Override
    public boolean hasFire() {
        return true;
    }

    @Override
    public AbstractItem[] items() {
        return new AbstractItem[0];
    }
}
