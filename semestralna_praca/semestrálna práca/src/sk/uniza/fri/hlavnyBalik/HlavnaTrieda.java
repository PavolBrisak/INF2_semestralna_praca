package sk.uniza.fri.hlavnyBalik;


/**
 * Trieda reprezentuje spustenie hry míny.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class HlavnaTrieda {

    /**
     * Metóda main, ktorá spúšťa triedu Míny.
     */
    public static void main(String[] args) {
        Miny miny = new Miny();
        miny.hraj();
    }
}