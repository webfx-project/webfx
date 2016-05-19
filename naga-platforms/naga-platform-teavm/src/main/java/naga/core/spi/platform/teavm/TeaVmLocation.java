package naga.core.spi.platform.teavm;


import naga.core.spi.platform.web.WebLocation;
import org.teavm.jso.browser.Location;

/**
 * @author Bruno Salmon
 */
class TeaVmLocation implements WebLocation {

    private final Location jsoLocation;

    public TeaVmLocation(org.teavm.jso.browser.Location jsoLocation) {
        this.jsoLocation = jsoLocation;
    }

    @Override
    public String getHostName() {
        return jsoLocation.getHostName();
    }

    @Override
    public Integer getPort() {
        try {
            return Integer.valueOf(jsoLocation.getPort());
        } catch (Exception e) {
            return null;
        }
    }

    public static TeaVmLocation current() {
        return new TeaVmLocation(org.teavm.jso.browser.Location.current());
    }
}
