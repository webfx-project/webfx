package webfx.tool.buildtool.sourcegenerators;

import webfx.tool.buildtool.ProjectModule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
public final class GwtFilesGenerator {

    public static void generateGwtFiles(ProjectModule module) {
        GwtEmbedResourcesBundleSourceGenerator.generateGwtClientBundleSource(module);
        GwtServiceLoaderSuperSourceGenerator.generateServiceLoaderSuperSource(module);
        module.getGwtModuleFile().writeFile();
    }

    // Utility methods

    static void writeTextFile(Path path, String content) {
        try {
            if (content == null) {
                log(">>> Empty content");
                Files.deleteIfExists(path);
            } else {
                log(">>> Writing " + path);
                Files.createDirectories(path.getParent()); // Creating all necessary directories
                BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
                writer.write(content);
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void logSection(String section) {
        String middle = "***** " + section + " *****";
        String starsLine = Stream.generate(() -> "*").limit(middle.length()).collect(Collectors.joining());
        log("");
        log(starsLine);
        log(middle);
        log(starsLine);
    }

    static void log(Object message) {
        System.out.println(message.toString());
    }
}
