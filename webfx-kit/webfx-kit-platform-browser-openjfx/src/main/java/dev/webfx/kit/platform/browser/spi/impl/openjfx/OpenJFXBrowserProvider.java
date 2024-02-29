package dev.webfx.kit.platform.browser.spi.impl.openjfx;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.platform.browser.spi.BrowserProvider;
import javafx.application.Application;

/**
 * @author Bruno Salmon
 */
public class OpenJFXBrowserProvider implements BrowserProvider {

    @Override
    public void launchExternalBrowser(String url) {
        Application application = WebFxKitLauncher.getApplication();
        application.getHostServices().showDocument(url);
    }

}
