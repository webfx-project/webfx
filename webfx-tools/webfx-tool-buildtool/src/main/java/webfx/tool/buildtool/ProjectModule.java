package webfx.tool.buildtool;

import webfx.tool.buildtool.util.streamable.Streamable;
import webfx.tool.buildtool.util.splitfiles.SplitFiles;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
class ProjectModule extends ModuleImpl {

    private final static PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

    private final Streamable<ProjectModule> childrenModulesCache = Streamable.fromSpliterable(() -> SplitFiles.uncheckedWalk(getHomeDirectoryPath(), 1))
            .filter(path -> !isSameFile(path, getHomeDirectoryPath()))
            .filter(Files::isDirectory)
            .filter(path -> Files.exists(path.resolve("pom.xml")))
            .map(path -> new ProjectModule(path, this))
            .cache()
            ;
    private final Streamable<ProjectModule> childrenModulesInDepthCache = childrenModulesCache
            .flatMap(ProjectModule::getThisAndChildrenModulesInDepth)
            //.cache()
            ;
    private final Streamable<JavaClass> javaClassesCache = Streamable.fromSpliterable(() -> Files.exists(getJavaSourceDirectoryPath()) ? SplitFiles.uncheckedWalk(getJavaSourceDirectoryPath()) : Spliterators.emptySpliterator())
            .filter(javaFileMatcher::matches)
            .map(path -> new JavaClass(path, this))
            .cache()
            ;
    private final Streamable<String> usedJavaPackagesNamesCache = javaClassesCache
            .flatMap(JavaClass::analyzeUsedJavaPackagesNames)
            .distinct()
            .cache()
            ;
    private final Streamable<Module> directDependenciesCache = usedJavaPackagesNamesCache
            .map(m -> getRootModule().getJavaPackageNameModule(m))
            .filter(module -> module != null && module != this && !module.getArtifactId().equals(getArtifactId()))
            .distinct()
            .cache()
            ;
    private final Path homeDirectoryPath;
    private final ProjectModule parentModule;
    private final RootModule rootModule;

    /************************
     ***** Constructors *****
     ************************/

    ProjectModule(Path homeDirectoryPath) {
        this(homeDirectoryPath, null);
    }

    private ProjectModule(Path homeDirectoryPath, ProjectModule parentModule) {
        super(parentModule == null ? null : parentModule.getGroupId(), homeDirectoryPath.getFileName().toString(), parentModule == null ? null : parentModule.getVersion());
        this.parentModule = parentModule;
        this.homeDirectoryPath = homeDirectoryPath;
        rootModule = parentModule != null ? parentModule.getRootModule() : (RootModule) this;
    }


    /*************************
     ***** Basic getters *****
     *************************/

    Path getHomeDirectoryPath() {
        return homeDirectoryPath;
    }

    Path getJavaSourceDirectoryPath() {
        return homeDirectoryPath.resolve("src/main/java/");
    }

    ProjectModule getParentModule() {
        return parentModule;
    }

    RootModule getRootModule() {
        return rootModule;
    }

    ProjectModule getChildModuleInDepth(String artifactId) {
        return getThisAndChildrenModulesInDepth()
                .filter(module -> module.getArtifactId().equals(artifactId))
                .stream()
                .findFirst()
                .orElse(null);
    }

    ProjectModule getProjectModuleInDepth(String artifactId) {
        return getRootModule().getChildModuleInDepth(artifactId);
    }


    /*************************
     ***** Basic streams *****
     *************************/

    ///// Modules

    Streamable<ProjectModule> getChildrenModules() {
        return childrenModulesCache;
    }

    Streamable<ProjectModule> getChildrenModulesInDepth() {
        return childrenModulesInDepthCache;
    }

    Streamable<ProjectModule> getThisAndChildrenModulesInDepth() {
        return Streamable.of(this).concat(getChildrenModulesInDepth());
    }


    ///// Java classes

    Streamable<JavaClass> getJavaClasses() {
        return javaClassesCache;
    }

    /******************************
     ***** Analyzing streams  *****
     ******************************/

    ///// Modules

    Streamable<Module> analyzeDirectDependencies() {
        return directDependenciesCache;
    }

