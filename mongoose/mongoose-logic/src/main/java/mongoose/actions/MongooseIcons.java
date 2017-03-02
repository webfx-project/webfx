package mongoose.actions;

import javafx.scene.image.ImageView;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class MongooseIcons {

    public final static String addIcon16 = "{url: 'images/16/actions/add.png', width: 16, height: 16}";
    public final static String removeIcon16 = "{url: 'images/16/actions/remove.png', width: 16, height: 16}";
    public final static String certificateIcon16 = "{url: 'images/certificate.svg', width: 16, height: 16}";
    public final static String calendarIcon16 = "{url: 'images/calendar.svg', width: 16, height: 16}";

    public static ImageView getLanguageIcon32(Object language) {
        return ImageStore.createImageView("images/32/system/lang_" + language + ".png");
    }
}
