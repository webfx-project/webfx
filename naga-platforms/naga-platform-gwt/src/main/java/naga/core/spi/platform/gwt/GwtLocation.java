package naga.core.spi.platform.gwt;

import com.google.gwt.user.client.Window;
import naga.core.spi.platform.web.WebLocation;

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
        String port = Window.Location.getPort();
        return port == null || port.isEmpty() ? null : Integer.valueOf(port);
    }

    static GwtLocation current() {
        return new GwtLocation();
    }
}
