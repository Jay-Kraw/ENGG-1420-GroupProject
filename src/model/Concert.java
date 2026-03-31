package model;

public class Concert extends Events{

    String ageRestriction;


    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {


    }
    public Concert(String eventid,String title, String dateTime, String location, int capacity, boolean status , String ageRestriction){
        super(eventid,title, dateTime, location, capacity, status);
        this.ageRestriction=ageRestriction;
    }
}
