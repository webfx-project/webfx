package naga.framework.activity.uiroute;

import javafx.beans.property.ReadOnlyProperty;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.ActivityContextMixin;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public interface UiRouteActivityContextMixin
        <C extends UiRouteActivityContext<C>>

        extends ActivityContextMixin<C>,
        UiRouteActivityContext<C> {

    @Override
    default UiRouter getUiRouter() { return getActivityContext().getUiRouter(); }

    @Override
    default History getHistory() { return getActivityContext().getHistory(); }

    @Override
    default JsonObject getParams() { return getActivityContext().getParams(); }

    @Override
    default <T> T getParameter(String key) {
        return getActivityContext().getParameter(key);
    }

    @Override
    default ReadOnlyProperty<Boolean> activeProperty() {
        return getActivityContext().activeProperty();
    }

    @Override
    default I18n getI18n() {
        return getActivityContext().getI18n();
    }

    @Override
    default C setI18n(I18n i18n) {
        return getActivityContext().setI18n(i18n);
    }
}
