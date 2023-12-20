package dev.webfx.kit.registry.javafxgraphics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.robot.RobotPeer;

/**
 * @author Bruno Salmon
 */
public class GwtRobotPeer implements RobotPeer {

    @Override
    public void keyPress(KeyCode keyCode) {

    }

    @Override
    public void keyRelease(KeyCode keyCode) {

    }

    @Override
    public double getMouseX() {
        return 0;
    }

    @Override
    public double getMouseY() {
        return 0;
    }

    @Override
    public void mouseMove(double x, double y) {

    }

    @Override
    public void mousePress(MouseButton... buttons) {

    }

    @Override
    public void mouseRelease(MouseButton... buttons) {

    }

    @Override
    public void mouseWheel(int wheelAmt) {

    }

    @Override
    public Color getPixelColor(double x, double y) {
        return Color.WHITE;
    }
}
