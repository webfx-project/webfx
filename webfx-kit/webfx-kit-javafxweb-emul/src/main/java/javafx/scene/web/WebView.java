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

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        widthProperty().set(width);
        heightProperty().set(height);
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
