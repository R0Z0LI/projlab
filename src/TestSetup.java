import TestSets.ApplyAgent_TestSet;

import java.util.Scanner;

public class TestSetup {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Mit szeretnél tesztelni? Válassz egy számot:");
        System.out.println("1. Move Virologist");
        System.out.println("2. Collect amino acid or nucleotide");
        System.out.println("3. Collect equipment");
        System.out.println("4. Scan genetic code");
        System.out.println("5. Create agent");
        System.out.println("6. Apply agent");
        System.out.println("7. Steal");
        System.out.println("8. Throw equipment");
        System.out.println("9. Step agent");

        switch (sc.next()) {
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                ApplyAgent_TestSet test6 = new ApplyAgent_TestSet();
                test6.test();
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;
            default:
                // exit
        }

    }

}