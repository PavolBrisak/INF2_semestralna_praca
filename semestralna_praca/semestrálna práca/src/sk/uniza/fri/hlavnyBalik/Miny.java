package sk.uniza.fri.hlavnyBalik;

import sk.uniza.fri.policko.PolickoSBombou;
import sk.uniza.fri.policko.IPolicko;
import sk.uniza.fri.policko.PolickoSCislom;
import sk.uniza.fri.policko.PolickoPrazdne;
import sk.uniza.fri.policko.PolickoSVlajkou;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

/**
 * Trieda Míny obsahuje hraciu plochu, v ktorej hráč hrá hru.
 *
 * @author      Pavol Brišák
 * @version     1.0
 */
public class Miny {
    private JFrame hraciaPlocha;
    private IPolicko[][] poleMiny;
    private IPolicko[][] vrchnePole;
    private int velkostPolaStranaA;
    private int velkostPolaStranaB;
    private int pocetBomb;
    private int pocetPolicok;
    private int rozmery;
    private Random random;
    private JLabel cisloBomb;
    private JLabel hodiny;
    private boolean zapisovaliSme;

    /**
     * Konštruktor triedy, v ktorom sa inicializujú atribúty.
     * Spýtame sa hráča na rozmery a nastavíme rozmery poľa.
     */
    public Miny() {
        this.hraciaPlocha = new JFrame();
        this.privitajHraca();
        try {
            this.spytajSaNaRozmery();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        this.poleMiny = new IPolicko[this.getVelkostPolaStranaA()][this.getVelkostPolaStranaB()];
        this.vrchnePole = new IPolicko[this.getVelkostPolaStranaA()][this.getVelkostPolaStranaB()];
        this.pocetBomb = 25;
        this.pocetPolicok = this.getVelkostPolaStranaA() * this.getVelkostPolaStranaB();
        this.rozmery = 25;
        this.zapisovaliSme = false;
        this.random = new Random();
    }

    /**
     * Metóda volá ostatné metódy triedy.
     * Na konci sa spustí hra.
     */
    public void hraj() {
        this.vytvorPole();
        this.zistiPocetBomb();
        this.nastavHraciuPlochu();
        this.spusti();
    }

    /**
     * Metóda každú sekundu kontroluje počet bômb
     * a následne nastaví čas v danom tlačidle.
     */
    public void spusti() {
        for (int i = 0; i <= 3600; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
            this.upravPocetBomb();
            this.nastavCas(i);
            //keby sme hrali celú hodinu, hra končí
            if (i == 3600) {
                JOptionPane.showMessageDialog(null, "Došiel vám čas!", "Koniec hry!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    /**
     * Jednoduchá metóda na privítanie hráča.
     */
    public void privitajHraca() {
        JOptionPane.showMessageDialog(null, "Vitajte v hre Míny.  Cieľom je správne označiť vlajkami miesta, kde si ste istý, že tam je bomba.\nČíslo značí koľko bômb sa okolo neho nachádza.\nVlajku si vkladáte a odoberáte pravým tlačidlom myši.", "Privítanie hráča", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Metóda slúži na spýtanie sa hráča na rozmery.
     * Sú ošetrené nesprávne zadané rozmery, pričom sa vyhodí výnimka.
     *
     * @throws Exception  vyhodená výnimka ak sme zadali nesprávne rozmery
     */
    public void spytajSaNaRozmery() throws Exception {
        boolean vyhodiliSme = false;
        String riadky = JOptionPane.showInputDialog(null, "Zvoľte si počet riadkov plochy   (viac ako 15, menej ako 25)\ndefault je 20");
        String stlpce = JOptionPane.showInputDialog(null, "Zvoľte si počet stĺpcov plochy   (viac ako 20, menej ako 50)\ndefault je 20");
        if ((riadky == null) && (stlpce == null)) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else if ((riadky == null) && (stlpce.equals(""))) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else if ((stlpce == null) && (riadky.equals(""))) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else if (stlpce == null) {
            this.velkostPolaStranaA = Integer.parseInt(riadky);
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else if (riadky == null) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = Integer.parseInt(stlpce);
            vyhodiliSme = true;
        } else if ((riadky.equals("")) && (stlpce.equals(""))) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else if (riadky.equals("")) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = Integer.parseInt(stlpce);
            vyhodiliSme = true;
        } else if (stlpce.equals("")) {
            this.velkostPolaStranaA = Integer.parseInt(riadky);
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        } else {
            this.velkostPolaStranaA = Integer.parseInt(riadky);
            this.velkostPolaStranaB = Integer.parseInt(stlpce);
        }
        if (((this.getVelkostPolaStranaA() < 15) || (this.getVelkostPolaStranaA() > 25)) && ((this.getVelkostPolaStranaB() < 20) || (this.getVelkostPolaStranaB() > 50))) {
            this.velkostPolaStranaA = 20;
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        }
        if ((this.getVelkostPolaStranaA() < 15) || (this.getVelkostPolaStranaA() > 25)) {
            this.velkostPolaStranaA = 20;
            vyhodiliSme = true;
        }
        if ((this.getVelkostPolaStranaB() < 20) || (this.getVelkostPolaStranaB() > 50)) {
            this.velkostPolaStranaB = 20;
            vyhodiliSme = true;
        }
        if (vyhodiliSme) {
            throw new NespravneZadaneRozmeryException("Nesprávne zadané rozmery sme nastavili na default!!");
        }
    }

    /**
     * Metóda vytvára hracie pole.
     * Zároveň sa nastaví počet bômb v hre.
     */
    public void vytvorPole() {
        if (this.getPocetPolicok() <= 460) {
            this.pocetBomb = this.pocetBomb + 5;
        } else if ((this.getPocetPolicok() > 460) && (this.getPocetPolicok() <= 600)) {
            this.pocetBomb = this.pocetBomb * 3;
        } else if ((this.getPocetPolicok() > 600) && (this.getPocetPolicok() <= 800)) {
            this.pocetBomb = this.pocetBomb * 4;
        } else if ((this.getPocetPolicok() > 800) && (this.getPocetPolicok() <= 1000)) {
            this.pocetBomb = this.pocetBomb * 5;
        } else {
            this.pocetBomb = this.pocetBomb * 7;
        }

        //najprv sa všade nastavia prázdne políčka
        //vo vrchnom poli sú všade políčka s vlajkou
        for (int i = 0; i < this.poleMiny.length; i++) {
            for (int j = 0; j < this.poleMiny[i].length; j++) {
                this.poleMiny[i][j] = new PolickoPrazdne(this.rozmery, j, i);
                this.vrchnePole[i][j] = new PolickoSVlajkou(this.rozmery, j, i);
            }
        }

        //náhodne sa nastavia políčka s bombami a okolo nich sa nastavia políčka s číslom
        for (int i = this.pocetBomb; i > 0; i--) {
            int stlpec = this.random.nextInt(this.velkostPolaStranaB);
            int riadok = this.random.nextInt(this.velkostPolaStranaA);
            this.poleMiny[riadok][stlpec] = new PolickoSBombou(this.rozmery, stlpec, riadok);
            this.nastavPolickaSCislom(riadok, stlpec);
        }
    }

    /**
     * Metóda nastaví hraciu plochu. Nastavia sa jej základne valstnosti.
     * Na plochu sa vložia tlačidlá.
     */
    public void nastavHraciuPlochu() {
        int sirka = (this.getVelkostPolaStranaB() + 2) * this.rozmery;
        int vyska = (this.getVelkostPolaStranaA() + 4) * this.rozmery;
        this.hraciaPlocha.setSize(sirka, vyska);
        this.hraciaPlocha.setResizable(false);
        this.hraciaPlocha.setLayout(null);
        this.hraciaPlocha.setTitle("Míny");
        this.hraciaPlocha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.hraciaPlocha.setLocationRelativeTo(null);
        this.nastavCisla();


        for (int i = 0; i < this.vrchnePole.length; i++) {
            for (int j = 0; j < this.vrchnePole[i].length; j++) {
                //každý objekt v tomto vrchnom poli po poslaní správy vytvorPolicko zareaguje inak
                this.hraciaPlocha.add(this.vrchnePole[i][j].vytvorPolicko());
            }
        }

        for (int i = 0; i < this.poleMiny.length; i++) {
            for (int j = 0; j < this.poleMiny[i].length; j++) {
                //v tomto poli mam ale rozne objekty
                //neviem na ktorej pozicii aky je
                //po zavolani spravy vytvorPolicko kazdy zareaguje inak pretoze
                //    metoda vytvorPolicko je v kazdom policku prekryta
                this.hraciaPlocha.add(this.poleMiny[i][j].vytvorPolicko());
            }
        }

        //všetky políčka sa zobrazia
        for (int i = 0; i < this.vrchnePole.length; i++) {
            for (int j = 0; j < this.vrchnePole[i].length; j++) {
                this.vrchnePole[i][j].zobraz();
            }
        }

        //políčkam nastavím tieto vytvorené polia, aby mohli správne fungovať
        for (int i = 0; i < this.vrchnePole.length; i++) {
            for (int j = 0; j < this.vrchnePole[i].length; j++) {
                this.poleMiny[i][j].setPolia(this.poleMiny, this.vrchnePole);
                this.vrchnePole[i][j].setPolia(this.poleMiny, this.vrchnePole);
            }
        }

        //tu sa nastavuje tlačidlo, ktoré zobrazuje aktuálny počet bômb v hre
        this.cisloBomb = new JLabel(String.valueOf(this.pocetBomb), SwingConstants.CENTER);
        int sirka2 = this.rozmery + (this.rozmery / 2);
        if (this.getPocetBomb() > 99) {
            sirka2 = this.rozmery * 2;
        }
        int vyska2 = this.rozmery + (this.rozmery / 2);
        int poziciaX = ((this.velkostPolaStranaB  * this.rozmery) - this.rozmery * 5) - sirka2;
        int poziciaY = 10;
        this.cisloBomb.setBounds(poziciaX, poziciaY, sirka2, vyska2);
        this.cisloBomb.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.rozmery / 10));
        this.cisloBomb.setFont(new Font("Serif", Font.BOLD, 25));
        this.cisloBomb.setVisible(true);


        //vytvorí sa štítok s textom Počet bômb v hre
        JLabel textik = new JLabel("  Počet bômb v hre", SwingConstants.LEFT);
        textik.setBounds((this.velkostPolaStranaB  * this.rozmery) - this.rozmery * 5, poziciaY, this.rozmery * 5, vyska2);
        textik.setFont(new Font("Serif", Font.BOLD, 15));
        textik.setVisible(true);

        //na tlačidlo vypis sa dá kliknúť a výpiše sa obsah údajov v uloženom súbore
        JLabel vypis = new JLabel("Výpis zo súboru", SwingConstants.CENTER);
        vypis.setBounds((((this.velkostPolaStranaB  * this.rozmery) - this.rozmery * 5) - sirka2) - this.rozmery * 6 - (this.rozmery / 5), poziciaY, this.rozmery * 6, vyska2);
        vypis.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.rozmery / 10));
        vypis.setFont(new Font("Serif", Font.BOLD, 15));
        vypis.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Miny.this.zapisovaliSme) {
                    Miny.this.vypisZoSuboru("src/udaje.txt");
                }
            }
        });
        vypis.setVisible(true);

        //tlačidlo hodinky len zobrazuje obrázok hodiniek
        JLabel hodinky = new JLabel();
        hodinky.setBounds(10, 4, this.rozmery + (this.rozmery / 2), vyska2 + (this.rozmery / 2));
        ImageIcon hodinky2 = new ImageIcon(".\\src\\sk\\uniza\\fri\\obrazky\\hodiny.png");
        Image obraz = hodinky2.getImage().getScaledInstance(this.rozmery + (this.rozmery / 2), this.rozmery + (this.rozmery / 2), java.awt.Image.SCALE_DEFAULT);
        hodinky2 = new ImageIcon(obraz);
        hodinky.setIcon(hodinky2);
        hodinky.setVisible(true);

        this.hodiny = new JLabel("0 : 0", SwingConstants.CENTER);
        int sirka3 = this.rozmery * 3 + (this.rozmery / 2);
        int poziciaX3 = (10 + 5) + this.rozmery + (this.rozmery / 2);
        this.hodiny.setBounds(poziciaX3, poziciaY, sirka3, vyska2);
        this.hodiny.setBorder(BorderFactory.createLineBorder(Color.BLACK, this.rozmery / 10));
        this.hodiny.setFont(new Font("Serif", Font.BOLD, 25));
        this.hodiny.setVisible(true);

        this.hraciaPlocha.add(textik);
        this.hraciaPlocha.add(this.cisloBomb);
        this.hraciaPlocha.add(vypis);
        this.hraciaPlocha.add(hodinky);
        this.hraciaPlocha.add(this.hodiny);
        this.hraciaPlocha.setVisible(true);
    }

    /**
     * Metóda vypisuje obsah súboru.
     *
     * @param nazovSuboru  názov súboru
     */
    public void vypisZoSuboru(String nazovSuboru) {
        try {
            FileInputStream file = new FileInputStream(nazovSuboru);
            Scanner zapisovac = new Scanner(file);
            if (!zapisovac.hasNext()) {
                System.out.println("Súbor neobsahuje žiadne údaje.");
            } else {
                while (zapisovac.hasNext()) {
                    System.out.println(zapisovac.nextLine());
                }
            }
            this.zapisovaliSme = true;
            zapisovac.close();
        } catch (IOException e) {
            System.out.println("Súbor sa nenašiel " + e.getMessage());
        }
    }

    /**
     * Metóda nastaví políčka s čislom okolo každej bomby.
     *
     * @param riadok  riadok bomby
     * @param stlpec  stĺpec bomby
     */
    public void nastavPolickaSCislom(int riadok, int stlpec) {
        if (((stlpec - 1) >= 0) && (!(this.poleMiny[riadok][stlpec - 1] instanceof PolickoSBombou))) {
            this.poleMiny[riadok][stlpec - 1] = new PolickoSCislom(this.rozmery, stlpec - 1, riadok);
        }
        if (((stlpec + 1) < this.velkostPolaStranaB) && (!(this.poleMiny[riadok][stlpec + 1] instanceof PolickoSBombou))) {
            this.poleMiny[riadok][stlpec + 1] = new PolickoSCislom(this.rozmery, stlpec + 1, riadok);
        }
        if ((riadok - 1) >= 0) {
            if (((stlpec - 1) >= 0) && (!(this.poleMiny[riadok - 1][stlpec - 1] instanceof PolickoSBombou))) {
                this.poleMiny[riadok - 1][stlpec - 1] = new PolickoSCislom(this.rozmery, stlpec - 1, riadok - 1);
            }
            if (((stlpec + 1) < this.velkostPolaStranaB) && (!(this.poleMiny[riadok - 1][stlpec + 1] instanceof PolickoSBombou))) {
                this.poleMiny[riadok - 1][stlpec + 1] = new PolickoSCislom(this.rozmery, stlpec + 1, riadok - 1);
            }
            if (!(this.poleMiny[riadok - 1][stlpec] instanceof PolickoSBombou)) {
                this.poleMiny[riadok - 1][stlpec] = new PolickoSCislom(this.rozmery, stlpec, riadok - 1);
            }
        }
        if ((riadok + 1) < this.velkostPolaStranaA) {
            if (((stlpec - 1) >= 0) && (!(this.poleMiny[riadok + 1][stlpec - 1] instanceof PolickoSBombou))) {
                this.poleMiny[riadok + 1][stlpec - 1] = new PolickoSCislom(this.rozmery, stlpec - 1, riadok + 1);
            }
            if (((stlpec + 1) < this.velkostPolaStranaB) && (!(this.poleMiny[riadok + 1][stlpec + 1] instanceof PolickoSBombou)))  {
                this.poleMiny[riadok + 1][stlpec + 1] = new PolickoSCislom(this.rozmery, stlpec + 1, riadok + 1);
            }
            if (!(this.poleMiny[riadok + 1][stlpec] instanceof PolickoSBombou)) {
                this.poleMiny[riadok + 1][stlpec] = new PolickoSCislom(this.rozmery, stlpec, riadok + 1);
            }
        }
    }

    /**
     * Metóda nastaví číslo pre políčko s číslom
     * podľa počtu bômb okolo neho.
     */
    public void nastavCisla() {
        for (int i = 0; i < this.poleMiny.length; i++) {
            for (int j = 0; j < this.poleMiny[i].length; j++) {
                if (this.poleMiny[i][j] instanceof PolickoSCislom) {
                    int cislo = this.zistiBombyOkolo(i, j);
                    ((PolickoSCislom)this.poleMiny[i][j]).setCislo(cislo);
                }
            }
        }
    }

    /**
     * Metóda zisti počet bômb okolo daného políčka
     * ktorého pozície sú zadané ako prameter.
     *
     * @param riadok  pozícia riadok políčka
     * @param stlpec  pozícia stĺpec políčka
     * @return        vráti počet bômb okolo daného políčka
     */
    public int zistiBombyOkolo(int riadok, int stlpec) {
        int pocet = 0;

        if ((riadok - 1) >= 0) {
            if (this.poleMiny[riadok - 1][stlpec] instanceof PolickoSBombou) {
                pocet++;
            }
            if ((stlpec + 1) < this.velkostPolaStranaB) {
                if (this.poleMiny[riadok - 1][stlpec + 1] instanceof PolickoSBombou) {
                    pocet++;
                }
            }
            if ((stlpec - 1) >= 0) {
                if (this.poleMiny[riadok - 1][stlpec - 1] instanceof PolickoSBombou) {
                    pocet++;
                }
            }
        }

        if ((stlpec + 1) < this.velkostPolaStranaB) {
            if (this.poleMiny[riadok][stlpec + 1] instanceof PolickoSBombou) {
                pocet++;
            }
        }
        if ((stlpec - 1) >= 0) {
            if (this.poleMiny[riadok][stlpec - 1] instanceof PolickoSBombou) {
                pocet++;
            }
        }

        if ((riadok + 1) < this.velkostPolaStranaA) {
            if (this.poleMiny[riadok + 1][stlpec] instanceof PolickoSBombou) {
                pocet++;
            }
            if ((stlpec + 1) < this.velkostPolaStranaB) {
                if (this.poleMiny[riadok + 1][stlpec + 1] instanceof PolickoSBombou) {
                    pocet++;
                }
            }
            if ((stlpec - 1) >= 0) {
                if (this.poleMiny[riadok + 1][stlpec - 1] instanceof PolickoSBombou) {
                    pocet++;
                }
            }
        }

        return pocet;
    }

    /**
     * Metóda nastaví počet bômb celej hry.
     */
    public void zistiPocetBomb() {
        int spravnyPocetBomb = 0;
        for (int i = 0; i < this.poleMiny.length; i++) {
            for (int j = 0; j < this.poleMiny[i].length; j++) {
                if (this.poleMiny[i][j] instanceof PolickoSBombou) {
                    spravnyPocetBomb++;
                }
            }
        }
        this.pocetBomb = spravnyPocetBomb;
    }

    /**
     * Metóda nastaví aktuálny čas hrania.
     *
     * @param sekunda  aktuálna sekunda hrania
     */
    public void nastavCas(int sekunda) {
        String cas = "";
        int minuta = sekunda / 60;
        int sekundy = sekunda % 60;
        cas = minuta + " : " + sekundy;
        this.hodiny.setText(cas);
    }

    /**
     * Metóda upravuje počet bômb, ktoré dané tlačidlo zobrazuje.
     */
    public void upravPocetBomb() {
        int pocet = 0;
        for (int i = 0; i < this.vrchnePole.length; i++) {
            for (int j = 0; j < this.vrchnePole[i].length; j++) {
                if (this.vrchnePole[i][j] instanceof PolickoSVlajkou) {
                    if (((PolickoSVlajkou)this.vrchnePole[i][j]).isMaVlajku()) {
                        pocet++;
                    }
                }
                if (this.poleMiny[i][j] instanceof  PolickoSBombou) {
                    if (((PolickoSBombou)this.poleMiny[i][j]).isNastalVybuch()) {
                        pocet++;
                    }
                }
            }
        }
        this.cisloBomb.setText(String.valueOf(this.pocetBomb - pocet));
        if ((this.pocetBomb - pocet) == 0) {
            this.zistiCiSkoncilaHra();
        }
    }

    /**
     * Metóda skontroluje, či skončila hra.
     * Kontroluje sa koľko bômb vybuchlo a koľko bômb sme správne označili.
     */
    public void zistiCiSkoncilaHra() {
        int pocetSpravnychVlajok = 0;
        int pocetNespravnychVlajok = 0;
        int pocetVybuchov = 0;
        for (int i = 0; i < this.vrchnePole.length; i++) {
            for (int j = 0; j < this.vrchnePole[i].length; j++) {
                if (this.vrchnePole[i][j] instanceof PolickoSVlajkou) {
                    if (((PolickoSVlajkou)this.vrchnePole[i][j]).isSpravnaVlajka()) {
                        pocetSpravnychVlajok++;
                    }
                    if ((!((PolickoSVlajkou)this.vrchnePole[i][j]).isSpravnaVlajka()) && (((PolickoSVlajkou)this.vrchnePole[i][j]).isMaVlajku())) {
                        pocetNespravnychVlajok++;
                    }
                }
                if (this.poleMiny[i][j] instanceof  PolickoSBombou) {
                    if (((PolickoSBombou)this.poleMiny[i][j]).isNastalVybuch()) {
                        pocetVybuchov++;
                    }
                }
            }
        }

        //kontrolujem, či sme správne označili bomby
        if ((((this.pocetBomb - pocetSpravnychVlajok) == 0)) && (pocetNespravnychVlajok == 0)) {
            for (int i = 0; i < this.vrchnePole.length; i++) {
                for (int j = 0; j < this.vrchnePole[i].length; j++) {
                    if ((this.vrchnePole[i][j]) instanceof PolickoSVlajkou) {
                        if (!((PolickoSVlajkou)this.vrchnePole[i][j]).isMaVlajku()) {
                            this.vrchnePole[i][j].skry();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Podarilo sa vám nájsť všetky bomby. Gratulujeme!\nÚdaje sme vám zapísali do súboru.",  "Výhra", JOptionPane.INFORMATION_MESSAGE);
            this.zapisDoSuboru(this.getPocetBomb(), this.getPocetPolicok(), pocetVybuchov, "Výhra", "src/udaje.txt");
            System.exit(0);
        } else if ((((this.pocetBomb - (pocetVybuchov + pocetSpravnychVlajok)) == 0) && (pocetSpravnychVlajok != 0)) && (pocetNespravnychVlajok == 0)) {
            for (int i = 0; i < this.vrchnePole.length; i++) {
                for (int j = 0; j < this.vrchnePole[i].length; j++) {
                    if ((this.vrchnePole[i][j]) instanceof PolickoSVlajkou) {
                        if (!((PolickoSVlajkou)this.vrchnePole[i][j]).isMaVlajku()) {
                            this.vrchnePole[i][j].skry();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Podarilo sa vám nájsť zvyšné bomby. Gratulujeme!\nÚdaje sme vám zapísali do súboru.",  "Výhra", JOptionPane.INFORMATION_MESSAGE);
            this.zapisDoSuboru(this.getPocetBomb(), this.getPocetPolicok(), pocetVybuchov, "Výhra", "src/udaje.txt");
            System.exit(0);
            //kontrola, či sme nevybuchli všade
        } else if ((this.pocetBomb - pocetVybuchov) == 0) {
            for (int i = 0; i < this.vrchnePole.length; i++) {
                for (int j = 0; j < this.vrchnePole[i].length; j++) {
                    if ((this.vrchnePole[i][j]) instanceof PolickoSVlajkou) {
                        if (!((PolickoSVlajkou)this.vrchnePole[i][j]).isMaVlajku()) {
                            this.vrchnePole[i][j].skry();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "No pekne. Vybuchli ste úplne všade :(\nÚdaje sme vám zapísali do súboru.",  "Prehra", JOptionPane.INFORMATION_MESSAGE);
            this.zapisDoSuboru(this.getPocetBomb(), this.getPocetPolicok(), pocetVybuchov, "Prehra", "src/udaje.txt");
            System.exit(0);
        }
    }

    /**
     * Metóda zapíše údaje o hranej hre do súboru.
     *
     * @param pocetBomb      počet bômb v hre
     * @param pocetPolicok   počet políčok hry
     * @param pocetVybuchov  počet výbuchov, ktoré zatiaľ nastali
     * @param vyhra          text označujúci výhru
     * @param nazovSuboru    názov súboru, do ktorého tieto údaje zapíšem
     */
    public void zapisDoSuboru(int pocetBomb, int pocetPolicok, int pocetVybuchov, String vyhra, String nazovSuboru) {
        try {
            FileOutputStream stream = new FileOutputStream(nazovSuboru, true);
            PrintStream zapisovac = new PrintStream(stream);
            BufferedOutputStream buff = new BufferedOutputStream(zapisovac);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            buff.write("**************************************\n".getBytes(StandardCharsets.UTF_8));
            buff.write(("Dátum a čas hrania --> " + formatter.format(calendar.getTime())  + "\n").getBytes(StandardCharsets.UTF_8));
            buff.write((vyhra  + "\n").getBytes(StandardCharsets.UTF_8));
            buff.write(("Počet bômb v hre --> " + pocetBomb + "\n").getBytes(StandardCharsets.UTF_8));
            buff.write(("Počet políčok v hre --> " + pocetPolicok + "\n").getBytes(StandardCharsets.UTF_8));
            buff.write(("Počet výbuchov --> " + pocetVybuchov + "\n").getBytes(StandardCharsets.UTF_8));
            buff.write(("Čas hrania --> " + this.hodiny.getText() + "\n").getBytes(StandardCharsets.UTF_8));

            buff.close();
        } catch (IOException e) {
            System.out.println("Došlo ku chybe: " + e.getMessage());
        }
    }

    /**
     * Getter na počet riadkov hry.
     *
     * @return  počet riadkov poľa
     */
    public int getVelkostPolaStranaA() {
        return this.velkostPolaStranaA;
    }

    /**
     * Getter na počet stĺpcov hry.
     *
     * @return  počet stĺpcov poľa
     */
    public int getVelkostPolaStranaB() {
        return this.velkostPolaStranaB;
    }

    /**
     * Getter na počet bômb hry.
     *
     * @return  počet bômb hry
     */
    public int getPocetBomb() {
        return this.pocetBomb;
    }

    /**
     * Getter na počet políčok hry.
     *
     * @return  počet políčok poľa
     */
    public int getPocetPolicok() {
        return this.pocetPolicok;
    }
}
