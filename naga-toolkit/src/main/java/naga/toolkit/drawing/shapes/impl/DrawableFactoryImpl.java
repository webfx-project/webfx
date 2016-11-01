package naga.toolkit.drawing.shapes.impl;

import naga.toolkit.drawing.shapes.DrawableFactory;

/**
 * @author Bruno Salmon
 */
public class DrawableFactoryImpl implements DrawableFactory {

    public static final DrawableFactoryImpl SINGLETON = new DrawableFactoryImpl();

    private DrawableFactoryImpl() {
    }
}
