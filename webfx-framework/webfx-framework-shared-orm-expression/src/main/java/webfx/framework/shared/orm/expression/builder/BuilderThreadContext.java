package webfx.framework.shared.orm.expression.builder;

import webfx.framework.shared.orm.expression.parser.lci.ParserDomainModelReader;
import webfx.platform.shared.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public final class BuilderThreadContext implements AutoCloseable {

    private final static ThreadLocal<BuilderThreadContext> contexts = new ThreadLocal<>();
    private static BuilderThreadContext jsContext; // used in a javascript context only (since ThreadLocal doesn't work with TeaVM)

    private final ParserDomainModelReader modelReader;

    public BuilderThreadContext(ParserDomainModelReader modelReader) {
        this.modelReader = modelReader;
    }

    public ParserDomainModelReader getModelReader() {
        return modelReader;
    }

    @Override
    public void close() {
        /* TODO: manage nested contexts
        contexts.set(null);
        jsContext = null;
        */
    }

    public static BuilderThreadContext open(ParserDomainModelReader dataReader) {
        if (dataReader == null) // This happens only with InlineFunction.parseBody()
            return null; // retuning null context to keep the last dataReader
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

    public static <T> T run(Callable<T> callable, ParserDomainModelReader dataReader) {
        try (BuilderThreadContext context = open(dataReader)) {
            return callable.call();
        }
    }
}
