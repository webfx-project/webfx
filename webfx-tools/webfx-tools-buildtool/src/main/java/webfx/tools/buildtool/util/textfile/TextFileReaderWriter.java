package webfx.tools.buildtool.util.textfile;

import webfx.tools.buildtool.util.splitfiles.SplitFiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TextFileReaderWriter {

    public static String readTextFile(Path path) {
        try {
            return SplitFiles.uncheckedReadTextFile(path);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static void writeTextFile(String content, Path path) {
        try {
            System.out.println(">>> Writing " + path);
            Files.createDirectories(path.getParent()); // Creating all necessary directories
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeTextFileIfNewOrModified(String content, Path path) {
        writeTextFileIfNewOrModified(content, readTextFile(path), path);
    }

    public static void writeTextFileIfNewOrModified(String newContent, String oldContent, Path path) {
        if (newContent == null)
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else if (!areTextFileContentsIdentical(newContent, oldContent))
           writeTextFile(newContent, path);
    }

    private static boolean areTextFileContentsIdentical(String content1, String content2) {
        return Objects.equals(removeLR(content1), removeLR(content2));
    }

    private static String removeLR(String content) {
        return content == null ? null : content.replaceAll("\r", "");
    }
}
