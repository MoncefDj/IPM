package IPM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import IPM.ResourcesLoader;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController implements Initializable {
	private final Stage stage;
	private double xOffset;
	private double yOffset;

	@FXML
	private HBox windowHeader;

	@FXML
	private MFXFontIcon closeIcon;

	@FXML
	private MFXFontIcon minimizeIcon;

	@FXML
	private MFXFontIcon alwaysOnTopIcon;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane logoContainer;

	@FXML
	private StackPane contentPane;

	public MainController(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
		minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));
		alwaysOnTopIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			boolean newVal = !stage.isAlwaysOnTop();
			alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), newVal);
			stage.setAlwaysOnTop(newVal);
		});

		windowHeader.setOnMousePressed(event -> {
			xOffset = stage.getX() - event.getScreenX();
			yOffset = stage.getY() - event.getScreenY();
		});
		windowHeader.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() + xOffset);
			stage.setY(event.getScreenY() + yOffset);
		});

		// setting the Logo Image.
		Image image = new Image(ResourcesLoader.load("photos/logo.png"), 80, 80, true, true);
		ImageView logo = new ImageView(image);
		logo.setFitHeight(64);
		logo.setFitWidth(64);
		logoContainer.getChildren().add(logo);

		FXMLLoader homeLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Home.fxml"));
		try {
			Parent homeRoot = homeLoader.load();
			contentPane.getChildren().add(homeRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HomeController.setContentPane(contentPane);
		ProductListController.setContentPane(contentPane);
		IncompatibeleiesSteppprController.setContentPane(contentPane);
		GraphVisualizationController.setContentPane(contentPane);
		ImportController.setContentPane(contentPane);
		ExportController.setContentPane(contentPane);

		ProductListController.setStage(stage);
		ImportController.setNotificationStage(stage);
		ExportController.setStage(stage);
	}
}
