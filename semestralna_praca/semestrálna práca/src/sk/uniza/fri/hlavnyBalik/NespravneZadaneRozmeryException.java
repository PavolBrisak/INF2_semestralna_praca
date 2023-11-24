package sk.uniza.fri.hlavnyBalik;


/**
 * Trieda reprezentuje vlastnú výnimku.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class NespravneZadaneRozmeryException extends Exception {
    private final String message;
    private final String nazov;

    /**
     * Konštruktor triedy, v ktorom sa nastaví správa výnimky.
     *
     * @param text  správa výnimky
     */
    public NespravneZadaneRozmeryException(String text) {
        this.nazov = "NespravneZadaneRozmeryException";
        this.message = text;
    }

    /**
     * Getter správy výnimky.
     *
     * @return  správa výnimky
     */
    @Override
    public String getMessage() {
        return this.getNazov() + " --> " + this.message;
    }

    /**
     * Getter názvu výnimky.
     *
     * @return  názov výnimky
     */
    public String getNazov() {
        return this.nazov;
    }
}
