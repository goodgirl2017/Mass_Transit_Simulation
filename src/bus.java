public class bus {
    private int busID;
    private int route;
    private int busLocationIndex;
    private int busLocation;
    private int busNextLocation;
    private int nextLocationTime;

    private int initialPassengers;
    private int passengerCapacity;
    private int currentPassenger;
    private int initialFuel;
    private int fuelCapacity;
    private int currentFuel;
    private int speed;
    private route busRoute;
    private int currentTime;
    private int ridersOffHigh;
    private int ridersOffLow;

    private boolean isStarted;

    public boolean isRouteChanged = false;
    public int changedRouteId;
    public int changedNextIndex = Integer.MIN_VALUE;

    public int getBusLocationIndex() {
        return busLocationIndex;
    }

    public int getBusLocation() {
        return busLocation;
    }

    public int getRidersOffHigh() {return this.ridersOffHigh;}

    public int getRidersOffLow() {return this.ridersOffLow;}

    public int getCurrentFuel() {
        return currentFuel;
    }

    public route getBusRoute() {
        return busRoute;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isStarted() {
        return isStarted;
    }



    public bus(int ID, int route, int location, int iniPass, int passCap, int iniFuel, int fuelCap, int speed) {
        this.busID = ID;
        this.route = route;
        this.busLocationIndex = location;
        this.initialPassengers = iniPass;
        this.passengerCapacity = passCap;
        this.initialFuel = iniFuel;
        this.fuelCapacity = fuelCap;
        this.speed = speed;
        this.busRoute = this.getRouteByID(this.route);
        this.busLocation = this.busRoute.currStop(this.busLocationIndex);
        this.busNextLocation = this.busLocation;
        this.currentTime = 0;
        this.currentPassenger = initialPassengers;
        this.isStarted = false;
    }

    public bus(int ID, int route, int location, int passCap, int speed) {
        this.busID = ID;
        this.route = route;
        this.busLocationIndex = location;
        this.initialPassengers = 0;
        this.passengerCapacity = passCap;
        this.speed = speed;
        this.busRoute = this.getRouteByID(this.route);
        this.busLocation = this.busRoute.currStop(this.busLocationIndex);
        this.busNextLocation = this.busLocation;
        this.currentTime = 0;
        this.currentPassenger = initialPassengers;
        this.isStarted = false;
    }

    public void setBusStartTime(int startTime) {
        this.currentTime = startTime;
        this.nextLocationTime = 0;
    }

    public void moveBus() {
            if (isStarted == false) {
                this.busNextLocation = this.busRoute.nextStop(this.busLocationIndex);
                updateNextLocationTime();
                updatePassengers();
            }
            else {
                this.currentTime = this.nextLocationTime;
                this.busNextLocation = this.busRoute.nextStop(this.busLocationIndex);
                updateLocation();
                updateNextLocationTime();
                updatePassengers();
                updateFuel();
            }
            this.isStarted = true;
    }

    public int getBusNextLocation() {
        return this.busNextLocation;
    }

    public int getNextLocationTime() {
        return this.nextLocationTime;
    }

    public int getCurrentPassenger() { return this.currentPassenger; }

    private route getRouteByID(int routeID) {
        return working_system.routeList.get(routeID);
    }

    private void updateLocation() {
        int totalRouteStops = busRoute.totalStops();

        if (isRouteChanged == false && changedNextIndex != Integer.MIN_VALUE) {
            this.busLocationIndex = changedNextIndex;
            changedNextIndex = Integer.MIN_VALUE;
        } else {
            if (this.busLocationIndex < totalRouteStops - 1) {
                this.busLocationIndex++;
            } else {
                this.busLocationIndex = 0;
            }
        }

        this.busLocation = this.busRoute.currStop(this.busLocationIndex);

        if (isRouteChanged == false) {
            this.busNextLocation = this.busRoute.nextStop(this.busLocationIndex);
        } else {
            this.route = this.changedRouteId;
//            this.busLocation = this.busRoute.nextStop(this.busLocationIndex);
//            this.busLocationIndex = changedNextIndex;
            this.busRoute = this.getRouteByID(this.route);
            this.busNextLocation = this.busRoute.currStop(this.changedNextIndex);
            isRouteChanged = false;
        }


    }

    private void updateNextLocationTime() {
        stop currStop = working_system.stopList.get(busLocation);
        stop nextStop = working_system.stopList.get(busNextLocation);
        Double distance =  currStop.distanceTo(nextStop);
        int travel_time = 1 + (distance.intValue() * 60 / this.getSpeed() );
        this.nextLocationTime = this.currentTime + travel_time;
    }

    private void updatePassengers() {
        //this.currentPassenger  = 0;
        stop currStop = working_system.stopList.get(busLocation);
        this.ridersOffLow = currStop.getRidersOffLow();
        this.ridersOffHigh = currStop.getRidersOffHigh();

        currStop.updateArrive();

        int ridersOff = updateRidersOff();
        currStop.updateTransfer(ridersOff);

        int onBoard = currStop.updateWaiting();
        currentPassenger = currentPassenger + onBoard;
        if (currentPassenger > passengerCapacity) {
            int numOutOfBound = currentPassenger - passengerCapacity ;
            currStop.updateWaiting2nd(numOutOfBound);
            currentPassenger = passengerCapacity;
        }

        currStop.updateLeave();

    }

    private int updateRidersOff() {
        int ridersOff = passenger.updatePassenger(ridersOffLow, ridersOffHigh);
        if (ridersOff > currentPassenger) {
            ridersOff = currentPassenger;
        }
        currentPassenger = currentPassenger - ridersOff;
        return ridersOff;
    }

    private void updateFuel() {
        this.currentFuel = 0;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getBusID() {
        return this.busID;
    }

    public int getRoute() {
        return this.route;
    }

    public int getLocation() {
        return this.busLocationIndex;
    }

    public int getInitialPassengers() {
        return this.initialPassengers;
    }

    public int getPassengerCapacity() {
        return this.passengerCapacity;
    }

    public int getInitialFuel() {
        return this.initialFuel;
    }

    public int getFuelCapacity() {
        return this.fuelCapacity;
    }


    // method used for rewind bus information
    public void rewindBus(int route, int busLocationIndex, int currentFuel, int currentTime, route busRoute, int busLocation, int busNextLocation, int nextLocationTime, int currentPassenger, int speed) {
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
    }

    public void setID(int ID) {
        this.busID = ID;
    }

    public void setRoute(route newroute) {
        this.busRoute= newroute;
    }

    public void setInitialPassengers(int intialPassengers) {
        this.initialPassengers = initialPassengers;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public void setInitialFuel(int initialFuel) {
        this.initialFuel = initialFuel;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCurrentStopIndex(int stopIndex) {
        this.busLocationIndex = stopIndex;
    }
    public void setCurrentPassenger(int newPassenger) { this.currentPassenger=newPassenger; }

    public void setRouteId(int stopId) {this.route = stopId;}

    public void setBusLocationIndex(int id) {this.busLocationIndex = id;}

    public void setBusLocation(int loc) {this.busLocation = loc;}

    public void setBusNextLocation(int loc) {this.busNextLocation = loc;}


}
