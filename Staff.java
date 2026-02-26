public class Staff extends User {
    private String staffId;
    private String department;

    public Staff(String userId, String name, String email, String staffId, String department) {
        super(userId, name, email, "Staff");
        this.staffId = staffId;
        this.department = department;
    }

    //Setters
    public void setStaffId() {
        this.staffId = staffId;
    }

    public void setDepartment() {
        this.department = department;
    }

    //Getters
    public String getStaffId() {
        return staffId;
    }

    public String getDepartment() {
        return department;
    }
}