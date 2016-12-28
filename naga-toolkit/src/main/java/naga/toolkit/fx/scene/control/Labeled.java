package naga.toolkit.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geometry.Pos;
import naga.toolkit.fx.properties.Properties;
import naga.toolkit.fx.properties.markers.*;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.scene.paint.Color;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public abstract class Labeled extends Control implements
        HasTextProperty,
        HasGraphicProperty,
        HasImageUrlProperty,
        HasFontProperty,
        HasAlignmentProperty,
        HasTextAlignmentProperty,
        HasTextFillProperty
        {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a Label with no text and graphic
     */
    public Labeled() { }

    /**
     * Creates a Label with text
     * @param text The text for the label.
     */
    public Labeled(String text) {
        setText(text);
    }

    /**
     * Creates a Label with text and a graphic
     * @param text The text for the label.
     * @param graphic The graphic for the label.
     */
    public Labeled(String text, Node graphic) {
        setText(text);
        setGraphic(graphic);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<Node> graphicProperty = new SimpleObjectProperty<Node>() {
        @Override
        protected void invalidated() {
            setScene(getScene()); // This will propagate the scene reference into the graphic
        }
    };
    @Override
    public Property<Node> graphicProperty() {
        return graphicProperty;
    }

    private final Property<String> imageUrlProperty = new SimpleObjectProperty<String>() {
        @Override
        protected void invalidated() {
            String url = getValue();
            setGraphic(url == null ? null : new ImageView(url));
        }
    };
    @Override
    public Property<String> imageUrlProperty() {
        return imageUrlProperty;
    }

    private final Property<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Font> fontProperty() {
        return fontProperty;
    }

    private final Property<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.CENTER_LEFT);
    @Override
    public Property<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    private final Property<TextAlignment> textAlignmentProperty = new SimpleObjectProperty<>(TextAlignment.LEFT);
    @Override
    public Property<TextAlignment> textAlignmentProperty() {
        return textAlignmentProperty;
    }

    private final Property<Paint> textFillProperty = new SimpleObjectProperty<>(Color.BLACK);
    @Override
    public Property<Paint> textFillProperty() {
        return textFillProperty;
    }

    {
        // Requesting a new layout pass on text and image properties change
        Properties.runOnPropertiesChange(property -> {
            Parent parent = getParent();
            if (parent != null)
                parent.requestLayout();
        }, textProperty, graphicProperty, fontProperty(), alignmentProperty(), textAlignmentProperty());
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        Node graphic = getGraphic();
        if (graphic != null)
            graphic.setScene(scene);
    }
}
