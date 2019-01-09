import java.awt.*;
import java.io.*;
import java.util.*;

//+++++++++++++++++++++++++++++++++++++++++++++++++
// ASSUMPTIONS:
// 1. passengers won't change at the initial stop
// 2. ridersArriveHigh in the "stop" class was assumed to be 10; the low is 0;
//+++++++++++++++++++++++++++++++++++++++++++++++++

public class working_system {
    public static Map<Integer, stop> stopList = new HashMap<>();
    public static Map<Integer, bus> busList = new HashMap<>();
    public static Map<Integer, route> routeList = new HashMap<>();
    public static eventQueue eventList = new eventQueue();
    public double maxXCoord = -10000;
    public double minXCoord = 10000;
    public double maxYCoord = -10000;
    public double minYCoord = 10000;

    public void resetSystem () {
        stopList = new HashMap<>();
        busList = new HashMap<>();
        routeList = new HashMap<>();
        eventList = new eventQueue();
    }

    public static bus getBus(int busId) {return busList.get(busId);}

    public static stop getStop(int stopId) {return stopList.get(stopId);}


    public static void main(String[] args) {

        try {

            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // REMEMBER TO CHAGNE BACK TO READING FROM args !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            String file_path = "../Project_Testing/CopiedForPassenger_test10_perimeter_scenic.txt";
//            File inputFile = new File(file_path);
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            File inputFile = new File(args[0]);
            BufferedReader inputText = new BufferedReader(new FileReader(inputFile));
            String inputCommand = "";

            while ((inputCommand = inputText.readLine()) != null) {
                String[] input = inputCommand.split(",");
                switch(input[0]) {
                    case "add_stop":
                        stop newStop = new stop(Integer.parseInt(input[1]), input[2],
                                Integer.parseInt(input[3]),
                                Double.parseDouble(input[4]),
                                Double.parseDouble(input[5]));
                        stopList.put(Integer.parseInt(input[1]), newStop);
                        break;
                    case "add_route":
                        route newRoute = new route(Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                input[3]);
                        routeList.put(Integer.parseInt(input[1]), newRoute);
                        break;
                    case"add_bus":
                        bus newBus;
                        if(input.length>6) {
                            newBus = new bus(Integer.parseInt(input[1]),
                                    Integer.parseInt(input[2]),
                                    Integer.parseInt(input[3]),
                                    Integer.parseInt(input[4]),
                                    Integer.parseInt(input[5]),
                                    Integer.parseInt(input[6]),
                                    Integer.parseInt(input[7]),
                                    Integer.parseInt(input[8]));
                        }
                        else{
                        newBus = new bus(Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[4]),
                                Integer.parseInt(input[5]));}
                        busList.put(Integer.parseInt(input[1]), newBus);
                        break;
                    case"add_event":
                        event newEvent = new event(Integer.parseInt(input[1]),
                                input[2],
                                Integer.parseInt(input[3]));
                        busList.get(Integer.parseInt((input[3]))).setBusStartTime(Integer.parseInt(input[1]));
                        eventList.addToEventList(newEvent);
                        break;
                    case "extend_route":
                        route routeSelected = routeList.get(Integer.parseInt(input[1]));
                        routeSelected.extend_route(Integer.parseInt(input[2]));
                        break;
                    case "add_depot":
                        break;
                }
            }

            if(args.length>1){
                File inputFile1 = new File(args[1]);
                BufferedReader inputText1 = new BufferedReader(new FileReader(inputFile1));
                String inputCommand1="";
                while ((inputCommand1 = inputText1.readLine()) != null) {
                    String[] input = inputCommand1.split(",");
                    if(stopList.containsKey(Integer.parseInt (input[0]))){
                        stop existingStop = stopList.get(Integer.parseInt(input[0]));
                        existingStop.setRidersArriveHigh(Integer.parseInt(input[1]));
                        existingStop.setRidersArriveLow(Integer.parseInt(input[2]));
                        existingStop.setRidersOffHigh(Integer.parseInt(input[3]));
                        existingStop.setRidersOffLow(Integer.parseInt(input[4]));
                        existingStop.setRidersOnHigh(Integer.parseInt(input[5]));
                        existingStop.setRidersOnLow(Integer.parseInt(input[6]));
                        existingStop.setRidersDepartHigh(Integer.parseInt(input[7]));
                        existingStop.setRidersDepartLow(Integer.parseInt(input[8]));
                    }
                }
            }

        } catch (IOException e) {
           e.printStackTrace();
           System.out.println("file read exception");
        }
   }

   public void changeBusRoute(int busID, String attribute, String newAttribute) {
       String[] routeStop = newAttribute.split(",");
       int newRouteId = Integer.valueOf(routeStop[0]);
       int newStopIndex = Integer.valueOf(routeStop[1]);

//                busList.get(busID).setRoute(newroute);
//                int preStop = newStopId-1;
//                if (preStop<0){
//                    preStop +=newroute.getStopList().size();
//                }
//                busList.get(busID).setCurrentStopIndex(preStop);
       bus changedBus = busList.get(busID);
       if (changedBus.isStarted()) {
           changedBus.isRouteChanged = true;
           changedBus.changedRouteId = newRouteId;
           changedBus.changedNextIndex = newStopIndex;
       } else {
           // route = changedRouteId;
           // busLocationIndex = changedNextIndex;
           // changedNextIndex = Integer.MIN_VALUE;
           // this.busRoute = this.getRouteByID(this.route);
           // this.busLocation = this.busRoute.currStop(this.busLocationIndex);
           // this.busNextLocation = this.busLocation;
           changedBus.setRouteId(newRouteId);
           changedBus.setBusLocationIndex(newStopIndex);
           route newroute =routeList.get(newRouteId);
           changedBus.setRoute(newroute);
           changedBus.setBusLocation(changedBus.getBusRoute().currStop(newStopIndex));
           changedBus.setBusNextLocation(changedBus.getBusRoute().currStop(newStopIndex));
       }


   }

    public void changeBus(int busID, String attribute, String newAttribute){
        switch (attribute) {
//            case "route,stop":
//                String[] routeStop = newAttribute.split(",");
//                int newRouteId = Integer.valueOf(routeStop[0]);
//                int newStopIndex = Integer.valueOf(routeStop[1]);
////                route newroute =routeList.get(newRouteId);
////                busList.get(busID).setRoute(newroute);
////                int preStop = newStopId-1;
////                if (preStop<0){
////                    preStop +=newroute.getStopList().size();
////                }
////                busList.get(busID).setCurrentStopIndex(preStop);
//                bus changedBus = busList.get(busID);
//                changedBus.isRouteChanged = true;
//                changedBus.changedRouteId = newRouteId;
//                changedBus.changedNextIndex = newStopIndex;
//                break;
            case "speed":
                busList.get(busID).setSpeed(Integer.valueOf(newAttribute));
                break;
            case "capacity":
                busList.get(busID).setPassengerCapacity(Integer.valueOf(newAttribute));
                /***passenger get off if current riders > capacity****/
                if (Integer.valueOf(newAttribute)<busList.get(busID).getCurrentPassenger()){
                    int offRider = busList.get(busID).getCurrentPassenger()-Integer.valueOf(newAttribute);
                    busList.get(busID).setCurrentPassenger(Integer.valueOf(newAttribute));
                    int curStop = busList.get(busID).getBusLocation();
                    stopList.get(curStop).addWaiting(offRider);
                }
                break;
        }
    }


    public void draw(Graphics g, Image busPic, Image stopPic, int fullWidth, int fullHeight, int marginWidth, int marginHeight){

        HashMap<Integer, Integer> multiBus = new HashMap();

       for(stop s:stopList.values()){
           if(maxXCoord<s.getStopLatitude()) maxXCoord = s.getStopLatitude();
           if(minXCoord>s.getStopLatitude()) minXCoord = s.getStopLatitude();
           if(maxYCoord<s.getStopLongtitude()) maxYCoord = s.getStopLongtitude();
           if(minYCoord>s.getStopLongtitude()) minYCoord = s.getStopLongtitude();
       }

        double xSlope = (fullWidth - 2*marginWidth)/(maxXCoord-minXCoord);
        double xIntercept = marginWidth-xSlope*minXCoord;
        double ySlope = (fullHeight-2*marginHeight)/(maxYCoord-minYCoord);
        double yIntercept = marginHeight -ySlope*minYCoord;

        int imageWidth = 30;
        int imageHeight = 30;
        int stopXOffset = 10+imageWidth;
        int imageYHalfOffset = imageHeight/2;
        int titleOffset = 5;
        int busXOffset = 10;

        route currentRoute = null;
        for(stop s: stopList.values()){
            for(route r:routeList.values()){
                if(r.getStopInRoute().contains(s.getStopID())){
                    currentRoute=r;
                }
            }
            Double xLoc = xIntercept+xSlope*s.getStopLatitude();
            Double yLoc = yIntercept+ySlope*s.getStopLongtitude();
            g.drawImage(stopPic, xLoc.intValue()-stopXOffset, yLoc.intValue()-imageYHalfOffset,imageWidth,imageHeight,null);
            g.drawString("stop #"+s.getStopID()+": "+s.getStopName(),xLoc.intValue()-stopXOffset,yLoc.intValue()-imageYHalfOffset-3*titleOffset);
            g.drawString("Waiting Passenger: "+s.getWaiting(),xLoc.intValue()-stopXOffset,yLoc.intValue()-imageYHalfOffset-titleOffset);
        }

        for(bus b:busList.values()){

            //get route and stop info
            route r =b.getBusRoute();
            stop s = stopList.get(b.getBusLocation());

            //how to get the x y coord
            Double xLoc = xIntercept+xSlope*s.getStopLatitude();
            Double yLoc = yIntercept+ySlope*s.getStopLongtitude();
            int lineOffset;
            if(!multiBus.containsKey(s.getStopID())){
                g.drawImage(busPic, xLoc.intValue()+busXOffset, yLoc.intValue()-imageYHalfOffset,imageWidth,imageHeight,null);
                lineOffset = 2;
            }
            else{
                lineOffset = multiBus.get(s.getStopID())+3;
            }
            stop ns = stopList.get(b.getBusNextLocation());
            g.drawString("b: "+b.getBusID()+"->s: " + ns.getStopID() + "@" + b.getNextLocationTime() + "//p: "+ b.getCurrentPassenger()+ "/f: "+ b.getCurrentFuel(),xLoc.intValue()-stopXOffset, yLoc.intValue()+imageYHalfOffset+lineOffset*titleOffset);

            multiBus.put(s.getStopID(),lineOffset);
        }

   }

}
