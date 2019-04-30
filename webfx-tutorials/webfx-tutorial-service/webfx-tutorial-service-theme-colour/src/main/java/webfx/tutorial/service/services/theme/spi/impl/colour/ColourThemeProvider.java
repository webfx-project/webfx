package webfx.tutorial.service.services.theme.spi.impl.colour;

import webfx.tutorial.service.services.theme.spi.ThemeProvider;

/**
 * @author Bruno Salmon
 */
public class ColourThemeProvider implements ThemeProvider {

    @Override
    public String getName() {
        return "Colour";
    }
}
