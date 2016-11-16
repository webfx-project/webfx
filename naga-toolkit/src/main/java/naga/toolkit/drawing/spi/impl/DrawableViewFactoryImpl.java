package naga.toolkit.drawing.spi.impl;

import naga.commons.util.function.Factory;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;
import naga.toolkit.drawing.spi.view.UnimplementedDrawableView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DrawableViewFactoryImpl implements DrawableViewFactory {

    private Map<Class<? extends Drawable>, Factory<? extends DrawableView>> drawableViewFactories = new HashMap<>();

    public <D extends Drawable, V extends DrawableView<? super D>> void registerDrawableViewFactory(Class<D> drawableClass, Factory<V> factory) {
        drawableViewFactories.put(drawableClass, factory);
    }

    @Override
    public <D extends Drawable, V extends DrawableView<D>> V createDrawableView(D drawable) {
        Factory<? extends DrawableView> factory = drawableViewFactories.get(drawable.getClass());
        if (factory != null)
            return (V) factory.create();
        System.out.println("WARNING: No DrawableView factory registered for " + drawable.getClass() + " in " + getClass());
        return (V) new UnimplementedDrawableView();
    }
}
