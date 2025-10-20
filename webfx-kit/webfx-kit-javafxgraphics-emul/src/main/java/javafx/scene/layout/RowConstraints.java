package javafx.scene.layout;

import javafx.beans.property.*;
import javafx.geometry.VPos;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * Defines optional layout constraints for a row in a {@link GridPane}.
 * If a RowConstraints object is added for a row in a gridpane, the gridpane
 * will use those constraint values when computing the row's height and layout.
 * <p>
 * For example, to create a GridPane with 10 rows 50 pixels tall:
 * <pre><code>
 *     GridPane gridpane = new GridPane();
 *     for (int i = 0; i < 10; i++) {
 *         RowConstraints row = new RowConstraints(50);
 *         gridpane.getRowConstraints().add(row);
 *     }
 * </code></pre>
 * Or, to create a GridPane where rows take 25%, 50%, 25% of its width:
 * <pre><code>
 *     GridPane gridpane = new GridPane();
 *     RowConstraints row1 = new RowConstraints();
 *     row1.setPercentWidth(25);
 *     RowConstraints row2 = new RowConstraints();
 *     row2.setPercentWidth(50);
 *     RowConstraints row3 = new RowConstraints();
 *     row3.setPercentWidth(25);
 *     gridpane.getRowConstraints().addAll(row1,row2,row3);
 * </code></pre>
 *
 * Note that adding an empty RowConstraints object has the effect of not setting
 * any constraints, leaving the GridPane to compute the row's layout based
 * solely on its content's size preferences and constraints.
 *
 * @since JavaFX 2.0
 */
public class RowConstraints extends ConstraintsBase {

    /**
     * Creates a row constraints object with no properties set.
     */
    public RowConstraints() {
        super();
    }

    /**
     * Creates a row constraint object with a fixed height.
     * This is a convenience for setting the preferred height constraint to the
     * fixed value and the minHeight and maxHeight constraints to the USE_PREF_SIZE
     * flag to ensure the row is always that height.
     *
     * @param height the height of the row
     */
    public RowConstraints(double height) {
        this();
        setMinHeight(USE_PREF_SIZE);
        setPrefHeight(height);
        setMaxHeight(USE_PREF_SIZE);
    }

    /**
     * Creates a row constraint object with a fixed size range.
     * This is a convenience for setting the minimum, preferred, and maximum
     * height constraints.
     *
     */
    public RowConstraints(double minHeight, double prefHeight, double maxHeight) {
        this();
        setMinHeight(minHeight);
        setPrefHeight(prefHeight);
        setMaxHeight(maxHeight);
    }

    /**
     * Creates a row constraint object with a fixed size range, vertical
     * grow priority, vertical alignment, and vertical fill behavior.
     *
     */
    public RowConstraints(double minHeight, double prefHeight, double maxHeight, Priority vgrow, VPos valignment, boolean fillHeight) {
        this(minHeight, prefHeight, maxHeight);
        setVgrow(vgrow);
        setValignment(valignment);
        setFillHeight(fillHeight);
    }

    /**
     * The minimum height for the row.
     * This property is ignored if percentHeight is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the minimum height
     * will be computed to be the largest minimum height of the row's content.
     */
    private DoubleProperty minHeight;

    public final void setMinHeight(double value) {
        minHeightProperty().setValue(value);
    }

    public final double getMinHeight() {
        return minHeight == null ? USE_COMPUTED_SIZE : minHeight.getValue();
    }

