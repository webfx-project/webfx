package webfx.demo.enzoclocks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Bruno Salmon
 */
final class PlusButton extends Region {

    private final Circle ledCentre = new Circle();
    private final Line hLine = new Line(), vLine = new Line();
    private final Paint pressedFill, releasedFill;

    PlusButton(Color ledColor) {
        releasedFill = ledColor;
        pressedFill  = ledColor.brighter();
        ledCentre.setFill(releasedFill);
        ledCentre.setOnMousePressed( e -> ledCentre.setFill(pressedFill));
        ledCentre.setOnMouseReleased(e -> ledCentre.setFill(releasedFill));

        Color lineColor = Color.IVORY;
        hLine.setStroke(lineColor);
        hLine.setStrokeLineCap(StrokeLineCap.ROUND);
        vLine.setStroke(lineColor);
        vLine.setStrokeLineCap(StrokeLineCap.ROUND);
        Node sign = new Group(hLine, vLine);
        getChildren().setAll(ledCentre, sign);
        sign.setMouseTransparent(true);
    }

    final void setOnAction(EventHandler<ActionEvent> actionHandler) {
        ledCentre.setOnMouseClicked(e -> actionHandler.handle(new ActionEvent(this, this)));
    }

    @Override
    public void layoutChildren() {
        double width = getWidth();
        double height = getHeight();
        double radius = Math.min(width, height) / 2;
        ledCentre.setRadius(radius);
        double lineLength = 0.4 * radius;
        hLine.setStartX(width / 2 - lineLength);
        hLine.setEndX(width / 2 + lineLength);
        hLine.setStrokeWidth(0.2 * radius);
        vLine.setStartY(width / 2 - lineLength);
        vLine.setEndY(width / 2 + lineLength);
        vLine.setStrokeWidth(0.2 * radius);
        for (Node child : getChildren())
            if (!(child instanceof Group))
                layoutInArea(child, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(hLine, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(vLine, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
    }
}
