package webfx.tool.buildtool;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class Target {

    private final ProjectModule module;
    private final TargetTag[] tags;

    Target(ProjectModule module) {
        this.module = module;
        this.tags = getArtifactIdTags(module.getArtifactId());
    }

    Target(TargetTag... tags) {
        this.module = null;
        this.tags = tags;
    }

    ProjectModule getModule() {
        return module;
    }

    TargetTag[] getTags() {
        return tags;
    }

    public boolean isPlatformSupported(Platform platform) {
        return Arrays.stream(getTags()).allMatch(tag -> tag.isPlatformSupported(platform));
    }

    int gradeTargetMatch(Target requestedTarget) {
        int grade = 0;
        for (TargetTag requestedTag : requestedTarget.getTags()) {
            for (TargetTag tag : getTags()) {
                int tagGrade = tag.gradeCompatibility(requestedTag);
                if (tagGrade < 0)
                    return tagGrade;
                grade += tagGrade;
            }
        }
        return grade;
    }

    private static TargetTag[] getArtifactIdTags(String artifactId) {
        return Arrays.stream(artifactId.split("-"))
                .map(TargetTag::fromTagBName)
                .filter(Objects::nonNull)
                .toArray(TargetTag[]::new);
    }
}
