import java.util.ArrayList;

public class Student extends User {

  private int currentTrimesterCredits;
  private int totalCredits;
  private int trimester;
  private ArrayList<String> courseCodes;

  public Student(int id, String name, String password, int trimester, int currentTrimesterCredits, int totalCredits) {
    super(id, name, password);
    this.trimester = trimester;
    this.currentTrimesterCredits = currentTrimesterCredits;
    this.totalCredits = totalCredits;
    this.courseCodes = new ArrayList<>();
  }

  public void setTriemster(int trimester) {
    this.trimester = trimester;
  }

  public void setCurrentTriemsterCredits(int currentTrimesterCredits) {
    this.currentTrimesterCredits = currentTrimesterCredits;
  }

  public void setTotalCredits(int totalCredits) {
    this.totalCredits = totalCredits;
  }

  public int getTrimester() {
    return trimester;
  }

  public int getCurrentTrimesterCredits() {
    return currentTrimesterCredits;
  }

  public int getTotalCredits() {
    return totalCredits;
  }

  public ArrayList<String> getCourseCodes() {
    return courseCodes;
  }

  public void addCourseCode(String courseCode) {
    this.courseCodes.add(courseCode);
  }

  public void removeCourseCode(String courseCode) {
    this.courseCodes.remove(courseCode);
  }

  public String toString() {
    return super.toString();
  }

  public String toCSVString() {
    return super.toCSVString() + ",\t" + trimester + ",\t" + currentTrimesterCredits + ",\t" + totalCredits;
  }
}