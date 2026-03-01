import java.util.*;

public class MainRun {
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        MainRun program = new MainRun();// this allows us to create users or events with a method
        User myNewUser = program.createUser();
        System.out.println("User Created Successfully!");
        System.out.println("ID: " + myNewUser.getEmail());

    }


    public User createUser() {
        Scanner input = new Scanner(System.in);
        String userId = String.valueOf(((int) (Math.random() * (999999 - 10000 + 1) + 10000)));
        System.out.println("Enter your Username");
        String userName = input.nextLine();
        System.out.println("Enter your Email");
        String userEmail = input.nextLine();

        return new User(userId, userName, userEmail, "Guest");
    }

    public Concert createConcert() {
        String[] x = EventBaseInfo();
        System.out.println("Enter The Age Restriction");
        int AgeRestriction = input.nextInt();
        return new Concert(x[0], x[1], x[2], Integer.parseInt(x[3]), true, AgeRestriction);

    }

    public Seminar CreateSeminar() {
        String[] x = EventBaseInfo();
        System.out.println("Enter The SpeakerName");
        String SpeakerName = input.nextLine();
        return new Seminar(x[0], x[1], x[2], Integer.parseInt(x[3]), true, SpeakerName);
    }

    public Workshop CreateWorkshop() {
        String[] x = EventBaseInfo();
        System.out.println("Enter The Workshop Topic");
        String Topic = input.nextLine();
        return new Workshop(x[0], x[1], x[2], Integer.parseInt(x[3]), true, Topic);
    }

    public String[] EventBaseInfo() {
        String[] x = new String[4];
        System.out.println("Enter The Event Title");
        x[0] = input.nextLine();
        System.out.println("Enter The Date and Time");
        x[1] = input.nextLine();
        System.out.println("Enter The Location");
        x[2] = input.nextLine();
        System.out.println("Enter the Capacity");
        x[1] = input.nextLine();
        return x;

    }

}