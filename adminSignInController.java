import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class adminSignInController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField login_adminID;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showpassword;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private CheckBox showPassword;

    @FXML
    private AnchorPane signin_page;

    public static final String adminFile = "admins.csv";

    public static int indexOf(int adminID, List<Admin> admins) {
        for (int i = 0; i < admins.size(); i++)
            if (adminID == admins.get(i).getId())
                return i; // found at index i
        return -1; // not found
    }

    public static List<Admin> readAdminFromFile() {
        List<Admin> admins = new ArrayList<>();

        try {
            // read admins.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(adminFile));
            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");
                // items[0] is id, items[1] is name
                int id = Integer.parseInt(items[0]); // convert String to int
                admins.add(new Admin(id, items[1], items[2], items[3]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return admins;
    }

    public static void saveAdminListToFile(List<Admin> admins) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < admins.size(); i++)
            sb.append(admins.get(i).toCSVString() + "\n");
        Files.write(Path.of(adminFile), sb.toString().getBytes());
    }

    public void Login() throws IOException {
        alertMessage alert = new alertMessage();

        // check if have empty field
        if (login_adminID.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("All fields are neccessary to be filled.");
        } else {
            // Read admin.csv to check if the ID and password exist
            List<Admin> admins = readAdminFromFile();
            boolean adminExists = false;

            int adminID = Integer.parseInt(login_adminID.getText().trim());

            for (Admin admin : admins) {
                if ((adminID == admin.getId()) && login_password.getText().equals(admin.getPassword())) {
                    adminExists = true;
                    Data.adminID = adminID;
                    Data.adminName = admin.getName();
                    break;
                }
            }

            if (adminExists) {
                alert.successMessage("Login successful!\nWelcome," + Data.adminName);
                // Close the login screen
                Stage loginStage = (Stage) loginBtn.getScene().getWindow();
                loginStage.close();
                // Open admin screen
                Parent root = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Course Management System");
                stage.setScene(new Scene(root));
                stage.show();
                reset();
            } else {
                alert.errorMessage(
                        "Warning: Admin with ID " + Data.adminID + " does not exist or incorrect password.");
            }
        }
    }

    public void reset() {
        login_adminID.setText("");
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
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIN.fxml"));
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
    public void initialize(URL url, ResourceBundle rb) {
        userList();
    }

}
