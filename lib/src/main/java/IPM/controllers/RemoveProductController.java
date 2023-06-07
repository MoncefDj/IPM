package IPM.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import IPM.model.DataStore;
import IPM.model.Product;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class RemoveProductController implements Initializable {
	private Stage stage;
	private ArrayList<Product> selectedProducts = new ArrayList<Product>();
	
	@FXML
	MFXScrollPane removeProductScrollPane;
	
	FlowPane removeProductFlowPane = new FlowPane();

	public RemoveProductController(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleRemoveMFXButton(ActionEvent event) {
		DataStore.getProductList().removeAll(selectedProducts);
		DataStore.getProductList().forEach(product -> {
			selectedProducts.forEach(selectedProduct -> product.RemoveIncompatibilities(selectedProduct.getID()));
		});
		this.stage.close();
	}

	@FXML
	public void handleCanceldMFXButton(ActionEvent event) {
		this.stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		removeProductFlowPane.setMaxHeight(Double.MAX_VALUE);
		removeProductFlowPane.setMaxWidth(Double.MAX_VALUE);
		removeProductFlowPane.setHgap(8);
		removeProductFlowPane.setVgap(8);

		DataStore.getProductList().forEach(productToSelect -> {
			MFXButton button = new MFXButton(productToSelect.getName());
			button.setPrefWidth(Region.USE_COMPUTED_SIZE);

			button.setOnAction(event -> {
				if (button.getStyleClass().contains("selected")) {
					selectedProducts.remove(productToSelect);
					button.getStyleClass().remove("selected");
				} else {
					selectedProducts.add(productToSelect);
					button.getStyleClass().add("selected");
				}
			});

			removeProductFlowPane.getChildren().add(button);
		});
		
		removeProductScrollPane.setContent(removeProductFlowPane);

	}

	public boolean isRemovable() {
		return !selectedProducts.isEmpty();
	}

}
