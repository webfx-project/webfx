package webfx.tool.buildtool.modulefiles;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import webfx.tool.buildtool.ProjectModule;

import java.nio.file.Path;

/**
 * @author Bruno Salmon
 */
public final class MavenModuleFile extends XmlModuleFile {

    public MavenModuleFile(ProjectModule module) {
        super(module, true);
    }

    @Override
    Path getModulePath() {
        return resolveFromModuleHomeDirectory("pom.xml");
    }

    @Override
    void updateDocument(Document document) {
        Node dependenciesNode = lookupNode("/project/dependencies");
        getModule().getDirectDependencies()
                .forEach(System.out::println);
    }
}
