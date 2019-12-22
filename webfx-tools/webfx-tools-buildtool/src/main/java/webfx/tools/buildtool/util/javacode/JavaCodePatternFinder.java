package webfx.tools.buildtool.util.javacode;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
class JavaCodePatternFinder implements Iterable<String> {

    private final JavaCodePattern javaCodePattern;
    private final JavaCode javaCode;

    JavaCodePatternFinder(JavaCodePattern javaCodePattern, JavaCode javaCode) {
        this.javaCodePattern = javaCodePattern;
        this.javaCode = javaCode;
    }

    private String getTextCode() {
        return javaCode.getTextCode();
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private final Matcher matcher = javaCodePattern.getPattern().matcher(getTextCode());
            private final StringOrCommentFinder blockCommentFinder = new StringOrCommentFinder("/*", "*/");
            private final StringOrCommentFinder inlineCommentFinder = new StringOrCommentFinder("//", "\n");
            private final StringOrCommentFinder stringFinder = new StringOrCommentFinder("\"", "\"");

            @Override
            public boolean hasNext() {
                while (matcher.find()) {
                    if (!isInsideStringOrComment(matcher.start(javaCodePattern.getMatchingGroup())))
                        return true;
                }
                return false;
            }

            @Override
            public String next() {
                return mapFoundGroup(matcher.group(javaCodePattern.getMatchingGroup()));
            }

            private boolean isInsideStringOrComment(int index) {
                return blockCommentFinder.isInsideStringOrComment(index)
                        || inlineCommentFinder.isInsideStringOrComment(index) && inlineCommentFinder.stringOrCommentStartIndex > blockCommentFinder.stringOrCommentEndIndex
                        || stringFinder.isInsideStringOrComment(index) && stringFinder.stringOrCommentStartIndex > blockCommentFinder.stringOrCommentEndIndex
                        ;
            }
        };
    }

    private class StringOrCommentFinder {
        private final String stringOrCommentStartingToken;
        private final String stringOrCommentEndingToken;

        private boolean stringOrCommentOpen; // Set to true when index is in a block comment on last isInsideComment() call
        private int stringOrCommentStartIndex;
        private int stringOrCommentEndIndex;

        private StringOrCommentFinder(String stringOrCommentStartingToken, String stringOrCommentEndingToken) {
            this.stringOrCommentStartingToken = stringOrCommentStartingToken;
            this.stringOrCommentEndingToken = stringOrCommentEndingToken;
            updateStringOrCommentStartIndex();
            updateStringOrCommentEndIndex();
        }

        private void updateStringOrCommentStartIndex() {
            int fromIndex = stringOrCommentEndIndex;
            if (fromIndex > 0 && stringOrCommentEndingToken.equals(stringOrCommentStartingToken))
                fromIndex++;
            stringOrCommentStartIndex = getTextCode().indexOf(stringOrCommentStartingToken, fromIndex);
        }

        private void updateStringOrCommentEndIndex() {
            int fromIndex = stringOrCommentStartIndex + 1;
            while (true) {
                stringOrCommentEndIndex = getTextCode().indexOf(stringOrCommentEndingToken, fromIndex);
                if (stringOrCommentEndIndex > 1 && stringOrCommentEndingToken.equals(stringOrCommentStartingToken) && getTextCode().charAt(stringOrCommentEndIndex) == '\\') {
                    fromIndex = stringOrCommentEndIndex + 1;
                } else
                    break;
            }
        }

        private boolean isInsideStringOrComment(int index) {
            updateStateToIndex(index);
            return stringOrCommentOpen;
        }

        private void updateStateToIndex(int index) {
            while (true) {
                if (!stringOrCommentOpen) { // If no block comment so far
                    // if no more comment on the line or after the index,
                    if (stringOrCommentStartIndex == -1 || stringOrCommentStartIndex > index)
                        return; // then returning with still blockCommentOpen = false
                    // A new comment has started before the index, now searching the end of this comment
                    if (stringOrCommentEndIndex != -1 && stringOrCommentEndIndex < stringOrCommentStartIndex) // The condition to require an update of stringOrCommentEndIndex
                        updateStringOrCommentEndIndex();
                    if (stringOrCommentEndIndex == -1 || stringOrCommentEndIndex > index) { // If no end of the comment on this line,
                        stringOrCommentOpen = true; // then returning with stringOrCommentOpen = true
                        return;
                    }
                    // Updating stringOrCommentStartIndex before looping
                    updateStringOrCommentStartIndex();
                } else {
                    if (stringOrCommentEndIndex == -1 || stringOrCommentEndIndex > index) // Means the comment is still not closed
                        return; // So exiting the loop with still stringOrCommentOpen = true
                    // End of comment was reached but perhaps a new comment was reopen after, so updating stringOrCommentStartIndex to know
                    if (stringOrCommentStartIndex != -1 && stringOrCommentStartIndex < stringOrCommentEndIndex) // the condition to be updated
                        updateStringOrCommentStartIndex();
                    if (stringOrCommentStartIndex == -1 || stringOrCommentStartIndex > index) { // Means no new comment on that line
                        stringOrCommentOpen = false;
                        return;
                    }
                    // Updating stringOrCommentEndingToken before looping
                    updateStringOrCommentEndIndex();
                }
            }
        }
    }


    String mapFoundGroup(String group) {
        return group;
    }

    String resolveFullClassName(String className) {
        if (className.contains("."))
            return className;
        Iterator<String> classImportIterator = new JavaCodePatternFinder(new JavaCodePattern(Pattern.compile("^\\s*import\\s+([a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)*\\." + className + "\\s*)\\s*;", Pattern.MULTILINE), 1), javaCode).iterator();
        return classImportIterator.hasNext() ? classImportIterator.next()
                : javaCodePackageName() + "." + className;
    }

    String javaCodePackageName() {
        Iterator<String> packageIterator = new JavaCodePatternFinder(new JavaCodePattern(Pattern.compile("^\\s*package\\s+([a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)*)\\s*;", Pattern.MULTILINE), 1), javaCode).iterator();
        return packageIterator.hasNext() ?
                packageIterator.next()
                : null;
    }
}
