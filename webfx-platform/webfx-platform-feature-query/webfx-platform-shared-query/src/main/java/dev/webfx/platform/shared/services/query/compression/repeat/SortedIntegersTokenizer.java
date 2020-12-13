package dev.webfx.platform.shared.services.query.compression.repeat;

/**
 * @author Bruno Salmon
 */
final class SortedIntegersTokenizer {
    StringBuilder sb;
    int lastSeqStart;
    int lastInt;
    String token;

    void pushInt(int nextInt) {
        if (sb == null)
            sb = new StringBuilder().append(lastSeqStart = lastInt = nextInt);
        else if (nextInt == lastInt + 1)
            lastInt = nextInt;
        else {
            if (nextInt <= lastInt)
                throw new IllegalArgumentException("Integer must be pushed in the ascending order");
            if (lastInt > lastSeqStart)
                sb.append('&').append(lastInt - lastSeqStart);
            sb.append('+').append(nextInt - lastInt);
            lastSeqStart = lastInt = nextInt;
        }
    }

    String token() {
        if (token == null && sb != null) {
            if (lastInt > lastSeqStart)
                sb.append('&').append(lastInt - lastSeqStart);
            token = sb.toString();
            sb = null;
        }
        return token;
    }

}
