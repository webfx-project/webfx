package webfx.tools.buildtool;

import webfx.tools.util.reusablestream.ReusableStream;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Bruno Salmon
 */
public final class ModuleDependency implements Comparable<ModuleDependency> {

    public enum Type {
        SOURCE,
        NOT_FOUND_SOURCE,
        RESOURCE,
        EMULATION,
        APPLICATION,
        PLUGIN,
        IMPLICIT_PROVIDER
    }

    private final Module sourceModule;
    private final Module destinationModule;
    private final Type type;
    private final boolean optional;
    private final String scope;
    private final String classifier;
    private final Target executableTarget;

    public ModuleDependency(Module sourceModule, Module destinationModule, Type type) {
        this(sourceModule, destinationModule, type, false, null, null, null);
    }

    public ModuleDependency(Module sourceModule, Module destinationModule, Type type, boolean optional, String scope, String classifier, Target executableTarget) {
        this.sourceModule = sourceModule;
        this.destinationModule = destinationModule;
        this.type = type;
        this.optional = optional;
        this.scope = scope;
        this.classifier = classifier;
        this.executableTarget = executableTarget;
    }

    public Module getSourceModule() {
        return sourceModule;
    }

    public Module getDestinationModule() {
        return destinationModule;
    }

    public Type getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getScope() {
        return scope;
    }

    public String getClassifier() {
        return classifier;
    }

    public Target getExecutableTarget() {
        return executableTarget;
    }

    @Override
    public int compareTo(ModuleDependency dep) {
        // By default we sort the dependencies by ordering the by the destination module name
        return destinationModule.getName().compareTo(dep.destinationModule.getName());
    }

    ReusableStream<ModuleDependency> collectThisAndTransitiveDependencies() {
        Set<ModuleDependency> dependencies = new LinkedHashSet<>();
        collectThisAndTransitiveDependencies(dependencies, sourceModule instanceof ProjectModule ? (ProjectModule) sourceModule : null);
        return ReusableStream.fromIterable(dependencies);
    }

    private void collectThisAndTransitiveDependencies(Collection<ModuleDependency> dependencies, ProjectModule targetModule) {
        if (dependencies.stream().noneMatch(d -> d.destinationModule == destinationModule)) { // Avoiding infinite recursion
            dependencies.add(this);
            // We don't include the webfx-kit dependencies unless it is a GWT executable
            if (destinationModule.getName().startsWith("webfx-kit-javafx") && !targetModule.isExecutable(Platform.GWT))
                return;
            ProjectModule pm = destinationModule instanceof ProjectModule ? (ProjectModule) destinationModule : null;
            if (pm != null)
                pm.getDirectDependenciesWithoutFinalExecutableResolutions().forEach(dep ->
                        dep.collectThisAndTransitiveDependencies(dependencies, targetModule)
                );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleDependency)) return false;

        ModuleDependency that = (ModuleDependency) o;

        if (optional != that.optional) return false;
        if (!sourceModule.equals(that.sourceModule)) return false;
        if (!destinationModule.equals(that.destinationModule)) return false;
        if (type != that.type) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
        return classifier != null ? classifier.equals(that.classifier) : that.classifier == null;

    }

    @Override
    public int hashCode() {
        int result = sourceModule.hashCode();
        result = 31 * result + destinationModule.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (optional ? 1 : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + (classifier != null ? classifier.hashCode() : 0);
        return result;
    }

    public static ModuleDependency createDependency(Module srcModule, Module dstModule, Type type) {
        return new ModuleDependency(srcModule, dstModule, type);
    }

    public static ModuleDependency createSourceDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.SOURCE);
    }

    public static ModuleDependency createNotFoundSourceDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.NOT_FOUND_SOURCE);
    }

    public static ModuleDependency createResourceDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.RESOURCE);
    }

    public static ModuleDependency createEmulationDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.EMULATION);
    }

    public static ModuleDependency createApplicationDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.APPLICATION);
    }

    public static ModuleDependency createPluginDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.PLUGIN);
    }

    public static ModuleDependency createImplicitProviderDependency(Module srcModule, Module dstModule) {
        return createDependency(srcModule, dstModule, Type.IMPLICIT_PROVIDER);
    }
}
