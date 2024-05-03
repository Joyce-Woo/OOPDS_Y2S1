import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The `courseUtil` class provides utility methods for handling courses.
 * It includes methods for reading courses from a file, finding courses by code,
 * saving a list of courses to a file, and other related functionalities.
 */
public class courseUtil {

    /**
     * The path to the CSV file containing course information.
     */
    public static final String courseFile = "courses.csv";

    /**
     * Finds the index of a course in the given list based on its course code.
     *
     * @param courseCode The course code to search for.
     * @param courses    The list of courses to search within.
     * @return The index of the course in the list, or -1 if not found.
     */
    public static int indexOf(String courseCode, List<Course> courses) {
        for (int i = 0; i < courses.size(); i++)
            if (courseCode.equals(courses.get(i).getCode()))
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Finds and returns a course object based on its course code.
     *
     * @param courseCode The course code to search for.
     * @return The Course object with the specified course code, or null if not found.
     */
    public static Course findCourseByCode(String courseCode) {
        List<Course> courseList = readCourseFromFile();
        for (Course c : courseList) {
            if (courseCode.equals(c.getCode())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Reads course information from the CSV file and returns a list of Course objects.
     *
     * @return A list of Course objects representing the courses read from the file.
     */
    public static List<Course> readCourseFromFile() {
        List<Course> courses = new ArrayList<>();
        try {
            // read courses.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(courseFile));
            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");

                String courseCode = items[0].trim(); 
                String courseName = items[1].trim();
                int credits = Integer.parseInt(items[2].trim());
                String lecturer = items[3].trim();
                String coursePR = items[4].trim();
                courses.add(new Course(courseCode, courseName, credits, lecturer, coursePR));

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    /**
     * Saves a list of Course objects to the CSV file.
     *
     * @param courses The list of Course objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void saveCourseListToFile(List<Course> courses) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < courses.size(); i++)
            sb.append(courses.get(i).toCSVString() + "\n");
        Files.write(Path.of(courseFile), sb.toString().getBytes());
    }
}
