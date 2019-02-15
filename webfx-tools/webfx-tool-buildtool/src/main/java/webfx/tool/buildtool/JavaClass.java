package webfx.tool.buildtool;

import webfx.tool.buildtool.util.spliterable.operable.OperableSpliterable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
final class JavaClass {

    private static Pattern javaPackagePattern = Pattern.compile("([\\s(])([a-z]+(\\.[a-z]+)+)(\\.[A-Z])");

    private final Path javaFilePath;
    private final ProjectModule projectModule;
    private String packageName;
    private String className;
    private final OperableSpliterable<String> usedJavaPackagesNamesCache;

    /***********************
     ***** Constructor *****
     ***********************/

    JavaClass(Path javaFilePath, ProjectModule projectModule) {
        this.javaFilePath = javaFilePath;
        this.projectModule = projectModule;
        // Cache is instantiated now (because declared final)
        usedJavaPackagesNamesCache = OperableSpliterable.create(() ->
                extractPackagesFromJavaContent(new String(Files.readAllBytes(javaFilePath))).spliterator()
        ).cache();
    }


    /*************************
     ***** Basic getters *****
     *************************/

    Path getJavaFilePath() {
        return javaFilePath;
    }

    ProjectModule getProjectModule() {
        return projectModule;
    }

    String getPackageName() {
        if (packageName == null) {
            getClassName();
            packageName = className.substring(0, className.lastIndexOf('.'));
        }
        return packageName;
    }

    String getClassName() {
        if (className == null)
            className = javaFilePath.toString().substring(projectModule.getJavaSourceDirectoryPath().toString().length() + 1, javaFilePath.toString().length() - 5).replaceAll("[/\\\\]", ".");
        return className;
    }


    /******************************
     ***** Analyzing streams  *****
     ******************************/

    Stream<String> analyzeUsedJavaPackagesNames() {
        return usedJavaPackagesNamesCache.stream();
    }


    /********************
     ***** Logging  *****
     ********************/

    @Override
    public String toString() {
        return getClassName();
    }


    /*************************************
     ***** Java file parsing methods *****
     *************************************/

    private Collection<String> extractPackagesFromJavaContent(String javaContent) {
        Collection<String> packages = new ArrayList<>();
        if (!blockCommentOpen) {
            Matcher matcher = javaPackagePattern.matcher(javaContent);
            while (matcher.find()) {
                if (!isInsideComment(javaContent, matcher.start(2))) {
                    String packageName = matcher.group(2);
                    if (!packages.contains(packageName))
                        packages.add(packageName);
                }
            }
        }
        if (packages.isEmpty())
            isInsideComment(javaContent, 0); // Necessary call to update the comment state
        return packages;
    }

    private String lastJavaContent;
    private boolean blockCommentOpen; // Set to true when index was in a block comment on last isInsideComment() call
    private int blockCommentStartIndex;
    private int blockCommentEndIndex;
    private int inlineCommentStartIndex;

    private boolean isInsideComment(String javaContent, int index) {
        if (javaContent != lastJavaContent) {
            if (lastJavaContent != null)
                updateBlockCommentState(lastJavaContent.length());
            lastJavaContent = javaContent;
            blockCommentOpen = false;
            inlineCommentStartIndex = javaContent.indexOf("//");
            blockCommentStartIndex = javaContent.indexOf("/*");
            blockCommentEndIndex = javaContent.indexOf("*/");
        }
        updateBlockCommentState(index);
        return blockCommentOpen || inlineCommentStartIndex >= 0 && index > inlineCommentStartIndex;
    }

    private void updateBlockCommentState(int index) {
        while (true) {
            if (!blockCommentOpen) { // If no block comment so far
                // if no more block comment on the line or after the index,
                if (blockCommentStartIndex == -1 || blockCommentStartIndex > index)
                    return; // then returning with still blockCommentOpen = false
                // A new block comment has started before the index, now searching the end of this block
                if (blockCommentEndIndex != -1 && blockCommentEndIndex < blockCommentStartIndex) // The condition to require an update of blockCommentEndIndex
                    blockCommentEndIndex = lastJavaContent.indexOf("*/", blockCommentStartIndex);
                if (blockCommentEndIndex == -1 || blockCommentEndIndex > index) { // If no end of the block comment on this line,
                    blockCommentOpen = true; // then returning with blockCommentOpen = true
                    return;
                }
                // Updating blockCommentStartIndex before looping
                blockCommentStartIndex = lastJavaContent.indexOf("/*", blockCommentEndIndex);
            } else {
                if (blockCommentEndIndex == -1 || blockCommentEndIndex > index) // Means the comment is still not close
                    return; // So exiting the loop with still blockCommentOpen = true
                // End of comment was reached but perhaps a new comment was reopen after, so updating inlineCommentStartIndex to know
                if (blockCommentStartIndex != -1 && blockCommentStartIndex < blockCommentEndIndex) // the condition to be updated
                    blockCommentStartIndex = lastJavaContent.indexOf("/*", blockCommentEndIndex);
                if (blockCommentStartIndex == -1 || blockCommentStartIndex > index) { // Means no new comment on that line
                    blockCommentOpen = false;
                    return;
                }
                // Updating blockCommentEndIndex before looping
                blockCommentEndIndex = lastJavaContent.indexOf("*/", blockCommentStartIndex);
            }
        }
    }
}
