package com.sun.javafx.stage;

import javafx.stage.Window;

/**
 * Used to access internal window methods.
 */
public final class WindowHelper {
    private static WindowAccessor windowAccessor;

/*
    static {
        forceInit(Window.class);
    }
*/

    private WindowHelper() {
    }

    public static void notifyLocationChanged(final Window window,
                                             final double x,
                                             final double y) {
        windowAccessor.notifyLocationChanged(window, x, y);
    }

    public static void notifySizeChanged(final Window window,
                                         final double width,
                                         final double height) {
        windowAccessor.notifySizeChanged(window, width, height);
    }

/*
    static AccessControlContext getAccessControlContext(Window window) {
        return windowAccessor.getAccessControlContext(window);
    }
*/

    public static void setWindowAccessor(final WindowAccessor newAccessor) {
        if (windowAccessor != null) {
            throw new IllegalStateException();
        }

        windowAccessor = newAccessor;
    }

    public static WindowAccessor getWindowAccessor() {
        return windowAccessor;
    }

    public interface WindowAccessor {
        void notifyLocationChanged(Window window, double x, double y);

        void notifySizeChanged(Window window, double width, double height);

/*
        void notifyScreenChanged(Window window, Object from, Object to);

        float getUIScale(Window window);
        float getRenderScale(Window window);

        ReadOnlyObjectProperty<Screen> screenProperty(Window window);

        AccessControlContext getAccessControlContext(Window window);
*/
    }

/*
    private static void forceInit(final Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true,
                    classToInit.getClassLoader());
        } catch (final ClassNotFoundException e) {
            throw new AssertionError(e);  // Can't happen
        }
    }
*/
}
