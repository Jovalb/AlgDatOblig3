package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        Integer[] a = {6, 3, 9, 1, 5, 7, 10, 2, 4, 8, 11, 6, 8};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi: a){
            tre.leggInn(verdi);
        }
        /*System.out.println(tre.antall());
        System.out.println(tre.antall(5));
        System.out.println(tre.antall(4));
        System.out.println(tre.antall(7));
        System.out.println(tre.antall(10));*/

        /*System.out.println(tre);
        tre.fjern(8);
        System.out.println(tre);
        System.out.println(tre.fjernAlle(6));
        System.out.println(tre.antall());*/
        System.out.println(tre);
        System.out.println(tre.antall());
        tre.nullstill();
        System.out.println(tre.antall());
        System.out.println(tre + tre.omvendtString());

        //System.out.println(tre.omvendtString());
    }
}
