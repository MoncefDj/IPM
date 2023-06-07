package IPM.model;

import java.util.ArrayList;

import IPM.ExcelFileHandler;

public class DataStore {
	static private ArrayList<Product> productList = new ArrayList<Product>();
	/*
	 * static private ArrayList<Product> productList =
	 * ListConverter.observableListToArrayList(Model.products);
	 */

	static public void AddProduct(Product product) {
		productList.add(product);
	}

	static public void RemoveProduct(Product product) {
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).getID() == product.getID()) {
				productList.remove(i);
			}
		}
	}

	static public void setProductList(ArrayList<Product> myProductList) {
		productList = myProductList;
	}

	static public ArrayList<Product> getProductList() {
		return productList;
	}

	static public void SaveToFile(ArrayList<Product> List) {
		ExcelFileHandler.Save(List);
	}

	static public ArrayList<Product> LoadFromFile() {
		return ExcelFileHandler.load();
	}
}
