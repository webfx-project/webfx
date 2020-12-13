package dev.webfx.platform.shared.services.query.compression.repeat;

/**
 * @author Bruno Salmon
 */
final class SortedIntegersTokenReader {
    final String token;
    int pos = 0;
    int lastSeqStart;
    int lastInt;
    int lastSeqEnd;

    public SortedIntegersTokenReader(String token) {
        this.token = token;
    }

    boolean hasNext() {
        return token != null && (pos != -1 || lastInt <= lastSeqEnd);
    }

    int nextInt() {
        if (!hasNext())
            return -1;
        if (pos == 0 || lastInt > lastSeqEnd) {
            int plusPos = token.indexOf('+', pos);
            String commaToken = plusPos != -1 ? token.substring(pos, plusPos) : token.substring(pos);
            pos = plusPos != -1 ? plusPos + 1 : -1;
            int andPos = commaToken.indexOf('&');
            if (andPos == -1)
                lastSeqStart = lastInt = lastSeqEnd = lastSeqEnd + Integer.parseInt(commaToken);
            else {
                lastSeqStart = lastInt = lastSeqEnd + Integer.parseInt(commaToken.substring(0, andPos));
                lastSeqEnd = lastSeqStart + Integer.parseInt(commaToken.substring(andPos + 1));
            }
        }
        return lastInt++;
    }
}
