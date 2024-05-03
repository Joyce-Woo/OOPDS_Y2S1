import java.util.ArrayList;

public class Lecturer extends User {

  private ArrayList<String> courseCodes;

  public Lecturer(int id, String name, String password) {
    super(id, name, password);
    this.courseCodes = new ArrayList<>();
  }

  public ArrayList<String> getCourseCodes() {
    return courseCodes;
  }

  public void addCourseCode(String courseCode) {
    courseCodes.add(courseCode);
  }
}
