package at.ac.hcw.alertaprice;

public class ComparePrices {

    public void compareAlert(WebAlert alert){
        if (Integer.parseInt(alert.getPreviousValue()) < Integer.parseInt(alert.getCurrValue())){
        }
    }
}
