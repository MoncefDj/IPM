package IPM.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import IPM.ListConverter;
import IPM.ResourcesLoader;
import IPM.model.Product;
import IPM.model.VerticeColors;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GraphVisualizationController {
	private static StackPane contentPane;
	private static Map<Vertex<Product>, Integer> colorMap;
	private static SmartGraphPanel<Product, String> graphView;

	@FXML
	private void handlebackMFXButton() throws IOException {
		FXMLLoader loader = new FXMLLoader(ResourcesLoader.loadURL("fxml/ProductList.fxml"));
		Parent root = loader.load();
		contentPane.getChildren().clear();
		contentPane.getChildren().add(root);
	}

	@FXML
	private void handleadvancedviewButtonMFXButton() throws IOException {
		Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 720, 480);
		scene.getStylesheets().add(ResourcesLoader.load("css/Smartgraph.css"));
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();

		FXMLLoader exportLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Export.fxml"));
		Parent exportRoot = exportLoader.load();

		MFXScrollPane exportScrollPane = (MFXScrollPane) exportRoot.lookup("#exportScrollPane");
		HBox resultsHBox = new HBox();

		Map<Integer, List<Vertex<Product>>> verticesByValue = groupVerticesByValue(colorMap);

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

		contentPane.getChildren().clear();
		contentPane.getChildren().add(exportRoot);

	}

	@FXML
	private void handleNextMFXButton() throws IOException {
		FXMLLoader exportLoader = new FXMLLoader(ResourcesLoader.loadURL("fxml/Export.fxml"));
		Parent exportRoot = exportLoader.load();

		MFXScrollPane exportScrollPane = (MFXScrollPane) exportRoot.lookup("#exportScrollPane");
		HBox resultsHBox = new HBox();

		Map<Integer, List<Vertex<Product>>> verticesByValue = groupVerticesByValue(colorMap);

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

		contentPane.getChildren().clear();
		contentPane.getChildren().add(exportRoot);

	}

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}

	static public void setColorMap(Map<Vertex<Product>, Integer> colorMap) {
		GraphVisualizationController.colorMap = colorMap;
	}

	public static void setGraphView(SmartGraphPanel<Product, String> graphView) {
		GraphVisualizationController.graphView = graphView;
	}

	public static Map<Integer, List<Vertex<Product>>> groupVerticesByValue(Map<Vertex<Product>, Integer> vertexValues) {
		Map<Integer, List<Vertex<Product>>> verticesByValue = new HashMap<>();

		for (Map.Entry<Vertex<Product>, Integer> entry : vertexValues.entrySet()) {
			Vertex vertex = entry.getKey();
			int value = entry.getValue();

			List<Vertex<Product>> verticesWithSameValue = verticesByValue.computeIfAbsent(value,
					k -> new ArrayList<>());
			verticesWithSameValue.add(vertex);
		}

		return verticesByValue;
	}

}
