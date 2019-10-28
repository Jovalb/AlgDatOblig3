package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {

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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String høyreGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

    } // BladnodeIterator


} // ObligSBinTre
