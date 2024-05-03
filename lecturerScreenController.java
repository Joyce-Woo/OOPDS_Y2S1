import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;

public class lecturerScreenController implements Initializable {
    @FXML
    private AnchorPane ViewStudent;

    @FXML
    private ComboBox<String> selecedCourse;

    @FXML
    private Label display_course;

    @FXML
    private Label lecturerID;

    @FXML
    private Label lecturerName;

    @FXML
    private Button searchCourseBtn;

    @FXML
    private Button signOutBtn;

    @FXML
    private TableView<Enrollment> studentTable;

    @FXML
    private TableColumn<String, Enrollment> student_col_id;

    @FXML
    private TableColumn<String, Enrollment> student_col_name;

    @FXML
    private Button viewStudentBtn;


    public void displayLecturerInfo() {
        lecturerID.setText(String.valueOf(Data.lecturerID));
        lecturerName.setText(Data.lecturerName);
    }

    // search student for specific course
    public void search() {

        String courseCode = selecedCourse.getSelectionModel().getSelectedItem();

        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        Course selectedCourse = courseUtil.findCourseByCode(courseCode);

        display_course.setText(courseCode);

        List<Enrollment> currentEnroll = allCurrentEnroll.stream()
                .filter(enroll -> enroll.getCourseCode().equals(selectedCourse.getCode()))
                .collect(Collectors.toList());

        ObservableList<Enrollment> studentList = FXCollections.observableArrayList(currentEnroll);

        student_col_id.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        student_col_name.setCellValueFactory(new PropertyValueFactory<>("StudentName"));

        studentTable.setItems(studentList);
    }

    public void coursesList() {
        List<Course> courseAvailable = courseUtil.readCourseFromFile();
        String lecturerName = Data.lecturerName;

        List<String> courses = courseAvailable.stream()
                .filter(course -> course.getLecturerName().equals(lecturerName)) 
                .map(Course::getCode) // transform each Course object into its name
                .collect(Collectors.toList()); // collect the map value into a new list

        ObservableList listData = FXCollections.observableList(courses);
        selecedCourse.setItems(listData);
    }

    public void refreash(){
        selecedCourse.getSelectionModel().clearSelection();
        display_course.setText("");
        studentTable.getItems().clear();;
    }


    public void switchForm(ActionEvent event) {
        if (event.getSource() == viewStudentBtn) {
            ViewStudent.setVisible(true);
        }
        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("lecturerSignIn.fxml"));
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
        displayLecturerInfo();
        coursesList();
        refreash();
    }
}
