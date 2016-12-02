package naga.toolkit.spi.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.fx.spi.view.NodeViewFactory;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.FlowPane;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class UnimplementedNode implements
        HBox,
        VBox,
        VPage,
        FlowPane,
        Button,
        CheckBox,
        RadioButton,
        SearchBox,
        Slider,
        Gauge,
        Table,
        TextView,
        TextField,
        Image,
        HtmlView,
        ToggleSwitch,
        LineChart,
        BarChart,
        PieChart,
        AreaChart,
        ScatterChart,
        DrawingNode {

    private final Object node;
    public UnimplementedNode() {
        Button button = Toolkit.get().createButton();
        button.setText("Unimplemented node");
        node = button.unwrapToNativeNode();
    }

    private Observable<ActionEvent> actionEventObservable = Observable.never();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }

    private Property<DisplayResultSet> displayResultSetProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultSetProperty;
    }

    private Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

    private Property<Integer> maxProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

    private Property<Integer> minProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private Property<String> placeholderProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> placeholderProperty() {
        return placeholderProperty;
    }

    private Property<Boolean> selectedProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

    private Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }

    private Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private Property<Integer> valueProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }

    Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

    private Property<GuiNode> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> headerProperty() {
        return headerProperty;
    }

    private Property<GuiNode> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> centerProperty() {
        return centerProperty;
    }

    private Property<GuiNode> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> footerProperty() {
        return footerProperty;
    }

    private ObservableList<GuiNode> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode> getChildren() {
        return children;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    private final Property<Image> imageProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }

    @Override
    public <T> T unwrapToNativeNode() {
        return (T) node;
    }

    @Override
    public void requestFocus() {
    }

    private final Property<Node> rootDrawableProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> rootNodeProperty() {
        return rootDrawableProperty;
    }

    public void setNodeViewFactory(NodeViewFactory nodeViewFactory) {
    }
}
