package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.ShapeFactory;

/**
 * @author Bruno Salmon
 */
public class ShapeFactoryImpl implements ShapeFactory {

    public static final ShapeFactoryImpl SINGLETON = new ShapeFactoryImpl();

    private ShapeFactoryImpl() {
    }
}
