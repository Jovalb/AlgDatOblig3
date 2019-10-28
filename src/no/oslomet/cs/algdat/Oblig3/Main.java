package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        ObligSBinTre<Character> tre1 = new ObligSBinTre<>(Comparator.naturalOrder());
        ObligSBinTre<String> tre2 = new ObligSBinTre<>(Comparator.naturalOrder());
        System.out.println(tre.antall());
        System.out.println(tre1.antall());
        System.out.println(tre2.antall());
    }
}
