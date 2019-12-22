package webfx.tools.buildtool;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class Target {

    private final ProjectModule module;
    private final TargetTag[] tags;

    Target(ProjectModule module) {
        this.module = module;
        this.tags = TargetTag.parseTags(module.getName());
    }

    public Target(TargetTag... tags) {
        this.module = null;
        this.tags = tags;
    }

    ProjectModule getModule() {
        return module;
    }

    TargetTag[] getTags() {
        return tags;
    }

    public boolean hasTag(TargetTag tag) {
        return Arrays.asList(tags).contains(tag);
    }

    public Platform[] getSupportedPlatforms() {
        return Arrays.stream(Platform.values()).filter(this::isPlatformSupported).toArray(Platform[]::new);
    }

    public boolean isPlatformSupported(Platform platform) {
        return Arrays.stream(getTags()).allMatch(tag -> tag.isPlatformSupported(platform));
    }

    public boolean isMonoPlatform() {
        return getSupportedPlatforms().length == 1;
    }

    public boolean isMonoPlatform(Platform platform) {
        Platform[] supportedPlatforms = getSupportedPlatforms();
        return supportedPlatforms.length == 1 && supportedPlatforms[0] == platform;
    }

    int gradeTargetMatch(Target requestedTarget) {
        int grade = 0;
        for (TargetTag requestedTag : requestedTarget.getTags()) {
            for (TargetTag tag : getTags()) {
                int tagGrade = tag.gradeCompatibility(requestedTag);
                if (tagGrade < 0 && tag.gradeCompatibility(requestedTag) < 0)
                    return tagGrade;
                grade += tagGrade;
            }
        }
        return grade;
    }
}
