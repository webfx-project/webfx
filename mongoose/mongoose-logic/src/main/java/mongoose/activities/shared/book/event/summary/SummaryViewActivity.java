package mongoose.activities.shared.book.event.summary;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.platform.json.Json;
import naga.platform.spi.Platform;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class SummaryViewActivity extends BookingProcessViewActivity {

    public SummaryViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        BorderPane summaryPanel = HighLevelComponents.createSectionPanel(null, null, "Summary", i18n);
        VBox panelsVBox = new VBox(summaryPanel);

        borderPane.setCenter(panelsVBox);

        nextButton.setOnAction(e -> getWorkingDocument().submit().setHandler(ar -> {
            if (ar.failed())
                Platform.log("Error submitting booking", ar.cause());
            else {
                //Platform.log("Document id = " + getWorkingDocument().getDocument().getId());
                getHistory().push("/event/" + getEvent().getPrimaryKey() + "/bookings", Json.createObject().set("refresh", Instant.now()));
            }
        }));
    }

    @Override
    public void onResume() {
        startLogic();
        super.onResume();
    }

    private void startLogic() {
    }

}
