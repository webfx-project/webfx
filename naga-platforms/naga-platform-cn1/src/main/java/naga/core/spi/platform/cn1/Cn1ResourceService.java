package naga.core.spi.platform.cn1;

import com.codename1.ui.Display;
import naga.core.spi.platform.ResourceService;
import naga.core.util.Strings;
import naga.core.util.async.Future;

import java.io.InputStreamReader;

/**
 * @author Bruno Salmon
 */
final class Cn1ResourceService implements ResourceService {

    public static Cn1ResourceService SINGLETON = new Cn1ResourceService();

    private Cn1ResourceService() {
    }

    @Override
    public Future<String> getText(String resourcePath) {
        resourcePath = "/" + Strings.replaceAll(resourcePath, "/", "_");
        try (InputStreamReader reader = new InputStreamReader(Display.getInstance().getResourceAsStream(null, resourcePath), "UTF-8")) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) > -1)
                sb.append((char)ch);
            return Future.succeededFuture(sb.toString());
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }
}
