package IPM.model;

import java.util.ArrayList;
import IPM.ListConverter;
import javafx.collections.ObservableList;

public class Model {
	public static final ObservableList<Product> products;
	static {
		// the Why Graph Coloring? example
		ArrayList<Integer> incompatibleProducts1 = new ArrayList<Integer>();
		incompatibleProducts1.add(2);
		incompatibleProducts1.add(3);
		incompatibleProducts1.add(4);
		ArrayList<String> productProperties1 = new ArrayList<String>();
		productProperties1.add("Highly flammable: It has a low flash point and can easily ignite.");
		productProperties1.add("Volatile: It evaporates quickly at normal temperatures.");
		productProperties1
				.add("Combustible: It can burn and release heat and energy when exposed to a flame or spark.");
		Product product1 = new Product(1, "Flammable gasoline", "Flammable liquid", productProperties1,
				incompatibleProducts1);

		ArrayList<Integer> incompatibleProducts2 = new ArrayList<Integer>();
		incompatibleProducts2.add(5);
		incompatibleProducts2.add(3);
		incompatibleProducts2.add(1);
		ArrayList<String> productProperties2 = new ArrayList<String>();
		productProperties2.add("Facilitates combustion: It promotes the rapid oxidation of other substances.");
		productProperties2.add("Can release oxygen: It can provide oxygen to support combustion.");
		productProperties2.add(
				" Can react vigorously with reducing agents: It can undergo chemical reactions with substances that donate electrons.");
		Product product2 = new Product(2, "Oxidizing agent",
				" Chemical compound that facilitates the combustion of other substances", productProperties2,
				incompatibleProducts2);

		ArrayList<Integer> incompatibleProducts3 = new ArrayList<Integer>();
		incompatibleProducts3.add(2);
		incompatibleProducts3.add(1);
		incompatibleProducts3.add(4);
		ArrayList<String> productProperties3 = new ArrayList<String>();
		productProperties3.add("Highly acidic: It has a low pH value.");
		productProperties3
				.add("Can cause corrosion: It can chemically erode or deteriorate certain materials upon contact.");
		productProperties3.add(
				"Can be harmful upon direct contact: It can cause burns or irritation to the skin, eyes, and respiratory system.");
		Product product3 = new Product(3, "Corrosive acid", " Corrosive chemical or strong acid", productProperties3,
				incompatibleProducts3);

		ArrayList<Integer> incompatibleProducts4 = new ArrayList<Integer>();
		incompatibleProducts4.add(3);
		incompatibleProducts4.add(1);
		ArrayList<String> productProperties4 = new ArrayList<String>();
		productProperties4.add(
				"Toxicity to pests: It is formulated to be harmful or lethal to specific pests, such as insects, rodents, or weeds.");
		productProperties4.add(
				"Can pose health risks: It can be toxic to humans and other non-target organisms if not handled properly.");
		productProperties4
				.add("Used for pest control: It is applied to eliminate or manage unwanted pests in various settings.");
		Product product4 = new Product(4, "Toxic pesticide",
				" Chemical compound used for pest control or as an insecticide", productProperties4,
				incompatibleProducts4);

		ArrayList<Integer> incompatibleProducts5 = new ArrayList<Integer>();
		incompatibleProducts5.add(6);
		incompatibleProducts5.add(2);
		ArrayList<String> productProperties5 = new ArrayList<String>();
		productProperties5.add("High reactivity: It readily undergoes chemical reactions with other substances.");
		productProperties5.add("Can be oxidized: It can lose electrons and form positive ions in chemical reactions.");
		productProperties5.add(
				"Can be highly reactive with certain acids or water: It can produce flammable or potentially explosive gases.");
		Product product5 = new Product(5, "Reactive meta", "Metal with high reactivity, prone to chemical reactions",
				productProperties5, incompatibleProducts5);

		ArrayList<Integer> incompatibleProducts6 = new ArrayList<Integer>();
		incompatibleProducts6.add(5);
		ArrayList<String> productProperties6 = new ArrayList<String>();
		productProperties6.add("Alkaline: It has a high pH value.");
		productProperties6.add("Can neutralize acids: It can react with acidic substances to form salts and water.");
		productProperties6.add("Can cause skin and eye irritation: Direct contact can lead to burns or irritation.");
		Product product6 = new Product(6, "Strong base", " Alkaline substance with high pH", productProperties6,
				incompatibleProducts6);

		ArrayList<Product> productList = new ArrayList<Product>();
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		productList.add(product4);
		productList.add(product5);
		productList.add(product6);

		products = ListConverter.arrayListToObservableList(productList);
	}
}
