package mongoose.activities.shared.logic.ui.theme;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;

/**
 * @author Bruno Salmon
 */
public class LightTheme implements ThemeProvider {

    @Override
    public void apply() {
        //Theme.setMainBackground(new Background(new BackgroundImage(new Image("images/theme/light/tt-white-leather.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        Theme.setMainBackground(new Background(new BackgroundFill(Color.grayRgb(0xF4), new CornerRadii(10), null)));
        Theme.setMainTextFill(Color.grayRgb(0x40));
        Theme.setDialogBackground(new Background(new BackgroundFill(LinearGradient.valueOf("#e0e0e0 0%, #e9e9e9 100%"), new CornerRadii(10), new Insets(1))));
        //Theme.setDialogBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(3))}, new BackgroundImage[]{new BackgroundImage(new Image("images/theme/light/tt-fabric-plaid.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)}));
        //Theme.setDialogBorder(new Border(new BorderStroke(LinearGradient.valueOf("#c7c7c7 0%, #d8d8d8 90%, #ccc 100%"), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        Theme.setDialogBorder(new Border(new BorderStroke(Color.grayRgb(0xc7), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        Theme.setDialogTextFill(Color.grayRgb(0x70));
    }
}
