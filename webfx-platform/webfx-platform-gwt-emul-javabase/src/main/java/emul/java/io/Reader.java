package emul.java.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * Minimal interface to work with Lexer.java
 */

public abstract class Reader implements Closeable {

    abstract public int read(char cbuf[], int off, int len) throws IOException;

    abstract public void close() throws IOException;

}