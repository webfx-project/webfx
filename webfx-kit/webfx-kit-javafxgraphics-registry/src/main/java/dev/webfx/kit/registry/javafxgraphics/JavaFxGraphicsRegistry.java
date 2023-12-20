package dev.webfx.kit.registry.javafxgraphics;

public class JavaFxGraphicsRegistry {

    public static native void registerGroup();

    public static native void registerRectangle();

    public static native void registerArc();

    public static native void registerCircle();

    public static native void registerLine();

    public static native void registerText();

    public static native void registerImageView();

    public static native void registerCanvas();

    public static native void registerPath();

    public static native void registerSVGPath();

    public static native void registerRegion();

    /**
     * Create the peer associated with the Robot class. The implementation must return an instance of the interface
     * javafx.scene.robot.RobotPeer declared in the webfx-kit-javafxgraphics-emul module. The signature below says it
     * returns an Object, but it's to avoid a circular dependency between the 2 modules. Conceptually it should be
     * RobotPeer.
     *
     * @return an instance of RobotPeer
     */
    public static native Object createRobotPeer();

}
