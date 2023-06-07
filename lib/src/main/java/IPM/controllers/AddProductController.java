package IPM.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import IPM.model.DataStore;
import IPM.model.Product;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AddProductController implements Initializable {
	private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
	private Stage stage;
	private boolean isProduct = false;

	@FXML
	private Label validationLabel;

	@FXML
	private MFXButton addMFXButton;

	@FXML
	private MFXTextField nameMFXTextField;

	@FXML
	private MFXTextField categoryMFXTextField;

	public AddProductController(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void handleAddMFXButton(ActionEvent event) {
		if (nameMFXTextField.getValidator().isValid()) {
			Product product = new Product(DataStore.getProductList().size() + 1, nameMFXTextField.getText(),
					categoryMFXTextField.getText(), new ArrayList<String>(), new ArrayList<Integer>());
			DataStore.AddProduct(product);

			isProduct = true;

			this.stage.close();
		}
	}

	@FXML
	private void handleCanceldMFXButton(ActionEvent event) {
		isProduct = false;

		this.stage.close();
	}

	public boolean isProduct() {
		return isProduct;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Constraint nameExistsConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
				.setMessage("Name already exists").setCondition(Bindings.createBooleanBinding(() -> {
					String name = nameMFXTextField.getText();
					if (DataStore.getProductList().isEmpty()) {
						return true;
					}
					for (Product product : DataStore.getProductList()) {
						if (product.getName().equals(name)) {
							return false;
						}
					}
					return true;
				}, nameMFXTextField.textProperty())).get();

		Constraint lengthConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
				.setMessage("Name must be at least 1 characters long")
				.setCondition(nameMFXTextField.textProperty().length().greaterThanOrEqualTo(1)).get();

		Constraint firstLetterConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
				.setMessage("The first character must be a letter")
				.setCondition(Bindings.createBooleanBinding(
						() -> nameMFXTextField.getLength() == 0
								|| Character.isLetter(nameMFXTextField.getText().charAt(0)),
						nameMFXTextField.textProperty()))
				.get();

		nameMFXTextField.getValidator().constraint(lengthConstraint).constraint(firstLetterConstraint)
				.constraint(nameExistsConstraint);

		nameMFXTextField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				validationLabel.setVisible(false);
				nameMFXTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
			}
		});

		nameMFXTextField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue && !newValue) {
				List<Constraint> constraints = nameMFXTextField.validate();
				if (!constraints.isEmpty()) {
					nameMFXTextField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
					validationLabel.setText(constraints.get(0).getMessage());
					validationLabel.setVisible(true);
				}
			}
		});

	}

}
