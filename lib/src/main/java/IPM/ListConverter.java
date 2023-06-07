package IPM;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class provides methods for converting between ArrayList and ObservableList.
 */
public class ListConverter {

    /**
     * Converts an ArrayList to an ObservableList.
     *
     * @param arrayList the ArrayList to convert
     * @param <T>       the type of elements in the list
     * @return the converted ObservableList
     */
    public static <T> ObservableList<T> arrayListToObservableList(ArrayList<T> arrayList) {
        ObservableList<T> observableList = FXCollections.observableArrayList();
        observableList.addAll(arrayList);
        return observableList;
    }

    /**
     * Converts an ObservableList to an ArrayList.
     *
     * @param observableList the ObservableList to convert
     * @param <T>            the type of elements in the list
     * @return the converted ArrayList
     */
    public static <T> ArrayList<T> observableListToArrayList(ObservableList<T> observableList) {
        ArrayList<T> arrayList = new ArrayList<>(observableList);
        return arrayList;
    }
}
