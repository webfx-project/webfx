package javafx.scene.layout;

import javafx.beans.property.*;
import javafx.geometry.HPos;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * Defines optional layout constraints for a column in a {@link GridPane}.
 * If a ColumnConstraints object is added for a column in a gridpane, the gridpane
 * will use those constraint values when computing the column's width and layout.
 * <p>
 * For example, to create a GridPane with 5 columns 100 pixels wide:
 * <pre><code>
 *     GridPane gridpane = new GridPane();
 *     for (int i = 0; i < 5; i++) {
 *         ColumnConstraints column = new ColumnConstraints(100);
 *         gridpane.getColumnConstraints().add(column);
 *     }
 * </code></pre>
 * Or, to create a GridPane where columns take 25%, 50%, 25% of its width:
 * <pre><code>
 *     GridPane gridpane = new GridPane();
 *     ColumnConstraints col1 = new ColumnConstraints();
 *     col1.setPercentWidth(25);
 *     ColumnConstraints col2 = new ColumnConstraints();
 *     col2.setPercentWidth(50);
 *     ColumnConstraints col3 = new ColumnConstraints();
 *     col3.setPercentWidth(25);
 *     gridpane.getColumnConstraints().addAll(col1,col2,col3);
 * </code></pre>
 *
 * Note that adding an empty ColumnConstraints object has the effect of not setting
 * any constraints, leaving the GridPane to compute the column's layout based
 * solely on its content's size preferences and constraints.
 *
 * @since JavaFX 2.0
 */
public class ColumnConstraints extends ConstraintsBase {

    /**
     * Create a column constraint object with no properties set.
     */
    public ColumnConstraints() {
        super();
    }

    /**
     * Creates a column constraint object with a fixed width.
     * This is a convenience for setting the preferred width constraint to the
     * fixed value and the minWidth and maxWidth constraints to the USE_PREF_SIZE
     * flag to ensure the column is always that width.
     *
     * @param width the width of the column
     */
    public ColumnConstraints(double width) {
        this();
        setMinWidth(USE_PREF_SIZE);
        setPrefWidth(width);
        setMaxWidth(USE_PREF_SIZE);
    }

    /**
     * Creates a column constraint object with a fixed size range.
     * This is a convenience for setting the minimum, preferred, and maximum
     * width constraints.
     *
     */
    public ColumnConstraints(double minWidth, double prefWidth, double maxWidth) {
        this();
        setMinWidth(minWidth);
        setPrefWidth(prefWidth);
        setMaxWidth(maxWidth);
    }

    /**
     * Creates a column constraint object with a fixed size range, horizontal
     * grow priority, horizonal alignment, and horizontal fill behavior.
     *
     */
    public ColumnConstraints(double minWidth, double prefWidth, double maxWidth, Priority hgrow, HPos halignment, boolean fillWidth) {
        this(minWidth, prefWidth, maxWidth);
        setHgrow(hgrow);
        setHalignment(halignment);
        setFillWidth(fillWidth);
    }

    /**
     * The minimum width for the column.
     * This property is ignored if percentWidth is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the minimum width
     * will be computed to be the largest minimum width of the column's content.
     */
    private DoubleProperty minWidth;

    public final void setMinWidth(double value) {
        minWidthProperty().setValue(value);
    }

    public final double getMinWidth() {
        return minWidth == null ? USE_COMPUTED_SIZE : minWidth.getValue();
    }

