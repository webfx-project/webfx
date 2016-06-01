package naga.core.spi.platform.gwt;

import com.google.gwt.user.client.Window;
import naga.core.routing.location.WindowLocation;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
class GwtWindowLocation implements WindowLocation {

    static GwtWindowLocation current() {
        return new GwtWindowLocation();
    }

    private GwtWindowLocation() {
    }

    @Override
    public String getHref() {
        return Window.Location.getHref();
    }

    @Override
    public String getProtocol() {
        return Strings.removeSuffix(Window.Location.getProtocol(), ":");
    }

    @Override
    public String getHost() {
        return Window.Location.getHost();
    }

    @Override
    public String getHostname() {
        return Window.Location.getHostName();
    }

    @Override
    public String getPort() {
        return Window.Location.getPort();
    }

    public String getPathname() {
        return Window.Location.getPath();
    }

    @Override
    public String getSearch() {
        return Window.Location.getQueryString();
    }

    @Override
    public String getQueryString() {
        return Strings.removePrefix(getSearch(), "?");
    }

    @Override
    public String getHash() {
        return Window.Location.getHash();
    }

    @Override
    public String getFragment() {
        return Strings.removePrefix(getHash(), "#");
    }

    @Override
    public void assignHref(String href) {
        Window.Location.assign(href);
    }

    @Override
    public void replaceHref(String href) {
        Window.Location.replace(href);
    }
}
