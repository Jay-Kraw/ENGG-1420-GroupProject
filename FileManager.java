import java.io.*;
import java.util.*;

public class FileManager{
    //user
    public static void saveUsers(List<User> users, String filename){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){

            writer.println("userId,name,email,userType");

            for (User u : users){
                writer.println(u.getUserId()+","+u.getName()+","+u.getEmail()+","+u.getUserType());
            }
            System.out.println("User save successfully");
        }catch (IOExcwption e){
            System.out.println("Error saving users:" + e.getMessage());
        }
    }
    //events
    public static void saveEvents(List<Event> events, String filename){
        try(PrintWriter writer = new PrintWriter (new FileWriter(filename))){
            writer.println("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestricition");

            for(Event e : events){
                String topic = "";
                String speaker = "";
                String age = "";

                if(e instanceof Workshop){
                    topic = ((Workshop)e).getTopic();
                }
                else if (e instanceof Seminar){
                    speaker = ((Seminar)e).getSpeakerName();
                }
                else if (e instanceof Concert){
                    age = ((Concert)e).getAgeRestriction();
                }
                writer.println(e.getEventId() + "," + e.getTitle() + "," + e.getDateTime() + "," + e.getLocation() + "," + e.getCapacity() + "," + e.getStatus() + "," + e.getEventType() + "," + topic + "," + speaker + "," + age);

            }
            System.out.println("Events saved successfully");
        }catch (IOException e){
            System.out.println("Error saving events: "+e.getMessage());
        }
    }
    //bookings
    public static void saveBookings(List<Booking> bookings, String filename){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            writer.println("bookingId,userId,eventId,createdAt,bookingStatus");
            for(Booking b : bookings){
                writer.println(b.getBookingId() + "," + b.getUserId() + "," + b.getEventId() + "," + b.getCreatedAt() + "," + b.getStatus());
            }
            System.out.println("Bookings saved successfully");
        }catch(IOException e){
            System.out.println("Error saving bookings: "+e.getMessage());
        }
    }
}