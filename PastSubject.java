public class PastSubject {
    private int studentId;
    private int trimester;
    private int credits;
    private String course;

    public PastSubject(int studentID, int trimester, int credits, String course) {
        this.studentId = studentID;
        this.trimester = trimester;
        this.credits = credits;
        this.course = course;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        Course c = courseUtil.findCourseByCode(course);
        return c.getName();
    }

    public int getTrimester() {
        return trimester;
    }

    public int getCredits() {
        return credits;
    }

    public String getCourseCode() {
        return course;
    }

    public String toCSVString() {
        return studentId + ",\t" + trimester + ",\t" + credits + ",\t" + course;
    }

}
