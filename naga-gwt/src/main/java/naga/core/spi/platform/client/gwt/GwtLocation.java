package naga.core.spi.platform.client.gwt;

import com.google.gwt.user.client.Window;
import naga.core.spi.platform.client.web.WebLocation;

/**
 * @author Bruno Salmon
 */
class GwtLocation implements WebLocation {

    private GwtLocation() {
    }

    @Override
    public String getHostName() {
        return Window.Location.getHostName();
    }

    @Override
    public Integer getPort() {
        return Integer.valueOf(Window.Location.getPort());
    }

    static GwtLocation current() {
        return new GwtLocation();
    }
}
