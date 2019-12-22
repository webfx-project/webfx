package webfx.tools.buildtool.util.javacode;

import webfx.tools.buildtool.util.textfile.TextFileReaderWriter;

import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public final class JavaCode {

    private Supplier<Path> javaPathSupplier;
    private Path javaFilePath;
    private String textCode;

    public JavaCode(Supplier<Path> javaPathSupplier) {
        this.javaPathSupplier = javaPathSupplier;
    }

    public JavaCode(Path javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public JavaCode(String textCode) {
        this.textCode = textCode;
    }

    public String getTextCode() {
        if (textCode == null) {
            if (javaFilePath == null)
                javaFilePath = javaPathSupplier.get();
            textCode = TextFileReaderWriter.readTextFile(javaFilePath);
        }
        return textCode;
    }
}
