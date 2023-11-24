package sk.uniza.fri.policko;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Trieda reprezentuje políčko s číslom.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class PolickoSCislom implements IPolicko {
    private int rozmery;
    private int poziciaStlpec;
    private int poziciaRiadok;
    private int cislo;
    private JLabel polickoSCislom;
    private int hrubkaOkraja;
    private ImageIcon obrazok;
    private IPolicko[][] vrchnePole;

    /**
     * Konštruktor triedy, v ktorom sa inicializujú atribúty.
     */
    public PolickoSCislom(int rozmery, int poziciaStlpec, int poziciaRiadok) {
        this.rozmery = rozmery;
        this.hrubkaOkraja = this.rozmery / 10;
        this.poziciaStlpec = poziciaStlpec;
        this.poziciaRiadok = poziciaRiadok;
        this.polickoSCislom = new JLabel();
    }

    /**
     * Nastavuje sa číslo daného políčka.
     *
     * @param cislo  číslo pre dané políčko
     */
    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    /**
     * Setter pre nastavenie polí.
     *
     * @param poleMiny    pole mín
     * @param vrchnePole  pole vrchných políčok
     */
    @Override
    public void setPolia(IPolicko[][] poleMiny, IPolicko[][] vrchnePole) {
        this.vrchnePole = vrchnePole;
    }

    /**
     * Metóda vytvorí políčko, ktoré sa vkladá na hraciu plochu.
     *
     * @return  políčko vkladané na hraciu plochu
     */
    @Override
    public JLabel vytvorPolicko() {
        this.polickoSCislom.setBounds(this.getRozmery() * this.poziciaStlpec + (this.getRozmery() / 2),  this.getRozmery() * this.poziciaRiadok + ( 2 * this.getRozmery() + 5), this.getRozmery(), this.getRozmery());
        this.polickoSCislom.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.hrubkaOkraja));
        this.polickoSCislom.setIcon(this.nacitajObrazok("cislo"));
        this.polickoSCislom.setVisible(true);
        return this.polickoSCislom;
    }

    /**
     * Metóda vykoná akciu daného políčka.
     *
     * @param pole     pole, do ktorého ukladám políčka
     * @param poradie  poradie pre políčko
     * @return         pole uložených políčok
     */
    @Override
    public ArrayList<IPolicko> vykonajAkciu(ArrayList<IPolicko> pole, int poradie) {
        this.vrchnePole[this.poziciaRiadok][this.poziciaStlpec].skry();
        if (poradie == 0) {
            return pole;
        }
        poradie++;
        if (poradie != pole.size()) {
            return pole.get(poradie).vykonajAkciu(pole, poradie);
        } else {
            return pole;
        }
    }

    /**
     * Metóda načíta obrázok pre dané políčko.
     *
     * @param subor  názov súboru
     * @return       ikona obrázka
     */
    @Override
    public Icon nacitajObrazok(String subor) {
        this.obrazok = new ImageIcon(".\\src\\sk\\uniza\\fri\\obrazky\\" + this.getCislo() + ".png");
        Image obraz = this.obrazok.getImage().getScaledInstance(this.getRozmery(), this.getRozmery(), java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        return this.obrazok;
    }

    /**
     * Getter čísla políčka.
     *
     * @return  číslo políčka
     */
    public int getCislo() {
        return this.cislo;
    }

    /**
     * Zobrazí sa políčko.
     */
    @Override
    public void zobraz() {
        this.polickoSCislom.setVisible(true);
    }

    /**
     * Políčko sa skryje.
     */
    @Override
    public void skry() {
        this.polickoSCislom.setVisible(false);
    }

    /**
     * Getter stĺpca políčka.
     *
     * @return  daný stĺpec políčka
     */
    @Override
    public int getStlpec() {
        return this.poziciaStlpec;
    }

    /**
     * Getter riadka políčka.
     *
     * @return  daný riadok políčka
     */
    @Override
    public int getRiadok() {
        return this.poziciaRiadok;
    }

    /**
     * Getter rozmerov políčka.
     *
     * @return  rozmery políčka
     */
    @Override
    public int getRozmery() {
        return this.rozmery;
    }
}
