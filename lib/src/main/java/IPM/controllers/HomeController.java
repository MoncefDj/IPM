package IPM.controllers;

import java.io.IOException;

import IPM.ResourcesLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController {
	private static StackPane contentPane;

	private Parent productListRoot;

	@FXML
	private StackPane homeImageContainerStackPane;
	@FXML
	private MFXButton getStartedButton;
	@FXML
	private MFXButton importButton;
	@FXML
	private StackPane homeStackPane;

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}

	public void initialize() throws IOException {
		// setting the Home Image.
		Image homeImage = new Image(ResourcesLoader.load("photos/Home.png"));
		ImageView homeImageView = new ImageView(homeImage);
		homeImageView.setFitWidth(320);
		homeImageView.setFitHeight(272);
		homeImageContainerStackPane.getChildren().add(homeImageView);

		FXMLLoader productListLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/ProductList.fxml"));
		productListRoot = productListLoader.load();
	}

	@FXML
	private void handleGetStartedButtonAction(ActionEvent event) throws IOException {
		contentPane.getChildren().clear();
		contentPane.getChildren().add(productListRoot);
	}

	@FXML
	private void handleImportMFXButton() throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Import.fxml"));
		ImportController importController = new ImportController(stage);
		loader.setController(importController);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
	}
}
