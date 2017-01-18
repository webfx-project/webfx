package mongoose.activities.shared.theme;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public class DarkTheme implements ThemeProvider {

    @Override
    public void apply() {
        Theme.setMainBackground(new Background(new BackgroundFill(Color.web("#101214"), null, null)));
        Theme.setMainTextFill(Color.WHITE);
        Theme.setDialogBackground(new Background(new BackgroundFill(Color.grayRgb(42), new CornerRadii(10), null)));
        Theme.setDialogBorder(new Border(new BorderStroke(Color.rgb(237, 162, 57), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THICK)));
        Theme.setDialogTextFill(Color.WHITE);
    }
}
