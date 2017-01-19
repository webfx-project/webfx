package naga.framework.activity.client;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import naga.commons.util.Strings;
import naga.commons.util.async.Future;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
import naga.platform.activity.AbstractActivity;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractUiActivity<C extends UiActivityContext<C>> extends AbstractActivity<C> implements UiActivityContextMixin<C> {

    private final Property<Boolean> activeProperty = new SimpleObjectProperty<>(false); // Should be stored in UiContext?

    protected ReadOnlyProperty<Boolean> activeProperty() {
        return activeProperty;
    }

    @Override
    protected void setActive(boolean active) {
        super.setActive(active);
        activeProperty.setValue(active);
    }

    @Override
    public Future<Void> onResumeAsync() {
        if (Toolkit.get().isReady())
            return Future.runAsync(this::onResume);
        Future<Void> future = Future.future();
        Toolkit.get().onReady(() -> {
            onResume();
            future.complete();
        });
        return future;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uiNode == null)
            uiNode = buildUi();
        setNode(uiNode);
    }

    private Node uiNode;
    public abstract Node buildUi();

    /** Helpers **/

    public static Text createTextView(String translationKey, I18n i18n) {
        Text text = new Text();
        i18n.translateString(text.textProperty(), translationKey);
        return text;
    }

    public static ImageView createImageView(String urlOrJson) { // TODO: move into Toolkit when Json will be move into naga-commons
        if (!Strings.startsWith(urlOrJson, "{"))
            return new ImageView(urlOrJson);
        return createImageView(Json.parseObject(urlOrJson));
    }

    public static ImageView createImageView(JsonObject json) {
        ImageView imageView = new ImageView(json.getString("url"));
        imageView.setFitWidth(json.getDouble("width"));
        imageView.setFitHeight(json.getDouble("height"));
        return imageView;
    }

    public static <T extends Labeled> T setGraphic(T labeled, String urlOrJson) {
        labeled.setGraphic(createImageView(urlOrJson));
        return labeled;
    }

}
