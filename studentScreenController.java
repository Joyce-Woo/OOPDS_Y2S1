import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class studentScreenController implements Initializable {
    @FXML
    private AnchorPane AddDropCourse;

    @FXML
    private Button viewSubjectListBtn;

    @FXML
    private Button addDropCourseBtn;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<String, Course> course_col_code;

    @FXML
    private TableColumn<String, Course> course_col_credithours;

    @FXML
    private TableColumn<String, Course> course_col_lecturer;

    @FXML
    private TableColumn<String, Course> course_col_name;

    @FXML
    private TableColumn<String, Course> course_col_prerequisite;

    @FXML
    private Label currentTrimesterCreditHours;

    @FXML
    private TableView<Enrollment> enrollmentTable;

    @FXML
    private TableColumn<String, Enrollment> enroll_col_code;

    @FXML
    private TableColumn<String, Enrollment> enroll_col_credithours;

    @FXML
    private Button homeBtn;

    @FXML
    private Button dropBtn;

    @FXML
    private Button enrollBtn;

    @FXML
    private ComboBox<String> displayDropCourseList;

    @FXML
    private ComboBox<String> displayEnrollCourseList;

    @FXML
    private Button signOutBtn;

    @FXML
    private Label studentID;

    @FXML
    private Label studentName;

    @FXML
    private Label totalCreditHours;

    @FXML
    private Label currentTrimester;

    @FXML
    private Button changeTrimesterBtn;

    @FXML
    private Label trimester1_course;

    @FXML
    private Label trimester2_course;

    @FXML
    private TableView<PastSubject> pastSubjectTable;

    @FXML
    private TableColumn<String, PastSubject> past_col_code;

    @FXML
    private TableColumn<String, PastSubject> past_col_credit;

    @FXML
    private TableColumn<String, PastSubject> past_col_name;

    @FXML
    private TableView<Enrollment> currentSubjectTable;

    @FXML
    private TableColumn<String, Enrollment> current_col_code;

    @FXML
    private TableColumn<String, Enrollment> current_col_credit;

    @FXML
    private TableColumn<String, Enrollment> current_col_name;

    @FXML
    private TableView<Course> futureSubjectTable;

    @FXML
    private TableColumn<String, Course> future_col_code;

    @FXML
    private TableColumn<String, Course> future_col_credit;

    @FXML
    private TableColumn<String, Course> future_col_name;

    @FXML
    private AnchorPane viewSubjectList;

    public void displayStudentInfo() {
        studentID.setText(String.valueOf(Data.studentID));
        studentName.setText(Data.studentName);
    }

    // -------------------------------Add/DropCourse-------------------------------------
    // show table: course available
    public void courseTable() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());

        course_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        course_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col_credithours.setCellValueFactory(new PropertyValueFactory<>("credit"));
        course_col_prerequisite.setCellValueFactory(new PropertyValueFactory<>("PR"));
        course_col_lecturer.setCellValueFactory(new PropertyValueFactory<>("LecturerName"));

        courseTable.setItems(courseList);
    }

    // show table: course already enroll
    public void enrollmentTable() {
        // Retrieve all enrollments from the file
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        // Filter enrollments for the logged-in student
        List<Enrollment> studentEnrollments = allEnrollments.stream()
                .filter(enrollment -> enrollment.getStudentId() == selectedStudent.getId())
                .collect(Collectors.toList());

        ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList(studentEnrollments);

        enroll_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        enroll_col_credithours.setCellValueFactory(new PropertyValueFactory<>("Credit"));

        enrollmentTable.setItems(enrollmentList);
    }

    public void enrollBtn() throws IOException {
        alertMessage alert = new alertMessage();
        String selectedAddCourse = displayEnrollCourseList.getSelectionModel().getSelectedItem();
        Course selectedCourse = courseUtil.findCourseByCode(selectedAddCourse);
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        boolean alreadyEnrolled = false;
        boolean currentTriCreditFull = false;
        boolean prerequisitesMet = false;

        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        List<PastSubject> pastSubjects = pastSubjectUtil.readPastSubjectFromFile();
        int currentCredits = selectedStudent.getCurrentTrimesterCredits();
        int afterAddCurrentCredits = currentCredits + selectedCourse.getCredit();
        int totalCredits = selectedStudent.getTotalCredits();
        int afterAddTotalCredits = totalCredits + selectedCourse.getCredit();

        // check if all blank field are selected
        if (displayEnrollCourseList.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please select a course to enroll. ");
        }

        // check if acheived 30 credits for whole program
        if (afterAddTotalCredits > 30) {
            alert.errorMessage("Cannot enroll. Congratulations, you have already completed this program!");
            return;
        }

        // a. check if the course is already added by the student
        // (check previous triemster)
        int currentTrimester = selectedStudent.getTrimester();
        // trimester 1
        if (currentTrimester == 1) {
            for (Enrollment enrollment : allEnrollments) {
                if (enrollment.getStudentId() == selectedStudent.getId()
                        && enrollment.getCourseCode().equals(selectedAddCourse)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
        } else {
            // check the past trimester's enrollments
            for (PastSubject p : pastSubjects) {
                if (p.getStudentId() == selectedStudent.getId() && p.getCourseCode().equals(selectedAddCourse)) {
                    // trimester 2
                    if (currentTrimester == 2 && p.getTrimester() == 1) {
                        alreadyEnrolled = true;
                        break;
                        // trimester 3
                    } else if (currentTrimester == 3 && (p.getTrimester() == 1 || p.getTrimester() == 2)) {
                        alreadyEnrolled = true;
                        break;
                    }
                }
            }
            // check the current trimester's enrollments
            for (Enrollment enrollment : allEnrollments) {
                if (enrollment.getStudentId() == selectedStudent.getId()
                        && enrollment.getCourseCode().equals(selectedAddCourse)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
        }

        // b. check if credits exceed limit for current trimester
        if (afterAddCurrentCredits > 12) {
            currentTriCreditFull = true;
        }

        // add enrolled course in arraylist
        for (Enrollment enroll : allEnrollments) {
            if (selectedStudent.getId() == enroll.getStudentId())
                selectedStudent.addCourseCode(enroll.getCourseCode());
        }

        // c. check prerequisites
        String[] prerequisiteCourses = selectedCourse.getPR().split(", ");
        if (currentTrimester == 1) {
            // for tri 1, all course which have prerequisites cannot be enroll
            prerequisitesMet = "Nil".equals(prerequisiteCourses[0]);
        } else { // for tri 2 and 3
            Student s = studentUtil.findStudentById(Data.studentID);
            int total = s.getTotalCredits();
            if (!"Nil".equals(prerequisiteCourses[0])) {
                boolean allPrerequisitesCompleted = Arrays.stream(prerequisiteCourses)
                        .allMatch(course -> {
                            if (course.startsWith("completed at least ")) {
                                int requiredCredits = Integer.parseInt(course.split(" ")[3]);
                                return total >= requiredCredits;
                            }
                            // check if prerequisites in past enrollments
                            List<String> pastEnroll = new ArrayList<>();
                            for (PastSubject p : pastSubjects) {
                                if (p.getStudentId() == selectedStudent.getId()) {
                                    pastEnroll.add(p.getCourseCode());
                                }
                            }
                            return pastEnroll.contains(course);

                        });

                if (allPrerequisitesCompleted) {
                    prerequisitesMet = true;
                }
            } else {
                prerequisitesMet = true;
            }
        }

        if (alreadyEnrolled) {
            alert.errorMessage("You are already enrolled in this course.");
        } else if (currentTriCreditFull) {
            alert.errorMessage(
                    "Cannot enroll, reach maximun credit hours. To enroll for next trimester, please click on Change Trimester.");
        } else if (!prerequisitesMet) {
            alert.errorMessage("Cannot enroll. Prerequisites for this course are not met.");
        } else {
            // save current trimester credit hours
            selectedStudent.setCurrentTriemsterCredits(afterAddCurrentCredits);
            // save total credit hours
            selectedStudent.setTotalCredits(afterAddTotalCredits);
            // update credit hours into student csv file
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

            // Enroll the student in the course
            Enrollment newEnrollment = new Enrollment(selectedStudent.getId(), selectedAddCourse,
                    selectedCourse.getLecturerName(),
                    selectedCourse.getCredit());
            // Add the new enrollment to the list
            allEnrollments.add(newEnrollment);
            // Save the updated list to file
            enrollmentUtil.saveEnrollmentListToFile(allEnrollments);
            alert.successMessage("Enrollment successful!");
        }
        EnrollmentTableRefresh();
        courseList();
        ClearField();
        creditInfo();
    }

    public void dropBtn() throws IOException {
        alertMessage alert = new alertMessage();
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        String selectedDropCourse = displayDropCourseList.getSelectionModel().getSelectedItem();

        Course selectedCourse = courseUtil.findCourseByCode(selectedDropCourse);
        int currentCredits = selectedStudent.getCurrentTrimesterCredits();
        int afterDropCurrentCredits = currentCredits - selectedCourse.getCredit();
        int totalCredits = selectedStudent.getTotalCredits();
        int afterDropTotalCredits = totalCredits - selectedCourse.getCredit();
        

        if (displayDropCourseList.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please select a course to drop. ");
        }

        Enrollment enrollmentToRemove = null;
        for (Enrollment e : allEnrollments) {
            if (e.getStudentId() == selectedStudent.getId() && e.getCourseCode().equals(selectedDropCourse)) {
                enrollmentToRemove = e;
                break;
            }
        }
        // Remove the course from the student
        selectedStudent.removeCourseCode(selectedDropCourse);

        // Remove the enrollment
        allEnrollments.remove(enrollmentToRemove);
        enrollmentUtil.saveEnrollmentListToFile(allEnrollments);
        alert.successMessage("Successfully drop course " + selectedDropCourse);

        // change student credit hours
        selectedStudent.setCurrentTriemsterCredits(afterDropCurrentCredits);
        selectedStudent.setTotalCredits(afterDropTotalCredits);
        List<Student> studentList = studentUtil.readStudentFromFile();
        int index = studentUtil.indexOf(selectedStudent.getId(), studentList);
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

        EnrollmentTableRefresh();
        ClearField();
        creditInfo();
    }

    public void EnrollmentTableRefresh() {
        int studentID = Data.studentID;

        // Retrieve all enrollments from the file
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();

        // Filter enrollments for the logged-in student
        List<Enrollment> studentEnrollments = allEnrollments.stream()
                .filter(enrollment -> enrollment.getStudentId() == studentID)
                .collect(Collectors.toList());

        ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList(studentEnrollments);

        enrollmentTable.getItems().clear(); // remove all
        enrollmentTable.getItems().addAll(enrollmentList);
    }

    public void ClearField() {
        totalCreditHours.setText("");
        currentTrimesterCreditHours.setText("");
        displayEnrollCourseList.getSelectionModel().clearSelection();
        displayDropCourseList.getSelectionModel().clearSelection();
    }

    // retrieve data of credit hours
    public void creditInfo() {
        List<Student> studentList = studentUtil.readStudentFromFile();
        for (Student s : studentList) {
            if (Data.studentID == s.getId()) {
                totalCreditHours.setText(String.valueOf(s.getTotalCredits()) + "/30");
                currentTrimesterCreditHours.setText(String.valueOf(s.getCurrentTrimesterCredits()));
                currentTrimester.setText(String.valueOf(s.getTrimester()));
            }
        }
    }

    // retrieve data of course combo box
    public void courseList() {
        // enroll combo box
        List<Course> courseAvailable = courseUtil.readCourseFromFile();

        List<String> courseCodes = courseAvailable.stream()
                .map(Course::getCode) // extract courseCode
                .collect(Collectors.toList()); // collect the map value into a new list

        ObservableList enrollList = FXCollections.observableList(courseCodes);
        displayEnrollCourseList.setItems(enrollList);

        // drop combo box
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        List<String> courseEnoll = new ArrayList<>();

        for (Enrollment e : allEnrollments) {
            if (e.getStudentId() == selectedStudent.getId()) {
                courseEnoll.add(e.getCourseCode());
            }
        }
        ObservableList dropList = FXCollections.observableList(courseEnoll);
        displayDropCourseList.setItems(dropList);
    }

    public void showPastEnrollCourse() {
        List<PastSubject> pastSubjects = pastSubjectUtil.readPastSubjectFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        String tri1 = "";
        String tri2 = "";

        for (PastSubject p : pastSubjects) {
            if (p.getStudentId() == selectedStudent.getId()) {
                if (selectedStudent.getTrimester() == 2 && p.getTrimester() == 1) {
                    // Trimester 1 courses for trimester 2 students
                    tri1 += p.getCourseCode() + ", ";
                } else if (selectedStudent.getTrimester() == 3) {
                    // Trimester 1 and 2 courses for trimester 3 students
                    if (p.getTrimester() == 1) {
                        tri1 += p.getCourseCode() + ", ";
                    }
                    if (p.getTrimester() == 2) {
                        tri2 += p.getCourseCode() + ", ";
                    }
                }
            }
        }
        // Set the text for trimester 1 courses
        if (!tri1.isEmpty()) {
            trimester1_course.setVisible(true);
            trimester1_course.setText("Trimester 1: " + tri1.substring(0, tri1.length() - 2));
            // Remove the last comma and space

        }
        // Set the text for trimester 2 courses
        if (!tri2.isEmpty()) {
            trimester2_course.setVisible(true);
            trimester2_course.setText("Trimester 2: " + tri2.substring(0, tri2.length() - 2));
        }
    }

    // ---------------------------------ViewPast,CurrentFutureSubject----------------------
    // past subject
    public void pastSubjectTable() {

        List<PastSubject> allPastEnroll = pastSubjectUtil.readPastSubjectFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        List<PastSubject> pastEnroll = allPastEnroll.stream()
                .filter(enroll -> enroll.getStudentId() == selectedStudent.getId())
                .collect(Collectors.toList());

        ObservableList<PastSubject> pastSubjectList = FXCollections.observableArrayList(pastEnroll);

        past_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        past_col_name.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
        past_col_credit.setCellValueFactory(new PropertyValueFactory<>("credits"));

        pastSubjectTable.setItems(pastSubjectList);
    }

    // current subject
    public void currentSubjectTable() {

        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        List<Enrollment> currentEnroll = allCurrentEnroll.stream()
                .filter(enroll -> enroll.getStudentId() == selectedStudent.getId())
                .collect(Collectors.toList());

        ObservableList<Enrollment> currentSubjectList = FXCollections.observableArrayList(currentEnroll);

        current_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        current_col_name.setCellValueFactory(new PropertyValueFactory<>("Credit"));
        current_col_credit.setCellValueFactory(new PropertyValueFactory<>("CourseName"));

        currentSubjectTable.setItems(currentSubjectList);
    }

    // future subject
    public void futureSubjectTable() {

        // Step 1: Retrieve the list of all courses from courseTable
        ObservableList<Course> allCourses = courseTable.getItems();

        // Step 2: Retrieve the list of courses from pastSubjectTable and
        // currentSubjectTable
        List<String> pastEnrolledCourses = pastSubjectTable.getItems().stream()
                .map(PastSubject::getCourseCode)
                .collect(Collectors.toList());

        List<String> currentEnrolledCourses = currentSubjectTable.getItems().stream()
                .map(Enrollment::getCourseCode)
                .collect(Collectors.toList());

        // Combine past and current enrolled courses
        Set<String> enrolledCourses = new HashSet<>(pastEnrolledCourses);
        enrolledCourses.addAll(currentEnrolledCourses);

        // Step 3: Filter out the courses that are not present in the past or current
        // enrollments
        List<Course> futureCourses = allCourses.filtered(course -> !enrolledCourses.contains(course.getCode()));

        // Step 4: Populate the table with the filtered list of courses
        ObservableList<Course> futureSubjectList = FXCollections.observableArrayList(futureCourses);

        future_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        future_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        future_col_credit.setCellValueFactory(new PropertyValueFactory<>("credit"));

        futureSubjectTable.setItems(futureSubjectList);
    }

    public void switchForm(ActionEvent event) {
        // switch to different dashboard
        if (event.getSource() == addDropCourseBtn) {
            AddDropCourse.setVisible(true);
            viewSubjectList.setVisible(false);
        } else if (event.getSource() == viewSubjectListBtn) {
            AddDropCourse.setVisible(false);
            viewSubjectList.setVisible(true);
        }

        if (event.getSource() == changeTrimesterBtn) {
            try {
                Parent dialogLayout = FXMLLoader.load(getClass().getResource("studentChangeTrimester.fxml"));
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setTitle("Select Trimester");
                dialogStage.setScene(new Scene(dialogLayout));
                dialogStage.showAndWait(); // Show the dialog and wait for it to be closed

                showPastEnrollCourse();
                EnrollmentTableRefresh();
                creditInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(event.getSource() == viewSubjectListBtn){
            pastSubjectTable();
            currentSubjectTable();
            futureSubjectTable();
        }

        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("studentSignIn.fxml"));
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
        // add course
        displayStudentInfo();

        courseTable();
        enrollmentTable();
        showPastEnrollCourse();

        courseList();
        creditInfo();

        // view subject list
        pastSubjectTable();
        currentSubjectTable();
        futureSubjectTable();
    }
}