    public final DoubleProperty minHeightProperty() {
        if (minHeight == null) {
            minHeight = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return minHeight;
    }

    /**
     * The preferred height for the row.
     * This property is ignored if percentHeight is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the preferred height
     * will be computed to be the largest preferred height of the row's content.
     */
    private DoubleProperty prefHeight;

    public final void setPrefHeight(double value) {
        prefHeightProperty().setValue(value);
    }

    public final double getPrefHeight() {
        return prefHeight == null ? USE_COMPUTED_SIZE : prefHeight.getValue();
    }

    public final DoubleProperty prefHeightProperty() {
        if (prefHeight == null) {
            prefHeight = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return prefHeight;
    }

    /**
     * The maximum height for the row.
     * This property is ignored if percentHeight is set.
     * <p>
     * The default value is USE_COMPUTED_SIZE, which means the maximum height
     * will be computed to be the smallest maximum height of the row's content.
     */
    private DoubleProperty maxHeight;

    public final void setMaxHeight(double value) {
        maxHeightProperty().setValue(value);
    }

    public final double getMaxHeight() {
        return maxHeight == null ? USE_COMPUTED_SIZE : maxHeight.getValue();
    }

    public final DoubleProperty maxHeightProperty() {
        if (maxHeight == null) {
            maxHeight = new SimpleDoubleProperty(USE_COMPUTED_SIZE) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return maxHeight;
    }

    /**
     * The height percentage of the row.  If set to a value greater than 0, the
     * row will be resized to that percentage of the available gridpane height and
     * the other size constraints (minHeight, prefHeight, maxHeight, vgrow) will
     * be ignored.
     *
     * The default value is -1, which means the percentage will be ignored.
     */
    private DoubleProperty percentHeight;

    public final void setPercentHeight(double value) {
        percentHeightProperty().setValue(value);
    }

    public final double getPercentHeight() {
        return percentHeight == null ? -1 : percentHeight.getValue();
    }

    public final DoubleProperty percentHeightProperty() {
        if (percentHeight == null) {
            percentHeight = new SimpleDoubleProperty(-1d) {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "percentHeight";
                }
            };
        }
        return percentHeight;
    }

    /**
     * The vertical grow priority for the row.  If set, the gridpane will
     * use this priority to determine whether the row should be given any
     * additional height if the gridpane is resized larger than its preferred height.
     * This property is ignored if percentHeight is set.
     * <p>
     * This default value is null, which means that the row's grow priority
     * will be derived from largest grow priority set on a content node.
     */
    private ObjectProperty<Priority> vgrow;

    public final void setVgrow(Priority value) {
        vgrowProperty().set(value);
    }

    public final Priority getVgrow() {
        return vgrow == null ? null : vgrow.get();
    }

    public final ObjectProperty<Priority> vgrowProperty() {
        if (vgrow == null) {
            vgrow = new ObjectPropertyBase<Priority>() {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "vgrow";
                }
            };
        }
        return vgrow;
    }

    /**
     * The vertical alignment for the row. If set, will be the default
     * vertical alignment for nodes contained within the row.
     * If this property is set to VPos.BASELINE, then the fillHeight property
     * will be ignored and nodes will always be resized to their preferred heights.
     */
    private ObjectProperty<VPos> valignment;

    public final void setValignment(VPos value) {
        valignmentProperty().set(value);
    }

    public final VPos getValignment() {
        return valignment == null ? null : valignment.get();
    }

    public final ObjectProperty<VPos> valignmentProperty() {
        if (valignment == null) {
            valignment = new ObjectPropertyBase<VPos>() {

                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "valignment";
                }
            };
        }
        return valignment;
    }

    /**
     * The vertical fill policy for the row.  The gridpane will
     * use this property to determine whether nodes contained within the row
     * should be expanded to fill the row's height or kept to their preferred heights.
     * <p>
     * The default value is true.
     */
    private BooleanProperty fillHeight;

    public final void setFillHeight(boolean value) {
        fillHeightProperty().setValue(value);
    }

    public final boolean isFillHeight() {
        return fillHeight == null ? true : fillHeight.getValue();
    }

    public final BooleanProperty fillHeightProperty() {
        if (fillHeight == null) {
            fillHeight = new SimpleBooleanProperty(true) {
                @Override
                protected void invalidated() {
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return RowConstraints.this;
                }

                @Override
                public String getName() {
                    return "fillHeight";
                }
            };
        }
        return fillHeight;
    }

    /**
     * Returns a string representation of this {@code RowConstraints} object.
     * @return a string representation of this {@code RowConstraints} object.
     */
    @Override public String toString() {
        return "RowConstraints percentHeight="+getPercentHeight()+
                " minHeight="+getMinHeight()+
                " prefHeight="+getPrefHeight()+
                " maxHeight="+getMaxHeight()+
                " vgrow="+getVgrow()+
                " fillHeight="+isFillHeight()+
                " valignment="+getValignment();
    }
}
