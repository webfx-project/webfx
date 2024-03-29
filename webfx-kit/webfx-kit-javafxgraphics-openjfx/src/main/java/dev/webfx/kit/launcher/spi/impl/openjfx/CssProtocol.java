package dev.webfx.kit.launcher.spi.impl.openjfx;

import dev.webfx.kit.launcher.WebFxKitLauncher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

/**
 * @author Bruno Salmon
 */
public class CssProtocol extends URLStreamHandlerProvider {

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("css".equals(protocol)) {
            return new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                    String requestedPath = u.getPath();
                    String cssResourcePath = WebFxKitLauncher.getWebFxCssResourcePath(requestedPath);
                    URL resource = ClassLoader.getSystemClassLoader().getResource(cssResourcePath);
                    if (resource == null)
                        throw new FileNotFoundException(requestedPath);
                    return resource.openConnection();
                }
            };
        }
        return null;
    }

}
