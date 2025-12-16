package at.ac.hcw.alertaprice;

import java.util.*;

import static at.ac.hcw.alertaprice.WebAlertManager.getWebAlerts;

public class FilterSortAndList {
    Scanner scanner = new Scanner(System.in);
    private ArrayList<WebAlert> filtersort = getWebAlerts();

    public void filterAlert() {

        System.out.println("Bitte Filtermethode wählen (1 = Alphabetisch ASC\n2 = Alphabetisch DESC\n3 = Preis ASC\n 4 = Preis DESC");
        int filtermethod = scanner.nextInt();


        switch (filtermethod) {
            case 1:
                filtersort.sort(Comparator.comparing(WebAlert::getName));
                System.out.println(filtersort);

            case 2:
                filtersort.sort(Comparator.comparing(WebAlert::getName).reversed());
                System.out.println(filtersort);

                        /* Macht basically: for (WebAlert alert : filtersort) {
                            filtersort.sort(Collections.reverseOrder());
                            System.out.println(filtersort);*/

            case 3:
                filtersort.sort(Comparator.comparing(WebAlert::getCurrentValue)); //typecasten in int
                System.out.println(filtersort);
                break;

            case 4:
                filtersort.sort(Comparator.comparing(WebAlert::getCurrentValue).reversed());
                System.out.println(filtersort);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + filtermethod);
        }


    }
}
