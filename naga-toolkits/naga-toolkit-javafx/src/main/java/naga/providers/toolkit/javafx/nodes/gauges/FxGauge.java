package naga.providers.toolkit.javafx.nodes.gauges;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.property.Property;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.properties.conversion.ConvertedProperty;

/**
 * @author Bruno Salmon
 */
public class FxGauge extends FxNode<Gauge> implements naga.toolkit.spi.nodes.gauges.Gauge {

    public FxGauge() {
        this(createGauge());
    }

    public FxGauge(Gauge gauge) {
        super(gauge);
    }

    private static Gauge createGauge() {
        return GaugeBuilder.create().build();
    }


    private ConvertedProperty<Integer, Number> valueProperty = JavaFxToolkit.numberToIntegerProperty(node.valueProperty());
    @Override
    public Property<Integer> valueProperty() {
        return valueProperty;
    }

    private ConvertedProperty<Integer, Number> minProperty = JavaFxToolkit.numberToIntegerProperty(node.minValueProperty());
    @Override
    public Property<Integer> minProperty() {
        return minProperty;
    }

    private ConvertedProperty<Integer, Number> maxProperty = JavaFxToolkit.numberToIntegerProperty(node.maxValueProperty());
    @Override
    public Property<Integer> maxProperty() {
        return maxProperty;
    }

}
