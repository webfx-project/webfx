package javafx.scene.web;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import dev.webfx.kit.registry.javafxweb.JavaFxWebRegistry;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public class WebView extends Parent {

    private static final double DEFAULT_MIN_WIDTH = 0;
    private static final double DEFAULT_MIN_HEIGHT = 0;
    private static final double DEFAULT_PREF_WIDTH = 800;
    private static final double DEFAULT_PREF_HEIGHT = 600;
    private static final double DEFAULT_MAX_WIDTH = Double.MAX_VALUE;
    private static final double DEFAULT_MAX_HEIGHT = Double.MAX_VALUE;

    private final WebEngine engine = new WebEngine(this);

    private final DoubleProperty width = new SimpleDoubleProperty(this, "width");

    public final double getWidth() {
        return width.get();
    }

    /**
     * Width of this {@code WebView}.
     * @return the width property
     */
    public DoubleProperty widthProperty() {
        return width;
    }

    private final DoubleProperty height = new SimpleDoubleProperty(this, "height");

    public final double getHeight() {
        return height.get();
    }

    /**
     * Height of this {@code WebView}.
     * @return the height property
     */
    public DoubleProperty heightProperty() {
        return height;
    }

    private final StringProperty urlProperty = new SimpleStringProperty();

    public void setUrl(String url) {
        urlProperty.set(url);
    }

    public String getUrl() {
        return urlProperty.get();
    }

    public StringProperty urlProperty() {
        return urlProperty;
    }

    private final StringProperty loadContentProperty = new SimpleStringProperty();

    public void setLoadContent(String loadContent) {
        loadContentProperty.set(loadContent);
    }

    public String getLoadContent() {
        return loadContentProperty.get();
    }

    public StringProperty loadContentProperty() {
        return loadContentProperty;
    }

    public WebEngine getEngine() {
        return engine;
    }

    @Override public boolean isResizable() {
        return true;
    }

    @Override public void resize(double width, double height) {
        if ((width != this.width.get()) || (height != this.height.get())) {
            this.width.set(width);
            this.height.set(height);
            //NodeHelper.markDirty(this, DirtyBits.NODE_GEOMETRY);
            //NodeHelper.geomChanged(this);
        }
    }

    /**
     * Called during layout to determine the minimum width for this node.
     *
     * @return the minimum width that this node should be resized to during layout
     */
    @Override public final double minWidth(double height) {
        final double result = getMinWidth();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Called during layout to determine the minimum height for this node.
     *
     * @return the minimum height that this node should be resized to during layout
     */
    @Override public final double minHeight(double width) {
        final double result = getMinHeight();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }


    /**
     * Called during layout to determine the preferred width for this node.
     *
     * @return the preferred width that this node should be resized to during layout
     */
    @Override public final double prefWidth(double height) {
        final double result = getPrefWidth();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Called during layout to determine the preferred height for this node.
     *
     * @return the preferred height that this node should be resized to during layout
     */
    @Override public final double prefHeight(double width) {
        final double result = getPrefHeight();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }
    /**
     * Called during layout to determine the maximum width for this node.
     *
     * @return the maximum width that this node should be resized to during layout
     */
    @Override public final double maxWidth(double height) {
        final double result = getMaxWidth();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Called during layout to determine the maximum height for this node.
     *
     * @return the maximum height that this node should be resized to during layout
     */
    @Override public final double maxHeight(double width) {
        final double result = getMaxHeight();
        return Double.isNaN(result) || result < 0 ? 0 : result;
    }

    /**
     * Minimum width property.
     * @return the minWidth property
     */
    public DoubleProperty minWidthProperty() {
        if (minWidth == null) {
            minWidth = new /*Styleable*/SimpleDoubleProperty(DEFAULT_MIN_WIDTH) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.MIN_WIDTH;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return minWidth;
    }
    private DoubleProperty minWidth;

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        return (this.minWidth != null)
                ? this.minWidth.get()
                : DEFAULT_MIN_WIDTH;
    }

    /**
     * Minimum height property.
     * @return the minHeight property
     */
    public DoubleProperty minHeightProperty() {
        if (minHeight == null) {
            minHeight = new /*Styleable*/SimpleDoubleProperty(DEFAULT_MIN_HEIGHT) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.MIN_HEIGHT;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return minHeight;
    }
    private DoubleProperty minHeight;

    public final void setMinHeight(double value) {
        minHeightProperty().set(value);
    }

    public final double getMinHeight() {
        return (this.minHeight != null)
                ? this.minHeight.get()
                : DEFAULT_MIN_HEIGHT;
    }

    /**
     * Convenience method for setting minimum width and height.
     * @param minWidth the minimum width
     * @param minHeight the minimum height
     */
    public void setMinSize(double minWidth, double minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    /**
     * Preferred width property.
     * @return the prefWidth property
     */
    public DoubleProperty prefWidthProperty() {
        if (prefWidth == null) {
            prefWidth = new /*Styleable*/SimpleDoubleProperty(DEFAULT_PREF_WIDTH) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.PREF_WIDTH;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return prefWidth;
    }
    private DoubleProperty prefWidth;

    public final void setPrefWidth(double value) {
        prefWidthProperty().set(value);
    }

    public final double getPrefWidth() {
        return (this.prefWidth != null)
                ? this.prefWidth.get()
                : DEFAULT_PREF_WIDTH;
    }

    /**
     * Preferred height property.
     * @return the prefHeight property
     */
    public DoubleProperty prefHeightProperty() {
        if (prefHeight == null) {
            prefHeight = new /*Styleable*/SimpleDoubleProperty(DEFAULT_PREF_HEIGHT) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.PREF_HEIGHT;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return prefHeight;
    }
    private DoubleProperty prefHeight;

    public final void setPrefHeight(double value) {
        prefHeightProperty().set(value);
    }

    public final double getPrefHeight() {
        return (this.prefHeight != null)
                ? this.prefHeight.get()
                : DEFAULT_PREF_HEIGHT;
    }

    /**
     * Convenience method for setting preferred width and height.
     * @param prefWidth the preferred width
     * @param prefHeight the preferred height
     */
    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    /**
     * Maximum width property.
     * @return the maxWidth property
     */
    public DoubleProperty maxWidthProperty() {
        if (maxWidth == null) {
            maxWidth = new /*Styleable*/SimpleDoubleProperty(DEFAULT_MAX_WIDTH) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.MAX_WIDTH;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return maxWidth;
    }
    private DoubleProperty maxWidth;

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        return (this.maxWidth != null)
                ? this.maxWidth.get()
                : DEFAULT_MAX_WIDTH;
    }

    /**
     * Maximum height property.
     * @return the maxHeight property
     */
    public DoubleProperty maxHeightProperty() {
        if (maxHeight == null) {
            maxHeight = new /*Styleable*/SimpleDoubleProperty(DEFAULT_MAX_HEIGHT) {
                @Override
                public void invalidated() {
                    if (getParent() != null) {
                        getParent().requestLayout();
                    }
                }
                /*@Override
                public CssMetaData<WebView, Number> getCssMetaData() {
                    return StyleableProperties.MAX_HEIGHT;
                }*/
                @Override
                public Object getBean() {
                    return WebView.this;
                }
                @Override
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return maxHeight;
    }
    private DoubleProperty maxHeight;

    public final void setMaxHeight(double value) {
        maxHeightProperty().set(value);
    }

    public final double getMaxHeight() {
        return (this.maxHeight != null)
                ? this.maxHeight.get()
                : DEFAULT_MAX_HEIGHT;
    }

    /**
     * Convenience method for setting maximum width and height.
     * @param maxWidth the maximum width
     * @param maxHeight the maximum height
     */
    public void setMaxSize(double maxWidth, double maxHeight) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return bounds.deriveWithNewBounds(0, 0, 0, (float) getWidth(), (float) getHeight(), 0);
    }

    @Override
    protected double impl_minWidth(double height) {
        return 0;
    }

    @Override
    protected double impl_minHeight(double width) {
        return 0;
    }
    @Override
    protected double impl_prefWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    protected double impl_prefHeight(double width) {
        return Double.MAX_VALUE;
    }

    protected double impl_maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    protected double impl_maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    static {
        JavaFxWebRegistry.registerWebView();
    }

}
