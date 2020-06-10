package labyrinth;

public class Field {

    /**
     * Adott mezőnek az információit tartalmazza.
     */
    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;
    private int treasure;

    /**
     * Mező létrehozásakor beállitjuk, hogy merre szabad az út a
     * továbbhaladáshoz.
     *
     * @param up Felfele szabad-e az út?
     * @param right Jobbra szabad-e az út?
     * @param down Lefele szabad-e az út?
     * @param left Balra szabad-e az út?
     * @param treasure A mező kincsének nyitókódja.
     */
    public Field(boolean up, boolean right, boolean down, boolean left, int treasure) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.treasure = treasure;
    }

    /**
     * Elzárhatjuk a felfele vezető utat.
     *
     * @param up Felfele szabaddá akarjuk-e tenni az utat?
     */
    public void setUp(boolean up) {
        this.up = up;
    }

    /**
     * Elzárhatjuk a lefele vezető utat.
     *
     * @param down Lefele szabaddá akarjuk-e tenni az utat?
     */
    public void setDown(boolean down) {
        this.down = down;
    }

    /**
     * Elzárhatjuk a jobbra vezeto utat.
     *
     * @param right Jobbra szabaddá akarjuk-e tenni az utat?
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Elzárhatjuk a balra vezeto utat.
     *
     * @param left Balra szabaddá akarjuk-e tenni az utat?
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * Megadja, hogy felfele szabad-e az út előttünk.
     *
     * @return Felfele szabad-e az út?
     */
    public boolean isUp() {
        return up;
    }

    /**
     * Megadja, hogy lefele szabad-e az út előttünk.
     *
     * @return Lefele szabad-e az út?
     */
    public boolean isDown() {
        return down;
    }

    /**
     * Megadja, hogy jobbra szabad-e az út előttünk.
     *
     * @return Jobbra szabad-e az út?
     */
    public boolean isRight() {
        return right;
    }

    /**
     * Megadja, hogy balra szabad-e az út előttünk.
     *
     * @return Balra szabad-e az út?
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Megadja, hogy melyik szám nyitja a mező kincsesládáját.
     *
     * @return Hanyas szám nyitja a mező kincsesládáját?
     */
    public int getTreasure() {
        return treasure;
    }

    /**
     * Mező kincsesládájának nyitókódjának átállítása.
     *
     * @param treasure Kincsesláda kódjának megváltoztatása, az adott számra.
     */
    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }
}
