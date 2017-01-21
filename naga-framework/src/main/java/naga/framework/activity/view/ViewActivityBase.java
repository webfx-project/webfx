package naga.framework.activity.view;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import naga.commons.util.Strings;
import naga.commons.util.async.Future;
import naga.framework.activity.uiroute.UiRouteActivityBase;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
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
