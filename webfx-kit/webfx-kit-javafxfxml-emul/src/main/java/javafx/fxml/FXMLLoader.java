package javafx.fxml;

import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public class FXMLLoader {

    private static final Map<String, Supplier<Node>> LOADERS = new HashMap<>();

    private final URL url;

    public FXMLLoader(URL url) {
        this.url = url;
    }

    public Node load() throws IOException {
        Supplier<Node> loader = LOADERS.get(url.getPath());
        return loader == null ? null : loader.get();
    }

    public static void registerLoader(String path, Supplier<Node> loader) {
        LOADERS.put(path, loader);
    }

}
