public class event {
    private int eventID;
    private int logicTime;
    private int ID;
    private String eventType;

    public event(int logic_time, String eventType, int ID) {
        this.logicTime = logic_time;
        this.eventType = eventType;
        this.ID = ID;
    }

    public int getLogicTime() {
        return this.logicTime;
    }
    public String getEventType() {
        return this.eventType;
    }
    public int getID() {
        return this.ID;
    }

}
