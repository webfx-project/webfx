package naga.commons.util.collection;

import naga.commons.util.function.Consumer;
import naga.commons.util.function.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Collections {

    public static <T> void forEach(Collection<T> collection, Consumer<T> consumer) {
        // collection.forEach(consumer); // Not GWT compilable for now
        for (T element : collection)
            consumer.accept(element);
    }

    public static <A, B> List<B> convert(Collection<A> aList, Converter<A, B> aToBConverter) {
        // return aList.stream().map(aToBConverter::convert).collect(Collectors.toList()); // Not GWT compilable for now
        List<B> bList = new ArrayList<>(aList.size());
        forEach(aList, a -> bList.add(aToBConverter.convert(a)));
        return bList;
    }

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
