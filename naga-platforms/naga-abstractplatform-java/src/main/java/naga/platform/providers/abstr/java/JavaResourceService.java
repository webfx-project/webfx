package naga.platform.providers.abstr.java;

import naga.commons.services.resource.spi.ResourceService;
import naga.commons.util.async.Future;

import java.util.Scanner;

/**
 * @author Bruno Salmon
 */
final class JavaResourceService implements ResourceService {

    public static JavaResourceService SINGLETON = new JavaResourceService();

    private JavaResourceService() {
    }

    @Override
    public Future<String> getText(String resourcePath) {
        try (Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(resourcePath))) {
            return Future.succeededFuture(scanner.useDelimiter("\\A").next());
        }
    }
}
