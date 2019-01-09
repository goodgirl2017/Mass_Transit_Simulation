import java.util.ArrayList;
import java.util.List;

public class route {
    private int routeID;
    private String routeName;
    private int routeNumber;
    private List<Integer> stopInRoute;

    public int getRouteID() {
        return routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public List<Integer> getStopInRoute() {
        return stopInRoute;
    }



    public route(int ID, int number, String name) {
        this.routeID = ID;
        this.routeName = name;
        this.routeNumber = number;
        this.stopInRoute = new ArrayList<Integer>();
    }

    public void extend_route(int newStop) {
        this.stopInRoute.add(newStop);
    }

    public int nextStop(int curStopIndex) {
        int nextIndex = 0;
        if (curStopIndex < (stopInRoute.size() - 1))
            nextIndex = curStopIndex + 1;
        return stopInRoute.get(nextIndex);
    }

    public int currStop(int curStopIndex) {
        return stopInRoute.get(curStopIndex);
    }

    public int totalStops() {
        return stopInRoute.size();
    }

    public List<Integer> getStopList() {
        return this.stopInRoute;
    }

}
