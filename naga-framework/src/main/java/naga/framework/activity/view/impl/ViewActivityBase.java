package naga.framework.activity.view.impl;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import naga.commons.util.Strings;
import naga.commons.util.async.Future;
import naga.framework.activity.uiroute.impl.UiRouteActivityBase;
import naga.framework.activity.view.ViewActivity;
import naga.framework.activity.view.ViewActivityContext;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
import naga.fx.util.ImageStore;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public abstract class ViewActivityBase
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityBase<C>
        implements ViewActivity<C>,
        ViewActivityContextMixin<C> {

    private Node viewNode;

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
        if (viewNode == null)
            viewNode = buildUi();
        setNode(viewNode);
    }


    /** Helpers **/

    public static Text createTextView(String translationKey, I18n i18n) {
        return i18n.translateText(new Text(), translationKey);
    }

    public static ImageView createImageView(String urlOrJson) { // TODO: move into Toolkit when Json will be move into naga-commons
        if (!Strings.startsWith(urlOrJson, "{"))
            return ImageStore.createImageView(urlOrJson);
        return createImageView(Json.parseObject(urlOrJson));
    }

    public static ImageView createImageView(JsonObject json) {
        ImageView imageView = ImageStore.createImageView(json.getString("url"));
        imageView.setFitWidth(json.getDouble("width"));
        imageView.setFitHeight(json.getDouble("height"));
        return imageView;
    }

    public static <T extends Labeled> T setGraphic(T labeled, String urlOrJson) {
        labeled.setGraphic(createImageView(urlOrJson));
        return labeled;
    }

}