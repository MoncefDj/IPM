package IPM.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import IPM.model.DataStore;
import IPM.model.Product;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UpdateController implements Initializable {
	private Stage stage;
	private Product product;
	private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

	@FXML
	private Label validationLabel;

	@FXML
	MFXTextField nameMFXTextField;

	@FXML
	MFXTextField categoryMFXTextField;

	public UpdateController(Stage stage, Product product) {
		this.stage = stage;
		this.product = product;
	}

	@FXML
	private void handleUpdateMFXButton() {
		if (nameMFXTextField.getValidator().isValid()) {
			if (!nameMFXTextField.getText().isEmpty()) {
				product.setName(nameMFXTextField.getText());
			}

			product.setCategory(categoryMFXTextField.getText());

			this.stage.close();
		}
	}

	@FXML
	private void handleCanceldMFXButton() {
		this.stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameMFXTextField.setText(this.product.getName());
		categoryMFXTextField.setText(this.product.getCategory());

		Constraint nameExistsConstraint = Constraint.Builder.build().setSeverity(Severity.ERROR)
				.setMessage("Name already exists").setCondition(Bindings.createBooleanBinding(() -> {
					String name = nameMFXTextField.getText();
					if (DataStore.getProductList().isEmpty()) {
						return true;
					}
					for (Product product : DataStore.getProductList()) {
						if (product.getName().equals(name) && !this.product.equals(product)) {
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
