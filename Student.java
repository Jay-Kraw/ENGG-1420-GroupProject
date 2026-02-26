public class Student extends User {
    private String studentId;
    private String program;

    public Student(String userId, String name, String email, String studentId, String program) {
        super(userId, name, email, "Student");
        this.studentId = studentId;
        this.program = program;
    }

    //Setters
    public void setStudentId() {
        this.studentId = studentId;
    }

    public void setProgram() {
        this.program = program;
    }

    //Getters
    public String getStudentId() {
        return studentId;
    }
    public String getProgram() {
        return program;
    }
}