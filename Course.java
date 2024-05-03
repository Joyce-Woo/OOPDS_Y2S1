import java.util.ArrayList;

public class Course {
    private int courseCredit;
    private String courseCode;
    private String courseName;
    private String courseLecturer;
    private String coursePR;
    private ArrayList<String> studentIds;

    public Course(String courseCode, String courseName, int courseCredit, String courseLecturer, String coursePR) {
        this.courseCredit = courseCredit;
        this.courseCode = courseCode.trim();
        this.courseName = courseName.trim();
        this.courseLecturer = courseLecturer;
        this.coursePR = coursePR.trim();
        this.studentIds = new ArrayList<>();// use set
    }

    public String getCode() {
        return courseCode;
    }

    public String getName() {
        return courseName;
    }

    public int getCredit() {
        return courseCredit;
    }

    public String getPR() {
        return coursePR.replaceAll("/", ", ");
    }

    public String getLecturerName() {
        return courseLecturer.replaceAll("/", ", ");
    }
    

    public void setLecturerName(String courseLecturer) {
        this.courseLecturer = courseLecturer.trim();
    }

    public void addStudentId(String studentId) {
        this.studentIds.add(studentId);
    }

    public void removeStudentId(String studentId) {
        this.studentIds.remove(studentId);
    }

    public String toString() {
        return courseCode + " \t" + courseName + "\t" + courseCredit + "\t " + courseLecturer + "\t" + coursePR;
    }

    public ArrayList<String> getStudentIds() {
        return studentIds;
    }

    public String toCSVString() {
        return courseCode + ",\t" + courseName + ",\t\t\t" + courseCredit + ",\t " + courseLecturer + ",\t" + coursePR;
    }
}