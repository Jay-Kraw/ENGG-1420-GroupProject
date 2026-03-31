package Test;

import model.Booking;
import model.BookingStatus;
import model.Events;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookingManager;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest {

    private BookingManager bm;
    private User testUser;
    private Events testEvent;

    @BeforeEach
    void setUp() {
        bm = new BookingManager();
        testUser = new User("U1", "Test User", "test@example.com", "Student");
        testEvent = new Events("E1", "Test Event", "2023-12-01", "Lab 1", 1, true);
    }

    @Test
    void testBookingConfirmed() {
        Booking b = bm.createBooking("B1", testUser, testEvent);
        assertNotNull(b);
        assertEquals(BookingStatus.CONFIRMED, b.getStatus());
        System.out.println("Test 1 passed");
    }

    @Test
    void testBookingWaitlisted() {
        bm.createBooking("B1", new User("U2", "Other", "o@e.com", "Staff"), testEvent);
        Booking b2 = bm.createBooking("B2", testUser, testEvent);
        assertNotNull(b2);
        assertEquals(BookingStatus.WAITLISTED, b2.getStatus());
        System.out.println("Test 2 passed");
    }

    @Test
    void testPromotionAfterCancel() {
        Booking b1 = bm.createBooking("B1", new User("UA", "User A", "a@e.com", "Staff"), testEvent);
        Booking b2 = bm.createBooking("B2", new User("UB", "User B", "b@e.com", "Staff"), testEvent);
        bm.cancelBooking(b1);
        assertEquals(BookingStatus.CONFIRMED, b2.getStatus());
        System.out.println("Test 3 passed");
    }

    @Test
    void testDuplicateBooking() {
        bm.createBooking("B1", testUser, testEvent);
        Booking b2 = bm.createBooking("B2", testUser, testEvent);
        assertNull(b2);
        System.out.println("Test 4 passed");
    }
}