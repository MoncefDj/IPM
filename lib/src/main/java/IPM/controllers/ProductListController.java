package IPM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import IPM.ExcelFileHandler;
import IPM.ListConverter;
import IPM.ResourcesLoader;
import IPM.model.DataStore;
import IPM.model.Product;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProductListController implements Initializable {
	private static Stage stage;
	private static StackPane contentPane;
	Parent incompatibeleiesSteppprRoot;

	@FXML
	private MFXTableView<Product> table;

	@FXML
	private void handlehomeMFXButton() throws IOException {
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Home.fxml"));
		Parent root = loader.load();
		contentPane.getChildren().clear();
		contentPane.getChildren().add(root);
	}

	@FXML
	private void handleAddProductMFXButton(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/AddProduct.fxml"));
		AddProductController addProductController = new AddProductController(stage);
		loader.setController(addProductController);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();

		if (addProductController.isProduct()) {
			table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));
		}
	}

	@FXML
	private void handleNextMFXButton(ActionEvent event) throws IOException {
		if (!DataStore.getProductList().isEmpty()) {
			FXMLLoader incompatibeleiesSteppprLoader = new FXMLLoader(
					ResourcesLoader.loadURL("fxml/IncompatibeleiesStepppr.fxml"));
			incompatibeleiesSteppprRoot = incompatibeleiesSteppprLoader.load();
			IncompatibeleiesSteppprController IncompatibeleiesSteppprController = incompatibeleiesSteppprLoader
					.getController();
			IncompatibeleiesSteppprController.initializeStepper();
			contentPane.getChildren().clear();
			contentPane.getChildren().add(incompatibeleiesSteppprRoot);
		} else {
			MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
					.publish(new Notification(new MFXFontIcon("fas-circle-xmark"), "Product List is empty!"));
		}
	}

	@FXML
	private void handleSaveMFXButton() {
		if (!DataStore.getProductList().isEmpty()) {
			ExcelFileHandler.Save(DataStore.getProductList());

			if (ExcelFileHandler.Icon == true) {
				MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
						.publish(new Notification(new MFXFontIcon("fas-circle-check"), ExcelFileHandler.Action));
			} else {
				MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
						.publish(new Notification(new MFXFontIcon("fas-circle-xmark"), ExcelFileHandler.Action));
			}
		} else {
			MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
					.publish(new Notification(new MFXFontIcon("fas-circle-xmark"), "Product List is empty!"));
		}
	}

	@FXML
	private void handleloadMFXButton() {
		ArrayList<Product> loadedProductList = ExcelFileHandler.load();
		DataStore.setProductList(loadedProductList);

		table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));

		if (ExcelFileHandler.Icon == true) {
			MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
					.publish(new Notification(new MFXFontIcon("fas-circle-check"), ExcelFileHandler.Action));
		} else {
			MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER)
					.publish(new Notification(new MFXFontIcon("fas-circle-xmark"), ExcelFileHandler.Action));
		}

	}

	@FXML
	private void handleRemoveProductMFXButton(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/RemoveProduct.fxml"));
		RemoveProductController removeProductController = new RemoveProductController(stage);
		loader.setController(removeProductController);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();

		if (removeProductController.isRemovable()) {
			table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));
		}
	}

	@FXML
	private void handleUpdateProductMFXButton() throws IOException {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/UpdateProduct.fxml"));
		UpdateProductController updateProductController = new UpdateProductController(stage);
		loader.setController(updateProductController);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();

		refreshTabel();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize the NotificationSystem
		Platform.runLater(() -> {
			MFXNotificationSystem.instance().initOwner(stage);
			MFXNotificationCenterSystem.instance().initOwner(stage);

			MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
			center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
				{
					setPrefHeight(400);
				}
			});
		});

		setupTable();
	}

	private void setupTable() {
		MFXTableColumn<Product> idColumn = new MFXTableColumn<>("ID", true, Comparator.comparing(Product::getID));
		MFXTableColumn<Product> nameColumn = new MFXTableColumn<>("Name", true, Comparator.comparing(Product::getName));
		MFXTableColumn<Product> categoryColumn = new MFXTableColumn<>("Category", true,
				Comparator.comparing(Product::getCategory));

		idColumn.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getID));
		nameColumn.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getName));
		categoryColumn.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getCategory));

		table.getTableColumns().addAll(idColumn, nameColumn, categoryColumn);
		table.getFilters().addAll(new IntegerFilter<>("ID", Product::getID),
				new StringFilter<>("Name", Product::getName), new StringFilter<>("Category", Product::getCategory));

		table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));
	}

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}

	public void refreshTabel() {
		Product refreshProduct = new Product(0, null, null, null, null);
		DataStore.getProductList().add(refreshProduct);
		table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));
		DataStore.getProductList().remove(refreshProduct);
		table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));
	}

	public static void setStage(Stage stage) {
		ProductListController.stage = stage;
	}

	private static class Notification extends MFXSimpleNotification {
		private final StringProperty headerText = new SimpleStringProperty();

		public Notification(MFXFontIcon notificationIcon, String headerContent) {
			MFXFontIcon fi = notificationIcon;
			headerText.set(headerContent);

			fi.setSize(16);
			MFXIconWrapper icon = new MFXIconWrapper(fi, 32);
			Label headerLabel = new Label();
			headerLabel.textProperty().bind(headerText);

			HBox header = new HBox(10, icon, headerLabel);
			header.setAlignment(Pos.CENTER_LEFT);
			header.setPadding(InsetsFactory.of(5, 10, 5, 0));
			header.setMaxWidth(Double.MAX_VALUE);

			BorderPane container = new BorderPane();
			container.getStyleClass().add("notification");
			container.setTop(header);
			container.getStylesheets().add(ResourcesLoader.load("css/Notification.css"));

			setContent(container);
		}

	}
}