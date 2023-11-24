package sk.uniza.fri.policko;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Trieda reprezentuje políčko s vlajkou.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class PolickoSVlajkou implements IPolicko, MouseListener {
    private int rozmery;
    private int poziciaStlpec;
    private int poziciaRiadok;
    private boolean spravnaVlajka;
    private boolean maVlajku;
    private JLabel polickoSVlajkou;
    private int hrubkaOkraja;
    private ImageIcon obrazok;
    private IPolicko[][] poleMiny;
    private IPolicko[][] vrchnePole;

    /**
     * Konštruktor triedy, v ktorom sa inicializujú atribúty.
     */
    public PolickoSVlajkou(int rozmery, int poziciaStlpec, int poziciaRiadok) {
        this.rozmery = rozmery;
        this.hrubkaOkraja = this.rozmery / 10;
        this.poziciaStlpec = poziciaStlpec;
        this.poziciaRiadok = poziciaRiadok;
        this.polickoSVlajkou = new JLabel();
        this.maVlajku = false;
        this.spravnaVlajka = false;
    }

    /**
     * Metóda vytvorí políčko, ktoré sa vkladá na hraciu plochu.
     *
     * @return  políčko vkladané na hraciu plochu
     */
    @Override
    public JLabel vytvorPolicko() {
        this.polickoSVlajkou.setBounds(this.getRozmery() * this.poziciaStlpec + (this.getRozmery() / 2),  this.getRozmery() * this.poziciaRiadok + ( 2 * this.getRozmery() + 5), this.getRozmery(), this.getRozmery());
        this.polickoSVlajkou.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.hrubkaOkraja));
        this.polickoSVlajkou.setIcon(this.nacitajObrazok("nevybuchol"));
        this.polickoSVlajkou.addMouseListener(this);
        this.polickoSVlajkou.setVisible(true);
        return this.polickoSVlajkou;
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
        //tu je tiež polymorfizmus
        //volám metódu vykonaj akciu políčka ktoré je presne pod políčkom s vlajkou
        //neviem ktoré políčko tam je a teda sa vykonajú rôzne akcie
        return this.poleMiny[this.poziciaRiadok][this.poziciaStlpec].vykonajAkciu(pole, poradie);
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
     * Setter pre nastavenie polí.
     *
     * @param poleMiny    pole mín
     * @param vrchnePole  pole vrchných políčok
     */
    @Override
    public void setPolia(IPolicko[][] poleMiny, IPolicko[][] vrchnePole) {
        this.poleMiny = poleMiny;
        this.vrchnePole = vrchnePole;
    }

    /**
     * Zobrazí sa políčko.
     */
    @Override
    public void zobraz() {
        this.polickoSVlajkou.setVisible(true);
    }

    /**
     * Políčko sa skryje.
     */
    @Override
    public void skry() {
        this.polickoSVlajkou.setVisible(false);
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
     * Getter hodnoty, či má vlajku políčka.
     *
     * @return  hodnota, či má vlajku
     */
    public boolean isMaVlajku() {
        return this.maVlajku;
    }

    /**
     * Getter hodnoty, či má správnu vlajku políčka.
     *
     * @return  hodnota, či má správnu vlajku
     */
    public boolean isSpravnaVlajka() {
        return this.spravnaVlajka;
    }

    /**
     * Metóda na kliknutie políčka.
     *
     * @param e   event, ktorý sa vykoná
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!(this.isMaVlajku())) {
                this.skry();
                this.maVlajku = false;
                this.spravnaVlajka = false;

                ArrayList<IPolicko> pole = new ArrayList<>();

                //tu je polymorfizmus, pozriem sa na pole pod tym vrchnym, a poslem spravu vykonajAkciu danemu interfacu
                //neviem ci tam je bomba, prazdne alebo cislo
                //kazdy tento objekt ma tuto metodu prekrytu a teda zareaguju inak
                ArrayList<IPolicko> polePridanych = this.vykonajAkciu(pole, 0);
                for (IPolicko policko : polePridanych) {
                    if (this.vrchnePole[policko.getRiadok()][policko.getStlpec()] instanceof PolickoSVlajkou) {
                        if (!(((PolickoSVlajkou)this.vrchnePole[policko.getRiadok()][policko.getStlpec()]).isMaVlajku())) {
                            this.vrchnePole[policko.getRiadok()][policko.getStlpec()].skry();
                        }
                    }
                }
            }
        }
        //kliknutie pravým tlačidlom myši
        //nastaví sa hodnota vlajok pre dané políčko
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (this.maVlajku) {
                this.polickoSVlajkou.setIcon(this.nacitajObrazok("nevybuchol"));
                this.maVlajku = false;
                this.spravnaVlajka = false;
            } else {
                this.polickoSVlajkou.setIcon(this.nacitajObrazok("vlajka"));
                this.maVlajku = true;
                this.spravnaVlajka = this.poleMiny[this.getRiadok()][this.getStlpec()] instanceof PolickoSBombou;
            }
        }
    }


    /**
     * Metóda na stlačenie políčka.
     *
     * @param e   event, ktorý sa vykoná
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Metóda na pustenie tlačidla políčka.
     *
     * @param e   event, ktorý sa vykoná
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Metóda na vchádzanie myši do políčka.
     *
     * @param e   event, ktorý sa vykoná
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        this.polickoSVlajkou.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, this.hrubkaOkraja));
    }

    /**
     * Metóda na výjdenie z políčka.
     *
     * @param e   event, ktorý sa vykoná
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.polickoSVlajkou.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.hrubkaOkraja));
    }
}
