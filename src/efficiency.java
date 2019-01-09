
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class efficiency {


    public static Map<Integer, stop> stopList = new HashMap<>();
    public static Map<Integer, bus> busList = new HashMap<>();
    private int waitingPassengers;
    private double busCost;
    private double systemEfficiency;
    private double kspeed;
    private double kcapacity;
    private double kwaiting;
    private double kbuses;
    private double kcombined;


    public efficiency(Map<Integer, stop> stopList, Map<Integer, bus> busList) {
        this.kspeed = 1.0;
        this.kcapacity = 1.0;
        this.kwaiting = 1.0;
        this.kbuses = 1.0;
        this.kcombined = 1.0;
        this.stopList=stopList;
        this.busList=busList;
    }


    public void setKspeed(double kspeed) {
        this.kspeed = kspeed;
    }

    public void setKcapacity(double kcapacity) {
        this.kcapacity = kcapacity;
    }

    public void setKwaiting(double kwaiting) {
        this.kwaiting = kwaiting;
    }

    public void setKbuses(double kbuses) {
        this.kbuses = kbuses;
    }

    public void setKcombined(double kcombined) {
        this.kcombined = kcombined;
    }

    public double getKspeed() {
        return this.kspeed;
    }

    public double getKcapacity() {
        return this.kcapacity;
    }

    public double getKwaiting() {
        return this.kwaiting;
    }

    public double getKbuses() {
        return this.kbuses;
    }

    public double getKcombined() {
        return this.kcombined;
    }

    public void calcWaitingPassengers() {
        int waitingPassengers = 0;
        Iterator it = stopList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            stop stop = (stop) pair.getValue();
            waitingPassengers+=stop.getWaiting();
        }
        this.waitingPassengers = waitingPassengers;
    }

    public void calcBusCost() {
        double busCost = 0;
        Iterator it = busList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            bus currBus = (bus)pair.getValue();
            double currCost = kspeed * currBus.getSpeed() + kcapacity * currBus.getPassengerCapacity();
            busCost=busCost+currCost;
        }
        this.busCost = busCost;
    }

    public void calculateEfficiency() {
        double systemEfficiency = kwaiting * this.waitingPassengers + kbuses * this.busCost + kcombined * this.waitingPassengers * this.busCost;
        this.systemEfficiency = systemEfficiency;
    }



    public double getEfficiency(){
        return this.systemEfficiency;
    }
}
