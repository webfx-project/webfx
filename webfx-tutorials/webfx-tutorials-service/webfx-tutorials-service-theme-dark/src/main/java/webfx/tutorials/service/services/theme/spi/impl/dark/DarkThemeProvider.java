package webfx.tutorials.service.services.theme.spi.impl.dark;

import webfx.tutorials.service.services.theme.spi.ThemeProvider;

/**
 * @author Bruno Salmon
 */
public class DarkThemeProvider implements ThemeProvider {

    @Override
    public String getName() {
        return "Dark";
    }
}
