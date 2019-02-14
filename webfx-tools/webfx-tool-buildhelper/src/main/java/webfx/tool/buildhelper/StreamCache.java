package webfx.tool.buildhelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
final class StreamCache<T> {

    private final StreamFactory<T> streamFactory;
    private final Supplier<Collection<T>> valuesCollectionFactory;
    private Collection<T> valuesCollection;

    StreamCache(StreamFactory<T> streamFactory, Supplier<Collection<T>> valuesCollectionFactory) {
        this.streamFactory = streamFactory;
        this.valuesCollectionFactory = valuesCollectionFactory;
    }

    Stream<T> stream() {
        if (valuesCollection != null)
            return valuesCollection.stream();
        Stream<T> stream = null;
        try {
            stream = streamFactory.createStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stream == null)
            stream = Stream.empty();
        Collection<T> buildingCache = valuesCollectionFactory.get();
        stream = stream
                .peek(buildingCache::add)
                .onClose(() -> valuesCollection = buildingCache);
        return stream;
    }

    interface StreamFactory<T> {
        Stream<T> createStream() throws Exception;
    }

    static <T> StreamCache<T> hashSetStreamCache(StreamFactory<T> streamFactory) {
        return new StreamCache<>(streamFactory, HashSet::new);
    }

}
