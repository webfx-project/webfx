package naga.toolkit.providers.gwt.nodes.gauges;

import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.gauge.GaugeOptions;
import com.googlecode.gwt.charts.client.options.Animation;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.providers.gwt.nodes.charts.ChartApiLoader;
import naga.toolkit.providers.gwt.nodes.GwtNode;

/**
 * @author Bruno Salmon
 */
public class GwtGauge extends GwtNode<SimpleLayoutPanel> implements Gauge<SimpleLayoutPanel> {

    private com.googlecode.gwt.charts.client.gauge.Gauge gaugeWidget;
    private GaugeOptions options;
    private DataTable dataTable;

    public GwtGauge() {
        this(new SimpleLayoutPanel());
    }

    public GwtGauge(SimpleLayoutPanel container) {
        super(container);
        ChartApiLoader.onChartApiLoaded(() -> {
            gaugeWidget = createGaugeWidget();
            options = GaugeOptions.create();
            Animation animation = Animation.create();
            animation.setDuration(0);
            options.setAnimation(animation);
            if (getMin() != null)
                options.setMin(getMin());
            minProperty().addListener((observable, oldMin, newMin) -> {
                options.setMin(getMin());
                syncValueToVisual();
            });
            if (getMax() != null)
                options.setMax(getMax());
            maxProperty().addListener((observable, oldMax, newMax) -> {
                options.setMax(getMax());
                syncValueToVisual();
            });
            dataTable = DataTable.create();
            dataTable.addColumn(ColumnType.NUMBER, "");
            dataTable.addRow();
            valueProperty().addListener((observable, oldValue, newValue) -> syncValueToVisual());
            node.setWidget(gaugeWidget);
            if (node.isAttached()) {
                node.onResize();
                syncValueToVisual();
            } else
                node.addAttachHandler(event -> Toolkit.get().scheduler().scheduleDeferred(() -> {
                    node.onResize();
                    syncValueToVisual();
                }));
        });
    }

    private static com.googlecode.gwt.charts.client.gauge.Gauge createGaugeWidget() {
        com.googlecode.gwt.charts.client.gauge.Gauge gauge = new com.googlecode.gwt.charts.client.gauge.Gauge();
        //gauge.setWidth("100%");
        //gauge.setHeight("100%");
        return gauge;
    }

    private void syncValueToVisual() {
        Integer value = getValue();
        dataTable.setValue(0, 0, value == null ? 0 : value);
        gaugeWidget.draw(dataTable, options);
    }

    private final Property<Integer> maxProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

    private final Property<Integer> minProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private final Property<Integer> valueProperty = new SimpleObjectProperty<>();

    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }
}
