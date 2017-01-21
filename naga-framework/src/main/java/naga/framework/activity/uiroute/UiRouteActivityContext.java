package naga.framework.activity.uiroute;

import javafx.beans.property.ReadOnlyProperty;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContext;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContext
        <THIS extends UiRouteActivityContext<THIS>>

        extends ActivityContext<THIS> {

    UiRouter getUiRouter();

    default History getHistory() {
        return getUiRouter().getHistory();
    }

    JsonObject getParams();

    default <T> T getParameter(String key) { return getParams().get(key); }

    ReadOnlyProperty<Boolean> activeProperty();

    I18n getI18n();

    THIS setI18n(I18n i18n);
}
