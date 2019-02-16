package webfx.tool.buildtool.util.javacode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * @author Bruno Salmon
 */
public final class PatternInJavaCodeFinder implements Iterable<String> {

    private final String javaCode;
    private final JavaCodePattern javaCodePattern;

    public PatternInJavaCodeFinder(JavaCodePattern javaCodePattern, Path javaFilePath) {
        this(javaCodePattern, loadJavaCode(javaFilePath));
    }

    private PatternInJavaCodeFinder(JavaCodePattern javaCodePattern, String javaCode) {
        this.javaCode = javaCode;
        this.javaCodePattern = javaCodePattern;
    }

    private static String loadJavaCode(Path javaFilePath) {
        try {
            return new String(Files.readAllBytes(javaFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private final Matcher matcher = javaCodePattern.getPattern().matcher(javaCode);
            private final CommentFinder blockCommentFinder = new CommentFinder("/*", "*/");
            private final CommentFinder inlineCommentFinder = new CommentFinder("//", "\n");

            @Override
            public boolean hasNext() {
                while (matcher.find()) {
                    if (!isInsideComment(matcher.start(javaCodePattern.getMatchingGroup())))
                        return true;
                }
                return false;
            }

            @Override
            public String next() {
                return matcher.group(javaCodePattern.getMatchingGroup());
            }

            private boolean isInsideComment(int index) {
                return blockCommentFinder.isInsideComment(index) || inlineCommentFinder.isInsideComment(index) && inlineCommentFinder.commentStartIndex > blockCommentFinder.commentEndIndex;
            }
        };
    }

    private class CommentFinder {
        private final String commentStartingToken;
        private final String commentEndingToken;

        private boolean commentOpen; // Set to true when index is in a block comment on last isInsideComment() call
        private int commentStartIndex;
        private int commentEndIndex;

        private CommentFinder(String commentStartingToken, String commentEndingToken) {
            this.commentStartingToken = commentStartingToken;
            this.commentEndingToken = commentEndingToken;
            updateCommentStartIndex();
            updateCommentEndIndex();
        }

        private void updateCommentStartIndex() {
            commentStartIndex = javaCode.indexOf(commentStartingToken, commentEndIndex);
        }

        private void updateCommentEndIndex() {
            commentEndIndex = javaCode.indexOf(commentEndingToken, commentStartIndex);
        }

        private boolean isInsideComment(int index) {
            updateCommentState(index);
            return commentOpen;
        }

        private void updateCommentState(int index) {
            while (true) {
                if (!commentOpen) { // If no block comment so far
                    // if no more comment on the line or after the index,
                    if (commentStartIndex == -1 || commentStartIndex > index)
                        return; // then returning with still blockCommentOpen = false
                    // A new comment has started before the index, now searching the end of this comment
                    if (commentEndIndex != -1 && commentEndIndex < commentStartIndex) // The condition to require an update of commentEndIndex
                        updateCommentEndIndex();
                    if (commentEndIndex == -1 || commentEndIndex > index) { // If no end of the comment on this line,
                        commentOpen = true; // then returning with commentOpen = true
                        return;
                    }
                    // Updating commentStartIndex before looping
                    updateCommentStartIndex();
                } else {
                    if (commentEndIndex == -1 || commentEndIndex > index) // Means the comment is still not closed
                        return; // So exiting the loop with still commentOpen = true
                    // End of comment was reached but perhaps a new comment was reopen after, so updating commentStartIndex to know
                    if (commentStartIndex != -1 && commentStartIndex < commentEndIndex) // the condition to be updated
                        updateCommentStartIndex();
                    if (commentStartIndex == -1 || commentStartIndex > index) { // Means no new comment on that line
                        commentOpen = false;
                        return;
                    }
                    // Updating blockCommentEndIndex before looping
                    updateCommentEndIndex();
                }
            }
        }
    }
}
