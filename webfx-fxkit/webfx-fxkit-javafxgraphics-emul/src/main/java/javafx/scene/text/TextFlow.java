package javafx.scene.text;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Temporary simplified implementation of TextFlow that works with 1 Text.
 * The html peer makes the text wrap (see HtmlScenePeer).
 *
 * @author Bruno Salmon
 */
public class TextFlow extends /*Pane */ VBox {

    {
        prefWidthProperty().bind(maxWidthProperty());
    }

    public TextFlow() {
    }

    public TextFlow(Node... children) {
        super(children);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    public double prefHeight(double width) {
        Insets insets = getPadding();
        double childPrefHeight;
        if (getChildren().isEmpty())
            childPrefHeight = 0;
        else
            childPrefHeight = getChildren().get(0).prefHeight(width);
        return snapSpace(insets.getTop()) +
                childPrefHeight +
                snapSpace(insets.getBottom());
    }

}
