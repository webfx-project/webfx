package webfx.tool.buildhelper.util.splitfiles;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author Bruno Salmon
 */
public class SplitFiles {

    public static Spliterator<Path> walk(Path start, FileVisitOption... options) {
        return walk(start, Integer.MAX_VALUE, options);
    }

    public static Spliterator<Path> walk(Path start, int maxDepth, FileVisitOption... options) {
        try {
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
            } catch (Error|RuntimeException e) {
                iterator.close();
                //throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Spliterators.emptySpliterator();
    }

}
