package javafx.scene.text;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import dev.webfx.kit.registry.javafxgraphics.JavaFxGraphicsRegistry;
import javafx.beans.property.*;
import javafx.geometry.VPos;
import javafx.scene.shape.Shape;

/**
 * @author Bruno Salmon
 */
public class Text extends Shape implements
        HasXProperty,
        HasYProperty,
        HasTextProperty,
        HasTextOriginProperty,
        HasTextAlignmentProperty,
        HasWrappingWidthProperty,
        HasFontProperty {

    public Text() {
    }

    public Text(String text) {
        setText(text);
    }

    public Text(double x, double y, String text) {
        this(text);
        setX(x);
        setY(y);
    }

    private final DoubleProperty xProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty xProperty() {
        return xProperty;
    }

    private final ObjectProperty<VPos> textOriginProperty = new SimpleObjectProperty<>(VPos.BASELINE);
    @Override
    public ObjectProperty<VPos> textOriginProperty() {
        return textOriginProperty;
    }

    private final ObjectProperty<TextAlignment> textAlignmentProperty = new SimpleObjectProperty<>(/* null for web CSS instead of TextAlignment.LEFT */);
    @Override
    public ObjectProperty<TextAlignment> textAlignmentProperty() {
        return textAlignmentProperty;
    }

    private final DoubleProperty wrappingWidthProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty wrappingWidthProperty() {
        return wrappingWidthProperty;
    }

    private final DoubleProperty lineSpacingProperty = new SimpleDoubleProperty(0d);

    public DoubleProperty lineSpacingProperty() {
        return lineSpacingProperty;
    }

    public double getLineSpacing() {
        return lineSpacingProperty.get();
    }

    public void setLineSpacing(double lineSpacing) {
        this.lineSpacingProperty.set(lineSpacing);
    }

    private final DoubleProperty yProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty yProperty() {
        return yProperty;
    }

    private final StringProperty textProperty = new SimpleStringProperty();
    @Override
    public StringProperty textProperty() {
        return textProperty;
    }

    private final ObjectProperty<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Font> fontProperty() {
        return fontProperty;
    }

    private static final TextBoundsType DEFAULT_BOUNDS_TYPE = TextBoundsType.LOGICAL;

    /**
     * Determines how the bounds of the text node are calculated.
     * Logical bounds is a more appropriate default for text than
     * the visual bounds. See {@code TextBoundsType} for more information.
     *
     * @defaultValue TextBoundsType.LOGICAL
     */
    private Property<TextBoundsType> boundsType;

    public final void setBoundsType(TextBoundsType value) {
        boundsTypeProperty().setValue(value);
    }

    public final TextBoundsType getBoundsType() {
        return boundsType == null ?
                DEFAULT_BOUNDS_TYPE : boundsTypeProperty().getValue();
    }

    public final Property<TextBoundsType> boundsTypeProperty() {
        if (boundsType == null) {
            boundsType =
                    new SimpleObjectProperty<>(DEFAULT_BOUNDS_TYPE)/* {
                        @Override public Object getBean() { return Text.this; }
                        @Override public String getName() { return "boundsType"; }
                        @Override public CssMetaData<Text,TextBoundsType> getCssMetaData() {
                            return StyleableProperties.BOUNDS_TYPE;
                        }
                        @Override public void invalidated() {
                            TextLayout layout = getTextLayout();
                            int type = 0;
                            if (boundsType.get() == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
                                type |= TextLayout.BOUNDS_CENTER;
                            }
                            if (layout.setBoundsType(type)) {
                                needsTextLayout();
                            } else {
                                impl_geomChanged();
                            }
                        }
                    }*/;
        }
        return boundsType;
    }

    /**
     * Defines if each line of text should have a line through it.
     *
     * @return if each line of text should have a line through it
     * @defaultValue false
     */
    public final BooleanProperty strikethroughProperty() {
        if (strikethroughProperty == null)
            strikethroughProperty = new SimpleBooleanProperty();
        return strikethroughProperty;
    }

    private BooleanProperty strikethroughProperty;

    public final void setStrikethrough(boolean value) {
        strikethroughProperty().set(value);
    }

    public final boolean isStrikethrough() {
        if (strikethroughProperty == null) {
            return false;
        }
        return strikethroughProperty.get();
    }


    @Override
    public double getBaselineOffset() {
        Font font = getFont();
        if (font != null)
            return font.getBaselineOffset();
        return super.getBaselineOffset();
    }

    private static boolean warned;
    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (!warned) {
            System.out.println("Warning: Text.impl_computeGeomBounds() not implemented");
            warned = true;
        }
        return new BoxBounds();
        //throw new UnsupportedOperationException("Text.impl_computeGeomBounds() not implemented");
    }

    static {
        JavaFxGraphicsRegistry.registerText();
    }
}
