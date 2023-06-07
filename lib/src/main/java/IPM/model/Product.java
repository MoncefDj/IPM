package IPM.model;

import java.util.ArrayList;

public class Product {
	private int id;
	private String name;
	private String category = "";
	private ArrayList<String> properties;
	private ArrayList<Integer> incompatibilities;

	public Product(int id, String name, String category, ArrayList<String> properties,
			ArrayList<Integer> incompatibilities) {
		this.setID(id);
		this.setName(name);
		this.setCategory(category);
		this.setProperties(properties);
		this.setIncompatibilities(incompatibilities);
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ArrayList<String> getProperties() {
		return this.properties;
	}

	public void setProperties(ArrayList<String> properties) {
		this.properties = properties;
	}

	public ArrayList<Integer> getIncompatibilities() {
		return this.incompatibilities;
	}

	public void setIncompatibilities(ArrayList<Integer> incompatibilities) {
		this.incompatibilities = incompatibilities;
	}

	public void AddProperties(String Property) {
		this.properties.add(Property);
	}

	public void RemoveProperties(String property) {
		for (int i = 0; i < this.properties.size(); i++) {
			if (this.properties.get(i).equals(property)) {
				properties.remove(i);
			}
		}
	}

	public void AddIncompatibilities(int incompatibility) {
		this.incompatibilities.add(incompatibility);
	}

	public void RemoveIncompatibilities(int incompatibility) {
		for (int i = 0; i < this.incompatibilities.size(); i++) {
			if (this.incompatibilities.get(i) == incompatibility) {
				this.incompatibilities.remove(i);
			}
		}
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
