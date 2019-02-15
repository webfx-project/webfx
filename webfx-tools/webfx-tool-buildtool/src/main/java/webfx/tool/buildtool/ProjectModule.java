package webfx.tool.buildtool;

import webfx.tool.buildtool.util.spliterable.operable.OperableSpliterable;
import webfx.tool.buildtool.util.splitfiles.SplitFiles;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.List;
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

    private final Path homeDirectoryPath;
    private final ProjectModule parentModule;
    private final RootModule rootModule;
    private final OperableSpliterable<ProjectModule> childrenModulesCache;
    private final OperableSpliterable<JavaClass> javaClassesCache;
    private final StreamCache<Module> directDependenciesStreamCache;

    /************************
     ***** Constructors *****
     ************************/

    ProjectModule(Path homeDirectoryPath) {
        this(homeDirectoryPath, null);
    }

    ProjectModule(Path homeDirectoryPath, ProjectModule parentModule) {
        super(parentModule == null ? null : parentModule.getGroupId(), homeDirectoryPath.getFileName().toString(), parentModule == null ? null : parentModule.getVersion());
        this.parentModule = parentModule;
        this.homeDirectoryPath = homeDirectoryPath;
        rootModule = parentModule != null ? parentModule.getRootModule() : (RootModule) this;
        // Streams cache are instantiated now (because declared final)
        childrenModulesCache = OperableSpliterable.create(() -> SplitFiles.walk(homeDirectoryPath, 1))
                .filter(path -> !isSameFile(path, homeDirectoryPath))
                .filter(Files::isDirectory)
                .filter(path -> Files.exists(path.resolve("pom.xml")))
                .map(path -> new ProjectModule(path, this))
                .cache();
        javaClassesCache = OperableSpliterable.create(Files.exists(getJavaSourceDirectoryPath()) ? () -> SplitFiles.walk(getJavaSourceDirectoryPath()) : Spliterators::emptySpliterator)
                .filter(javaFileMatcher::matches)
                .map(path -> new JavaClass(path, this))
                .cache();
        directDependenciesStreamCache = StreamCache.hashSetStreamCache(() ->
                analyzeUsedJavaPackagesNames()
                        .map(rootModule::getJavaPackageNameModule)
                        .filter(module -> module != null && module != this && !module.getArtifactId().equals(getArtifactId()))
                        .distinct()
        );
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
        return getThisAndChildrenModulesInDepthStream()
                .filter(module -> module.getArtifactId().equals(artifactId))
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

    Stream<ProjectModule> getChildrenModulesStream() {
        return childrenModulesCache.buildStream();
    }

    Stream<ProjectModule> getChildrenModulesInDepthStream() {
        return getChildrenModulesStream()
                .flatMap(ProjectModule::getThisAndChildrenModulesInDepthStream);
    }

    Stream<ProjectModule> getThisAndChildrenModulesInDepthStream() {
        return Stream.concat(Stream.of(this), getChildrenModulesInDepthStream());
    }

    ///// Java classes

    Stream<JavaClass> getJavaClassesStream() {
        return javaClassesCache.buildStream();
    }


    /******************************
     ***** Analyzing streams  *****
     ******************************/

    ///// Modules

    Stream<Module> analyzeDirectDependencies() {
        return directDependenciesStreamCache.stream();
    }

    Stream<ProjectModule> analyzeThisOrChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        return getThisAndChildrenModulesInDepthStream()
                .filter(module -> module.analyzeDirectDependencies().anyMatch(m -> moduleArtifactId.equals(m.getArtifactId())))
                ;
    }

    Stream<Module> analyzeThisAndChildrenModulesInDepthTransitiveDependencies() {
        if (transitiveDependenciesModules == null) {
            transitiveDependenciesModules = new HashSet<>();
            addTransitiveDependencies(this, false);
        }
        return transitiveDependenciesModules.stream();
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

    Stream<String> analyzeUsedJavaPackagesNames() {
        return getJavaClassesStream()
                .flatMap(JavaClass::analyzeUsedJavaPackagesNames)
                .distinct()
                ;
    }

    Stream<JavaClass> analyzeJavaClassesDependingOn(String destinationModule) {
        return getJavaClassesStream()
                        .filter(jc -> jc.analyzeUsedJavaPackagesNames().anyMatch(p -> destinationModule.equals(rootModule.getJavaPackageNameModule(p).getArtifactId())))
                ;
    }

    /***************************
     ***** Listing methods *****
     ***************************/

    void listJavaClasses() {
        listStreamElements("Listing " + this + " module java classes",
                getJavaClassesStream()
        );
    }

    void listDirectDependencies() {
        listStreamElements("Listing " + this + " module direct dependencies",
                analyzeDirectDependencies()
        );
    }

    void listChildrenModulesInDepth() {
        listStreamElements("Listing " + this + " children modules (in depth)",
                getChildrenModulesInDepthStream()
        );
    }

    void listThisAndChildrenModulesInDepthWithTheirDirectDependencies() {
        listStreamElements("Listing " + this + " and children modules (in depth) with their direct dependencies",
                getThisAndChildrenModulesInDepthStream(),
                ProjectModule::logModuleWithDirectDependencies
        );
    }

    void listThisAndChildrenModulesInDepthTransitiveDependencies() {
        listStreamElements("Listing " + this + " and children modules (in depth) transitive dependencies",
                analyzeThisAndChildrenModulesInDepthTransitiveDependencies()
        );
    }

    void listOrAndChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        listStreamElements("Listing " + this + " or children modules (in depth) directly depending on " + moduleArtifactId,
                analyzeThisOrChildrenModulesInDepthDirectlyDependingOn(moduleArtifactId)
        );
    }

    void listJavaClassesDependingOn(String destinationModule) {
        listStreamElements("Listing " + this + " module java classes depending on " + destinationModule,
                analyzeJavaClassesDependingOn(destinationModule)
                , jc -> logJavaClassWithPackagesDependingOn(jc, destinationModule)
        );
    }


    /***************************
     ***** Logging methods *****
     ***************************/

    void logModuleWithDirectDependencies() {
        log(this + " direct dependencies: " + toListAndClose(analyzeDirectDependencies()));
    }

    void logJavaClassWithPackagesDependingOn(JavaClass jc, String destinationModule) {
        log(jc + " through packages " +
                toListAndClose(jc.analyzeUsedJavaPackagesNames()
                        .filter(p -> destinationModule.equals(rootModule.getJavaPackageNameModule(p).getArtifactId()))
                        .distinct()
                ));
    }


    /**********************************
     ***** Static utility methods *****
     **********************************/

    static void listStreamElements(String section, Stream stream) {
        listStreamElements(section, stream, ProjectModule::log);
    }

    static <T> void listStreamElements(String section, Stream<T> stream, Consumer<? super T> elementLogger) {
        logSection(section);
        forEachAndClose(stream, elementLogger);
    }

    static void logSection(String section) {
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

    static <T> void forEachAndClose(Stream<T> stream, Consumer<? super T> action) {
        try (Stream<T> s = stream) {
            s.forEach(action);
        }
    }

    static <T> List<T> toListAndClose(Stream<T> stream) {
        try (Stream<T> s = stream) {
            return s.collect(Collectors.toList());
        }
    }
}
