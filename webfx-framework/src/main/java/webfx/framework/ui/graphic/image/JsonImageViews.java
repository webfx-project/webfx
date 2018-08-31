package webfx.framework.ui.graphic.image;

import javafx.scene.image.ImageView;
import webfx.util.Strings;
import webfx.fx.util.ImageStore;
import webfx.platform.services.json.Json;
import webfx.platform.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public final class JsonImageViews {

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
