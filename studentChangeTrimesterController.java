import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class studentChangeTrimesterController implements Initializable {
    @FXML
    private Button changeBtn;

    @FXML
    private ComboBox<Integer> changeTrimester;

    @FXML
    private Label currentTrimester;


    public void change() throws IOException {
        alertMessage alert = new alertMessage();

        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        int currentTri = selectedStudent.getTrimester();
        int selectedTri = changeTrimester.getSelectionModel().getSelectedItem();
        if (selectedTri == currentTri + 1) {
            // check if achieved min credits hours for current trimester
            boolean acheived = false;
            int currentCredits = selectedStudent.getCurrentTrimesterCredits();
            if (currentCredits >= 3) {
                acheived = true;
            }

            if (acheived) {
                // Remove enrolled courses from enrollment.csv and add to pastSubject.csv
                List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
                Iterator<Enrollment> iterator = allEnrollments.iterator();
                List<PastSubject> pastSubjects = pastSubjectUtil.readPastSubjectFromFile();
                while (iterator.hasNext()) {
                    Enrollment enrollment = iterator.next();
                    if (selectedStudent.getId() == enrollment.getStudentId()) {
                        // Convert Enrollment to PastSubject
                        PastSubject pastSubject = new PastSubject(enrollment.getStudentId(),
                                selectedStudent.getTrimester(), enrollment.getCredit() , enrollment.getCourseCode());
                        pastSubjects.add(pastSubject); // Add to past subjects list
                        iterator.remove(); // Remove from enrollment.csv
                    }
                }
                // Save to pastSubject.csv
                pastSubjectUtil.savePastSubjectListToFile(pastSubjects);

                // Update enrollment.csv
                enrollmentUtil.saveEnrollmentListToFile(allEnrollments);
                
                selectedStudent.setTriemster(selectedTri);
                selectedStudent.setCurrentTriemsterCredits(0);

                // update trimester into student csv file
                List<Student> studentList = studentUtil.readStudentFromFile();
                // Find the index of the selected student in the list
                int index = studentUtil.indexOf(selectedStudent.getId(), studentList);

                // Save the updated student list back to the file
                if (index != -1) {
                    // Update the course object in the list
                    studentList.set(index, selectedStudent);

                    // Save the updated student list back to the file
                    try {
                        studentUtil.saveStudentListToFile(studentList);
                    } catch (IOException ex) {
                        System.err.println("Error saving to " + ex.getMessage());
                    }
                }

                alert.successMessage("Successsfully change to trimester " + selectedTri);

            } else {
                alert.errorMessage(
                        "Cannot enroll course for next trimester. Insufficient credits for current trimester "
                                + currentTri + ". Must have at least 3 credits.");
            }
        } else if (selectedTri == currentTri) {
            alert.errorMessage("Cannot change. You are already in selected trimester " + selectedTri + " right now.");
        } else if (selectedTri - currentTri == 2) {
            alert.errorMessage("Cannot change. Please completed enroll for trimester " + (currentTri + 1)
                    + " before move on to trimester " + selectedTri);
        } else if (selectedTri < currentTri) {
            alert.errorMessage("Cannot change. You already completed enrollment for previous trimester.");
        }
        // Close the window
        Stage stage = (Stage) changeBtn.getScene().getWindow();
        stage.close();
    }

    // retrieve data of trimester combo box
    public void trimesterList() {
        List<Integer> list = new ArrayList<>();

        for (int data : Data.trimester) {
            list.add(data);
        }
        ObservableList listData = FXCollections.observableList(list);
        changeTrimester.setItems(listData);
    }

    public void displayCurrentTrimester() {
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        currentTrimester.setText(String.valueOf(selectedStudent.getTrimester()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trimesterList();
        displayCurrentTrimester();
    }
}
