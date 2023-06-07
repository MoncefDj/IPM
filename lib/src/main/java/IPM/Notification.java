package IPM;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * This class represents a custom notification that displays an icon, a header, and a content text.
 */
public class Notification extends MFXSimpleNotification {
    private final StringProperty headerText = new SimpleStringProperty();
    private final StringProperty contentText = new SimpleStringProperty();

    /**
     * Constructs a new Notification object and sets up the icon, header, and content text.
     */
    public Notification() {
        // Create the icon based on the ExcelFileHandler's icon value
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

        // Create the header label and HBox container
        Label headerLabel = new Label();
        headerLabel.textProperty().bind(headerText);
        HBox header = new HBox(10, icon, headerLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(InsetsFactory.of(5, 10, 5, 0));
        header.setMaxWidth(Double.MAX_VALUE);

        // Create the content label and BorderPane container
        Label contentLabel = new Label();
        contentLabel.getStyleClass().add("content");
        contentLabel.textProperty().bind(contentText);
        contentLabel.setWrapText(true);
        contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        contentLabel.setAlignment(Pos.TOP_LEFT);
        BorderPane container = new BorderPane();
        container.getStyleClass().add("notification");
        container.setTop(header);
        container.getStylesheets().add(ResourcesLoader.load("css/ExampleNotification.css"));

        // Set the content of the notification to the container
        setContent(container);
    }

    /**
     * Sets the content text of the notification.
     *
     * @param contentText the content text to set
     */
    public void setContentText(String contentText) {
        this.contentText.set(contentText);
    }
}
