package webfx.tool.buildtool;

import webfx.tool.buildtool.util.reusablestream.ReusableStream;
import webfx.tool.buildtool.util.splitfiles.SplitFiles;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
class ProjectModule extends ModuleImpl {

    private final static PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

    private final ReusableStream<JavaClass> javaClassesCache =
            ReusableStream.create(() -> isSourceModule() ? SplitFiles.uncheckedWalk(getJavaSourceDirectoryPath()) : Spliterators.emptySpliterator())
                    .filter(javaFileMatcher::matches)
                    .map(path -> new JavaClass(path, this))
                    .cache();
    private final ReusableStream<String> usedJavaPackagesCache =
            javaClassesCache
                    .flatMap(JavaClass::getUsedJavaPackages)
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedRequiredJavaServicesCache =
            javaClassesCache
                    .flatMap(JavaClass::getUsedRequiredJavaServices)
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedOptionalJavaServicesCache =
            javaClassesCache
                    .flatMap(JavaClass::getUsedOptionalJavaServices)
                    .distinct()
                    .cache();
    private final ReusableStream<String> declaredJavaServicesCache =
            getUsedJavaServices()
                    .filter(s -> javaClassesCache.anyMatch(jc -> s.equals(jc.getClassName())))
                    .cache();
    private final ReusableStream<String> providedJavaServicesCache =
            ReusableStream.create(() -> hasMetaInfJavaServiceFolder() ? SplitFiles.uncheckedWalk(getMetaInfJavaServicePath(), 1) : Spliterators.emptySpliterator())
                    .filter(path -> !SplitFiles.uncheckedIsSameFile(path, getMetaInfJavaServicePath()))
                    .map(path -> path.getFileName().toString())
                    .cache();
    private final ReusableStream<Module> directDependenciesCache =
            usedJavaPackagesCache
                    .map(m -> getRootModule().getJavaPackageModule(m))
                    .filter(module -> module != this && !module.getArtifactId().equals(getArtifactId()))
                    .distinct()
                    .cache();
    private final ReusableStream<Module> transitiveDependenciesCache =
            directDependenciesCache
                    .flatMap(m -> m instanceof ProjectModule ? ((ProjectModule) m).getNonCyclicThisAndTransitiveDependencies() : ReusableStream.of(m))
                    .distinct()
                    .cache();
    private final ReusableStream<ProjectModule> childrenModulesCache =
            ReusableStream.create(() -> SplitFiles.uncheckedWalk(getHomeDirectoryPath(), 1))
                    .filter(path -> !SplitFiles.uncheckedIsSameFile(path, getHomeDirectoryPath()))
                    .filter(Files::isDirectory)
                    .filter(path -> Files.exists(path.resolve("pom.xml")))
                    .map(path -> new ProjectModule(path, this))
                    .cache();
    private final ReusableStream<ProjectModule> childrenModulesInDepthCache =
            childrenModulesCache
                    .flatMap(ProjectModule::getThisAndChildrenModulesInDepth)
            //.cache()
            ;
    private final Path homeDirectoryPath;
    private final ProjectModule parentModule;
    private final RootModule rootModule;
    private Target target;
    private Boolean isSourceModule;
    private Boolean hasMetaInfJavaServiceFolder;

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

    Path getMetaInfJavaServicePath() {
        return homeDirectoryPath.resolve("src/main/resources/META-INF/services/");
    }

    ProjectModule getParentModule() {
        return parentModule;
    }

    RootModule getRootModule() {
        return rootModule;
    }

    Target getTarget() {
        if (target == null)
            target = new Target(this);
        return target;
    }

    boolean isSourceModule() {
        if (isSourceModule == null)
            isSourceModule = Files.exists(getJavaSourceDirectoryPath());
        return isSourceModule;
    }

    boolean hasMetaInfJavaServiceFolder() {
        if (hasMetaInfJavaServiceFolder == null)
            hasMetaInfJavaServiceFolder = isSourceModule() && Files.exists(getMetaInfJavaServicePath());
        return hasMetaInfJavaServiceFolder;
    }


    /*************************
     ***** Basic streams *****
     *************************/

    ///// Java classes

    ReusableStream<JavaClass> getJavaClasses() {
        return javaClassesCache;
    }

    ///// Modules

    ReusableStream<ProjectModule> getChildrenModules() {
        return childrenModulesCache;
    }

    ReusableStream<ProjectModule> getThisAndChildrenModules() {
        return ReusableStream.of(this).concat(getChildrenModules());
    }

    ReusableStream<ProjectModule> getChildrenModulesInDepth() {
        return childrenModulesInDepthCache;
    }

    ReusableStream<ProjectModule> getThisAndChildrenModulesInDepth() {
        return ReusableStream.of(this).concat(getChildrenModulesInDepth());
    }

