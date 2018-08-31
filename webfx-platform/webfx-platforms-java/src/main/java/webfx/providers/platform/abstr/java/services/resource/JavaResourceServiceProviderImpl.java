package webfx.providers.platform.abstr.java.services.resource;

import webfx.platform.services.resource.spi.ResourceServiceProvider;
import webfx.util.async.Future;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Bruno Salmon
 */
public final class JavaResourceServiceProviderImpl implements ResourceServiceProvider {

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
