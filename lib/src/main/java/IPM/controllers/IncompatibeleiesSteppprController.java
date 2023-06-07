package IPM.controllers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import IPM.GraphColoringAlgorithm;
import IPM.GraphConstructor;
import IPM.ResourcesLoader;
import IPM.model.DataStore;
import IPM.model.Product;

public class IncompatibeleiesSteppprController {
	private static StackPane contentPane;
	Parent graphVisualizationRoot;

	@FXML
	private MFXStepper stepper;

	public void initializeStepper() {
		List<MFXStepperToggle> stepperToggles;

		Scene scene = contentPane.getScene();
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				stepper.next();
			}
		});

		try {
			stepperToggles = createSteps();
			stepper.setOnLastNext(event -> {
				GraphConstructor graphConstructor = new GraphConstructor(DataStore.getProductList());
				Graph<Product, String> constructedGraph = graphConstructor.constructGraph();
				GraphColoringAlgorithm graphColoringAlgorithm = new GraphColoringAlgorithm(constructedGraph);
				SmartGraphPanel<Product, String> graphView = graphColoringAlgorithm.visualizeColoring(graphColoringAlgorithm.welshPowell());
				GraphVisualizationController.setGraphView(graphView);

				FXMLLoader graphVisualizationLoader = new FXMLLoader(
						ResourcesLoader.loadURL("fxml/GraphVisualization.fxml"));
				GraphVisualizationController.setColorMap(graphColoringAlgorithm.getColorMap());
				ExportController.setColorMap(graphColoringAlgorithm.getColorMap());
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
			});
			stepper.getStepperToggles().addAll(stepperToggles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<MFXStepperToggle> createSteps() throws IOException {
		List<MFXStepperToggle> steps = new ArrayList<MFXStepperToggle>();
		DataStore.getProductList().forEach(product -> {
			int i = steps.size() + 1;
			MFXStepperToggle step = new MFXStepperToggle(product.getName(),
					new MFXFontIcon("fas-file-powerpoint", 20, Color.web("#4397ce")));
			FlowPane togglesFlowPane = new FlowPane();
			togglesFlowPane.setHgap(8);
			togglesFlowPane.setVgap(8);

			DataStore.getProductList().forEach(productToSelect -> {
				if (!productToSelect.equals(product)) {

					MFXButton button = new MFXButton(productToSelect.getName());
					button.setPrefWidth(Region.USE_COMPUTED_SIZE);

					if (product.getIncompatibilities().contains(productToSelect.getID())) {
						button.getStyleClass().add("selected");
					}

					button.setOnAction(event -> {
						if (button.getStyleClass().contains("selected")) {
							product.RemoveIncompatibilities(productToSelect.getID());
							button.getStyleClass().remove("selected");
						} else {
							product.AddIncompatibilities(productToSelect.getID());
							button.getStyleClass().add("selected");
						}
					});

					togglesFlowPane.getChildren().add(button);
				}
			});

			togglesFlowPane.getStylesheets().add(ResourcesLoader.load("css/Toggels.css"));
			togglesFlowPane.setPadding(new Insets(36));
			togglesFlowPane.setMaxSize(720, 600);

			step.setContent(togglesFlowPane);
			step.setMaxSize(720, 600);

			steps.add(step);
		});

		return steps;
	}

	static public void setContentPane(StackPane myContentPane) {
		contentPane = myContentPane;
	}
}