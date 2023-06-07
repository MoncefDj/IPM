package IPM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

import IPM.model.Product;

/**
 * This class constructs a graph based on a list of products, where vertices represent products and edges represent
 * incompatibilities between products.
 */
public class GraphConstructor {

    private List<Product> productList;

    public GraphConstructor(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * Constructs a graph based on the list of products.
     *
     * @return the constructed graph
     */
    public Graph<Product, String> constructGraph() {
        Graph<Product, String> graph = new GraphEdgeList<>();
        Set<List<Integer>> incompatiblePairs = new HashSet<>();

        // Add vertices to the graph for each product
        for (Product product : productList) {
            addVertex(graph, product);
            updateIncompatiblePairsList(product, incompatiblePairs);
        }

        // Add edges between incompatible pairs of products
        for (List<Integer> pair : incompatiblePairs) {
            Product product1 = getProductById(pair.get(0));
            Product product2 = getProductById(pair.get(1));

            if (product2 != null) {
                addEdge(graph, product1, product2, createEdgeLabel(product1, product2));
            }
        }

        return graph;
    }

    /**
     * Adds a vertex to the graph for the given product.
     *
     * @param graph   the graph to add the vertex to
     * @param product the product to add as a vertex
     */
    private void addVertex(Graph<Product, String> graph, Product product) {
        graph.insertVertex(product);
    }

    /**
     * Adds an edge between two products with the given edge label.
     *
     * @param graph     the graph to add the edge to
     * @param product1  the first product to connect with an edge
     * @param product2  the second product to connect with an edge
     * @param edgeLabel the label to assign to the edge
     */
    private void addEdge(Graph<Product, String> graph, Product product1, Product product2, String edgeLabel) {
        graph.insertEdge(product1, product2, edgeLabel);
    }

    /**
     * Updates the list of incompatible pairs based on the incompatibilities of the given product.
     *
     * @param product           the product to update the list of incompatible pairs for
     * @param incompatiblePairs the list of incompatible pairs to update
     */
    private void updateIncompatiblePairsList(Product product, Set<List<Integer>> incompatiblePairs) {
        List<Integer> incompatibilities = product.getIncompatibilities();
        for (int i = 0; i < incompatibilities.size(); i++) {
            List<Integer> pair = new ArrayList<>();
            pair.add(product.getID());
            pair.add(incompatibilities.get(i));
            if (!incompatiblePairs.contains(pair) && !incompatiblePairs.contains(reverseList(pair))) {
                incompatiblePairs.add(pair);
            }
        }
    }

    /**
     * Reverses the order of elements in the given list.
     *
     * @param list the list to reverse
     * @return the reversed list
     */
    public List<Integer> reverseList(List<Integer> list) {
        List<Integer> reversedList = new ArrayList<>(list);
        Collections.reverse(reversedList);
        return reversedList;
    }

    /**
     * Returns the product with the given ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the product with the given ID, or null if no such product exists
     */
    private Product getProductById(int productId) {
        for (Product product : productList) {
            if (product.getID() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Creates an edge label by concatenating two products.
     *
     * @param product1 the first product to include in the label
     * @param product2 the second product to include in the label
     * @return the concatenated label
     */
    private String createEdgeLabel(Product product1, Product product2) {
        return product1.toString() + product2.toString();
    }
}
