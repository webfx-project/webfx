package naga.core.orm.expressionparser.expressionbuilder;

import naga.core.orm.expressionparser.lci.ParserModelReader;
import naga.core.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public class BuilderThreadContext implements AutoCloseable {

    private final static ThreadLocal<BuilderThreadContext> contexts = new ThreadLocal<>();
    private static BuilderThreadContext jsContext; // used in a javascript context only (since ThreadLocal doesn't work with TeaVM)

    private ParserModelReader modelReader;

    public BuilderThreadContext(ParserModelReader modelReader) {
        this.modelReader = modelReader;
    }

    public ParserModelReader getModelReader() {
        return modelReader;
    }

    @Override
    public void close() {
        /* TODO: manage nested contexts
        contexts.set(null);
        jsContext = null;
        */
    }

    public static BuilderThreadContext open(ParserModelReader dataReader) {
        BuilderThreadContext context = new BuilderThreadContext(dataReader);
        contexts.set(context);
        if (contexts.get() == null) // happens with TeaVM
            jsContext = context;
        return context;
    }

    public static BuilderThreadContext getInstance() {
        BuilderThreadContext context = contexts.get();
        return context != null ? context : jsContext;
    }

    public static <T> T run(Callable<T> callable, ParserModelReader dataReader) {
        try (BuilderThreadContext context = open(dataReader)) {
            return callable.call();
        }
    }

}
