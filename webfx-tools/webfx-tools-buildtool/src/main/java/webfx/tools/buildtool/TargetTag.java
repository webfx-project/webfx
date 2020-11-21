package webfx.tools.buildtool;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public enum TargetTag {

    PLAT_API_PARTITION(),
    JAVA            ("java", PLAT_API_PARTITION, Platform.JRE /*, Platform.ANDROID*/),
    JRE             ("jre", JAVA, Platform.JRE),
    JAVAFX          ("javafx", JRE), // => DESKTOP
    GLUON          ("gluon", JAVAFX), // => native
    WEB             ("web", PLAT_API_PARTITION, Platform.GWT, Platform.TEAVM), // => BROWSER
    GWT             ("gwt", WEB, Platform.GWT),
    TEAVM           ("teavm", WEB, Platform.TEAVM),

    ARCH_PARTITION  (),
    SHARED          ("shared", ARCH_PARTITION),
    SERVER          ("server", SHARED), // => JRE
    CLIENT          ("client", SHARED),
    BACKEND         ("backend", CLIENT),
    FRONTEND        ("frontend", CLIENT),

    VIEWER_PARTITION(), // => CLIENT
    DESKTOP         ("desktop", VIEWER_PARTITION), // => JRE
    MOBILE          ("mobile", VIEWER_PARTITION),
    BROWSER         ("browser", VIEWER_PARTITION),

    WEB_TECHNO_PARTITION(), // => WEB
    HTML            ("html", WEB_TECHNO_PARTITION),
    SVG             ("svg", WEB_TECHNO_PARTITION),

    SERVER_TECHNO_PARTITION(), // => SERVER
    VERTX           ("vertx", SERVER_TECHNO_PARTITION),

    EMUL            ("emul", null), // => GWT
    ;

    private static TargetTag directImpliedTag(TargetTag tag) {
        switch (tag) {
            case SERVER: return JRE;
            case JAVAFX: return DESKTOP;
            case WEB: return BROWSER;
            case VIEWER_PARTITION: return CLIENT;
            case DESKTOP: return JRE;
            //case WEB: return GWT;
            case WEB_TECHNO_PARTITION: return WEB;
            case SERVER_TECHNO_PARTITION: return SERVER;
            case EMUL: return GWT;
        }
        return null;
    }

    private final String tagName;
    private final TargetTag parentTag;
    private Platform[] supportedPlatforms;
    private final TargetTag partitionTag;
    private final int partitionDepth;
    private TargetTag[] transitiveImpliedTags;
    private Map<TargetTag, TargetTag> deepestMembers;

    TargetTag() {
        this(null, null);
    }

    TargetTag(String tagName, TargetTag parentTag) {
        this(tagName, parentTag, (Platform[]) null);
    }

    TargetTag(String tagName, TargetTag parentTag, Platform... supportedPlatforms) {
        this.parentTag = parentTag;
        this.tagName = tagName;
        this.supportedPlatforms = supportedPlatforms;
        TargetTag partitionTag = this;
        int partitionDepth = 0;
        while (partitionTag.getParentTag() != null) {
            partitionDepth++;
            partitionTag = partitionTag.getParentTag();
        }
        this.partitionTag = partitionTag;
        this.partitionDepth = partitionDepth;
    }

    public TargetTag getParentTag() {
        return parentTag;
    }

    String getTagName() {
        return tagName;
    }

    Platform[] getSupportedPlatforms() {
        if (supportedPlatforms == null) {
            List<Platform> platforms = new ArrayList<>(Arrays.asList(Platform.values())); // Wrapped in ArrayList to make it modifiable
            restrictPlatforms(platforms, this, true);
            supportedPlatforms = platforms.toArray(Platform[]::new);
        }
        return supportedPlatforms;
    }

    private static void restrictPlatforms(List<Platform> platforms, TargetTag tag, boolean includeImpliedTags) {
        if (tag.supportedPlatforms != null)
            platforms.removeIf(platform -> !tag.isPlatformSupported(platform));
        else if (tag.parentTag != null)
            restrictPlatforms(platforms, tag.parentTag, true);
        if (includeImpliedTags)
            for (TargetTag impliedTag : tag.getTransitiveImpliedTags())
                restrictPlatforms(platforms, impliedTag, false);
    }

    public TargetTag[] getTransitiveImpliedTags() {
        if (transitiveImpliedTags == null) {
            List<TargetTag> tags = new ArrayList<>();
            addTransitiveImpliedTags(tags, this);
            tags.remove(this);
            transitiveImpliedTags = tags.toArray(TargetTag[]::new);
        }
        return transitiveImpliedTags;
    }

    private static void addTransitiveImpliedTags(Collection<TargetTag> transitiveTags, TargetTag tag) {
        TargetTag directImpliedTag = directImpliedTag(tag);
        if (directImpliedTag != null && !transitiveTags.contains(directImpliedTag)) {
            transitiveTags.add(directImpliedTag);
            addTransitiveImpliedTags(transitiveTags, directImpliedTag);
        }
        if (tag.getParentTag() != null)
            addTransitiveImpliedTags(transitiveTags, tag.getParentTag());
    }

    public TargetTag getPartitionTag() {
        return partitionTag;
    }

    public int getPartitionDepth() {
        return partitionDepth;
    }

    boolean isPlatformSupported(Platform platform) {
        return Arrays.asList(getSupportedPlatforms()).contains(platform);
    }

    boolean isPlatformCompatible(TargetTag requestedTag) {
        return Arrays.stream(requestedTag.getSupportedPlatforms()).anyMatch(this::isPlatformSupported);
    }

    int gradeCompatibility(TargetTag requestedTag) {
        return isPlatformCompatible(requestedTag) ? gradePartitionCompatibility(requestedTag) : -1;
    }

    private int gradePartitionCompatibility(TargetTag requestedTag) {
        int grade = 0;
        Map<TargetTag, TargetTag> deepestMembers = getDeepestPartitionMembers();
        for (Map.Entry<TargetTag, TargetTag> requestedEntry : requestedTag.getDeepestPartitionMembers().entrySet()) {
            TargetTag deepestMember = deepestMembers.get(requestedEntry.getKey());
            if (deepestMember != null) {
                TargetTag deepestRequestedMember = requestedEntry.getValue();
                if (!deepestMember.belongsToSamePartitionBranch(deepestRequestedMember))
                    return -1;
                grade += deepestMember.getPartitionDepth();
            }
        }
        return grade;
    }

    private Map<TargetTag, TargetTag> getDeepestPartitionMembers() {
        if (deepestMembers == null) {
            deepestMembers = new HashMap<>();
            deepestMembers.put(getPartitionTag(), this);
            for (TargetTag impliedTag : getTransitiveImpliedTags()) {
                TargetTag partitionTag = impliedTag.getPartitionTag();
                TargetTag deepestMember = deepestMembers.get(partitionTag);
                if (deepestMember == null || impliedTag.getPartitionDepth() > deepestMember.getPartitionDepth())
                    deepestMembers.put(partitionTag, impliedTag);
            }
        }
        return deepestMembers;
    }

    private boolean belongsToSamePartitionBranch(TargetTag otherMember) {
        if (otherMember.getPartitionTag() != getPartitionTag())
            return false;
        TargetTag deepest = otherMember.getPartitionDepth() > getPartitionDepth() ? otherMember : this;
        TargetTag lightest = deepest == otherMember ? this : otherMember;
        while (true) {
            if (deepest == lightest)
                return true;
            if (deepest.getParentTag() == null)
                return false;
            deepest = deepest.getParentTag();
        }
    }

    static TargetTag fromTagBName(String tagName) {
        return Arrays.stream(TargetTag.values())
                .filter(tag -> tagName.equals(tag.getTagName()))
                .findFirst()
                .orElse(null);
    }

    public static TargetTag[] parseTags(String text) {
        return Arrays.stream(text.split("-"))
                .map(TargetTag::fromTagBName)
                .filter(Objects::nonNull)
                .toArray(TargetTag[]::new);
    }
}
