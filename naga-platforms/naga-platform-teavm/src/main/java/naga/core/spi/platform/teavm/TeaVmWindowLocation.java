package naga.core.spi.platform.teavm;


import naga.core.spi.platform.web.WindowLocation;
import org.teavm.jso.browser.Location;

/**
 * @author Bruno Salmon
 */
class TeaVmWindowLocation implements WindowLocation {

    private final Location jsoLocation;

    public TeaVmWindowLocation(org.teavm.jso.browser.Location jsoLocation) {
        this.jsoLocation = jsoLocation;
    }

    @Override
    public String getHref() {
        return jsoLocation.getFullURL();
    }

    @Override
    public String getProtocol() {
        return jsoLocation.getProtocol();
    }

    @Override
    public String getHost() {
        return jsoLocation.getHost();
    }

    @Override
    public String getHostName() {
        return jsoLocation.getHostName();
    }

    @Override
    public String getPort() {
        return jsoLocation.getPort();
    }

    @Override
    public String getPathName() {
        return jsoLocation.getPathName();
    }

    @Override
    public String getSearch() {
        return jsoLocation.getSearch();
    }

    @Override
    public String getHash() {
        return jsoLocation.getHash();
    }

    public static TeaVmWindowLocation current() {
        return new TeaVmWindowLocation(org.teavm.jso.browser.Location.current());
    }
}
