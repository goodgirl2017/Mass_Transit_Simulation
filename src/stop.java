public class stop {

    private int stopID;
    private String stopName;
    private int numRiders;
    private double stopLatitude;
    private double stopLongtitude;
    private int numWaiting;

    public int getNumTransfer() {
        return numTransfer;
    }

    private int numTransfer;
    private int ridersArriveHigh;
    private int ridersArriveLow;
    private int ridersOffHigh;
    private int ridersOffLow;
    private int ridersOnHigh;
    private int ridersOnLow;
    private int ridersDepartHigh;
    private int ridersDepartLow;

    public void setRidersArriveHigh(int ridersArriveHigh) {
        this.ridersArriveHigh = ridersArriveHigh;
    }

    public void setRidersArriveLow(int ridersArriveLow) {
        this.ridersArriveLow = ridersArriveLow;
    }

    public void setRidersOnHigh(int ridersOnHigh) {
        this.ridersOnHigh = ridersOnHigh;
    }

    public void setRidersOnLow(int ridersOnLow) {
        this.ridersOnLow = ridersOnLow;
    }

    public void setRidersDepartHigh(int ridersDepartHigh) {
        this.ridersDepartHigh = ridersDepartHigh;
    }

    public void setRidersDepartLow(int ridersDepartLow) {
        this.ridersDepartLow = ridersDepartLow;
    }

    public void setRidersOffHigh(int ridersOffHigh) {
        this.ridersOffHigh = ridersOffHigh;
    }

    public void setRidersOffLow(int ridersOffLow) {
        this.ridersOffLow = ridersOffLow;
    }

    public int getRidersOffHigh() {
        return ridersOffHigh;
    }

    public int getRidersOffLow() {
        return ridersOffLow;
    }

    public stop(int ID, String stopName, int numRiders, double latitude, double longtitude) {
        this.stopID = ID;
        this.stopName = stopName;
        this.numWaiting = numRiders;
        this.stopLatitude = latitude;
        this.stopLongtitude = longtitude;
        this.numTransfer = 0;
        this.ridersArriveHigh = 10;
        this.ridersArriveLow = 0;
        this.ridersOnHigh = 10;
        this.ridersOnLow = 0;
        this.ridersDepartHigh = 10;
        this.ridersDepartLow = 0;
        this.ridersOffHigh = 10;
        this.ridersOffLow = 0;
    }

    public void updateArrive() {
        numWaiting = numWaiting + passenger.updatePassenger(ridersArriveLow, ridersArriveHigh);
        System.out.println("Arrive+Waiting: " + numWaiting);
    }

    public void updateTransfer(int transferNum) {
        numTransfer = transferNum;
    }

    public int updateWaiting() {
        int onBoard = passenger.updatePassenger(ridersOnLow, ridersOnHigh);
        if (onBoard > numWaiting) {
            onBoard = numWaiting;
        }
        numWaiting = numWaiting - onBoard;
        System.out.println("Aboard passenger number: "+ onBoard);
        System.out.println("Waiting number after aboard: " + numWaiting);
        return onBoard;
    }

    public void updateWaiting2nd(int numOutOfCapacity) {
        numWaiting = numWaiting + numOutOfCapacity;
        System.out.println("Updated waitings due to exceeding passenger capacity" + numWaiting);
    }

    public void updateLeave() {
        System.out.println("Transfer passenger numbers before leave/RidersOff: "+ numTransfer);
        int numLeave = passenger.updatePassenger(ridersDepartLow, ridersDepartHigh);
        if (numLeave > numWaiting + numTransfer) {
            numLeave = numWaiting + numTransfer;
        }

        if (numLeave <= numTransfer) {
            numTransfer = numTransfer - numLeave;
            numWaiting = numWaiting + numTransfer;
            numTransfer = 0;
        } else {
            numWaiting = numWaiting - (numLeave - numTransfer);
            numTransfer = 0;
        }
        System.out.println("Leave passenger number: "+ numLeave);
        System.out.println("Waitings after leaving: " + numWaiting);
    }


    public double distanceTo(stop thatStop) {
        double x1 = this.stopLatitude;
        double y1 = this.stopLongtitude;
        double x2 = thatStop.getStopLatitude();
        double y2 = thatStop.getStopLongtitude();
        double distance = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
        double trueDistance = 70.0 * distance;
        return trueDistance;
    }

    public double getStopLatitude() {
        return this.stopLatitude;
    }

    public double getStopLongtitude() {
        return this.stopLongtitude;
    }

    public int getStopID() {
        return stopID;
    }

    public String getStopName() {
        return stopName;
    }

    public int getNumRiders() {
        return numRiders;
    }

    // return number of waiting for effciency
    public int getWaiting() {
        return this.numWaiting;
    }


    // method for rewind stop information
    public void rewindStop(int numWaiting) {
        this.numWaiting = numWaiting;
    }

    public void addWaiting(int addRider){this.numWaiting+=addRider;}

}
