package emul.java.io;

import java.io.IOException;
import java.io.Reader;

/**
 * Minimal interface to work with Lexer.java
 */

public class StringReader extends Reader {

    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;

    /**
     * Creates a new string reader.
     *
     * @param s String providing the character stream.
     */
    public StringReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        //synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                    ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            if (next >= length)
                return -1;
            int n = Math.min(length - next, len);
            str.getChars(next, next + n, cbuf, off);
            next += n;
            return n;
        //}
    }

    public void close() {
        str = null;
    }

    private void ensureOpen() throws IOException {
        if (str == null)
            throw new IOException("Stream closed");
    }

}