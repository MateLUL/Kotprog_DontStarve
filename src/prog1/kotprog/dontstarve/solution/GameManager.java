package prog1.kotprog.dontstarve.solution;

import prog1.kotprog.dontstarve.solution.character.BaseCharacter;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.exceptions.NotImplementedException;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawCarrot;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemLog;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTwig;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemStone;
import prog1.kotprog.dontstarve.solution.level.BaseField;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.level.Level;
import prog1.kotprog.dontstarve.solution.utility.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A játék működéséért felelős osztály.<br>
 * Az osztály a singleton tervezési mintát valósítja meg.
 */
public final class GameManager {
    /**
     * Joinolt karakterek listája.
     */
    List<BaseCharacter> characters;

    /**
     * Betöltött szint.
     */
    Level currentLevel;

    /**
     * Betöltött szint mezői.
     */
    BaseField[][] loadedLevel;

    private boolean loaded;
    private boolean started;
    private int timeInGame;

    /**
     * Az osztályból létrehozott egyetlen példány (nem lehet final).
     */
    private static GameManager instance = new GameManager();

    /**
     * Random objektum, amit a játék során használni lehet.
     */
    private final Random random = new Random();

    /**
     * Az osztály privát konstruktora.
     */
    private GameManager() {
        loaded = false;
        started = false;
        timeInGame = 0;
        characters = new ArrayList<>();
    }

    /**
     * Az osztályból létrehozott példány elérésére szolgáló metódus.
     * @return az osztályból létrehozott példány
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * A létrehozott random objektum elérésére szolgáló metódus.
     * @return a létrehozott random objektum
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Egy karakter becsatlakozása a játékba.<br>
     * A becsatlakozásnak számos feltétele van:
     * <ul>
     *     <li>A pálya már be lett töltve</li>
     *     <li>A játék még nem kezdődött el</li>
     *     <li>Csak egyetlen emberi játékos lehet, a többi karaktert a gép irányítja</li>
     *     <li>A névnek egyedinek kell lennie</li>
     * </ul>
     * @param name a csatlakozni kívánt karakter neve
     * @param player igaz, ha emberi játékosról van szó; hamis egyébként
     * @return a karakter pozíciója a pályán, vagy (Integer.MAX_VALUE, Integer.MAX_VALUE) ha nem sikerült hozzáadni
     */
    public Position joinCharacter(String name, boolean player) {
        int playerCount = 0;
        boolean notUniqueNames = false;

        if (player) {
            playerCount++;
        }

        //Checking if characters array already has a player and the new char's name is unique
        for (BaseCharacter character : characters) {
            if (character.isHumanPlayer()) {
                playerCount++;
            }

            if (name.equals(character.getName())) {
                notUniqueNames = true;
            }
        }

        if (!isGameStarted() && isLoaded() && playerCount <= 1 && !notUniqueNames) {
            Position initialPosition = new Position(0, 0);
            int minWidth = 0;
            int minLength = 0;

            initialPosition = setInitialPosition(initialPosition, minWidth, minLength);
            characters.add(new Character(name, player, initialPosition));
            addRandomItems(name);

            return initialPosition;
        }
        return new Position(Integer.MAX_VALUE,Integer.MAX_VALUE);
    }

    //Set character's starting position
    private Position setInitialPosition(Position initialPosition, int minWidth, int minLength) {
        for (int i = 0; i < currentLevel.getWidth(); i++) {
            minWidth++;
            for (int j = 0; j < currentLevel.getHeight(); j++) {
                minLength++;

                for (BaseCharacter character : characters) {
                    if (loadedLevel[i][j].isWalkable() && minWidth >= 50 && minLength >= 50 && !Objects.equals(character.getCurrentPosition(), new Position(i, j))) {
                        initialPosition = new Position(i, j);

                        minLength = 0;
                        minWidth = 0;
                    }

                    if (Objects.equals(character.getCurrentPosition(), new Position(i, j))) {
                        minLength = 0;
                        minWidth = 0;
                    }
                }
            }
        }

        return initialPosition;
    }

    //Adding random items to character's inventory
    private void addRandomItems(String name) {
        for (int i = 0; i < 4; i++) {
            int randomItem = getRandom().nextInt(5) + 1;

            switch (randomItem) {
                case 1 -> Objects.requireNonNull(getCharacter(name)).getInventory().addItem(new ItemStone(1));
                case 2 -> Objects.requireNonNull(getCharacter(name)).getInventory().addItem(new ItemTwig(1));
                case 3 -> Objects.requireNonNull(getCharacter(name)).getInventory().addItem(new ItemLog(1));
                case 4 -> Objects.requireNonNull(getCharacter(name)).getInventory().addItem(new ItemRawBerry(1));
                case 5 -> Objects.requireNonNull(getCharacter(name)).getInventory().addItem(new ItemRawCarrot(1));
            }
        }
    }

