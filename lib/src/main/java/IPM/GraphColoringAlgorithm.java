package IPM;

import java.util.*;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import IPM.model.*;

/**
 * This class implements the Welsh-Powell graph coloring algorithm to color a given graph.
 */
public class GraphColoringAlgorithm {
    private Graph<Product, String> graphToColor;
    private Map<Vertex<Product>, Integer> colorMap;

    /**
     * Constructor for the GraphColoringAlgorithm class.
     * @param graphToColor The graph to be colored.
     */
    public GraphColoringAlgorithm(Graph<Product, String> graphToColor) {
        this.graphToColor = graphToColor;
    }

    /**
     * This method implements the Welsh-Powell graph coloring algorithm.
     * @return A map that associates each vertex with its color.
     */
    public Map<Vertex<Product>, Integer> welshPowell() {
        // Find the maximum degree of the vertices in the graph
        int maxDegree = graphToColor.vertices().stream()
                .mapToInt(vertex -> graphToColor.incidentEdges(vertex).size())
                .max().orElse(0);

        // Initialize the color list with all the possible colors
        ArrayList<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < maxDegree + 1; i++) {
            colorList.add(i);
        }

        // Sort the vertices by degree in descending order
        Map<Vertex<Product>, Integer> sortedVerticesMap = sortVerticesByDegree(graphToColor);

        // Create a map that tells if a vertex is colored
        Map<Vertex<Product>, Integer> vertexColorMap = new HashMap<>();
        sortedVerticesMap.forEach((vertex, degree) -> {
            vertexColorMap.put(vertex, null);
        });

        // Iterate over the sorted vertices and color them
        sortedVerticesMap.forEach((vertex, degree) -> {
            if (vertexColorMap.get(vertex) == null) {
                // Create a set with the colors used by the adjacent vertices that are already colored
                HashSet<Integer> usedColors = new HashSet<>();
                graphToColor.incidentEdges(vertex).forEach(edge -> {
                    Vertex<Product> adjVertex = graphToColor.opposite(vertex, edge);
                    Integer color = vertexColorMap.get(adjVertex);
                    if (color != null) {
                        usedColors.add(color);
                    }
                });

                // Select a color not in the set of used colors
                Integer color = colorList.stream().filter(c -> !usedColors.contains(c)).findFirst().orElse(null);
                vertexColorMap.put(vertex, color);
            }
        });

        colorMap = vertexColorMap;
        return vertexColorMap;
    }

    /**
     * This method visualizes the graph coloring using the SmartGraph library.
     * @return A SmartGraphPanel object that displays the colored graph.
     */
    public SmartGraphPanel<Product, String> visualizeColoring(Map<Vertex<Product>, Integer> vertexColorMap) {
        VerticeColors verticeColors = new VerticeColors();
        String customProps = "edge.label = false" + "\n" + "edge.arrow = false";
        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        SmartGraphPanel<Product, String> graphView = new SmartGraphPanel<>(graphToColor, properties,
                new SmartCircularSortedPlacementStrategy());

        vertexColorMap.forEach((vertex, color) -> {
            graphView.getStylableVertex(vertex).setStyle("-fx-fill: " + verticeColors.getFillColor(color)
                    + "; -fx-stroke: " + verticeColors.getStrokeColor(color) + ";");
        });

        return graphView;
    }

    /**
     * This method sorts the vertices of a graph by degree in descending order.
     * @param graph The graph whose vertices are to be sorted.
     * @return A map that associates each vertex with its degree, sorted by degree in descending order.
     */
    private Map<Vertex<Product>, Integer> sortVerticesByDegree(Graph<Product, String> graph) {
        // Calculate degree for each vertex
        Map<Vertex<Product>, Integer> degreesMapToSort = calculateDegrees(graph.vertices().iterator());

        // Sort the vertices by degree in descending order
        degreesMapToSort = sortByDegreeDesc(degreesMapToSort);

        return degreesMapToSort;
    }

    /**
     * This method sorts a map by value in descending order.
     * @param mapToSort The map to be sorted.
     * @return A map that is a sorted version of the input map.
     */
    private Map<Vertex<Product>, Integer> sortByDegreeDesc(Map<Vertex<Product>, Integer> mapToSort) {
        List<Map.Entry<Vertex<Product>, Integer>> list = new ArrayList<>(mapToSort.entrySet());
        list.sort(Map.Entry.<Vertex<Product>, Integer>comparingByValue().reversed());

        Map<Vertex<Product>, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Vertex<Product>, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * This method calculates the degrees of the vertices in a graph.
     * @param iterator An iterator over the vertices of the graph.
     * @return A map that associates each vertex with its degree.
     */
    private Map<Vertex<Product>, Integer> calculateDegrees(Iterator<Vertex<Product>> iterator) {
        Map<Vertex<Product>, Integer> degreesMap = new HashMap<>();
        iterator.forEachRemaining(vertex -> {
            degreesMap.put(vertex, graphToColor.incidentEdges(vertex).size());
        });
        return degreesMap;
    }

    /**
     * This method returns the map that associates each vertex with its color.
     * @return A map that associates each vertex with its color.
     */
    public Map<Vertex<Product>, Integer> getColorMap() {
        return colorMap;
    }
}
