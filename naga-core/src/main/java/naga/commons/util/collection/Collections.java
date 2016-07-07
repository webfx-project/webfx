package naga.commons.util.collection;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public class Collections {

    public static String toString(Iterator it) {
        if (!it.hasNext())
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            sb.append('\n');
            Object e = it.next();
            sb.append(e);
            if (!it.hasNext())
                return sb.append("\n]").toString();
            sb.append(',').append(' ');
        }
    }

}
