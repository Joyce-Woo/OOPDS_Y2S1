import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The `pastSubjectUtil` class provides utility methods for handling past subjects.
 * It includes methods for reading past subjects from a file, finding past subjects by student ID and trimester,
 * saving a list of past subjects to a file, and other related functionalities.
 */
public class pastSubjectUtil {

    /**
     * The path to the CSV file containing past subject information.
     */
    public static final String pastSubjectFile = "pastSubject.csv";

    /**
     * Finds the index of a past subject in the given list based on student ID and trimester.
     *
     * @param studentID    The student ID to search for.
     * @param trimester    The trimester to search for.
     * @param pastSubjects The list of past subjects to search within.
     * @return The index of the past subject in the list, or -1 if not found.
     */
    public static int indexOf(int studentID, int trimester, List<PastSubject> pastSubjects) {
        for (int i = 0; i < pastSubjects.size(); i++)
            if (studentID == pastSubjects.get(i).getStudentId() && trimester == pastSubjects.get(i).getTrimester())
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Reads past subject information from the CSV file and returns a list of PastSubject objects.
     *
     * @return A list of PastSubject objects representing the past subjects read from the file.
     */
    public static List<PastSubject> readPastSubjectFromFile() {
        List<PastSubject> pastSubjects = new ArrayList<>();

        try {
            // read pastSubjects.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(pastSubjectFile));

            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");

                if(items.length == 4){
                    int studentId = Integer.parseInt(items[0].trim()); // convert String to int
                    int trimester = Integer.parseInt(items[1].trim());
                    int credits = Integer.parseInt(items[2].trim());
                    String course = items[3].trim();

                    pastSubjects.add(new PastSubject(studentId, trimester, credits, course));
                } else{
                    System.err.println("Invalid csv file format in line " + (i + 1) + ": " + lines.get(i));
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pastSubjects;
    }

    /**
     * Saves a list of PastSubject objects to the CSV file.
     *
     * @param pastSubjects The list of PastSubject objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void savePastSubjectListToFile(List<PastSubject> pastSubjects) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pastSubjects.size(); i++)
            sb.append(pastSubjects.get(i).toCSVString() + "\n");
        Files.write(Path.of(pastSubjectFile), sb.toString().getBytes());
    }
}
