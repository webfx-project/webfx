package webfx.framework.ui.graphic.image;

import javafx.scene.image.ImageView;
import webfx.platforms.core.util.Strings;
import webfx.fxkits.core.util.ImageStore;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;

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
