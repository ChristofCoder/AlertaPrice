package at.ac.hcw.alertaprice;

import java.util.*;

import static at.ac.hcw.alertaprice.WebAlertManager.getWebAlerts;

public class FilterSortAndList {
    Scanner scanner = new Scanner(System.in);
    private ArrayList<WebAlert> filtersort = getWebAlerts();

    public void filterAlert(){

        System.out.println("Bitte Filtermethode wählen (1 = Alphabetisch, 2 = Preis");
        int filtermethod = scanner.nextInt();


        switch (filtermethod) {
            case 1:
                System.out.println("Bitte Reihenfolge wählen (1 = ASC, 2 = DESC)");
                int sortorder = scanner.nextInt();
                switch (sortorder) {
                    case 1:
                        filtersort.sort(Comparator.comparing(WebAlert::getName));
                            System.out.println(filtersort);

                    case 2:
                        filtersort.sort(Comparator.comparing(WebAlert::getName).reversed());
                        System.out.println(filtersort);

                        /* Macht basically: for (WebAlert alert : filtersort) {
                            filtersort.sort(Collections.reverseOrder());
                            System.out.println(filtersort);*/

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + sortorder);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + filtermethod);
        }



        }
}
