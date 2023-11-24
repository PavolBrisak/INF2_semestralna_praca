package sk.uniza.fri.policko;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Trieda reprezentuje prázdne políčko.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class PolickoPrazdne implements IPolicko {
    private int rozmery;
    private int poziciaStlpec;
    private int poziciaRiadok;
    private JLabel polickoPrazdne;
    private int hrubkaOkraja;
    private ImageIcon obrazok;
    private IPolicko[][] poleMiny;

    /**
     * Konštruktor triedy, v ktorom sa inicializujú atribúty.
     */
    public PolickoPrazdne(int rozmery, int poziciaStlpec, int poziciaRiadok) {
        this.rozmery = rozmery;
        this.hrubkaOkraja = this.rozmery / 10;
        this.poziciaStlpec = poziciaStlpec;
        this.poziciaRiadok = poziciaRiadok;
        this.polickoPrazdne = new JLabel();
    }

    /**
     * Metóda vytvorí políčko, ktoré sa vkladá na hraciu plochu.
     *
     * @return  políčko vkladané na hraciu plochu
     */
    @Override
    public JLabel vytvorPolicko() {
        this.polickoPrazdne.setBounds(this.getRozmery() * this.poziciaStlpec + (this.getRozmery() / 2),  this.getRozmery() * this.poziciaRiadok + ( 2 * this.getRozmery() + 5), this.getRozmery(), this.getRozmery());
        this.polickoPrazdne.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.hrubkaOkraja));
        this.polickoPrazdne.setIcon(this.nacitajObrazok("prazdne"));
        return this.polickoPrazdne;
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
    }

    /**
     * Metóda vykoná akciu daného políčka.
     * Rekurzívne prechádzam políčka okolo tohto prázdneho políčka.
     * Vkladám ich do daného poľa a vykonám akciu pre každé toto políčko.
     *
     * @param pole     pole, do ktorého ukladám políčka
     * @param poradie  poradie pre políčko
     * @return         pole uložených políčok
     */
    @Override
    public ArrayList<IPolicko> vykonajAkciu(ArrayList<IPolicko> pole, int poradie) {

        if (!pole.contains(this)) {
            pole.add(this);
        }

        if ((this.poziciaRiadok - 1) >= 0) {
            if ((this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec] instanceof PolickoSCislom)) {
                if (!pole.contains(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec])) {
                    pole.add(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec]);
                }
            }
            if ((this.poziciaStlpec + 1) < this.poleMiny[1].length) {
                if ((this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec + 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec + 1] instanceof PolickoSCislom)) {
                    if (!pole.contains(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec + 1])) {
                        pole.add(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec + 1]);
                    }
                }
            }
            if ((this.poziciaStlpec - 1) >= 0) {
                if ((this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec - 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec - 1] instanceof PolickoSCislom)) {
                    if (!pole.contains(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec - 1])) {
                        pole.add(this.poleMiny[this.poziciaRiadok - 1][this.poziciaStlpec - 1]);
                    }
                }
            }
        }

        if ((this.poziciaStlpec + 1) < this.poleMiny[1].length) {
            if ((this.poleMiny[this.poziciaRiadok][this.poziciaStlpec + 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok][this.poziciaStlpec + 1] instanceof PolickoSCislom)) {
                if (!pole.contains(this.poleMiny[this.poziciaRiadok][this.poziciaStlpec + 1])) {
                    pole.add(this.poleMiny[this.poziciaRiadok][this.poziciaStlpec + 1]);
                }
            }
        }
        if ((this.poziciaStlpec - 1) >= 0) {
            if ((this.poleMiny[this.poziciaRiadok][this.poziciaStlpec - 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok][this.poziciaStlpec - 1] instanceof PolickoSCislom)) {
                if (!pole.contains(this.poleMiny[this.poziciaRiadok][this.poziciaStlpec - 1])) {
                    pole.add(this.poleMiny[this.poziciaRiadok][this.poziciaStlpec - 1]);
                }
            }
        }


        if ((this.poziciaRiadok + 1) < this.poleMiny.length) {
            if ((this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec] instanceof PolickoSCislom)) {
                if (!pole.contains(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec])) {
                    pole.add(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec]);
                }
            }

            if ((this.poziciaStlpec + 1) < this.poleMiny[1].length) {
                if ((this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec + 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec + 1] instanceof PolickoSCislom)) {
                    if (!pole.contains(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec + 1])) {
                        pole.add(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec + 1]);
                    }
                }
            }
            if ((this.poziciaStlpec - 1) >= 0) {
                if ((this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec - 1] instanceof PolickoPrazdne) || (this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec - 1] instanceof PolickoSCislom)) {
                    if (!pole.contains(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec - 1])) {
                        pole.add(this.poleMiny[this.poziciaRiadok + 1][this.poziciaStlpec - 1]);
                    }
                }
            }
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
        this.polickoPrazdne.setVisible(true);
    }

    /**
     * Políčko sa skryje.
     */
    @Override
    public void skry() {
        this.polickoPrazdne.setVisible(false);
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
