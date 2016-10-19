package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.booking.BookingsProcessViewModelBuilder;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.platform.spi.Platform;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.shapes.ShapeFactory;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.util.ObservableLists;

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
        Rectangle rectangle = ShapeFactory.get().createRectangle();
        rectangle.setFill(Color.GREENYELLOW);
        rectangle.setHeight(100d);
        Platform.schedulePeriodic(10, () -> rectangle.setWidth(200d * (1 + Math.sin(System.currentTimeMillis() * Math.PI / 500))));
        ObservableLists.setAllNonNulls(drawingNode.getChildrenShapes(), rectangle);
        calendarPanel.setCenter(drawingNode);
        teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        panelsVBox = toolkit.createVBox(calendarPanel, teachingsPanel);
        contentNode = toolkit.createVPage()
                .setCenter(panelsVBox)
                .setFooter(previousButton);
    }
}
