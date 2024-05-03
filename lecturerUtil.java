import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `lecturerUtil` class provides utility methods for handling lecturers.
 * It includes methods for reading lecturers from a file, finding lecturers by name,
 * saving a list of lecturers to a file, and other related functionalities.
 */
public class lecturerUtil {

    /**
     * The path to the CSV file containing lecturer information.
     */
    public static final String lecturerFile = "lecturers.csv";

    /**
     * Finds the index of a lecturer in the given list based on its lecturer ID.
     *
     * @param lecturerID The lecturer ID to search for.
     * @param lecturers  The list of lecturers to search within.
     * @return The index of the lecturer in the list, or -1 if not found.
     */
    public static int indexOf(int lecturerID, List<Lecturer> lecturers) {
        for (int i = 0; i < lecturers.size(); i++)
            if (lecturerID == lecturers.get(i).getId())
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Finds and returns a lecturer object based on its name.
     *
     * @param lecturerName The name of the lecturer to search for.
     * @return The Lecturer object with the specified name, or null if not found.
     */
    public static Lecturer findLecturerByName(String lecturerName) {
        List<Lecturer> lecturerList = readLecturerFromFile();
        for (Lecturer l : lecturerList) {
            if (lecturerName.equals(l.getName())) {
                return l;
            }
        }
        return null;
    }

    /**
     * Reads lecturer information from the CSV file and returns a list of Lecturer objects.
     *
     * @return A list of Lecturer objects representing the lecturers read from the file.
     */
    public static List<Lecturer> readLecturerFromFile() {
        List<Lecturer> lecturers = new ArrayList<>();

        try {
            // read lecturers.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(lecturerFile));
            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");
                int id = Integer.parseInt(items[0].trim()); // convert String to int
                String name = items[1].trim();
                String password = items[2].trim();
                lecturers.add(new Lecturer(id, name, password));

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lecturers;
    }

    /**
     * Reads lecturer IDs and names from the CSV file and returns a map.
     *
     * @return A map where keys are lecturer IDs and values are lecturer names.
     */
    // New method to read lecturer names and corresponding Lecturer objects
    public static Map<Integer, String> readLecturerIDandName() {
        Map<Integer, String> lecturerMap = new HashMap<>();
        List<Lecturer> lecturers = readLecturerFromFile();
        for (Lecturer lecturer : lecturers) {
            lecturerMap.put(lecturer.getId(), lecturer.getName());
        }
        return lecturerMap;
    }

    /**
     * Saves a list of Lecturer objects to the CSV file.
     *
     * @param lecturers The list of Lecturer objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void saveLecturerListToFile(List<Lecturer> lecturers) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lecturers.size(); i++)
            sb.append(lecturers.get(i).toCSVString() + "\n");
        Files.write(Path.of(lecturerFile), sb.toString().getBytes());
    }
}
