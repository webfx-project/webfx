package naga.toolkit.spi.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.layouts.HBox;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class UnimplementedNode<N> implements HBox<N, N>, VBox<N, N>, VPage<N, N>, Button<N>, CheckBox<N>, SearchBox<N>, Slider<N>, Gauge<N>, Table<N>, TextField<N>, ToggleSwitch<N>, LineChart<N>, BarChart<N>, PieChart<N>, AreaChart<N>, ScatterChart<N> {

    private final N node;
    public UnimplementedNode() {
        Button button = Toolkit.get().createButton();
        button.setText("Unimplemented node");
        node = (N) button.unwrapToNativeNode();
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

    Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private Property<Integer> valueProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }

    private Property<GuiNode<N>> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<N>> headerProperty() {
        return headerProperty;
    }

    private Property<GuiNode<N>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<N>> centerProperty() {
        return centerProperty;
    }

    private Property<GuiNode<N>> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<N>> footerProperty() {
        return footerProperty;
    }

    private ObservableList<GuiNode<N>> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<GuiNode<N>> getChildren() {
        return children;
    }

    @Override
    public N unwrapToNativeNode() {
        return node;
    }

    @Override
    public void requestFocus() {
    }
}
