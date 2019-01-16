package mongoose.client.activity.themes;

import javafx.scene.paint.Color;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.framework.client.ui.util.border.BorderUtil;

/**
 * @author Bruno Salmon
 */
public final class DarkTheme implements ThemeProvider {

    @Override
    public void apply() {
        Theme.setMainBackground(BackgroundUtil.newWebColorBackground("#101214"));
        Theme.setMainTextFill(Color.WHITE);
        Theme.setDialogBackground(BackgroundUtil.newBackground(Color.grayRgb(42),10));
        Theme.setDialogBorder(BorderUtil.newBorder(Color.rgb(237, 162, 57),10));
        Theme.setDialogTextFill(Color.WHITE);
    }
}
