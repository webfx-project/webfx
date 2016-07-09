package naga.providers.platform.abstr.java.services.resource;

import naga.platform.services.resource.spi.ResourceService;
import naga.commons.util.async.Future;

import java.util.Scanner;

/**
 * @author Bruno Salmon
 */
public final class JavaResourceService implements ResourceService {

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
