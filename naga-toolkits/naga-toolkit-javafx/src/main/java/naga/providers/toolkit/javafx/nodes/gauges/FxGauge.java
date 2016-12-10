package naga.providers.toolkit.javafx.nodes.gauges;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.property.Property;
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


    private ConvertedProperty<Double, Number> valueProperty = ConvertedProperty.numberToDoubleProperty(node.valueProperty());
    @Override
    public Property<Double> valueProperty() {
        return valueProperty;
    }

    private ConvertedProperty<Double, Number> minProperty = ConvertedProperty.numberToDoubleProperty(node.minValueProperty());
    @Override
    public Property<Double> minProperty() {
        return minProperty;
    }

    private ConvertedProperty<Double, Number> maxProperty = ConvertedProperty.numberToDoubleProperty(node.maxValueProperty());
    @Override
    public Property<Double> maxProperty() {
        return maxProperty;
    }

}
