package naga.core.spi.platform.gwt;

import com.google.gwt.user.client.Window;
import naga.core.spi.platform.web.WindowLocation;

/**
 * @author Bruno Salmon
 */
class GwtWindowLocation implements WindowLocation {

    private GwtWindowLocation() {
    }


    @Override
    public String getHref() {
        return Window.Location.getHref();
    }

    @Override
    public String getProtocol() {
        return Window.Location.getProtocol();
    }

    @Override
    public String getHost() {
        return Window.Location.getHost();
    }

    @Override
    public String getHostName() {
        return Window.Location.getHostName();
    }

    @Override
    public String getPort() {
        return Window.Location.getPort();
    }

    @Override
    public String getPathName() {
        return Window.Location.getPath();
    }

    @Override
    public String getSearch() {
        return Window.Location.getQueryString();
    }

    @Override
    public String getHash() {
        return Window.Location.getHash();
    }

    static GwtWindowLocation current() {
        return new GwtWindowLocation();
    }
}
