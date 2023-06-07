package IPM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import IPM.ResourcesLoader;
import IPM.model.DataStore;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateProductController implements Initializable {
	private Stage stage;
	
	@FXML MFXScrollPane updateScrollPane;

	FlowPane updateProductFlowPane = new FlowPane();

	public UpdateProductController(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void handleCanceldMFXButton() {
		this.stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateProductFlowPane.setMaxHeight(Double.MAX_VALUE);
		updateProductFlowPane.setMaxWidth(Double.MAX_VALUE);
		updateProductFlowPane.setHgap(8);
		updateProductFlowPane.setVgap(8);

		DataStore.getProductList().forEach(productToUpdate -> {
			MFXButton button = new MFXButton(productToUpdate.getName());
			button.setPrefWidth(Region.USE_COMPUTED_SIZE);

			button.setOnAction(event -> {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Update.fxml"));
				UpdateController UpdatetController = new UpdateController(stage, productToUpdate);
				loader.setController(UpdatetController);
				Parent root;
				try {
					root = loader.load();
					Scene scene = new Scene(root);
					MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
					scene.setFill(Color.TRANSPARENT);
					stage.initStyle(StageStyle.TRANSPARENT);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.setScene(scene);

					stage.showAndWait();

					this.stage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			updateProductFlowPane.getChildren().add(button);
		});
		
		updateScrollPane.setContent(updateProductFlowPane);

	}
}
