package webfx.tool.buildtool;

import webfx.tool.buildtool.util.reusablestream.ReusableStream;
import webfx.tool.buildtool.util.splitfiles.SplitFiles;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;

/**
 * @author Bruno Salmon
 */
class ProjectModule extends ModuleImpl {

    private final static PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

    private final ReusableStream<JavaClass> javaClassesCache =
            ReusableStream.create(() -> isSourceModule() ? SplitFiles.uncheckedWalk(getJavaSourceDirectory()) : Spliterators.emptySpliterator())
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
            ReusableStream.create(() -> hasMetaInfJavaServicesDirectory() ? SplitFiles.uncheckedWalk(getMetaInfJavaServicesDirectory(), 1) : Spliterators.emptySpliterator())
                    .filter(path -> !SplitFiles.uncheckedIsSameFile(path, getMetaInfJavaServicesDirectory()))
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
            ReusableStream.create(() -> SplitFiles.uncheckedWalk(getHomeDirectory(), 1))
                    .filter(path -> !SplitFiles.uncheckedIsSameFile(path, getHomeDirectory()))
                    .filter(Files::isDirectory)
                    .filter(path -> Files.exists(path.resolve("pom.xml")))
                    .map(path -> new ProjectModule(path, this))
                    .cache();
    private final ReusableStream<ProjectModule> childrenModulesInDepthCache =
            childrenModulesCache
                    .flatMap(ProjectModule::getThisAndChildrenModulesInDepth)
            //.cache()
            ;
    private final Path homeDirectory;
    private final ProjectModule parentModule;
    private final RootModule rootModule;
    private Target target;
    private Boolean isSourceModule;
    private Boolean hasMetaInfJavaServicesDirectory;

    /************************
     ***** Constructors *****
     ************************/

    ProjectModule(Path homeDirectory) {
        this(homeDirectory, null);
    }

    private ProjectModule(Path homeDirectory, ProjectModule parentModule) {
        super(parentModule == null ? null : parentModule.getGroupId(), homeDirectory.getFileName().toString(), parentModule == null ? null : parentModule.getVersion());
        this.parentModule = parentModule;
        this.homeDirectory = homeDirectory;
        rootModule = parentModule != null ? parentModule.getRootModule() : (RootModule) this;
    }


    /*************************
     ***** Basic getters *****
     *************************/

    Path getHomeDirectory() {
        return homeDirectory;
    }

    Path getJavaSourceDirectory() {
        return homeDirectory.resolve("src/main/java/");
    }

    Path getMetaInfJavaServicesDirectory() {
        return homeDirectory.resolve("src/main/resources/META-INF/services/");
    }

    Path getWebfxXmlModuleFilePath() {
        return homeDirectory.resolve("src/main/resources/META-INF/webfx.xml");
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
            isSourceModule = Files.exists(getJavaSourceDirectory());
        return isSourceModule;
    }

    boolean hasMetaInfJavaServicesDirectory() {
        if (hasMetaInfJavaServicesDirectory == null)
            hasMetaInfJavaServicesDirectory = isSourceModule() && Files.exists(getMetaInfJavaServicesDirectory());
        return hasMetaInfJavaServicesDirectory;
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
                .map(s -> getMetaInfJavaServicesDirectory().resolve(s))
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
}