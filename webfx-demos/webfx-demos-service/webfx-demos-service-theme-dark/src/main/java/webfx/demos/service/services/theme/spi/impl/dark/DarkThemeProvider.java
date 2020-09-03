package webfx.demos.service.services.theme.spi.impl.dark;

import webfx.demos.service.services.theme.spi.ThemeProvider;

/**
 * @author Bruno Salmon
 */
public class DarkThemeProvider implements ThemeProvider {

    @Override
    public String getName() {
        return "Dark";
    }
}
