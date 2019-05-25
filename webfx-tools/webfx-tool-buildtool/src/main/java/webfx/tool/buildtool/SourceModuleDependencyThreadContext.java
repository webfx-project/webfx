package webfx.tool.buildtool;

/**
 * @author Bruno Salmon
 */
final class SourceModuleDependencyThreadContext implements AutoCloseable {

    private final static ThreadLocal<SourceModuleDependencyThreadContext> contexts = new ThreadLocal<>();
    private static SourceModuleDependencyThreadContext jsContext; // used in a javascript context only (since ThreadLocal doesn't work with TeaVM)

    private final Module sourceModule;

    SourceModuleDependencyThreadContext(Module sourceModule) {
        this.sourceModule = sourceModule;
    }

    Module getSourceModule() {
        return sourceModule;
    }

    @Override
    public void close() {
        /* TODO: manage nested contexts */
        contexts.set(null);
        jsContext = null;
    }

    static SourceModuleDependencyThreadContext open(Module sourceModule) {
        SourceModuleDependencyThreadContext context = new SourceModuleDependencyThreadContext(sourceModule);
        contexts.set(context);
        if (contexts.get() == null) // happens with TeaVM
            jsContext = context;
        return context;
    }

    static SourceModuleDependencyThreadContext getInstance() {
        SourceModuleDependencyThreadContext context = contexts.get();
        return context != null ? context : jsContext;
    }
}
