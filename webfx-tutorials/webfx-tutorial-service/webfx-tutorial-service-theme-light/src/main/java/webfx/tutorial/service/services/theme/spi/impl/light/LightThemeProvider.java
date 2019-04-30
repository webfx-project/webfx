package webfx.tutorial.service.services.theme.spi.impl.light;

import webfx.tutorial.service.services.theme.spi.ThemeProvider;

/**
 * @author Bruno Salmon
 */
public class LightThemeProvider implements ThemeProvider {

    @Override
    public String getName() {
        return "Light";
    }
}
