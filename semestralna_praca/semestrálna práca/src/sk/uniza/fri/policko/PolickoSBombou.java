package sk.uniza.fri.policko;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Trieda reprezentuje políčko s bombou.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class PolickoSBombou implements IPolicko {
    private int rozmery;
    private int poziciaStlpec;
    private int poziciaRiadok;
    private JLabel polickoSBombou;
    private int hrubkaOkraja;
    private ImageIcon obrazok;
    private IPolicko[][] vrchnePole;
    private boolean nastalVybuch;

    /**
     * Konštruktor triedy, v ktorom sa inicializujú atribúty.
     */
    public PolickoSBombou(int rozmery, int poziciaStlpec, int poziciaRiadok) {
        this.rozmery = rozmery;
        this.hrubkaOkraja = this.rozmery / 10;
        this.poziciaStlpec = poziciaStlpec;
        this.poziciaRiadok = poziciaRiadok;
        this.polickoSBombou = new JLabel();
        this.nastalVybuch = false;
    }

    /**
     * Metóda vytvorí políčko, ktoré sa vkladá na hraciu plochu.
     *
     * @return  políčko vkladané na hraciu plochu
     */
    @Override
    public JLabel vytvorPolicko() {
        this.polickoSBombou.setBounds(this.getRozmery() * this.poziciaStlpec + (this.getRozmery() / 2),  this.getRozmery() * this.poziciaRiadok + ( 2 * this.getRozmery() + 5), this.getRozmery(), this.getRozmery());
        this.polickoSBombou.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.hrubkaOkraja));
        this.polickoSBombou.setIcon(this.nacitajObrazok("bomba"));
        return this.polickoSBombou;
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
     * Metóda vykoná akciu daného políčka.
     *
     * @param pole     pole, do ktorého ukladám políčka
     * @param poradie  poradie pre políčko
     * @return         pole uložených políčok
     */
    @Override
    public ArrayList<IPolicko> vykonajAkciu(ArrayList<IPolicko> pole, int poradie) {
        this.vrchnePole[this.poziciaRiadok][this.poziciaStlpec].skry();
        JOptionPane.showMessageDialog(null, "Vybuchli ste!!\nSkúste označiť zvyšné bomby.", "Výbuch", JOptionPane.INFORMATION_MESSAGE);
        this.polickoSBombou.setIcon(this.nacitajObrazok("vybuchol"));
        this.setNastalVybuch(true);
        return pole;
    }

    /**
     * Metóda načíta obrázok pre dané políčko.
     *
     * @param subor  názov súboru
     * @return       ikona obrázka
     */
    @Override
    public Icon nacitajObrazok(String subor) {
        this.obrazok = new ImageIcon(".\\src\\sk\\uniza\\fri\\obrazky\\" + subor + ".png");
        Image obraz = this.obrazok.getImage().getScaledInstance(this.getRozmery(), this.getRozmery(), java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        return this.obrazok;
    }

    /**
     * Zobrazí sa políčko.
     */
    @Override
    public void zobraz() {
        this.polickoSBombou.setVisible(true);
    }

    /**
     * Políčko sa skryje.
     */
    @Override
    public void skry() {
        this.polickoSBombou.setVisible(false);
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

    /**
     * Nastaví sa hodnota, či bomba vybuchla.
     *
     * @param vybuch  hodnota, či bomba vybuchla
     */
    public void setNastalVybuch(boolean vybuch) {
        this.nastalVybuch = vybuch;
    }

    /**
     * Vraciam hodnotu, či nastal výbuch.
     *
     * @return  hodnotu, či nastal výbuch.
     */
    public boolean isNastalVybuch() {
        return this.nastalVybuch;
    }
}