    /**
     * Egy adott nevű karakter lekérésére szolgáló metódus.<br>
     * @param name A lekérdezni kívánt karakter neve
     * @return Az adott nevű karakter objektum, vagy null, ha már a karakter meghalt vagy nem is létezett
     */
    public BaseCharacter getCharacter(String name) {
        for (BaseCharacter character : characters) {
            if (name.equals(character.getName()) && character.getHp() != 0) {
                return character;
            }
        }

        return null;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy hány karakter van még életben.
     * @return Az életben lévő karakterek száma
     */
    public int remainingCharacters() {
        int remaining = 0;

        for (BaseCharacter character : characters) {
            if (character.getHp() > 0) {
                remaining++;
            }
        }

        return remaining;
    }

    /**
     * Ezen metódus segítségével történhet meg a pálya betöltése.<br>
     * A pálya betöltésének azelőtt kell megtörténnie, hogy akár 1 karakter is csatlakozott volna a játékhoz.<br>
     * A pálya egyetlen alkalommal tölthető be, később nem módosítható.
     * @param level a fájlból betöltött pálya
     */
    public void loadLevel(Level level) {
        if (!isLoaded()) {
            loadedLevel = new Field[level.getWidth()][level.getHeight()];
            currentLevel = level;

            for (int i = 0; i < level.getWidth(); i++) {
                for (int j = 0; j < level.getHeight(); j++) {
                    loadedLevel[i][j] = new Field(level.getColor(i, j));
                }
            }

            setLoaded();
        }
    }

    private boolean isLoaded() {
        return loaded;
    }

    private void setLoaded() {
        this.loaded = true;
    }

    /**
     * A pálya egy adott pozícióján lévő mező lekérdezésére szolgáló metódus.
     * @param x a vízszintes (x) irányú koordináta
     * @param y a függőleges (y) irányú koordináta
     * @return az adott koordinátán lévő mező
     */
    public BaseField getField(int x, int y) {
        return loadedLevel[x][y];
    }

    /**
     * A játék megkezdésére szolgáló metódus.<br>
     * A játék csak akkor kezdhető el, ha legalább 2 karakter már a pályán van,
     * és közülük pontosan az egyik az emberi játékos által irányított karakter.
     * @return igaz, ha sikerült elkezdeni a játékot; hamis egyébként
     */
    public boolean startGame() {
        if (!isGameStarted()) {
            int playerCounter = 0;

            if (characters.size() >= 2) {
                for (BaseCharacter character : characters) {
                    if (character.isHumanPlayer()) {
                        playerCounter++;
                    }
                }

                if (playerCounter == 1) {
                    setGameStarted();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Ez a metódus jelzi, hogy 1 időegység eltelt.<br>
     * A metódus először lekezeli a felhasználói inputot, majd a gépi ellenfelek cselekvését végzi el,
     * végül eltelik egy időegység.<br>
     * Csak akkor csinál bármit is, ha a játék már elkezdődött, de még nem fejeződött be.
     * @param action az emberi játékos által végrehajtani kívánt akció
     */
    public void tick(Action action) {
        throw new NotImplementedException();
    }

    /**
     * Ezen metódus segítségével lekérdezhető az aktuális időpillanat.<br>
     * A játék kezdetekor ez az érték 0 (tehát a legelső időpillanatban az idő 0),
     * majd minden eltelt időpillanat után 1-gyel növelődik.
     * @return az aktuális időpillanat
     */
    public int time() {
        return timeInGame;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük a játék győztesét.<br>
     * Amennyiben a játéknak még nincs vége (vagy esetleg nincs győztes), akkor null-t ad vissza.
     * @return a győztes karakter vagy null
     */
    public BaseCharacter getWinner() {
        if (remainingCharacters() == 1 || isGameEnded()) {
            for (BaseCharacter character : characters) {
                if (character.getHp() > 0) {
                    return character;
                }
            }
        }

        return null;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék elkezdődött-e már.
     * @return igaz, ha a játék már elkezdődött; hamis egyébként
     */
    public boolean isGameStarted() {
        return started;
    }

    private void setGameStarted() {
        this.started = true;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék befejeződött-e már.
     * @return igaz, ha a játék már befejeződött; hamis egyébként
     */
    public boolean isGameEnded() {
        String name = "";

        for (BaseCharacter character : characters) {
            if (character.isHumanPlayer()) {
                name = character.getName();
                break;
            }
        }

        return remainingCharacters() == 1 || Objects.requireNonNull(getCharacter(name)).getHp() == 0;
    }

    /**
     * Ezen metódus segítségével beállítható, hogy a játékot tutorial módban szeretnénk-e elindítani.<br>
     * Alapértelmezetten (ha nem mondunk semmit) nem tutorial módban indul el a játék.<br>
     * Tutorial módban a gépi karakterek nem végeznek cselekvést, csak egy helyben állnak.<br>
     * A tutorial mód beállítása még a karakterek csatlakozása előtt történik.
     * @param tutorial igaz, amennyiben tutorial módot szeretnénk; hamis egyébként
     */
    public void setTutorial(boolean tutorial) {
        throw new NotImplementedException();
    }
}