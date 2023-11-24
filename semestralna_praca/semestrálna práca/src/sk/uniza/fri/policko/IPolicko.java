package sk.uniza.fri.policko;

import javax.swing.JLabel;
import javax.swing.Icon;
import java.util.ArrayList;

/**
 * Trieda reprezentuje interface políčka.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public interface IPolicko {
    JLabel vytvorPolicko();
    ArrayList<IPolicko> vykonajAkciu(ArrayList<IPolicko> pole, int poradie);
    Icon nacitajObrazok(String subor);
    void setPolia(IPolicko[][] poleMiny, IPolicko[][] vrchnePole);
    void zobraz();
    void skry();
    int getStlpec();
    int getRiadok();
    int getRozmery();
}
