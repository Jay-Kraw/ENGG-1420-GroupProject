
import java.math.*;

public class Events {
    private int eventId;
    String title;
    String dateTime;
    String location;
    int capacity;
    Boolean status;

    public Events(String title, String dateTime, String location, int capacity, boolean status) {
        eventId = (int) (Math.random() * ((9999 - 1000) + 1) + 1000);
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity=capacity;
        this.status=status;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // capacity
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // status
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
