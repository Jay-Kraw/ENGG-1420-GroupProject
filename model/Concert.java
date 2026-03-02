package model;

public class Concert extends Events{

    int ageRestriction;


    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {


    }
    public Concert(String title, String dateTime, String location, int capacity, boolean status , int ageRestriction){
        super(title, dateTime, location, capacity, status);
        this.ageRestriction=ageRestriction;
    }
}
