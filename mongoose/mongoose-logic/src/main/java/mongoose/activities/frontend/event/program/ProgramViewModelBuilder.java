package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.animation.*;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.transform.Rotate;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModelBuilder extends BookingsProcessViewModelBuilder<ProgramViewModel> {

    protected VPage calendarPanel;
    protected VPage teachingsPanel;
    protected VBox panelsVBox;

    @Override
    protected ProgramViewModel createViewModel() {
        return new ProgramViewModel(contentNode, previousButton);
    }

    protected void buildComponents(Toolkit toolkit, I18n i18n) {
        super.buildComponents(toolkit, i18n);
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n);
        // Drawing node (just testing API for now)
        DrawingNode drawingNode = toolkit.createDrawingNode();
        Rectangle r = DrawableFactory.get().createRectangle();
        double width = 200d;
        double height = 50d;
        r.setWidth(width);
        r.setHeight(height);
        r.setFill(Color.web("0x609993"));

        r.setStroke(Color.ORANGE);
        r.setStrokeWidth(5d);
        r.getStrokeDashArray().setAll(25d, 20d, 5d, 20d);
        r.setStrokeLineCap(StrokeLineCap.ROUND);
        r.setStrokeLineJoin(StrokeLineJoin.ROUND);

        TextShape dayText = DrawableFactory.get().createText();
        dayText.setText("Vendredi");
        dayText.setWrappingWidth(width);
        dayText.setTextAlignment(TextAlignment.CENTER);
        dayText.setFont(Font.font("Verdana", 11));
        dayText.setFill(Color.WHITE);
        dayText.setTextOrigin(VPos.TOP);
        dayText.setY(3d);
        TextShape digitText = DrawableFactory.get().createText();
        digitText.setText("2");
        digitText.setWrappingWidth(width);
        digitText.setTextAlignment(TextAlignment.CENTER);
        digitText.setFont(Font.font("Verdana", 20));
        digitText.setFill(Color.WHITE);
        digitText.setTextOrigin(VPos.CENTER);
        digitText.setY(height / 2);
        TextShape monthText = DrawableFactory.get().createText();
        monthText.setText("Décembre");
        monthText.setWrappingWidth(width);
        monthText.setTextAlignment(TextAlignment.CENTER);
        monthText.setFont(Font.font("Verdana", 11));
        monthText.setFill(Color.WHITE);
        monthText.setTextOrigin(VPos.BOTTOM);
        monthText.setY(height - 3d);
        Group group = DrawableFactory.get().createGroup();
        group.getDrawableChildren().setAll(r, dayText, digitText, monthText);
        drawingNode.getDrawableChildren().setAll(group);
        Rotate rotate = Rotate.create();
        rotate.setPivotX(width / 2);
        rotate.setPivotY(height / 2);
        group.getTransforms().setAll(rotate);
        digitText.getTransforms().setAll(rotate);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.ofSeconds(5), new KeyValue(rotate.angleProperty(), 360d)
                ,new KeyValue(r.strokeDashOffsetProperty(), 70d * 5)
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.setAutoReverse(true);
        timeline.play();
        calendarPanel.setCenter(drawingNode);
        teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        panelsVBox = toolkit.createVBox(calendarPanel, teachingsPanel);
        contentNode = toolkit.createVPage()
                .setCenter(panelsVBox)
                .setFooter(previousButton);
    }
}
