package IPM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.brunomnsilva.smartgraph.graph.Vertex;
import IPM.model.Product;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExcelFileHandler {

	public static String Action;
	public static boolean Icon;

	// Method to save products to an Excel file
	public static void Save(ArrayList<Product> productList) {
		Workbook workbook = null;
		FileOutputStream outputStream = null;
		try {
			workbook = WorkbookFactory.create(true);
			Sheet sheet = workbook.createSheet("Products");

			// Create header row with column names
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Name");
			headerRow.createCell(2).setCellValue("Category");
			headerRow.createCell(3).setCellValue("Properties");
			headerRow.createCell(4).setCellValue("Incompatibilities");

			int rowCount = 1;
			// Iterate over each product and create a row for it in the sheet
			for (Product product : productList) {
				Row row = sheet.createRow(rowCount++);
				row.createCell(0).setCellValue(product.getID());
				row.createCell(1).setCellValue(product.getName());
				row.createCell(2).setCellValue(product.getCategory());

				// Create a comma-separated string of properties
				StringBuilder propertiesBuilder = new StringBuilder();
				for (String property : product.getProperties()) {
					propertiesBuilder.append(property).append(",");
				}
				if (propertiesBuilder.length() > 0) {
					propertiesBuilder.deleteCharAt(propertiesBuilder.length() - 1);
				}
				row.createCell(3).setCellValue(propertiesBuilder.toString());

				// Create a comma-separated string of incompatibilities
				StringBuilder incompatibilitiesBuilder = new StringBuilder();
				for (Integer incompatibility : product.getIncompatibilities()) {
					incompatibilitiesBuilder.append(incompatibility).append(",");
				}
				if (incompatibilitiesBuilder.length() > 0) {
					incompatibilitiesBuilder.deleteCharAt(incompatibilitiesBuilder.length() - 1);
				}
				row.createCell(4).setCellValue(incompatibilitiesBuilder.toString());
			}

			// Configure file chooser for saving the Excel file
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
			filechooser.setInitialDirectory(new File(System.getProperty("user.home")));

			// Show file save dialog and get the selected file
			File file = filechooser.showSaveDialog(new Stage());
			if (file != null) {
				outputStream = new FileOutputStream(file);
				workbook.write(outputStream);
				System.out.println("Saved !");
				Action = "Saved successful !";
				Icon = true;
			} else {
				// User clicked "Cancel", handle it here
				System.out.println("Save cancelled by the user");
				Action = "Saving has been cancelled !";
				Icon = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the output stream and workbook
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Method to export products with color information to an Excel file
	public static void Export(ArrayList<Product> productList, Map<Vertex<Product>, Integer> verticeColorMap,
			ArrayList<Vertex<Product>> vertexList) {
		Workbook workbook = null;
		FileOutputStream outputStream = null;
		try {
			workbook = WorkbookFactory.create(true);
			Sheet sheet = workbook.createSheet("Products");

			// Create header row with column names
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Name");
			headerRow.createCell(2).setCellValue("Category");
			headerRow.createCell(3).setCellValue("Properties");
			headerRow.createCell(4).setCellValue("Incompatibilities");
			headerRow.createCell(5).setCellValue("Colors");

			int rowCount = 1;
			// Iterate over each product and create a row for it in the sheet
			for (Product product : productList) {
				Row row = sheet.createRow(rowCount++);
				row.createCell(0).setCellValue(product.getID());
				row.createCell(1).setCellValue(product.getName());
				row.createCell(2).setCellValue(product.getCategory());

				// Create a comma-separated string of properties
				StringBuilder propertiesBuilder = new StringBuilder();
				for (String property : product.getProperties()) {
					propertiesBuilder.append(property).append(",");
				}
				if (propertiesBuilder.length() > 0) {
					propertiesBuilder.deleteCharAt(propertiesBuilder.length() - 1);
				}
				row.createCell(3).setCellValue(propertiesBuilder.toString());

				// Create a comma-separated string of incompatibilities
				StringBuilder incompatibilitiesBuilder = new StringBuilder();
				for (Integer incompatibility : product.getIncompatibilities()) {
					incompatibilitiesBuilder.append(incompatibility).append(",");
				}
				if (incompatibilitiesBuilder.length() > 0) {
					incompatibilitiesBuilder.deleteCharAt(incompatibilitiesBuilder.length() - 1);
				}
				row.createCell(4).setCellValue(incompatibilitiesBuilder.toString());

				// Find the color for the product's vertex and set it in the cell
				for (Vertex<Product> vertex : vertexList) {
					if (vertex.element().getID() == product.getID()) {
						row.createCell(5).setCellValue(verticeColorMap.get(vertex));
						break;
					}
				}
			}

			// Configure file chooser for saving the Excel file
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

			// Show file save dialog and get the selected file
			File file = fileChooser.showSaveDialog(new Stage());
			if (file != null) {
				outputStream = new FileOutputStream(file);
				workbook.write(outputStream);
				System.out.println("Exported !");
				Action = "Exported successful !";
				Icon = true;
			} else {
				System.out.println("Export cancelled by the user");
				Action = "Exporting has been cancelled !";
				Icon = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the output stream and workbook
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}
	}

	// Method to import products and color information from an Excel file
	public static Map<Vertex<Product>, Integer> Import() {
		Workbook workbook = null;
		Map<Vertex<Product>, Integer> map = new HashMap<>();
		try {
			// Configure file chooser for selecting the Excel file to import
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
			filechooser.setInitialDirectory(new File(System.getProperty("user.home")));

			// Show file open dialog and get the selected file
			File fileToLoad = filechooser.showOpenDialog(new Stage());

			if (fileToLoad != null) {

				FileInputStream fileInputStream = new FileInputStream(fileToLoad);

				// Create workbook from the selected file
				workbook = WorkbookFactory.create(fileInputStream);

				// Get the sheet named "Products"
				Sheet sheet = workbook.getSheet("Products");
				if (sheet == null) {
					throw new IllegalArgumentException("Sheet 'Products' not found.");
				}

				int rowCount = sheet.getLastRowNum();
				// Iterate over each row in the sheet starting from the second row (skipping the
				// header row)
				for (int i = 1; i <= rowCount; i++) {
					Row row = sheet.getRow(i);

					// Extract data from each cell and create a product object
					String propertiesString = row.getCell(3).getStringCellValue();
					ArrayList<String> propertiesList = new ArrayList<>(Arrays.asList(propertiesString.split(",")));
					propertiesList.replaceAll(String::trim);

					String incompatibilitiesString = row.getCell(4).getStringCellValue();
					String[] incompatibilitiesArr = incompatibilitiesString.split(",");
					ArrayList<Integer> incompatibilities = new ArrayList<>();
					for (String incompatibility : incompatibilitiesArr) {
						if (!incompatibility.trim().isEmpty()) {
							incompatibilities.add(Integer.parseInt(incompatibility.trim()));
						}
					}

					int color = (int) row.getCell(5).getNumericCellValue();

					Product product = new Product((int) row.getCell(0).getNumericCellValue(),
							row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue(), propertiesList,
							incompatibilities);

					// Create a vertex with the product and add it to the map with the corresponding
					// color
					Vertex<Product> vertex = () -> product;
					map.put(vertex, color);
				}

				fileInputStream.close();
				workbook.close();
				System.out.println("Imported !");
				Action = "Imported successful !";
				Icon = true;

			} else {

				// User clicked "Cancel", handle it here
				System.out.println("Import cancelled by the user");
				Action = "Importin has been cancelled !";
				Icon = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			Action = "Error !";
			Icon = false;
		}
		return map;
	}

	// Method to convert the map of vertices and colors to a list of products
	public static ArrayList<Product> MapToProductList(Map<Vertex<Product>, Integer> map) {
		ArrayList<Product> list = new ArrayList<Product>();

		// Extract the product from each vertex in the map and add it to the list
		for (Map.Entry<Vertex<Product>, Integer> entry : map.entrySet()) {
			list.add(entry.getKey().element());
		}

		return list;
	}

	// Method to load products from an Excel file
	public static ArrayList<Product> load() {
		ArrayList<Product> products = new ArrayList<>();
		try {
			// Configure file chooser for selecting the Excel file to load
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
			filechooser.setInitialDirectory(new File(System.getProperty("user.home")));

			// Show file open dialog and get the selected file
			File fileToLoad = filechooser.showOpenDialog(new Stage());

			if (fileToLoad != null) {

				FileInputStream file = new FileInputStream(fileToLoad);
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next(); // Skip the header row

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();

					// Read the data from each cell and create a product object
					int id = (int) cellIterator.next().getNumericCellValue();
					String name = cellIterator.next().getStringCellValue();
					String category = cellIterator.next().getStringCellValue();
					String propertiesString = cellIterator.next().getStringCellValue();
					ArrayList<String> propertiesList = new ArrayList<>(Arrays.asList(propertiesString.split(",")));
					propertiesList.replaceAll(String::trim);

					String incompatibilitiesString = cellIterator.next().getStringCellValue();
					String[] incompatibilitiesArr = incompatibilitiesString.split(",");
					ArrayList<Integer> incompatibilities = new ArrayList<>();
					for (String incompatibility : incompatibilitiesArr) {
						if (!incompatibility.trim().isEmpty()) {
							incompatibilities.add(Integer.parseInt(incompatibility.trim()));
						}
					}

					Product product = new Product(id, name, category, propertiesList, incompatibilities);
					products.add(product);
				}

				file.close();
				workbook.close();
				System.out.println("Loaded !");
				Action = "Loaded successful !";
				Icon = true;

			} else {
				// User clicked "Cancel", handle it here
				System.out.println("load cancelled by the user");
				Action = "Loading has been cancelled !";
				Icon = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Action = "Error !";
			Icon = false;
		}
		return products;
	}

}
