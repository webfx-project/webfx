package naga.core.spi.toolkit.javafx.nodes;

/**
 * @author Bruno Salmon
 */

import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import naga.core.spi.toolkit.javafx.FxNode;
import naga.core.spi.toolkit.nodes.SearchBox;
import naga.core.spi.platform.Platform;
import naga.core.util.Strings;

public class FxSearchBox extends FxNode<Region> implements SearchBox<Region> {

    private final FxTextField fxTextField;

    public FxSearchBox() {
        super(new Region() {
            {
                getChildren().addAll(new TextField(), new Button());
                setMinHeight(24);
                //setPrefSize(200, 24);
                setMaxSize(Double.MAX_VALUE, 24);
                try {
                    getStylesheets().add(getClass().getResource("/css/search-box.css").toExternalForm());
                    getStyleClass().add("search-box");
                } catch (Exception e) {
                    Platform.log(e);
                }
            }
            @Override
            protected void layoutChildren() {
                getChildren().get(0).resize(getWidth(), getHeight());
                getChildren().get(1).resizeRelocate(getWidth() - 18, 6, 12, 13);
            }
        });
        TextField textField = (TextField) node.getChildrenUnmodifiable().get(0);
        textField.setPromptText("Search");
        Button clearButton = (Button) node.getChildrenUnmodifiable().get(1);
        clearButton.setVisible(false);

        clearButton.setOnAction(actionEvent -> clear());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            clearButton.setVisible(Strings.isNotEmpty(textField.getText()));
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE)
                clear();
        });
        fxTextField = new FxTextField(textField);
    }

    public void clear() {
        fxTextField.setText("");
        requestFocus();
    }

    @Override
    public void requestFocus() {
        fxTextField.requestFocus();
    }

    @Override
    public Property<String> textProperty() {
        return fxTextField.textProperty();
    }

    @Override
    public Property<String> placeholderProperty() {
        return fxTextField.placeholderProperty();
    }
}

