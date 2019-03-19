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
import java.util.stream.Collectors;

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
                    .distinct();
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
    private final ReusableStream<Module> findableSourceDirectDependenciesCache =
            usedJavaPackagesCache
                    .map(p -> getRootModule().getJavaPackageModule(p))
                    .map(this::replaceModuleWithEmulatedIfApplicable)
                    .filter(module -> module != this && !module.getArtifactId().equals(getArtifactId()))
                    .distinct()
                    .cache();
    private final ReusableStream<Module> unfindableSourceDirectDependenciesCache =
            ReusableStream.create(() -> getWebfxModuleFile().getSourceModules())
                    .cache();
    private final ReusableStream<Module> sourceDirectDependenciesCache =
            ReusableStream.concat(
                    findableSourceDirectDependenciesCache,
                    unfindableSourceDirectDependenciesCache
            );
    private final ReusableStream<Module> resourceDirectDependenciesCache =
            ReusableStream.create(() -> getWebfxModuleFile().getResourceModules())
                    .cache();
    private final ReusableStream<Module> implicitApplicationModuleDependencyCache =
            ReusableStream.create(() -> {
                List<Module> implicitModules = new ArrayList<>();
                if (isExecutable()) {
                    String artifactId = getArtifactId();
                    ProjectModule applicationModule = getRootModule().findProjectModule(artifactId.substring(0, artifactId.lastIndexOf('-')));
                    if (applicationModule != null)
                        implicitModules.add(applicationModule);
                }
                return implicitModules.spliterator();
            });
    private final ReusableStream<Module> pluginDirectDependenciesCache =
            ReusableStream.create(() -> getWebfxModuleFile().getPluginModules())
                    .cache();
    private final ReusableStream<Module> directDependenciesWithoutImplicitProvidersCache =
            ReusableStream.concat(
                    sourceDirectDependenciesCache,
                    resourceDirectDependenciesCache,
                    implicitApplicationModuleDependencyCache,
                    pluginDirectDependenciesCache
            )
                    .distinct()
                    .cache();
    private final ReusableStream<Module> transitiveDependenciesWithoutImplicitProvidersCache =
            directDependenciesWithoutImplicitProvidersCache
                    .flatMap(ProjectModule::collectThisAndTransitiveDependencies)
                    .flatMap(this::replaceModuleWithEmulatedAndTransitiveDependenciesIfApplicable)
                    .distinct()
                    .cache();
    private final ReusableStream<Providers> executableProvidersCache =
            ReusableStream.create(this::collectExecutableProviders)
                    .cache();
    private final ReusableStream<ProjectModule> executableProvidersModulesCache =
            executableProvidersCache
                    .flatMap(Providers::getProviderModules);
    private final ReusableStream<Module> directDependenciesCache =
            ReusableStream.concat(
                    directDependenciesWithoutImplicitProvidersCache,
                    executableProvidersModulesCache
                    )
                    .distinct()
                    .cache();
    private final ReusableStream<Module> transitiveDependenciesCache =
            directDependenciesCache
                    .flatMap(ProjectModule::collectThisAndTransitiveDependencies)
                    .flatMap(this::replaceModuleWithEmulatedAndTransitiveDependenciesIfApplicable)
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
    private final ReusableStream<ProjectModule> transitiveProjectModulesWithoutImplicitProvidersCache =
            ReusableStream.create(() -> ProjectModule.filterProjectModules(transitiveDependenciesWithoutImplicitProvidersCache));
    private final ReusableStream<ProjectModule> requiredJavaServiceImplementationScopeCache =
            ReusableStream.concat(
                    transitiveProjectModulesWithoutImplicitProvidersCache,
                    ReusableStream.create(() -> getRootModule().findProjectModule("webfx-platform").getThisAndChildrenModulesInDepth().filter(m -> m.getTarget().gradeTargetMatch(getTarget()) >= 0)),
                    ReusableStream.create(() -> getRootModule().findProjectModule("webfx-framework").getThisAndChildrenModulesInDepth().filter(m -> m.getTarget().gradeTargetMatch(getTarget()) >= 0)),
                    ReusableStream.create(() -> getParentModule().getThisAndChildrenModulesInDepth().filter(m -> m.getTarget().gradeTargetMatch(getTarget()) >= 0))
            )
                    .distinct()
                    .cache();
    private final ReusableStream<ProjectModule> optionalJavaServiceImplementationScopeCache =
            requiredJavaServiceImplementationScopeCache
                    .filter(m -> transitiveDependenciesCache.anyMatch(m2 -> m.getArtifactId().startsWith(m2.getArtifactId())))
                    .cache();
    private final ReusableStream<ProjectModule> transitiveRequiredJavaServicesImplementationModules =
            transitiveProjectModulesWithoutImplicitProvidersCache
                    .flatMap(ProjectModule::getUsedRequiredJavaServices)
                    .distinct()
                    //.filter(s -> transitiveProjectModulesWithoutImplicitProvidersCache.noneMatch(m -> m.providesJavaService(s)))
                    .map(s -> RootModule.findBestMatchModuleProvidingJavaService(requiredJavaServiceImplementationScopeCache, s, getTarget()))
                    .distinct()
                    .cache();

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

    public Path getResourcesDirectory() {
        return homeDirectory.resolve("src/main/resources/");
    }

    private Path getMetaInfJavaServicesDirectory() {
        return getResourcesDirectory().resolve("META-INF/services/");
    }

    ProjectModule getParentModule() {
        return parentModule;
    }

    public RootModule getRootModule() {
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

    private boolean hasMetaInfJavaServicesDirectory() {
        if (hasMetaInfJavaServicesDirectory == null)
            hasMetaInfJavaServicesDirectory = isSourceModule() && Files.exists(getMetaInfJavaServicesDirectory());
        return hasMetaInfJavaServicesDirectory;
    }

    public WebfxModuleFile getWebfxModuleFile() {
        if (webfxModuleFile == null)
            webfxModuleFile = new WebfxModuleFile(this);
        return webfxModuleFile;
    }

    public JavaModuleFile getJavaModuleFile() {
        if (javaModuleFile == null)
            javaModuleFile = new JavaModuleFile(this);
        return javaModuleFile;
    }

    public GwtModuleFile getGwtModuleFile() {
        if (gwtModuleFile == null)
            gwtModuleFile = new GwtModuleFile(this);
        return gwtModuleFile;
    }

    public MavenModuleFile getMavenModuleFile() {
        if (mavenModuleFile == null)
            mavenModuleFile = new MavenModuleFile(this);
        return mavenModuleFile;
    }

    private Module replaceModuleWithEmulatedIfApplicable(Module m) {
        if (getTarget().isMonoPlatform(Platform.GWT)) {
            switch (m.getArtifactId()) {
                case "javafx-base":
                case "javafx-graphics":
                case "javafx-controls":
                    //    return getRootModule().getChildModuleInDepth("webfx-fxkit-emul-" + m.getArtifactId().replace("-", ""));
                    return getRootModule().findProjectModule("webfx-fxkit-gwt");
            }
        }
        return m;
    }

    private ReusableStream<Module> replaceModuleWithEmulatedAndTransitiveDependenciesIfApplicable(Module m) {
        Module emulatedModule = replaceModuleWithEmulatedIfApplicable(m);
        if (emulatedModule == m) {
            if (getTarget().isMonoPlatform(Platform.JRE))
                switch (m.getArtifactId()) {
                    case "javafx-base":
                    case "javafx-graphics":
                    case "javafx-controls":
                        //    return getRootModule().getChildModuleInDepth("webfx-fxkit-emul-" + m.getArtifactId().replace("-", ""));
                        return ReusableStream.of(m, getRootModule().findProjectModule("webfx-fxkit-javafx"));
                }
            return ReusableStream.of(m);
        }
        return ReusableStream.fromIterable(((ProjectModule) emulatedModule).collectThisAndTransitiveDependencies())
                .map(this::replaceModuleWithEmulatedIfApplicable)
                .distinct()
                ;
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
        return ReusableStream.concat(ReusableStream.of(this), getChildrenModules());
    }

    ReusableStream<ProjectModule> getChildrenModulesInDepth() {
        return childrenModulesInDepthCache;
    }

    ReusableStream<ProjectModule> getThisAndChildrenModulesInDepth() {
        return ReusableStream.concat(ReusableStream.of(this), getChildrenModulesInDepth());
    }

    ProjectModule findProjectModule(String artifactId) {
        return findProjectModule(artifactId, false);
    }

    ProjectModule findProjectModule(String artifactId, boolean silent) {
        Optional<ProjectModule> projectModule = getChildrenModulesInDepth()
                .filter(module -> module.getArtifactId().equals(artifactId))
                .findFirst();
        if (projectModule.isPresent())
            return projectModule.get();
        if (silent)
            return null;
        throw new IllegalArgumentException("Unable to find " + artifactId + " module under " + getArtifactId() + " module");
    }

    public ReusableStream<Module> getResourceModules() {
        return resourceDirectDependenciesCache;
    }

    public ReusableStream<String> getResourcePackages() {
        return getWebfxModuleFile().getResourcePackages();
    }

    public ReusableStream<String> getEmbedResources() {
        return getWebfxModuleFile().getEmbedResources();
    }

    public ReusableStream<String> getSystemProperties() {
        return getWebfxModuleFile().getSystemProperties();
    }

    public boolean isExecutable() {
        return getArtifactId().contains("-application-") && getTarget().isMonoPlatform();
    }

    public boolean isExecutable(Platform platform) {
        return isExecutable() && getTarget().isPlatformSupported(platform);
    }

    /******************************
     ***** Analyzing streams  *****
     ******************************/

    ///// Modules
    public ReusableStream<Module> getDirectDependencies() {
        return directDependenciesCache;
    }

    ReusableStream<Module> getThisAndDirectDependencies() {
        return ReusableStream.concat(ReusableStream.of((Module) this), directDependenciesCache);
    }

    ReusableStream<ProjectModule> getThisOrChildrenModulesInDepthDirectlyDependingOn(String moduleArtifactId) {
        return getThisAndChildrenModulesInDepth()
                .filter(module -> module.isDirectlyDependingOn(moduleArtifactId))
                ;
    }

    boolean isDirectlyDependingOn(String moduleArtifactId) {
        return getDirectDependencies().anyMatch(m -> moduleArtifactId.equals(m.getArtifactId()));
    }

    public ReusableStream<Module> getTransitiveDependencies() {
        return transitiveDependenciesCache;
    }

    public ReusableStream<Module> getThisAndTransitiveDependencies() {
        return ReusableStream.concat(ReusableStream.of((Module) this), transitiveDependenciesCache);
    }

    private Set<Module> collectThisAndTransitiveDependencies() {
        Set<Module> modules = new HashSet<>();
        collectThisAndTransitiveDependencies(modules);
        return modules;
    }

    private void collectThisAndTransitiveDependencies(Collection<Module> result) {
        if (!result.contains(this)) {
            result.add(this);
            getDirectDependencies().forEach(m -> {
                if ((m instanceof ProjectModule))
                    ((ProjectModule) m).collectThisAndTransitiveDependencies(result);
                else if (!result.contains(m))
                    result.add(m);
            });
        }
    }

    private static ReusableStream<Module> collectThisAndTransitiveDependencies(Module m) {
        if (!(m instanceof ProjectModule))
            return ReusableStream.of(m);
        return ReusableStream.fromIterable(((ProjectModule) m).collectThisAndTransitiveDependencies());
    }

    public ReusableStream<ProjectModule> getTransitiveRequiredJavaServicesImplementationModules() {
        return transitiveRequiredJavaServicesImplementationModules;
    }

    private ReusableStream<ProjectModule> getRequiredJavaServiceImplementationScope() {
        return requiredJavaServiceImplementationScopeCache;
    }

    private ReusableStream<ProjectModule> getOptionalJavaServiceImplementationScope() {
        return optionalJavaServiceImplementationScopeCache;
    }

    public ReusableStream<Providers> getExecutableProviders() {
        return executableProvidersCache;
    }

    private ReusableStream<Providers> collectExecutableProviders() {
        if (!isExecutable())
            return ReusableStream.empty();
        Set<ProjectModule> allProviderModules = new HashSet<>();
        Set<String> allSpiClassNames = new HashSet<>();
        return ReusableStream.concat(
                // Collecting single/required SPI providers
                collectProviders(this, true, allProviderModules, allSpiClassNames),
                // Collecting multiple/optional SPI providers
                collectProviders(this, false, allProviderModules, allSpiClassNames),
                // Collecting subsequent single/required SPI providers
                ReusableStream.create(() -> new HashSet<>(allProviderModules).spliterator())
                        .flatMap(m -> collectProviders(m, true, allProviderModules, allSpiClassNames)),
                // Collecting subsequent multiple/optional SPI providers
                ReusableStream.create(() -> new HashSet<>(allProviderModules).spliterator())
                        .flatMap(m -> collectProviders(m, false, allProviderModules, allSpiClassNames))
        );
    }

    private ReusableStream<Providers> collectProviders(ProjectModule module, boolean single, Set<ProjectModule> allProviderModules, Set<String> allSpiClassNames) {
        ReusableStream<ProjectModule> searchScope = single ? getRequiredJavaServiceImplementationScope() : getOptionalJavaServiceImplementationScope();
        ReusableStream<Module> stackWithoutImplicitProviders = ReusableStream.concat(ReusableStream.of(module), module.transitiveDependenciesWithoutImplicitProvidersCache);
        return ProjectModule.filterProjectModules(stackWithoutImplicitProviders)
                .flatMap(m -> single ? m.getUsedRequiredJavaServices() : m.getUsedOptionalJavaServices())
                .map(spiClassName -> {
                    if (allSpiClassNames.contains(spiClassName))
                        return null; // returning null to avoid rework
                    allSpiClassNames.add(spiClassName);
                    Providers providers = new Providers(spiClassName, RootModule.findModulesProvidingJavaService(searchScope, spiClassName, getTarget(), single));
                    providers.getProviderModules().collect(Collectors.toCollection(() -> allProviderModules));
                    if (providers.getProviderClassNames().count() == 0)
                        warning("No provider found for " + spiClassName + " among " + searchScope.map(ProjectModule::getArtifactId).stream().sorted().collect(Collectors.toList()));
                    return providers;
                })
                .filter(Objects::nonNull) // Removing nulls
                ;
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


    public ReusableStream<String> getUsedRequiredJavaServices() {
        return usedRequiredJavaServicesCache;
    }

    public ReusableStream<String> getUsedOptionalJavaServices() {
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
                .anyMatch(javaService::equals)
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

    //// Static utility methods

    public static ReusableStream<ProjectModule> filterProjectModules(ReusableStream<Module> modules) {
        return modules
                .filter(m -> m instanceof ProjectModule)
                .map(m -> (ProjectModule) m);
    }

    static void warning(Object message) {
        System.err.println("WARNING: " + message);
    }
}