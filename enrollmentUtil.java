import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The `enrollmentUtil` class provides utility methods for handling enrollments.
 * It includes methods for finding the index of an enrollment in a list based on course code,
 * reading enrollments from a file, saving a list of enrollments to a file, and other related functionalities.
 */
public class enrollmentUtil {

    /**
     * The path to the CSV file containing enrollment information.
     */
    public static final String enrollmentFile = "enrollments.csv";

    /**
     * Finds the index of an enrollment in the given list based on course code.
     *
     * @param courseCode The course code to search for.
     * @param enrollments The list of enrollments to search within.
     * @return The index of the enrollment in the list, or -1 if not found.
     */
    public static int indexOf(String courseCode, List<Enrollment> enrollments) {
        for (int i = 0; i < enrollments.size(); i++)
            if (courseCode.equals(enrollments.get(i).getCourseCode()))
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Reads enrollment information from the CSV file and returns a list of Enrollment objects.
     *
     * @return A list of Enrollment objects representing the enrollments read from the file.
     */
    public static List<Enrollment> readEnrollmentFromFile() {
        List<Enrollment> enrollments = new ArrayList<>();

        try {
            // read enrollments.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(enrollmentFile));
            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");
                int studentID = Integer.parseInt(items[0].trim()); // convert String to int
                String courseCode = items[1].trim();
                String lecturer = items[2].trim();
                int credit = Integer.parseInt(items[3].trim()); 
        
                enrollments.add(new Enrollment(studentID, courseCode, lecturer, credit));
            }
        } catch (

        IOException ex) {
            ex.printStackTrace();
        }
        return enrollments;
    }

    /**
     * Saves a list of Enrollment objects to the CSV file.
     *
     * @param enrollments The list of Enrollment objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void saveEnrollmentListToFile(List<Enrollment> enrollments) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < enrollments.size(); i++)
            sb.append(enrollments.get(i).toCSVString() + "\n");
        Files.write(Path.of(enrollmentFile), sb.toString().getBytes());
    }
}
