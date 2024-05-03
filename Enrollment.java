public class Enrollment {
    private int studentId;
    private String courseCode;
    private String lecturer;
    private int credit;

    public Enrollment(int studentId, String courseCode, String lecturer,int credit) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.lecturer = lecturer;
        this.credit = credit;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        Student s = studentUtil.findStudentById(studentId);
        return s.getName();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer){
        this. lecturer = lecturer;
    }

    public int getCredit(){
        return credit;
    }

    public String getCourseName() {
        Course c = courseUtil.findCourseByCode(courseCode);
        return c.getName();
    }


    public String toString() {
        return "Student ID: " + studentId + "\tCourse Code: " + courseCode + "\tLecturer: " + lecturer +"\tCredit: " + credit;
    }

    public String toCSVString() {
        return studentId + ",\t" + courseCode + ",\t" + lecturer+",\t" + credit;
    }
}
