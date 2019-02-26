package webfx.tool.buildtool;

import webfx.tool.buildtool.modulefiles.GwtModuleFile;
import webfx.tool.buildtool.modulefiles.JavaModuleFile;
import webfx.tool.buildtool.modulefiles.MavenModuleFile;
import webfx.tool.buildtool.modulefiles.WebfxModuleFile;
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
public class ProjectModule extends ModuleImpl {

    private final static PathMatcher javaFileMatcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

    private final ReusableStream<JavaClass> declaredJavaClassesCache =
            ReusableStream.create(() -> isSourceModule() ? SplitFiles.uncheckedWalk(getJavaSourceDirectory()) : Spliterators.emptySpliterator())
                    .filter(javaFileMatcher::matches)
                    .filter(path -> !"module-info.java".equals(path.getFileName().toString()))
                    .map(path -> new JavaClass(path, this))
                    .cache();
    private final ReusableStream<String> declaredJavaPackagesCache =
            declaredJavaClassesCache
                .map(JavaClass::getPackageName)
                .distinct()
            ;
    private final ReusableStream<String> usedJavaPackagesCache =
            declaredJavaClassesCache
                    .flatMap(JavaClass::getUsedJavaPackages)
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedRequiredJavaServicesCache =
            declaredJavaClassesCache
                    .flatMap(JavaClass::getUsedRequiredJavaServices)
                    .distinct()
                    .cache();
    private final ReusableStream<String> usedOptionalJavaServicesCache =
            declaredJavaClassesCache
                    .flatMap(JavaClass::getUsedOptionalJavaServices)
                    .distinct()
                    .cache();
    private final ReusableStream<String> declaredJavaServicesCache =
            getUsedJavaServices()
                    .filter(s -> declaredJavaClassesCache.anyMatch(jc -> s.equals(jc.getClassName())))
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
    private WebfxModuleFile webfxModuleFile;
    private JavaModuleFile javaModuleFile;
    private GwtModuleFile gwtModuleFile;
    private MavenModuleFile mavenModuleFile;

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

    public Path getHomeDirectory() {
        return homeDirectory;
    }

    public Path getJavaSourceDirectory() {
        return homeDirectory.resolve("src/main/java/");
    }

    Path getMetaInfJavaServicesDirectory() {
        return homeDirectory.resolve("src/main/resources/META-INF/services/");
    }

    ProjectModule getParentModule() {
        return parentModule;
    }

    RootModule getRootModule() {
        return rootModule;
    }

    public Target getTarget() {
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

    WebfxModuleFile getWebfxModuleFile() {
        if (webfxModuleFile == null)
            webfxModuleFile = new WebfxModuleFile(this);
        return webfxModuleFile;
    }

    JavaModuleFile getJavaModuleFile() {
        if (javaModuleFile == null)
            javaModuleFile = new JavaModuleFile(this);
        return javaModuleFile;
    }

    GwtModuleFile getGwtModuleFile() {
        if (gwtModuleFile == null)
            gwtModuleFile = new GwtModuleFile(this);
        return gwtModuleFile;
    }

    public MavenModuleFile getMavenModuleFile() {
        if (mavenModuleFile == null)
            mavenModuleFile = new MavenModuleFile(this);
        return mavenModuleFile;
    }

    /*************************
     ***** Basic streams *****
     *************************/

    ///// Java classes

    ReusableStream<JavaClass> getDeclaredJavaClasses() {
        return declaredJavaClassesCache;
    }

    ///// Java packages

    public ReusableStream<String> getDeclaredJavaPackages() {
        return declaredJavaPackagesCache;
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

    public ReusableStream<Module> getDirectDependencies() {
        return directDependenciesCache;
    }

    ReusableStream<Module> getThisAndDirectDependencies() {
        return ReusableStream.of((Module) this).concat(directDependenciesCache);
    }

    ReusableStream<ProjectModule> getThisOrChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        return getThisAndChildrenModulesInDepth()
                .filter(module -> module.isDirectlyDependingOn(moduleArtifactId))
                ;
    }

    boolean isDirectlyDependingOn(String moduleArtifactId) {
        return getDirectDependencies().anyMatch(m -> moduleArtifactId.equals(m.getArtifactId()));
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

    public ReusableStream<String> getUsedJavaPackages() {
        return usedJavaPackagesCache;
    }

    ReusableStream<JavaClass> getJavaClassesDependingOn(String destinationModule) {
        return getDeclaredJavaClasses()
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

    public ReusableStream<String> getUsedJavaServices() {
        return getUsedRequiredJavaServices().concat(getUsedOptionalJavaServices());
    }

    ReusableStream<String> getDeclaredJavaServices() {
        return declaredJavaServicesCache;
    }

    boolean declaresJavaService(String javaService) {
        return isSourceModule() && getDeclaredJavaServices()
                .anyMatch(javaService::equals);
    }

    public ReusableStream<String> getProvidedJavaServices() {
        return providedJavaServicesCache;
    }

    boolean providesJavaService(String javaService) {
        return getProvidedJavaServices()
                .filter(javaService::equals)
                .findAny()
                .isPresent()
                ;
    }

    public ReusableStream<String> getProvidedJavaServiceImplementations(String javaService) {
        return getProvidedJavaServices()
                .filter(javaService::equals)
                .map(s -> getMetaInfJavaServicesDirectory().resolve(s))
                .map(SplitFiles::uncheckedReadTextFile)
                .flatMap(content -> Arrays.asList(content.split("\\r?\\n")))
                .map(s -> s.replace('$', '.'))
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