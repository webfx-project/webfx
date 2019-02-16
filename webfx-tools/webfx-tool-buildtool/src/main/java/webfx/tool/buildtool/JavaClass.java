package webfx.tool.buildtool;

import webfx.tool.buildtool.util.streamable.Streamable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
final class JavaClass {

    private final Path javaFilePath;
    private final ProjectModule projectModule;
    private String packageName;
    private String className;
    private final Streamable<String> usedJavaPackagesNamesCache;

    /***********************
     ***** Constructor *****
     ***********************/

    JavaClass(Path javaFilePath, ProjectModule projectModule) {
        this.javaFilePath = javaFilePath;
        this.projectModule = projectModule;
        // Cache is instantiated now (because declared final)
        usedJavaPackagesNamesCache = Streamable.fromIterable(new PackagesUsedInJavaFilePathParser(javaFilePath))
                .distinct()
                .cache();
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

    Streamable<String> getUsedJavaPackagesNamesCache() {
        return usedJavaPackagesNamesCache;
    }

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
     ***** Java file parsing classes *****
     *************************************/

    private static class PackagesUsedInJavaFilePathParser implements Iterable<String> {

        private final Path javaFilePath;

        private PackagesUsedInJavaFilePathParser(Path javaFilePath) {
            this.javaFilePath = javaFilePath;
        }

        @Override
        public Iterator<String> iterator() {
            try {
                return new PackagesUsedInJavaFileContentParser(new String(Files.readAllBytes(javaFilePath)));
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyIterator();
            }
        }
    }

    private static class PackagesUsedInJavaFileContentParser implements Iterator<String> {

        private static Pattern javaPackagePattern = Pattern.compile("([\\s(])([a-z]+(\\.[a-z]+)+)(\\.[A-Z])");

        private final String javaFileContent;
        private final Matcher javaPackageMatcher;

        private boolean blockCommentOpen; // Set to true when index was in a block comment on last isInsideComment() call
        private int blockCommentStartIndex;
        private int blockCommentEndIndex;
        private int inlineCommentStartIndex;

        private PackagesUsedInJavaFileContentParser(String javaFileContent) {
            this.javaFileContent = javaFileContent;
            javaPackageMatcher = javaPackagePattern.matcher(javaFileContent);
            blockCommentOpen = false;
            inlineCommentStartIndex = -1; //javaFileContent.indexOf("//");
            blockCommentStartIndex = javaFileContent.indexOf("/*");
            blockCommentEndIndex = javaFileContent.indexOf("*/");
        }

        @Override
        public boolean hasNext() {
            while (javaPackageMatcher.find()) {
                if (!isInsideComment(javaPackageMatcher.start(2)))
                    return true;
            }
            return false;
        }

        @Override
        public String next() {
            return javaPackageMatcher.group(2);
        }

        private boolean isInsideComment(int index) {
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
                        blockCommentEndIndex = javaFileContent.indexOf("*/", blockCommentStartIndex);
                    if (blockCommentEndIndex == -1 || blockCommentEndIndex > index) { // If no end of the block comment on this line,
                        blockCommentOpen = true; // then returning with blockCommentOpen = true
                        return;
                    }
                    // Updating blockCommentStartIndex before looping
                    blockCommentStartIndex = javaFileContent.indexOf("/*", blockCommentEndIndex);
                } else {
                    if (blockCommentEndIndex == -1 || blockCommentEndIndex > index) // Means the comment is still not close
                        return; // So exiting the loop with still blockCommentOpen = true
                    // End of comment was reached but perhaps a new comment was reopen after, so updating inlineCommentStartIndex to know
                    if (blockCommentStartIndex != -1 && blockCommentStartIndex < blockCommentEndIndex) // the condition to be updated
                        blockCommentStartIndex = javaFileContent.indexOf("/*", blockCommentEndIndex);
                    if (blockCommentStartIndex == -1 || blockCommentStartIndex > index) { // Means no new comment on that line
                        blockCommentOpen = false;
                        return;
                    }
                    // Updating blockCommentEndIndex before looping
                    blockCommentEndIndex = javaFileContent.indexOf("*/", blockCommentStartIndex);
                }
            }
        }
    }
}
