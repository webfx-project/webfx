package naga.framework.ui.controls;

import javafx.scene.image.ImageView;
import naga.util.Strings;
import naga.fx.util.ImageStore;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public class ImageViewUtil {

    public static ImageView createImageView(Object urlOrJson) {
        if (urlOrJson == null)
            return null;
        if (urlOrJson instanceof JsonObject)
            return createImageView((JsonObject) urlOrJson);
        return createImageView(urlOrJson.toString());
    }

    public static ImageView createImageView(String urlOrJson) {
        if (!Strings.startsWith(urlOrJson, "{"))
            return ImageStore.createImageView(urlOrJson);
        return createImageView(Json.parseObject(urlOrJson));
    }

    public static ImageView createImageView(JsonObject json) {
        return ImageStore.createImageView(json.getString("url"), json.getDouble("width"), json.getDouble("height"));
    }
}
