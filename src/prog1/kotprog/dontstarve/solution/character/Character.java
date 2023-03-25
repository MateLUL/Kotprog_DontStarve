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

    public Character(String name, float hp, float hunger, float speed) {
        this.name = name;
        this.hp = hp;
        this.hunger = hunger;
        this.speed = speed;
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
}
