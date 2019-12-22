package webfx.tools.buildtool.util.splitfiles;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author Bruno Salmon
 */
public class SplitFiles {

    public static Spliterator<Path> walk(Path start, FileVisitOption... options) throws IOException {
        return walk(start, Integer.MAX_VALUE, options);
    }

    public static Spliterator<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        FileTreeIterator iterator = new FileTreeIterator(start, maxDepth, options);
        try {
            return Spliterators.spliteratorUnknownSize(new Iterator<>() {
                private boolean closed;

                @Override
                public boolean hasNext() {
                    if (closed)
                        return false;
                    if (iterator.hasNext())
                        return true;
                    iterator.close();
                    closed = true;
                    return false;
                }

                @Override
                public Path next() {
                    return iterator.next().file();
                }
            }, Spliterator.DISTINCT);
        } catch (Error | RuntimeException e) {
            iterator.close();
            throw e;
        }
    }

    public static Spliterator<Path> uncheckedWalk(Path start, FileVisitOption... options) throws RuntimeException {
        return uncheckedWalk(start, Integer.MAX_VALUE, options);
    }

    public static Spliterator<Path> uncheckedWalk(Path start, int maxDepth, FileVisitOption... options) throws RuntimeException {
        try {
            return walk(start, maxDepth, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean uncheckedIsSameFile(Path path1, Path path2) throws RuntimeException {
        try {
            return Files.isSameFile(path1, path2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String uncheckedReadTextFile(Path path) throws RuntimeException {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
