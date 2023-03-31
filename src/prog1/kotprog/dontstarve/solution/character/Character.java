package prog1.kotprog.dontstarve.solution.character;

import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.utility.Position;


/**
 * Egy egyszerű karakter leírására szolgáló interface implementációja.
 */
public class Character implements BaseCharacter {
    String name;
    float hp;
    float hunger;
    float speed;
    Action lastAction;
    Position currentPosition;
    BaseInventory inventory;
    boolean humanPlayer;

    /**
     * Class konstruktora.
     * @param name a karakter neve
     * @param humanPlayer a karakter ember által irányított-e vagy sem
     */
    public Character(String name, boolean humanPlayer, Position currentPosition) {
        this.name = name;
        hp = 100;
        hunger = 100;
        speed = 1;
        this.humanPlayer = humanPlayer;
        this.currentPosition = currentPosition;
        inventory = new Inventory();
    }

    /**
     * A karakter mozgási sebességének lekérdezésére szolgáló metódus.
     * @return a karakter mozgási sebessége
     */
    @Override
    public float getSpeed() {
        return speed;
    }

    /**
     * A karakter jóllakottságának mértékének lekérdezésére szolgáló metódus.
     * @return a karakter jóllakottsága
     */
    @Override
    public float getHunger() {
        return hunger;
    }

    /**
     * A karakter életerejének lekérdezésére szolgáló metódus.
     * @return a karakter életereje
     */
    @Override
    public float getHp() {
        return hp;
    }

    /**
     * A karakter inventory-jának lekérdezésére szolgáló metódus.
     * <br>
     * A karakter inventory-ja végig ugyanaz marad, amelyet referencia szerint kell visszaadni.
     * @return a karakter inventory-ja
     */
    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    /**
     * A játékos aktuális pozíciójának lekérdezésére szolgáló metódus.
     * @return a játékos pozíciója
     */
    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Az utolsó cselekvés lekérdezésére szolgáló metódus.
     * <br>
     * Egy létező Action-nek kell lennie, nem lehet null.
     * @return az utolsó cselekvés
     */
    @Override
    public Action getLastAction() {
        return lastAction;
    }

    /**
     * A játékos nevének lekérdezésére szolgáló metódus.
     * @return a játékos neve
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * A karakter hp-jának beállítása.
     * @param newValue az új érték
     */
    @Override
    public void setHp(int newValue) {
        hp -= newValue;
    }

    /**
     * A karakter éhségének beállítása.
     * @param newValue az új érték
     */
    @Override
    public void setHunger(int newValue) {
        hunger -= newValue;
    }

    /**
     * Annak a vizsgálata, hogy az adott karakter ember által irányított-e vagy sem.
     * @return igaz, ha ember által irányított, hamis, ha nem
     */
    @Override
    public boolean isHumanPlayer() {
        return humanPlayer;
    }
}
