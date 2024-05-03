import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The `studentUtil` class provides utility methods for handling students.
 * It includes methods for finding students by ID, finding the index of a student in a list,
 * reading students from a file, saving a list of students to a file, and other related functionalities.
 */
public class studentUtil {

    /**
     * The path to the CSV file containing student information.
     */
    public static final String studentFile = "students.csv";

    /**
     * Finds and returns a student object based on the student ID.
     *
     * @param studentId The student ID to search for.
     * @return The Student object with the specified ID, or null if not found.
     */
    public static Student findStudentById(int studentId) {
        List<Student> studentList = readStudentFromFile();
        for (Student s : studentList) {
            if (studentId == s.getId()) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds the index of a student in the given list based on student ID.
     *
     * @param studentID The student ID to search for.
     * @param students  The list of students to search within.
     * @return The index of the student in the list, or -1 if not found.
     */
    public static int indexOf(int studentID, List<Student> students) {
        for (int i = 0; i < students.size(); i++)
            if (studentID == students.get(i).getId())
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Reads student information from the CSV file and returns a list of Student objects.
     *
     * @return A list of Student objects representing the students read from the file.
     */
    public static List<Student> readStudentFromFile() {
        List<Student> students = new ArrayList<>();

        try {
            // read students.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(studentFile));

            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");

                if(items.length == 6){
                    int id = Integer.parseInt(items[0].trim()); // convert String to int
                    String name = items[1].trim();
                    String password = items[2].trim();
                    int trimester = Integer.parseInt(items[3].trim());
                    int currentTrimesterCredits = Integer.parseInt(items[4].trim());
                    int totalCredits = Integer.parseInt(items[5].trim());

                    students.add(new Student(id, name, password, trimester, currentTrimesterCredits, totalCredits));
                } else{
                    System.err.println("Invalid csv file format in line " + (i + 1) + ": " + lines.get(i));
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    /**
     * Saves a list of Student objects to the CSV file.
     *
     * @param students The list of Student objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void saveStudentListToFile(List<Student> students) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < students.size(); i++)
            sb.append(students.get(i).toCSVString() + "\n");
        Files.write(Path.of(studentFile), sb.toString().getBytes());
    }
}