    ProjectModule getChildModuleInDepth(String artifactId) {
        return getChildrenModulesInDepth()
                .filter(module -> module.getArtifactId().equals(artifactId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find " + artifactId + " module under " + getArtifactId() + " module"))
                ;
    }


    /******************************
     ***** Analyzing streams  *****
     ******************************/

    ///// Modules

    ReusableStream<Module> getDirectDependencies() {
        return directDependenciesCache;
    }

    ReusableStream<Module> getThisAndDirectDependencies() {
        return ReusableStream.of((Module) this).concat(directDependenciesCache);
    }

    ReusableStream<ProjectModule> getThisOrChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        return getThisAndChildrenModulesInDepth()
                .filter(module -> module.getDirectDependencies().anyMatch(m -> moduleArtifactId.equals(m.getArtifactId())))
                ;
    }

    ReusableStream<Module> getTransitiveDependencies() {
        return transitiveDependenciesCache;
    }

    ReusableStream<Module> getThisAndTransitiveDependencies() {
        return ReusableStream.of((Module) this).concat(transitiveDependenciesCache);
    }

    private ReusableStream<Module> getNonCyclicThisAndTransitiveDependencies() {
        return ReusableStream.of((Module) this).concat(
                getDirectDependencies()
                        .flatMap(m -> {
                            Collection<Module> loop = getRootModule().getModulesInCyclicDependenciesLoop(this, m);
                            return loop != null ? loop
                                    : (m instanceof ProjectModule) ? ((ProjectModule) m).getNonCyclicThisAndTransitiveDependencies()
                                    : List.of(m);
                        })
        );
    }

    ///// Packages

    ReusableStream<String> getUsedJavaPackages() {
        return usedJavaPackagesCache;
    }

    ReusableStream<JavaClass> getJavaClassesDependingOn(String destinationModule) {
        return getJavaClasses()
                .filter(jc -> jc.getUsedJavaPackages().anyMatch(p -> destinationModule.equals(rootModule.getJavaPackageModule(p).getArtifactId())))
                ;
    }


    ///// Services


    ReusableStream<String> getUsedRequiredJavaServices() {
        return usedRequiredJavaServicesCache;
    }

    ReusableStream<String> getUsedOptionalJavaServices() {
        return usedOptionalJavaServicesCache;
    }

    ReusableStream<String> getUsedJavaServices() {
        return getUsedRequiredJavaServices().concat(getUsedOptionalJavaServices());
    }

    ReusableStream<String> getDeclaredJavaServices() {
        return declaredJavaServicesCache;
    }

    boolean declaresJavaService(String javaService) {
        return isSourceModule() && getDeclaredJavaServices()
                .anyMatch(javaService::equals);
    }

    ReusableStream<String> getProvidedJavaServices() {
        return providedJavaServicesCache;
    }

    boolean providesJavaService(String javaService) {
        return getProvidedJavaServices()
                .filter(javaService::equals)
                .findAny()
                .isPresent()
                ;
    }

    ReusableStream<String> getProvidedJavaServiceImplementations(String javaService) {
        return getProvidedJavaServices()
                .filter(javaService::equals)
                .map(s -> getMetaInfJavaServicePath().resolve(s))
                .map(SplitFiles::uncheckedReadTextFile)
                .flatMap(content -> Arrays.asList(content.split("\\r?\\n")))
                ;
    }

    ReusableStream<String> findRequiredServices() {
        return getTransitiveDependencies()
                .map(m -> m instanceof ProjectModule ? (ProjectModule) m : null)
                .filter(Objects::nonNull)
                .flatMap(ProjectModule::getUsedJavaServices)
                .distinct()
                ;
    }

    ReusableStream<ProjectModule> findModulesProvidingRequiredService(TargetTag... serviceTags) {
        return findModulesProvidingRequiredService(new Target(serviceTags));
    }

    ReusableStream<ProjectModule> findModulesProvidingRequiredService(Target serviceTarget) {
        return findRequiredServices()
                .map(js -> getRootModule().findBestMatchModuleProvidingJavaService(js, serviceTarget))
                .distinct()
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
                getDirectDependencies()
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
                getTransitiveDependencies()
        );
    }

    void listOrAndChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        listIterableElements("Listing " + this + " or children modules (in depth) directly depending on " + moduleArtifactId,
                getThisOrChildrenModulesInDepthDirectlyDependingOn(moduleArtifactId)
        );
    }

    void listJavaClassesDependingOn(String destinationModule) {
        listIterableElements("Listing " + this + " module java classes depending on " + destinationModule,
                getJavaClassesDependingOn(destinationModule)
                , jc -> logJavaClassWithPackagesDependingOn(jc, destinationModule)
        );
    }


    /***************************
     ***** Logging methods *****
     ***************************/

    private void logModuleWithDirectDependencies() {
        log(this + " direct dependencies: " + getDirectDependencies()
                .collect(Collectors.toList()));
    }

    private void logJavaClassWithPackagesDependingOn(JavaClass jc, String destinationModule) {
        log(jc + " through packages " +
                jc.getUsedJavaPackages()
                        .filter(p -> destinationModule.equals(rootModule.getJavaPackageModule(p).getArtifactId()))
                        .distinct()
                        .collect(Collectors.toList()));
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
}