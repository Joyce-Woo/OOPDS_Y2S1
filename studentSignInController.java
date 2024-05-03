import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class studentSignInController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showpassword;

    @FXML
    private TextField login_studentID;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private CheckBox showPassword;

    @FXML
    private AnchorPane signin_page;


    public void Login(ActionEvent event) throws IOException {
        alertMessage alert = new alertMessage();

        // check if have empty field
        if (login_studentID.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("All fields are neccessary to be filled.");
        } else {
            // Read admin.csv to check if the ID and password exist
            List<Student> students = studentUtil.readStudentFromFile();
            boolean studentExists = false;

            int studentID = Integer.parseInt(login_studentID.getText().trim());

            for (Student student : students) {
                if ((studentID == student.getId()) && login_password.getText().equals(student.getPassword())) {
                    studentExists = true;
                    Data.studentID = studentID;
                    Data.studentName = student.getName();
                    break;
                }
            }

            if (studentExists) {
                alert.successMessage("Login successful!\nWelcome," + Data.studentName);
                // Close the login screen
                Stage loginStage = (Stage) loginBtn.getScene().getWindow();
                loginStage.close();
                // Open student screen
                Parent root = FXMLLoader.load(getClass().getResource("studentScreen.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Course Management System");
                stage.setScene(new Scene(root));
                stage.show();
                reset();
            } else {
                alert.errorMessage(
                        "Warning: Student with ID " + Data.studentID + " does not exist or incorrect password.");
            }
        }
    }

    public void reset() {
        login_studentID.setText("");
        login_password.setText("");
        login_showpassword.setText("");
        showPassword.setSelected(false);
        login_showpassword.setVisible(false);
        login_password.setVisible(true);
        login_user.getSelectionModel().clearSelection();
    }


    public void showPassword() {
        if (showPassword.isSelected()) {
            login_showpassword.setText(login_password.getText());
            login_showpassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showpassword.getText());
            login_showpassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void userList() {
        List<String> list = new ArrayList<>();

        for (String data : Data.user) {
            list.add(data);
        }
        ObservableList listData = FXCollections.observableList(list);
        login_user.setItems(listData);
    }

    public void switchPage() {

        if (login_user.getSelectionModel().getSelectedItem() == "Student") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("studentSignIn.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (login_user.getSelectionModel().getSelectedItem() == "Admin") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIn.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (login_user.getSelectionModel().getSelectedItem() == "Lecturer") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("lecturerSignIn.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        login_user.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList();
    }
}