    Streamable<ProjectModule> analyzeThisOrChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        return getThisAndChildrenModulesInDepth()
                .filter(module -> module.analyzeDirectDependencies().stream().anyMatch(m -> moduleArtifactId.equals(m.getArtifactId())))
                ;
    }

    Set<Module> analyzeThisAndChildrenModulesInDepthTransitiveDependencies() {
        if (transitiveDependenciesModules == null) {
            transitiveDependenciesModules = new HashSet<>();
            addTransitiveDependencies(this, false);
        }
        return transitiveDependenciesModules;
    }

    private Set<Module> transitiveDependenciesModules;

    private void addTransitiveDependencies(Module module, boolean includeModule) {
        if (!transitiveDependenciesModules.contains(module)) {
            if (includeModule)
                transitiveDependenciesModules.add(module);
            if (module instanceof ProjectModule)
                ((ProjectModule) module).analyzeDirectDependencies().forEach(m -> addTransitiveDependencies(m, true));
        }
    }

    ///// Packages (names)

    Streamable<String> analyzeUsedJavaPackagesNames() {
        return usedJavaPackagesNamesCache;
    }

    Streamable<JavaClass> analyzeJavaClassesDependingOn(String destinationModule) {
        return getJavaClasses()
                .filter(jc -> jc.analyzeUsedJavaPackagesNames().stream().anyMatch(p -> destinationModule.equals(rootModule.getJavaPackageNameModule(p).getArtifactId())))
                ;
    }

    /***************************
     ***** Listing methods *****
     ***************************/

    void listJavaClasses() {
        listIterableElements("Listing " + this + " module java classes",
                getJavaClasses()
        );
    }

    void listDirectDependencies() {
        listIterableElements("Listing " + this + " module direct dependencies",
                analyzeDirectDependencies()
        );
    }

    void listChildrenModulesInDepth() {
        listIterableElements("Listing " + this + " children modules (in depth)",
                getChildrenModulesInDepth()
        );
    }

    void listThisAndChildrenModulesInDepthWithTheirDirectDependencies() {
        listIterableElements("Listing " + this + " and children modules (in depth) with their direct dependencies",
                getThisAndChildrenModulesInDepth(),
                ProjectModule::logModuleWithDirectDependencies
        );
    }

    void listThisAndChildrenModulesInDepthTransitiveDependencies() {
        listIterableElements("Listing " + this + " and children modules (in depth) transitive dependencies",
                analyzeThisAndChildrenModulesInDepthTransitiveDependencies()
        );
    }

    void listOrAndChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        listIterableElements("Listing " + this + " or children modules (in depth) directly depending on " + moduleArtifactId,
                analyzeThisOrChildrenModulesInDepthDirectlyDependingOn(moduleArtifactId)
        );
    }

    void listJavaClassesDependingOn(String destinationModule) {
        listIterableElements("Listing " + this + " module java classes depending on " + destinationModule,
                analyzeJavaClassesDependingOn(destinationModule)
                , jc -> logJavaClassWithPackagesDependingOn(jc, destinationModule)
        );
    }


    /***************************
     ***** Logging methods *****
     ***************************/

    private void logModuleWithDirectDependencies() {
        log(this + " direct dependencies: " + analyzeDirectDependencies()
                .stream().collect(Collectors.toList()));
    }

    private void logJavaClassWithPackagesDependingOn(JavaClass jc, String destinationModule) {
        log(jc + " through packages " +
                jc.analyzeUsedJavaPackagesNames()
                        .filter(p -> destinationModule.equals(rootModule.getJavaPackageNameModule(p).getArtifactId()))
                        .distinct()
                        .stream().collect(Collectors.toList()));
    }


    /**********************************
     ***** Static utility methods *****
     **********************************/

    static <T> void listIterableElements(String section, Iterable<T> iterable) {
        listIterableElements(section, iterable, ProjectModule::log);
    }

    private static <T> void listIterableElements(String section, Iterable<T> iterable, Consumer<? super T> elementLogger) {
        logSection(section);
        iterable.forEach(elementLogger);
    }

    private static void logSection(String section) {
        String middle = "***** " + section + " *****";
        String starsLine = Stream.generate(() -> "*").limit(middle.length()).collect(Collectors.joining());
        log("");
        log(starsLine);
        log(middle);
        log(starsLine);
    }

    static void log(Object message) {
        System.out.println(message.toString());
    }

    static void warning(Object message) {
        System.err.println("WARNING: " + message);
    }

    private static boolean isSameFile(Path path1, Path path2) {
        try {
            return Files.isSameFile(path1, path2);
        } catch (IOException e) {
            return false;
        }
    }
}