package IPM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.brunomnsilva.smartgraph.graph.Vertex;

import IPM.ExcelFileHandler;
import IPM.ListConverter;
import IPM.ResourcesLoader;
import IPM.model.DataStore;
import IPM.model.Product;
import IPM.model.VerticeColors;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ImportController implements Initializable {
	private static StackPane contentPane;
	private static Stage notificationStage;

	private Parent productListRoot;
	private Parent exportRoot;

	private Stage stage;

	public ImportController(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void handleCloseButton() {
		this.stage.close();
	}

	@FXML
	private void handleproductListMFXButton() {
		ArrayList<Product> loadedProductList = ExcelFileHandler.load();
		DataStore.setProductList(loadedProductList);

		MFXTableView<Product> table = (MFXTableView<Product>) productListRoot.lookup("#table");
		table.setItems(ListConverter.arrayListToObservableList(DataStore.getProductList()));

		if (ExcelFileHandler.Icon) {
			contentPane.getChildren().clear();
			contentPane.getChildren().add(productListRoot);
		}

		this.stage.close();

		MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER).publish(new Notification());
	}

	@FXML
	private void handleresultsMFXButton() {
		Map<Vertex<Product>, Integer> importedMap = ExcelFileHandler.Import();
		ArrayList<Product> loadedProductList = ExcelFileHandler.MapToProductList(importedMap);
		DataStore.setProductList(loadedProductList);

		MFXScrollPane exportScrollPane = (MFXScrollPane) exportRoot.lookup("#exportScrollPane");
		HBox resultsHBox = new HBox();

		Map<Integer, List<Vertex<Product>>> verticesByValue = GraphVisualizationController
				.groupVerticesByValue(importedMap);

		verticesByValue.forEach((color, vertice) -> {

			MFXListView<Product> coloredList = new MFXListView<Product>();
			ArrayList<Product> products = new ArrayList<Product>();

			vertice.forEach(vertex -> {
				products.add(vertex.element());
			});

			StringConverter<Product> converter = FunctionalStringConverter.to(product -> product.toString());

			coloredList.setConverter(converter);
			coloredList.setItems(ListConverter.arrayListToObservableList(products));
			coloredList.features().enableBounceEffect();
			coloredList.features().enableSmoothScrolling(0.5);
			coloredList.setMaxSize(144, 180);

			VerticeColors verticeColors = new VerticeColors();

			coloredList.setStyle("-fx-background-color:" + verticeColors.getFillColor(color)
					+ "; -fx-background-radius: 10;\r\n" + "	-fx-border-radius: 10;\r\n" + "	-fx-padding: 4;");

			resultsHBox.getChildren().add(coloredList);
			resultsHBox.setSpacing(16);
		});

		exportScrollPane.setContent(resultsHBox);

		if (ExcelFileHandler.Icon) {
			ExportController.setColorMap(importedMap);
			
			contentPane.getChildren().clear();
			contentPane.getChildren().add(exportRoot);
		}

		this.stage.close();

		MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER).publish(new Notification());
	}

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize the NotificationSystem
		Platform.runLater(() -> {
			MFXNotificationSystem.instance().initOwner(notificationStage);
			MFXNotificationCenterSystem.instance().initOwner(notificationStage);

			MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
			center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
				{
					setPrefHeight(400);
				}
			});
		});

		FXMLLoader productListLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/ProductList.fxml"));
		try {
			productListRoot = productListLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FXMLLoader exportLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Export.fxml"));
		try {
			exportRoot = exportLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setNotificationStage(Stage notificationStage) {
		ImportController.notificationStage = notificationStage;
	}

	private static class Notification extends MFXSimpleNotification {
		private final StringProperty headerText = new SimpleStringProperty();

		public Notification() {
			MFXFontIcon fi;

			if (ExcelFileHandler.Icon == true) {

				fi = new MFXFontIcon("fas-circle-check");
				headerText.set(ExcelFileHandler.Action);

			} else {

				fi = new MFXFontIcon("fas-circle-xmark");
				headerText.set(ExcelFileHandler.Action);
			}

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
