import model.*;

import java.util.*;

public class MainRun {
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        MainRun program = new MainRun();// this allows us to create users or events with a method
    }


    public String[] userBaseInfo() {
        String[] x = new String[3];
        Scanner input = new Scanner(System.in);
        x[0] = String.valueOf(((int) (Math.random() * (999999 - 10000 + 1) + 10000)));
        System.out.println("Enter your Username");
        x[1] = input.nextLine();
        System.out.println("Enter your Email");
        x[2]= input.nextLine();

        return x;
    }

    public Student createStudent(){
        String []x = userBaseInfo();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your StudentId");
        String ID = input.nextLine();
        System.out.println("Enter your Program");
        String program= input.nextLine();
        return new Student(x[0],x[1],x[2],ID,program);

    }

    public Staff createStaff(){
        String []x = userBaseInfo();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your StaffId");
        String ID = input.nextLine();
        System.out.println("Enter your Department");
        String department= input.nextLine();
        return new Staff(x[0],x[1],x[2],ID,department);
    }

    public Guest createGuest(){
        String []x = userBaseInfo();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your GuestCode");
        String ID = input.nextLine();
        System.out.println("Enter your ExpiryDate");
        String expiryDate = input.nextLine();
        return new Guest(x[0],x[1],x[2],ID, expiryDate);
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
        System.out.println("Enter The model.Workshop Topic");
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