    public final DoubleProperty minWidthProperty() {
        if (minWidth == null) {
            minWidth = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return minWidth;
    }

    /**
     * The preferred width for the column.
     * This property is ignored if percentWidth is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the preferred width
     * will be computed to be the largest preferred width of the column's content.
     */
    private DoubleProperty prefWidth;

    public final void setPrefWidth(double value) {
        prefWidthProperty().setValue(value);
    }

    public final double getPrefWidth() {
        return prefWidth == null ? USE_COMPUTED_SIZE : prefWidth.getValue();
    }

    public final DoubleProperty prefWidthProperty() {
        if (prefWidth == null) {
            prefWidth = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return prefWidth;
    }

    /**
     * The maximum width for the column.
     * This property is ignored if percentWidth is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the maximum width
     * will be computed to be the smallest maximum width of the column's content.
     */
    private DoubleProperty maxWidth;

    public final void setMaxWidth(double value) {
        maxWidthProperty().setValue(value);
    }

    public final double getMaxWidth() {
        return maxWidth == null ? USE_COMPUTED_SIZE : maxWidth.getValue();
    }

    public final DoubleProperty maxWidthProperty() {
        if (maxWidth == null) {
            maxWidth = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return maxWidth;
    }

    /**
     * The width percentage of the column.  If set to a value greater than 0,
     * the column will be resized to this percentage of the gridpane's available
     * width and the other size constraints (minWidth, prefWidth, maxWidth, hgrow)
     * will be ignored.
     *
     * The default value is -1, which means the percentage will be ignored.
     */
    private DoubleProperty percentWidth;

    public final void setPercentWidth(double value) {
        percentWidthProperty().setValue(value);
    }

    public final double getPercentWidth() {
        return percentWidth == null ? -1 : percentWidth.getValue();
    }

    public final DoubleProperty percentWidthProperty() {
        if (percentWidth == null) {
            percentWidth = new SimpleDoubleProperty(-1d) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "percentWidth";
                }
            };
        }
        return percentWidth;
    }


    /**
     * The horizontal grow priority for the column.  If set, the gridpane will
     * use this priority to determine whether the column should be given any
     * additional width if the gridpane is resized larger than its preferred width.
     * This property is ignored if percentWidth is set.
     * <p>
     * This default value is null, which means that the column's grow priority
     * will be derived from largest grow priority set on a content node.
     */
    private ObjectProperty<Priority> hgrow;

    public final void setHgrow(Priority value) {
        hgrowProperty().set(value);
    }

    public final Priority getHgrow() {
        return hgrow == null ? null : hgrow.get();
    }

    public final ObjectProperty<Priority> hgrowProperty() {
        if (hgrow == null) {
            hgrow = new ObjectPropertyBase<Priority>() {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "hgrow";
                }
            };
        }
        return hgrow;
    }

    /**
     * The horizontal alignment for the column. If set, will be the default
     * horizontal alignment for nodes contained within the column.
     * <p>
     * The default value is null, which means the column alignment will fall
     * back to the default halignment set on the gridpane.
     */
    private ObjectProperty<HPos> halignment;

    public final void setHalignment(HPos value) {
        halignmentProperty().set(value);
    }

    public final HPos getHalignment() {
        return halignment == null ? null : halignment.get();
    }

    public final ObjectProperty<HPos> halignmentProperty() {
        if (halignment == null) {
            halignment = new ObjectPropertyBase<HPos>() {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "halignment";
                }
            };
        }
        return halignment;
    }

    /**
     * The horizontal fill policy for the column.  The gridpane will
     * use this property to determine whether nodes contained within the column
     * should be expanded to fill the column or kept to their preferred widths.
     * <p>
     * The default value is true.
     *
     */
    private Property<Boolean> fillWidth;

    public final void setFillWidth(boolean value) {
        fillWidthProperty().setValue(value);
    }

    public final boolean isFillWidth() {
        return fillWidth == null ? true : fillWidth.getValue();
    }

    public final Property<Boolean> fillWidthProperty() {
        if (fillWidth == null) {
            fillWidth = new SimpleObjectProperty<Boolean>(true) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return ColumnConstraints.this;
                }

                @Override
                public String getName() {
                    return "fillWidth";
                }
            };
        }
        return fillWidth;
    }

    /**
     * Returns a string representation of this {@code ColumnConstraints} object.
     * @return a string representation of this {@code ColumnConstraints} object.
     */
    @Override public String toString() {
        return "ColumnConstraints percentWidth="+getPercentWidth()+
                " minWidth="+getMinWidth()+
                " prefWidth="+getPrefWidth()+
                " maxWidth="+getMaxWidth()+
                " hgrow="+getHgrow()+
                " fillWidth="+isFillWidth()+
                " halignment="+getHalignment();
    }
}
