package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        /*Integer[] a = {6, 3, 9, 1, 5, 7, 10, 2, 4, 8, 11, 6, 8};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi: a){
            tre.leggInn(verdi);
        }
        System.out.println(tre.antall());
        System.out.println(tre.antall(5));
        System.out.println(tre.antall(4));
        System.out.println(tre.antall(7));
        System.out.println(tre.antall(10));

        System.out.println(tre);
        tre.fjern(8);
        System.out.println(tre);
        System.out.println(tre.fjernAlle(6));
        System.out.println(tre.antall());
        System.out.println(tre);
        System.out.println(tre.antall());
        tre.nullstill();
        System.out.println(tre.antall());
        System.out.println(tre + tre.omvendtString());*/

        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        char [] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for (char c : verdier) {
            tre.leggInn(c);
        }

        //System.out.println(tre.høyreGren());
        //System.out.println(tre.lengstGren());

        ObligSBinTre<Integer> tre1 = new ObligSBinTre<>(Comparator.naturalOrder());
        tre1.leggInn(1);
        //System.out.println(tre1.bladnodeverdier());

        /*String [] s = tre.grener();

        for (String e : s){
            System.out.println(e);
        }*/

        /*System.out.println(tre.bladnodeverdier());
        System.out.println(tre.postString());*/

        for (Character c : tre) System.out.print(c + " ");

        /*while(!tre.tom()){
            System.out.println(tre);
            tre.fjernHvis(x -> true);
        }*/


    }
}
