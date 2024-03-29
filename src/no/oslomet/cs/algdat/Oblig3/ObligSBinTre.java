package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////
/////////////////  Laget av Jo Vetle Albrethsen s329329 ////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public ObligSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi, p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi, null, null, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        endringer++;                             // oppdaterer endringer
        return true;
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        // hentet kode fra kompendiet 5.2.8 d)
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi, p.verdi);      // sammenligner
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            }      // går til venstre
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
                //rot.forelder = null;    // hvis b blir roten setter vi forelder til null
            } else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q; // her oppdaterer vi barnet sin forelder
                }
            } else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q; // her oppdaterer vi barnet sin forelder
                }
            }
        } else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null) {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        endringer++;    // oppdaterer endringer
        return true;
    }

    public int fjernAlle(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");
        int antallFjernet = 0;

        while (inneholder(verdi)) {     // sjekker om treet inneholder tallet som det spørres om
            fjern(verdi);       // fjerner verdien
            antallFjernet++;
        }

        return antallFjernet;
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        if (verdi == null) {
            return 0;
        }

        // kode hentet fra kompendiet
        Node<T> p = rot;
        int antallVerdi = 0;

        while (p != null) // her sammenligner vi nodene med veridne helt til noden vi er på går til null
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre; // Hvis verdien er mindre enn nåværende node leter vi på venstre
            else  // Hvis verdien er større eller lik nåværende node går vi til høyre
            {
                if (cmp == 0) antallVerdi++;  // Her har vi funnet verdien og øker antall og går til høyre
                p = p.høyre;
            }
        }
        return antallVerdi;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        if (tom()) {
            return;
        } else {
            Node<T> p = rot, q;

            while (!tom()) {    // jobber helt til treet er tomt
                if (p.venstre != null) {    // sjekker helt til venstre først
                    p = p.venstre;
                } else if (p.høyre != null) { // sjekker høyre om venstre ikke er mulig
                    p = p.høyre;
                } else if (antall == 1) {    // sletter siste element
                    fjern(p.verdi);
                } else {    // utfører fjerning
                    q = p;  // lagrer midlertidig med hjelpevariabel
                    fjern(p.verdi);     // fjerner
                    p = q.forelder; // går opp et hakk
                }
            }
        }
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        if (p == null) {
            return null;
        }

        if (p.høyre != null) {  // her traverserer vi inn i høyre substre når mulig
            p = p.høyre;
            while (p.venstre != null) {     // går nedover til nederste venstre punkt i høyre subtre
                p = p.venstre;
            }
        } else {    // hvis høyre er null må vi gå opp et hakk
            while (p != null) {     // her løper vi opp treet tilbake til første node som ikke har tidligere brukt noder på sin høyre side
                if (p.forelder != null && p.forelder.høyre != p) {
                    return p.forelder;
                } else {
                    p = p.forelder;
                }
            }
        }

        return p;
    }

    @Override
    public String toString() {
        Node<T> p = rot;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        if (tom()) {        // hvis input er tomt returnerer vi tom array
            stringBuilder.append("]");
            return stringBuilder.toString();
        } else {
            while (p.venstre != null) {  // blar helt ned til nederste venstre blad
                p = p.venstre;
            }

            for (int i = 0; i < antall; i++) {
                if (i == antall - 1) {
                    stringBuilder.append(p.verdi);      // glemmer komma på siste plass for ryddighet
                } else {
                    stringBuilder.append(p.verdi + ", "); // legger inn første verdi
                }
                p = nesteInorden(p);    // traverserer til neste inorder
            }

            stringBuilder.append("]");

            return stringBuilder.toString();
        }

    }

    public String omvendtString() {

        if (tom()) return "[]";     // fant ut at dette var mer effektivt hvis liste var tom

        TabellStakk<Node<T>> stakk = new TabellStakk<>();       // lager stakk som skal brukes
        Node<T> p = rot;        // starter i roten

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");


        while (p.venstre != null) {  // blar helt ned til nederste venstre blad
            p = p.venstre;
        }

        for (int i = 0; i < antall; i++) {
            stakk.leggInn(p);       // legger inn verdi i stakk i LIFO order
            p = nesteInorden(p);    // traverserer til neste inorder
        }

        while (!stakk.tom()) {
            if (stakk.antall() == 1) {   // hvis vi er på siste verdi hopper vi over komma
                stringBuilder.append(stakk.taUt());
            } else {
                stringBuilder.append(stakk.taUt() + ", ");      // legger inn i string med å ta ut siste verdi i stakk
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public String høyreGren() {
        if (tom()) return "[]";

        Node<T> p = rot;        // starter i roten

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        while (p.høyre != null || p.venstre != null) {
            if (p.høyre != null) {
                stringBuilder.append(p.verdi + ", ");
                p = p.høyre;
            } else {
                stringBuilder.append(p.verdi + ", ");
                p = p.venstre;
            }
        }

        stringBuilder.append(p.verdi); // tar med siste verdi uten komma


        stringBuilder.append("]");

        return stringBuilder.toString();
    }


    public String lengstGren() {
        if (tom()) {
            return "[]";
        } else if (antall == 1) {
            return "[" + rot.verdi + "]";
        }

        ArrayDeque<Node<T>> stakk = new ArrayDeque<>(); // lager først et deque for å hjelpe å traversere
        StringBuilder stringBuilder = new StringBuilder();
        int lengsteVei = 0;     // hjelpevariabel
        int tempVei = 0;        // hjelpevariabel
        Node<T> p = rot, lengstGren = null, q;      // initialiserer p og lengstegren siden de skal brukes senere
        // q er en hjelpenode
        stakk.add(p);       // legger roten inn i stakk

        while (!stakk.isEmpty()) {        // løkken jobber helt til alle noder er sjekket ut av stakken
            p = stakk.removeLast();

            if (p.venstre != null) {        //sjekker venstre
                stakk.add(p.venstre);
            }
            if (p.høyre != null) {          //sjekker høyre
                stakk.add(p.høyre);
            }
            if (p.venstre == null && p.høyre == null) {  // når vi kommer til bladnode sjekker vi lengden på gren frem dit
                q = p;
                while (q.forelder != null) {
                    q = q.forelder;
                    tempVei++;
                }
            }
            if (tempVei >= lengsteVei) {     // lagrer den lengste grenen og sammenligner med de andre grenene
                lengstGren = p;
                lengsteVei = tempVei;
            }
            tempVei = 0;

        }

        p = lengstGren; // siste gren som blir satt som lengst skrives ut

        stakk.add(p);
        while (p.forelder != null) {
            p = p.forelder;
            stakk.add(p);       // utnytter stakk på nytt og legger inn hele grenen inn i stakk
        }

        stringBuilder.append("[");

        while (!stakk.isEmpty()) {
            p = stakk.removeLast();     // henter ut av stakken og lager string
            if (stakk.isEmpty()) {
                stringBuilder.append(p.verdi);
            } else {
                stringBuilder.append(p.verdi + ", ");
            }
        }
        //stringBuilder.append(p.verdi + ", ");// her måtte jeg reversere komma og [ ] pga det kom i revers
        //stringBuilder.append(p.verdi); // siste uten komma

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public String[] grener() {

        ArrayList<String> grener = new ArrayList<>();
        String[] stringArr;

        if (tom()) {
            stringArr = new String[grener.size()];
            return stringArr;
        }

        ArrayDeque<Node<T>> stakk = new ArrayDeque<>(); // lager først et deque for å hjelpe å traversere
        ArrayDeque<Node<T>> stakk2 = new ArrayDeque<>(); // lager nytt deque for å helpe med å bygge streng
        StringBuilder stringBuilder = new StringBuilder();
        int lengsteVei = 0;     // hjelpevariabel
        int tempVei = 0;        // hjelpevariabel
        Node<T> p = rot, lengstGren = null, q;      // initialiserer p og lengstegren siden de skal brukes senere
        // q er en hjelpenode
        stakk.add(p);       // legger roten inn i stakk

        while (!stakk.isEmpty()) {        // løkken jobber helt til alle noder er sjekket ut av stakken
            p = stakk.removeLast();

            if (p.venstre != null) {    // sjekker venstre
                stakk.add(p.venstre);
            }
            if (p.høyre != null) {      // sjekker høyre
                stakk.add(p.høyre);
            }
            if (p.venstre == null && p.høyre == null) {  // når vi kommer til bladnode sjekker vi lengden på gren frem dit
                stringBuilder.append("[");
                q = p;
                stakk2.add(q);      // utnytter stakk2 for p traversere og bygge streng
                while (q.forelder != null) {
                    q = q.forelder;
                    stakk2.add(q);
                }
                while (!stakk2.isEmpty()) {
                    q = stakk2.removeLast();
                    if (stakk2.isEmpty()) {
                        stringBuilder.append(q.verdi);
                    } else {
                        stringBuilder.append(q.verdi + ", ");
                    }
                }
                stringBuilder.append("]");
                grener.add(stringBuilder.toString());
                stringBuilder = new StringBuilder(); // resetter stringbuilder

            }

        }

        stringArr = new String[grener.size()];
        int n = stringArr.length - 1;

        for (int i = n; i >= 0; i--) {
            stringArr[n - i] = grener.get(i);
        }

        return stringArr;
    }

    private void inOrderBladNoder(Node<T> node, StringJoiner stringJoiner) {
        if (node == null) {
            return;
        }

        inOrderBladNoder(node.venstre, stringJoiner);
        if (node.venstre == null && node.høyre == null) {
            stringJoiner.add(node.verdi.toString());
        }
        inOrderBladNoder(node.høyre, stringJoiner);
    }

    public String bladnodeverdier() {
        Node<T> p = rot;
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        //      utnytter stringjoiner for å skape strengen


        if (tom()) {
            return "[]";
        } else if (antall == 1) {
            stringJoiner.add(p.verdi.toString());
        } else {
            inOrderBladNoder(rot, stringJoiner);
        }
        return stringJoiner.toString();
    }

    private void postOrderHjelper(Node<T> node, StringJoiner stringJoiner) {
        if (node == null) {
            return;
        }

        //Her utnytter vi 2 stakker for å iterativt legge inn alle verdiene i post-order
        TabellStakk<Node<T>> stakk1 = new TabellStakk<>();
        TabellStakk<Node<T>> stakk2 = new TabellStakk<>();

        stakk1.leggInn(node);       // begynner med roten
        while (!stakk1.tom()) {
            node = stakk1.taUt();       // roten legges inn først / henter ut siste node fra stakk 1
            stakk2.leggInn(node);
            if (node.venstre != null) {
                stakk1.leggInn(node.venstre);       // traverserer først venstre
            }
            if (node.høyre != null) {        // deretter traverserer høyre
                stakk1.leggInn(node.høyre);
            }
        }

        while (!stakk2.tom()) {
            stringJoiner.add(stakk2.taUt().toString());
        }

    }

    public String postString() {
        if (tom()) {
            return "[]";
        }
        Node<T> p = rot;
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        postOrderHjelper(p, stringJoiner);

        return stringJoiner.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() { // konstruktør
            if (tom()) {
                return;
            }
            while (p.venstre != null) {     // begynner fra lengst venstre node
                p = p.venstre;
            }

            while (p.venstre != null || p.høyre != null && nesteInorden(p) != null) {
                p = nesteInorden(p);    // finner første i inorden uten barn
            }
        }

        @Override
        public boolean hasNext() {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next() {
            if (iteratorendringer != endringer) {   // sjekker at endringer stemmer
                throw new ConcurrentModificationException("Iteratorendringer stemmer ikke med endringer!");
            } else if (hasNext()) {
                removeOK = true;
                q = p;  // lagrer verdien til den som skal printes
                p = nesteInorden(p);    // fortsetter videre inorden
                while (hasNext() && (p.venstre != null || p.høyre != null)) {  // sjekker om det er bladnode
                    p = nesteInorden(p);
                }
                return q.verdi;
            } else {
                throw new NoSuchElementException("Ingen flere bladnoder!");
            }
        }

        @Override
        public void remove() {
            if (!removeOK) {
                throw new IllegalStateException("Ikke tillatt å bruke metoden!");
            } else if (iteratorendringer != endringer) {
                throw new ConcurrentModificationException("Iteratorendringer stemmer ikke med endringer!");
            } else {
                removeOK = false;
            }
            if (antall == 1) {  // hvis vi er på siste element setter vi den bare til null
                q = null;
            } else {
                Node<T> parent = q.forelder;    // bruker forelderen til q
                if (parent.venstre == q) {      // hvis q er venstrebarn setter vi den til null
                    parent.venstre = null;
                    q = null;
                } else if (parent.høyre == q) { // hvis q er høyrebarn setter vi den til null
                    q = null;
                    parent.høyre = null;
                }
            }


            antall--;   // oppdaterer antall, endringer og iteratorendringer
            endringer++;
            iteratorendringer++;

        }

    } // BladnodeIterator


} // ObligSBinTre
