import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Map;

public class test extends Application {

    // Define VBox and Popup
    private VBox popupContent = new VBox();
    private Label selectedItemsLabel = new Label("Selected Items: ");

    @Override
    public void start(Stage primaryStage) {
        // Create a VBox to hold the checkboxes and the label
        VBox root = new VBox(selectedItemsLabel);

        // Create a popup for the checkbox options
        Popup popup = new Popup();
        popup.getContent().add(popupContent);

        // Define your method
        selectLecturer();

        // Show the popup when the label is clicked
        selectedItemsLabel.setOnMouseClicked(event -> popup.show(primaryStage, event.getScreenX(), event.getScreenY()));

        // Close the popup when focus is lost
        root.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                popup.hide();
            }
        });

        // Set the scene
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi-Select Lecturer Example");
        primaryStage.show();
    }

    public void selectLecturer() {
        // Assume lecturerUtil is an instance of your lecturer utility class
        Map<Integer, String> lecturerMap = lecturerUtil.readLecturerIDandName();
        ObservableList<String> lecturerInfo = FXCollections.observableArrayList();

        // Clear any existing items in the VBox
        popupContent.getChildren().clear();

        // Iterate over the entries of the map
        for (Map.Entry<Integer, String> entry : lecturerMap.entrySet()) {
            int lecturerId = entry.getKey();
            String lecturerName = entry.getValue();
            String info = lecturerId + " - " + lecturerName;
            lecturerInfo.add(info);

            CheckBox checkBox = new CheckBox(info);
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedItemsLabel.setText(selectedItemsLabel.getText() + info + ", ");
                } else {
                    selectedItemsLabel.setText(selectedItemsLabel.getText().replace(info + ", ", ""));
                }
            });
            popupContent.getChildren().add(checkBox);
        }

        // Set the list of lecturer info to your ListView or ComboBox
        // If you want to set it to a ComboBox, uncomment the line below
        // course_lecturer.setItems(lecturerInfo);
    }

    // Main method
    public static void main(String[] args) {
        launch(args);
    }
}
