package emul.javafx.scene.layout;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.util.Callback;
import emul.javafx.geometry.Insets;
import emul.javafx.geometry.Orientation;
import emul.javafx.geometry.Pos;
import emul.javafx.scene.Node;
import webfx.fx.properties.markers.HasAlignmentProperty;
import webfx.fx.properties.markers.HasSpacingProperty;

import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class Box extends Pane implements
        HasSpacingProperty,
        HasAlignmentProperty {

    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/
    private static final String MARGIN_CONSTRAINT = "box-margin";

    /**
     * Sets the margin for the child when contained by an box.
     * If set, the box will layout the child with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child mode of the box
     * @param value the margin of space around the child
     */
    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of an hbox
     * @return the margin for the child or null if no margin was set
     */
    public static Insets getMargin(Node child) {
        return (Insets)getConstraint(child, MARGIN_CONSTRAINT);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    static final Callback<Node, Insets> marginAccessor = Box::getMargin;

    private boolean biasDirty = true;
    boolean performingLayout = false;
    protected Orientation bias;
    private double[][] tempArray;

    Box() {
        super();
    }

    Box(double spacing) {
        this();
        setSpacing(spacing);
    }

    Box(Node... children) {
        super();
        getChildren().addAll(children);
    }

    Box(double spacing, Node... children) {
        this();
        setSpacing(spacing);
        getChildren().addAll(children);
    }

    private final Property<Double> spacingProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> spacingProperty() {
        return spacingProperty;
    }

    private final Property<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.TOP_LEFT);
    @Override
    public Property<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    /**
     *
     * @return null unless one of its children has a content bias.
     */
    @Override
    public Orientation getContentBias() {
        if (biasDirty) {
            bias = null;
            List<Node> children = getManagedChildren();
            for (Node child : children) {
                Orientation contentBias = child.getContentBias();
                if (contentBias != null) {
                    bias = contentBias;
                    if (contentBias == Orientation.HORIZONTAL)
                        break;
                }
            }
            biasDirty = false;
        }
        return bias;
    }

    @Override
    public void requestLayout() {
        if (!performingLayout) {
            biasDirty = true;
            bias = null;
            super.requestLayout();
        }
    }

    static double sum(double[] array, int size) {
        int i = 0;
        double res = 0;
        while (i != size)
            res += array[i++];
        return res;
    }

    double[][] getTempArray(int size) {
        if (tempArray == null)
            tempArray = new double[2][size]; // First array for the result, second for temporary computations
        else if (tempArray[0].length < size)
            tempArray = new double[2][Math.max(tempArray.length * 3, size)];
        return tempArray;
    }

}
