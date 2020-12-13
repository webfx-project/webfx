package dev.webfx.platform.client.services.windowlocation.spi.impl;

import dev.webfx.platform.client.services.windowlocation.spi.BrowsingLocation;
import dev.webfx.platform.client.services.windowlocation.spi.PathLocation;
import dev.webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class BrowsingLocationImpl extends PathLocationImpl implements BrowsingLocation {

    private final String protocol;
    private final String hostname;
    private final String port;

    public BrowsingLocationImpl(String protocol, String hostname, String port, PathLocation pathLocation) {
        this(protocol, hostname, port, pathLocation.getPathname(), pathLocation.getQueryString(), pathLocation.getFragment());
    }

    public BrowsingLocationImpl(String protocol, String hostname, String port, String pathname, String queryString, String fragment) {
        super(pathname, queryString, fragment);
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public String getPort() {
        return port;
    }

    public static BrowsingLocationImpl fromHref(String href) {
        int protoEnd = href.indexOf("://");
        String protocol = href.substring(0, protoEnd);
        int hostStart = protoEnd + 3;
        int hostEnd = href.indexOf('/', hostStart);
        String host  = href.substring(hostStart, hostEnd);
        int portColon = host.indexOf(':');
        String hostname, port;
        if (portColon == -1) {
            hostname = host;
            port = "";
        } else {
            hostname = host.substring(0, portColon);
            port = host.substring(portColon + 1);
        }
        String path = href.substring(hostEnd);
        return new BrowsingLocationImpl(protocol, hostname, port, new PathLocationImpl(path));
    }

    public static BrowsingLocationImpl fromJson(JsonObject json) {
        String href = json.getString("href");
        if (href != null)
            return fromHref(href);
        String protocol = json.getString("protocol");
        String hostname = json.getString("hostname");
        String port = json.getString("json");
        if (hostname == null) {
            String host = json.getString("host");
            int portColon = host.indexOf(':');
            if (portColon == -1) {
                hostname = host;
                port = "";
            } else {
                hostname = host.substring(0, portColon);
                port = host.substring(portColon + 1);
            }
        }
        return new BrowsingLocationImpl(protocol, hostname, port, PathLocationImpl.fromJson(json));
    }
}
