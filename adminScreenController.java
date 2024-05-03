import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class adminScreenController implements Initializable {

    @FXML
    private AnchorPane CourseInfo;

    @FXML
    private AnchorPane LecturerInfo;

    @FXML
    private AnchorPane StudentInfo;

    @FXML
    private AnchorPane SpecificCodeInfo;

    @FXML
    private Label adminID;

    @FXML
    private Label adminName;

    @FXML
    private Button courseListBtn;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TextField course_code;

    @FXML
    private TableColumn<String, Course> course_col_code;

    @FXML
    private TableColumn<String, Course> course_col_name;

    @FXML
    private TableColumn<String, Course> course_col_credithours;

    @FXML
    private TableColumn<String, Course> course_col_lecturer;

    @FXML
    private TableColumn<String, Course> course_col_prerequisite;

    @FXML
    private TextField course_credithours;

    @FXML
    private TextField course_prerequisite;

    @FXML
    private TextField course_name;

    @FXML
    private Button homeBtn;

    @FXML
    private Button lecturerListBtn;

    @FXML
    private TableView<Lecturer> lecturerTable;

    @FXML
    private TableColumn<String, Lecturer> lecturer_col_id;

    @FXML
    private TableColumn<String, Lecturer> lecturer_col_name;

    @FXML
    private TextField lecturer_id;

    @FXML
    private TextField lecturer_name;

    @FXML
    private Button signOutBtn;

    @FXML
    private Button specificCourseBtn;

    @FXML
    private Button studentListBtn;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<String, Student> student_col_id;

    @FXML
    private TableColumn<String, Student> student_col_name;

    @FXML
    private TextField student_id;

    @FXML
    private TextField student_name;

    @FXML
    private Button updateCourseBtn;

    @FXML
    private VBox displayLecturer;

    @FXML
    private Button addCourseBtn;

    @FXML
    private Button assignLecturerBtn;

    @FXML
    private Button searchCourseBtn;

    @FXML
    private TableView<Enrollment> specificCourseTable;

    @FXML
    private ComboBox<String> specificCourse_code;

    @FXML
    private TableColumn<String, Enrollment> specificCourse_col_studentid;

    @FXML
    private TableColumn<String, Enrollment> specificCourse_col_studentname;

    @FXML
    private Label display_code;

    @FXML
    private Label display_lecturer;

    public void displayAdminInfo() {
        adminID.setText(String.valueOf(Data.adminID));
        adminName.setText(Data.adminName);
    }

    // -----------------------------studentlist---------------------------------------
    // add student
    public void studentTable() {
        ObservableList<Student> studentList = FXCollections.observableArrayList(studentUtil.readStudentFromFile());

        student_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        student_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        studentTable.setItems(studentList);
    }

    public void createNewStudentBtn() {
        alertMessage alert = new alertMessage();

        if (student_id.getText().isEmpty()
                || student_name.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields.");
        } else {
            int newId = Integer.parseInt(student_id.getText());
            String newName = student_name.getText();
            // default password: name123(name: no spacing and all lower case)
            String newPassword = newName.replaceAll("\\s+", "").toLowerCase() + "123";
            // default
            int trimester = 1;
            int currentCredits = 0;
            int totalCredits = 0;

            boolean idExist = false;
            List<Student> studentList = studentUtil.readStudentFromFile();
            for (Student s : studentList) {
                // reject id if it already exists
                if (newId == s.getId()) {
                    alert.errorMessage("Cannot add ID " + newId + " because it already exists.");
                    idExist = true;
                }
            }

            // save new student if id does not exist
            if (idExist == false) {
                studentList.add(new Student(newId, newName, newPassword, trimester, currentCredits, totalCredits));
                try {
                    studentUtil.saveStudentListToFile(studentList);
                    alert.successMessage("Student with name: " + newName + ", id: " + newId + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        ClearStudentField();
        StudentTableRefresh();
    }

    public void StudentTableRefresh() {
        ObservableList<Student> studentList = FXCollections.observableArrayList(studentUtil.readStudentFromFile());
        studentTable.getItems().clear(); // remove all
        studentTable.getItems().addAll(studentList);
    }

    public void ClearStudentField() {
        student_id.setText("");
        student_name.setText("");
    }

    // -----------------------------lecturerlist----------------------------------------
    // get data for lecturer list
    public void lecturerTable() {
        ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList(lecturerUtil.readLecturerFromFile());

        lecturer_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        lecturer_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        lecturerTable.setItems(lecturerList);
    }

    public void createNewLecturerBtn() {
        alertMessage alert = new alertMessage();

        if (lecturer_id.getText().isEmpty()
                || lecturer_name.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields.");
        } else {
            int newId = Integer.parseInt(lecturer_id.getText());
            String newName = lecturer_name.getText();
            // default password: name123
            String newPassword = newName.replaceAll("\\s+", "").toLowerCase() + "123";

            boolean idExist = false;
            List<Lecturer> lecturerList = lecturerUtil.readLecturerFromFile();
            for (Lecturer l : lecturerList) {
                // reject id if it already exists
                if (newId == l.getId()) {
                    alert.errorMessage("Cannot add ID " + newId + " because it already exists.");
                    idExist = true;
                }
            }

            // save new Lecturer if id does not exist
            if (idExist == false) {
                lecturerList.add(new Lecturer(newId, newName, newPassword));
                try {
                    lecturerUtil.saveLecturerListToFile(lecturerList);
                    alert.successMessage("Lectuer with name: " + newName + ", id: " + newId + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        ClearLecturerField();
        LecturerTableRefresh();
    }

    public void LecturerTableRefresh() {
        ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList(lecturerUtil.readLecturerFromFile());
        lecturerTable.getItems().clear(); // remove all
        lecturerTable.getItems().addAll(lecturerList);
    }

    public void ClearLecturerField() {
        lecturer_id.setText("");
        lecturer_name.setText("");
    }

    // -----------------------------courseslist + assign course to
    // lecture---------------------------
    // get data for course list
    public void courseTable() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());

        course_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        course_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col_credithours.setCellValueFactory(new PropertyValueFactory<>("credit"));
        course_col_prerequisite.setCellValueFactory(new PropertyValueFactory<>("PR"));
        course_col_lecturer.setCellValueFactory(new PropertyValueFactory<>("LecturerName"));

        courseTable.setItems(courseList);
    }

    public void createNewCourseBtn() {
        alertMessage alert = new alertMessage();

        if (course_code.getText().isEmpty() || course_name.getText().isEmpty()
                || course_credithours.getText().isEmpty()) {
            alert.errorMessage("Please fill course code, course name and credit hours.");
        } else {
            String newCourseCode = course_code.getText();
            String newCourseName = course_name.getText();
            int newCredits = Integer.parseInt(course_credithours.getText());
            String newCoursePrerequisite = (course_prerequisite.getText().isEmpty()) ? "Nil"
                    : course_prerequisite.getText().replaceAll(",", "/");
            String newCourseLecturer = (getSelectedLecturers().isEmpty()) ? "None"
                    : getSelectedLecturers();
            boolean idExist = false;
            List<Course> courseList = courseUtil.readCourseFromFile();
            for (Course c : courseList) {
                // reject course code if it already exists
                if (newCourseCode.equals(c.getCode())) {
                    alert.errorMessage("Cannot add Course Code: " + newCourseCode + " because it already exists.");
                    idExist = true;
                }
            }

            // save new course if id does not exist
            if (idExist == false) {
                courseList.add(
                        new Course(newCourseCode, newCourseName, newCredits, newCourseLecturer, newCoursePrerequisite));
                try {
                    courseUtil.saveCourseListToFile(courseList);
                    alert.successMessage("Course with code: " + newCourseCode + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        updateCourseBtn.setVisible(false);
        ClearCourseField();
        CourseTableRefresh();
    }

    // use to assign course to lecturer
    public void assignNewCourseBtn() {

        alertMessage alert = new alertMessage();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        int num = courseTable.getSelectionModel().getSelectedIndex();

        if (num < 0) {
            alert.errorMessage("Please select the item first.");
            return;
        } else {
            course_code.setText(selectedCourse.getCode());
            course_name.setText(selectedCourse.getName());
            course_credithours.setText(String.valueOf(selectedCourse.getCredit()));
            course_prerequisite.setText(selectedCourse.getPR());

            // make other field non-editable
            course_code.setEditable(false);
            course_name.setEditable(false);
            course_credithours.setEditable(false);
            course_prerequisite.setEditable(false);

            // able to update course after assign lecturer
            assignLecturerBtn.setDisable(true);
            addCourseBtn.setDisable(true);

            updateCourseBtn.setVisible(true);
        }
    }

    public void updateNewCourseBtn() {
        alertMessage alert = new alertMessage();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();

        if (getSelectedLecturers().isEmpty()) {
            alert.errorMessage("Please select lecturer to assign course:" + selectedCourse.getCode());
        } else {
            if (alert.confirmMessage("Are you sure you want to Update Course Code: "
                    + course_code.getText())) {
                // Retrieve new values from input fields
                String newCourseLecturer = (getSelectedLecturers().isEmpty()) ? "None"
                        : getSelectedLecturers();

                // Update the lecturer name of the course
                selectedCourse.setLecturerName(newCourseLecturer);

                List<Course> courseList = courseUtil.readCourseFromFile();
                // Find the index of the selected course in the list
                int index = courseUtil.indexOf(selectedCourse.getCode(), courseList);

                // Save the updated course list back to the file
                if (index != -1) {
                    // Update the course object in the list
                    courseList.set(index, selectedCourse);

                    // Save the updated course list back to the file
                    try {
                        courseUtil.saveCourseListToFile(courseList);
                        alert.successMessage(
                                "Course with code: " + selectedCourse.getCode() + " is successfully updated");
                    } catch (IOException ex) {
                        System.err.println("Error saving to " + ex.getMessage());
                    }
                }

                // update the lecturer to enrollment.csv
                List<Enrollment> enrollments = enrollmentUtil.readEnrollmentFromFile();
                for (Enrollment e : enrollments) {
                    if (e.getCourseCode().equals(selectedCourse.getCode())) {
                        e.setLecturer(newCourseLecturer);
                    }
                }
                try {
                    enrollmentUtil.saveEnrollmentListToFile(enrollments);
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        assignLecturerBtn.setDisable(false);
        addCourseBtn.setDisable(false);

        updateCourseBtn.setVisible(false);

        ClearCourseField();
        CourseTableRefresh();
    }

    // get the selected assign lecturer check box and store into string
    public String getSelectedLecturers() {
        StringBuilder selectedLecturers = new StringBuilder();

        for (Node node : displayLecturer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedLecturers.append(checkBox.getText().split(" - ")[1]).append("/");
                }
            }
        }
        // delete last "/"
        if (selectedLecturers.length() > 0) { // at least one lecturer assigned to a course
            selectedLecturers.deleteCharAt(selectedLecturers.length() - 1);
        }
        return selectedLecturers.toString();
    }

    // display lecturer in check box for assign courses
    public void selectLecturer() {
        Map<Integer, String> lecturerMap = lecturerUtil.readLecturerIDandName();

        // Clear any existing items in the VBox
        displayLecturer.getChildren().clear();

        // Iterate over the entries of the map
        for (Map.Entry<Integer, String> entry : lecturerMap.entrySet()) {
            int lecturerId = entry.getKey();
            String lecturerName = entry.getValue();
            String info = lecturerId + " - " + lecturerName;

            // Create a CheckBox for each lecturer
            CheckBox checkBox = new CheckBox(info);
            checkBox.setId(String.valueOf(lecturerId)); // Set the ID to the lecturer ID
            displayLecturer.getChildren().add(checkBox);
        }
    }

    public void CourseTableRefresh() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());
        courseTable.getItems().clear(); // remove all
        courseTable.getItems().addAll(courseList);
    }

    public void ClearCourseField() {
        course_code.setText("");
        course_name.setText("");
        course_credithours.setText("");
        course_prerequisite.setText("");
        clearLecturerCheckBoxSelection();
    }

    public void clearLecturerCheckBoxSelection() {
        for (Node node : displayLecturer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }

    // ----------------------------------specificCourse---------------------------
    public void specificCourse() {
        String courseCode = specificCourse_code.getSelectionModel().getSelectedItem();

        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        Course selectedCourse = courseUtil.findCourseByCode(courseCode);

        display_code.setText(courseCode);
        display_lecturer.setText(selectedCourse.getLecturerName());

        List<Enrollment> currentEnroll = allCurrentEnroll.stream()
                .filter(enroll -> enroll.getCourseCode().equals(selectedCourse.getCode()))
                .collect(Collectors.toList());

        ObservableList<Enrollment> studentList = FXCollections.observableArrayList(currentEnroll);

        specificCourse_col_studentid.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        specificCourse_col_studentname.setCellValueFactory(new PropertyValueFactory<>("StudentName"));

        specificCourseTable.setItems(studentList);
    }

    public void coursesList() {
        List<Course> courseAvailable = courseUtil.readCourseFromFile();

        List<String> courseCodes = courseAvailable.stream()
                .map(Course::getCode) // extract courseCode
                .collect(Collectors.toList()); // collect the map value into a new list

        ObservableList listData = FXCollections.observableList(courseCodes);
        specificCourse_code.setItems(listData);
    }

    public void clearSpecificCourse(){
        specificCourse_code.getSelectionModel().clearSelection();
        display_code.setText("");
        display_lecturer.setText("");
        specificCourseTable.getItems().clear();;
    }

    // ------------------------switchform-----------------------------------
    public void switchForm(ActionEvent event) {
        // switch to different dashboard
        if (event.getSource() == studentListBtn) {
            StudentInfo.setVisible(true);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(false);
        } else if (event.getSource() == lecturerListBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(true);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(false);
        } else if (event.getSource() == courseListBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(true);
            SpecificCodeInfo.setVisible(false);
        } else if (event.getSource() == specificCourseBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(true);
        }

        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIn.fxml"));
                Stage signInStage = new Stage();
                signInStage.setTitle("Login");
                signInStage.setScene(new Scene(root));
                signInStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayAdminInfo();
        studentTable();
        lecturerTable();
        courseTable();
        selectLecturer();
        coursesList();
        clearSpecificCourse();
    }
}
