import java.util.*;

public class eventQueue {
    private static Comparator<event> comparator;
    public static Queue<event> eventList;
    public static Stack<EventSenario> prevInfo;

    public eventQueue() {
        comparator = new eventTimeComparator();
        eventList = new PriorityQueue<event>(1,comparator);
        prevInfo = new Stack<EventSenario>();
    }

    // method for storing information used for rewind
    public EventSenario storeRewindinfo(int busId, int stopId) {
        bus busToStore = working_system.busList.get(busId);
        stop stopToStore = working_system.stopList.get(stopId);
        EventSenario info = new EventSenario(busId, busToStore.getRoute(), busToStore.getBusLocationIndex(), busToStore.getCurrentFuel(), busToStore.getCurrentTime(), busToStore.getBusRoute(), busToStore.getBusLocation(), busToStore.getBusNextLocation(), busToStore.getNextLocationTime(), busToStore.getCurrentPassenger(), busToStore.getSpeed(), stopId, stopToStore.getWaiting());
        if (prevInfo.size() >= 3) {
            prevInfo.removeElementAt(0);
        }
        prevInfo.push(info);
        return info;
    }

    // rewind method
    public void rewind(eventQueue eventQ) {

        if (isEmpty()) {
            return;
        }

        EventSenario info = popLatestEventSenario();
        bus prevBus = working_system.getBus(info.getBusId());
        stop prevStop = working_system.getStop(info.getStopID());
        stop currentStop = working_system.getStop(info.getNextStopId());
        prevBus.rewindBus(info.getRoute(), info.getBusLocationIndex(), info.getCurrentFuel(), info.getCurrentTime(), info.getBusRoute(), info.getBusLocation(), info.getBusNextLocation(), info.getNextLocationTime(), info.getCurrentPassenger(), info.getSpeed());
        prevStop.rewindStop(info.getNumWaiting());
        currentStop.rewindStop(info.getNextStopNumWaiting());

        // find the event in the eventlist with the right busId
        for (event newEvent: eventList) {
            if (newEvent.getID() == info.getBusId()) {
                removeEvent(newEvent);
                event nextEvent = new event(info.getCurrentTime(), "move_bus", info.getBusId());
                eventQ.addToEventList(nextEvent);
                break;
            }
        }


    }

    public void removeEvent(event newEvent) {
        eventList.remove(newEvent);
    }

    public boolean isEmpty() {return prevInfo.size() < 1;}

    public EventSenario popLatestEventSenario() {return prevInfo.pop();}

    public event popExecuteEvent() {
        return eventList.poll();
    }

    public void addToEventList(event newEvent) {
        eventList.add(newEvent);
    }

    public class eventTimeComparator implements Comparator<event> {
        public int compare(event x, event y) {
               return (x.getLogicTime() - y.getLogicTime());
        }
   }

    public void addMoveBusHistory(bus movingBus, stop currentBus) {
//        bus copiedBus = new bus();
   }

    public void performNext (eventQueue eventList,Map<Integer, bus> busList) {
        event eventNow = eventList.popExecuteEvent();
        int busID = eventNow.getID();
        bus busNow = busList.get(busID);
        // store information for rewind
        int stopID = busNow.getBusLocation();
        EventSenario info = storeRewindinfo(busID, stopID);
        int newStopID = busNow.getBusNextLocation();
        stop stopToStore = working_system.stopList.get(newStopID);
        info.setNextStop(newStopID, stopToStore.getWaiting());

        System.out.println("\n------Ongoing Event------");

       busNow.moveBus();

       int nextTime = busNow.getNextLocationTime();
       event nextEvent = new event(nextTime, "move_bus", busID);
       eventList.addToEventList(nextEvent);

        System.out.println("Bus: "+busID);
        System.out.println("Route: "+busNow.getRoute());
        System.out.println("Current stop: "+busNow.getBusLocation()+"----"+working_system.stopList.get(busNow.getBusLocation()).getStopName());
        System.out.println("Next stop: "+busNow.getBusNextLocation()+"----"+working_system.stopList.get(busNow.getBusNextLocation()).getStopName());

    }
}
