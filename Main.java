import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminSignIn.fxml"));
            Parent root = fxmlLoader.load();

            // Access the controller and set the data
            // AdminSignInController controller = fxmlLoader.getController();
            // List<String[]> csvData = readCSV("admin.csv");
            // controller.setData(csvData);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }

}