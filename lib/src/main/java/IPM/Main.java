package IPM;

import IPM.controllers.MainController;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * This class is the entry point for the application.
 */
public class Main extends Application {

    /**
     * Starts the application by loading the main FXML file and setting up the primary stage.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main FXML file and set the controller factory
            FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Main.fxml"));
            loader.setControllerFactory(c -> new MainController(primaryStage));
            Parent root = loader.load();

            // Create the scene and set the theme, size, and transparency
            Scene scene = new Scene(root, 1260, 630);
            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
            scene.setFill(Color.TRANSPARENT);

            // Set the stage style, scene, and show the stage
            Image icon = new Image(ResourcesLoader.load("photos/icon.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method that launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
