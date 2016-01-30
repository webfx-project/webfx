package naga.core.util;

import java.util.List;

/*
 * @author Bruno Salmon
 */

public class Arrays {

    private Arrays() {
    }

    public static Object[] newArray(Object... a) {
        return a;
    }

    public static Object[] asArray(Object o) {
        return o instanceof Object[] ? (Object[]) o : null;
    }

    public static <T> List<T> asList(T... a) {
        return java.util.Arrays.asList(a);
    }

    public static Object[] copyOf(Object o) {
        Object[] objects = asArray(o);
        return objects == null ? null : copyOf(objects);
    }

    public static <T> T[] copyOf(T... a) {
        return java.util.Arrays.copyOf(a, a.length);
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    // Bytes array operations

    // GZIP

    /* Working in TeaVM but not GWT

    public static byte[] gzipBytes(byte[] bytes) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] ungzipBytes(byte[] bytes) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            GZIPInputStream gzip = new GZIPInputStream(in);
            int size = 8192;
            byte[] buffer = new byte[size];
            ByteArrayOutputStream out = new ByteArrayOutputStream(size * 8);
            int length;
            while ((length = gzip.read(buffer, 0, size)) != -1)
                out.write(buffer, 0, length);
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
    */
}
