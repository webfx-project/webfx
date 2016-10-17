package naga.toolkit.drawing.spi.impl;

import naga.commons.util.function.Factory;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.ShapeViewFactory;
import naga.toolkit.drawing.spi.view.ShapeView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ShapeViewFactoryImpl implements ShapeViewFactory {

    private Map<Class<? extends Shape>, Factory<? extends ShapeView>> shapeViewFactories = new HashMap<>();

    public <S extends Shape, T extends ShapeView<? super S>> void registerShapeViewFactory(Class<S> shapeClass, Factory<T> factory) {
        shapeViewFactories.put(shapeClass, factory);
    }

    @Override
    public <S extends Shape, T extends ShapeView<S>> T createShapeView(S shape) {
        Factory<? extends ShapeView> factory = shapeViewFactories.get(shape.getClass());
        return (T) factory.create();
    }
}
