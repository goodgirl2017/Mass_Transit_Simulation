import java.util.*;

public class EventSenario {
    private int busId;
    private int route;
    private int busLocationIndex;
    private int currentFuel;
    private int currentTime;
    private route busRoute;
    private int busLocation;
    private int busNextLocation;
    private int nextLocationTime;
    private int currentPassenger;
    private int speed;
    private int stopID;
    private int numWaiting;
    private int nextStopId;
    private int nextStopNumWaiting;

    public EventSenario(int busId, int route, int busLocationIndex, int currentFuel, int currentTime, route busRoute, int busLocation, int busNextLocation, int nextLocationTime, int currentPassenger, int speed, int stopID, int numWaiting) {
        this.busId = busId;
        this.route = route;
        this.busLocationIndex = busLocationIndex;
        this.currentFuel = currentFuel;
        this.currentTime = currentTime;
        this.busRoute = busRoute;
        this.busLocation = busLocation;
        this.busNextLocation = busNextLocation;
        this.nextLocationTime = nextLocationTime;
        this.currentPassenger = currentPassenger;
        this.speed = speed;
        this.stopID = stopID;
        this.numWaiting = numWaiting;
    }

    public void setNextStop(int stopId, int waiting) {
        this.nextStopId = stopId;
        this.nextStopNumWaiting = waiting;
    }

    public int getNextStopId() {return this.nextStopId;}

    public int getNextStopNumWaiting() {return this.nextStopNumWaiting;}

    public int getBusId() {return this.busId;}

    public int getRoute() {return this.route;}

    public int getBusLocationIndex() {return this.busLocationIndex;}

    public int getCurrentFuel() {return this.currentFuel;}

    public int getCurrentTime() {return this.currentTime;}

    public route getBusRoute() {return this.busRoute;}

    public int getBusLocation() {return this.busLocation;}

    public int getBusNextLocation() {return this.busNextLocation;}

    public int getNextLocationTime() {return this.nextLocationTime;}

    public int getCurrentPassenger() {return this.currentPassenger;}

    public int getSpeed() {return this.speed;}

    public int getStopID() {return this.stopID;}

    public int getNumWaiting() {return this.numWaiting;}

}
