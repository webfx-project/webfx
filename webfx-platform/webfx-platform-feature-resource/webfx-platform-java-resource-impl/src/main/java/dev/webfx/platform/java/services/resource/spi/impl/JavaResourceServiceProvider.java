package dev.webfx.platform.java.services.resource.spi.impl;

import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.services.resource.spi.ResourceServiceProvider;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Bruno Salmon
 */
public final class JavaResourceServiceProvider implements ResourceServiceProvider {

    @Override
    public String toUrl(String resourcePath, Class<?> loadingClass) {
        return loadingClass.getResource(resourcePath).toExternalForm();
    }

    @Override
    public Future<String> getText(String resourcePath) {
        try (Scanner scanner = createScanner(getClass().getClassLoader().getResourceAsStream(resourcePath))) {
            return Future.succeededFuture(scanner == null ? null : scanner.useDelimiter("\\A").next());
        }
    }

    private static Scanner createScanner(InputStream inputStream) {
        return inputStream == null ? null : new Scanner(inputStream, "UTF-8");
    }
}
