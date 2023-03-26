package prog1.kotprog.dontstarve.solution.character;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.utility.Position;

public class Character implements BaseCharacter {
    String name;
    float hp, hunger, speed;
    Action lastAction;
    Position currentPosition;
    BaseInventory inventory;
    boolean isHumanPlayer;

    public Character(String name, Action lastAction, Position currentPosition, BaseInventory inventory, boolean isHumanPlayer) {
        this.name = name;
        hp = 100;
        hunger = 100;
        speed = 1;
        this.lastAction = lastAction;
        this.currentPosition = currentPosition;
        this.inventory = inventory;
        this.isHumanPlayer = isHumanPlayer;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getHunger() {
        return hunger;
    }

    @Override
    public float getHp() {
        return hp;
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setHp(int newValue) {
        hp -= newValue;
    }

    @Override
    public void setHunger(int newValue) {
        hunger -= newValue;
    }

    @Override
    public boolean isHumanPlayer() {
        if (isHumanPlayer)
            return true;
        else
            return false;
    }
}
