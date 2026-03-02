import java.time.LocalDateTime;
import java.util.Objects;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final String eventId;
    private final LocalDateTime createdAt;
    private BookingStatus status;


    //constructor
    public Booking(String bookingId, String userId, String eventId){
        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.WAITLISTED;
    }

    //getter
    public String getBookingId(){
        return bookingId;
    }
    public String getUserId(){
        return userId;
    }
    public String getEventId(){
        return eventId;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public BookingStatus getStatus(){
        return status;
    }

    //state chagne
    public void confirm(){
        if (status != BookingStatus.CANCELLED){
            status = BookingStatus.CONFIRMED;
        }
    }
    public void waitlist(){
        if (status != BookingStatus.CANCELLED){
            status = BookingStatus.WAITLISTED;
        }
    }
    public void cancel(){
        status = BookingStatus.CANCELLED;
    }
    public boolean isActive(){
        return status == BookingStatus.CONFIRMED || status == BookingStatus.WAITLISTED;
    }

    //equality
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return bookingId.equals(booking.bookingId);
    }
    @Override
    public int hashCode(){
        return Objects.hash(bookingId);
    }
    @Override
    public String toString(){
        return "Booking{" + "BookingId= " +bookingId + '\'' + ", UserId= " + userId + '\'' + ", EventId= " + eventId + '\''+ ", CreatedAt= "+ createdAt + ", Status= " +status + '}';
    }
}
