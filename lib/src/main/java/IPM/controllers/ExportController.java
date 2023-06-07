package IPM.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import IPM.ExcelFileHandler;
import IPM.GraphColoringAlgorithm;
import IPM.GraphConstructor;
import IPM.ResourcesLoader;
import IPM.model.DataStore;
import IPM.model.Product;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
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

public class ExportController implements Initializable {
	private static StackPane contentPane;
	private static Stage stage;
	private static Map<Vertex<Product>, Integer> colorMap;
	private static ArrayList<Vertex<Product>> verteiceList;
	private Parent graphVisualizationRoot;

	@FXML
	private void handleExportMFXButton() {
		ExcelFileHandler.Export(DataStore.getProductList(), colorMap, MapToVertextList(colorMap));

		MFXNotificationSystem.instance().setPosition(NotificationPos.TOP_CENTER).publish(new Notification());
	}

	@FXML
	private void handlehomeMFXButton() throws IOException {
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Home.fxml"));
		Parent root = loader.load();
		contentPane.getChildren().clear();
		contentPane.getChildren().add(root);
	}

	@FXML
	private void handlebackMFXButton() throws IOException {
		GraphConstructor graphConstructor = new GraphConstructor(DataStore.getProductList());
		Graph<Product, String> constructedGraph = graphConstructor.constructGraph();
		GraphColoringAlgorithm graphColoringAlgorithm = new GraphColoringAlgorithm(constructedGraph);
		
		Map<Vertex<Product>, Integer> verticeColorMap = new HashMap<>();
		constructedGraph.vertices().forEach(cgVertex ->{
			colorMap.forEach((vertex, color) -> {
				if (vertex.element().getID() == cgVertex.element().getID()) {
					verticeColorMap.put(cgVertex, color);
				}
			});
		});
		
		SmartGraphPanel<Product, String> graphView = graphColoringAlgorithm.visualizeColoring(verticeColorMap);
		GraphVisualizationController.setGraphView(graphView);

		FXMLLoader graphVisualizationLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/GraphVisualization.fxml"));
		GraphVisualizationController.setColorMap(verticeColorMap);
		ExportController.setColorMap(verticeColorMap);
		ArrayList<Vertex<Product>> verticeList = new ArrayList<Vertex<Product>>();
		constructedGraph.vertices().forEach(vertex -> verticeList.add(vertex));
		ExportController.setVerteiceList(verticeList);

		try {
			graphVisualizationRoot = graphVisualizationLoader.load();
			BorderPane graphVisualizationBorderPane = (BorderPane) graphVisualizationRoot
					.lookup("#graphVisualizationBorderPane");
			graphVisualizationBorderPane.setCenter(graphView);
		} catch (IOException e) {
			e.printStackTrace();
		}

		contentPane.getChildren().clear();
		contentPane.getChildren().add(graphVisualizationRoot);

		// Delay the initialization code for the SmartGraphPanel
		Platform.runLater(() -> {
			graphView.init();
		});
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

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}

	static public void setColorMap(Map<Vertex<Product>, Integer> colorMap) {
		ExportController.colorMap = colorMap;
	}

	static public void setVerteiceList(ArrayList<Vertex<Product>> verteiceList) {
		ExportController.verteiceList = verteiceList;
	}

	public static void setStage(Stage stage) {
		ExportController.stage = stage;
	}

	public static ArrayList<Vertex<Product>> MapToVertextList(Map<Vertex<Product>, Integer> map) {
		ArrayList<Vertex<Product>> list = new ArrayList<Vertex<Product>>();

		// Extract the Vertex from each object in the map and add it to the list
		for (Map.Entry<Vertex<Product>, Integer> entry : map.entrySet()) {
			list.add(entry.getKey());
		}

		return list;
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

	}
}
