package webfx.tools.buildtool;

import webfx.tools.util.reusablestream.ReusableStream;

/**
 * @author Bruno Salmon
 */
public final class Providers implements Comparable<Providers> {
    private final String spiClassName;
    private final ReusableStream<ProjectModule> providerModules;
    private final ReusableStream<String> providerClassNames;

    Providers(String spiClassName, ReusableStream<ProjectModule> providerModules) {
        this.spiClassName = spiClassName;
        this.providerModules = providerModules;
        this.providerClassNames = providerModules
                .flatMap(m -> m.getProvidedJavaServiceImplementations(spiClassName, true));
    }

    public String getSpiClassName() {
        return spiClassName;
    }

    public ReusableStream<ProjectModule> getProviderModules() {
        return providerModules;
    }

    public ReusableStream<String> getProviderClassNames() {
        return providerClassNames;
    }

    @Override
    public int compareTo(Providers o) {
        return spiClassName.compareTo(o.spiClassName);
    }
}
