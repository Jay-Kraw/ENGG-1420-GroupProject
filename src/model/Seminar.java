package model;

public class Seminar extends Events {


    String speakerName;


    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public Seminar(String eventid,String title, String dateTime, String location, int capacity, boolean status, String speakerName) {
        super(eventid,title, dateTime, location, capacity, status);
        this.speakerName = speakerName;

    }
}




