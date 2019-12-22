package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;

/**
 * @author Bruno Salmon
 */
public final class Like<T> extends BinaryBooleanExpression<T> {

    public Like(Expression<T> left, Expression<T> right) {
        super(left, " like ", right, 5);
    }

    public Boolean evaluateCondition(Object a, Object b, DomainReader<T> domainReader) {
        return b instanceof String && new LikeImpl((String) b).compare(a);
    }

// From HSQLDB Like.java

    static final class LikeImpl {

        private char[] cLike;
        private int[] wildCardType;
        private int iLen;
        private boolean isIgnoreCase;
        private final Character escapeChar = null;
        private static final int UNDERSCORE_CHAR = 1;
        private static final int PERCENT_CHAR = 2;

        /*
        Like(Character escape) {
            escapeChar = escape;
        }
        */

        LikeImpl(String s) {
            setParams(s, true);
        }

        void setParams(String s, boolean ignorecase) {

            isIgnoreCase = ignorecase;

            normalize(s, true);
        }

        boolean compare(Object o) {

            if (o == null) {
                //return iLen == 0;
                return false; // by Bruno
            }

            String s = o.toString();

            if (isIgnoreCase) {
                s = s.toUpperCase();
            }

            return compareAt(s, 0, 0, s.length());
        }

        private boolean compareAt(String s, int i, int j, int jLen) {

            for (; i < iLen; i++) {
                switch (wildCardType[i]) {

                    case 0:                  // general character
                        if ((j >= jLen) || (cLike[i] != s.charAt(j++))) {
                            return false;
                        }
                        break;

                    case UNDERSCORE_CHAR:    // underscore: do not test this character
                        if (j++ >= jLen) {
                            return false;
                        }
                        break;

                    case PERCENT_CHAR:       // percent: none or any character(s)
                        if (++i >= iLen) {
                            return true;
                        }

                        while (j < jLen) {
                            if ((cLike[i] == s.charAt(j))
                                    && compareAt(s, i, j, jLen)) {
                                return true;
                            }

                            j++;
                        }

                        return false;
                }
            }

            return j == jLen;

        }

        private void normalize(String pattern, boolean b) {

            boolean aNull = pattern == null;

            if (!aNull && isIgnoreCase) {
                pattern = pattern.toUpperCase();
            }

            iLen = 0;
            int iFirstWildCard = -1;

            int l = pattern == null ? 0
                    : pattern.length();

            cLike = new char[l];
            wildCardType = new int[l];

            boolean bEscaping = false,
                    bPercent = false;

            for (int i = 0; i < l; i++) {
                char c = pattern.charAt(i);

                if (!bEscaping) {
                    if (b && (escapeChar != null && escapeChar == c)) {
                        bEscaping = true;

                        continue;
                    } else if (c == '_') {
                        wildCardType[iLen] = UNDERSCORE_CHAR;

                        if (iFirstWildCard == -1) {
                            iFirstWildCard = iLen;
                        }
                    } else if (c == '%') {
                        if (bPercent) {
                            continue;
                        }

                        bPercent = true;
                        wildCardType[iLen] = PERCENT_CHAR;

                        if (iFirstWildCard == -1) {
                            iFirstWildCard = iLen;
                        }
                    } else {
                        bPercent = false;
                    }
                } else {
                    bPercent = false;
                    bEscaping = false;
                }

                cLike[iLen++] = c;
            }

            for (int i = 0; i < iLen - 1; i++) {
                if ((wildCardType[i] == PERCENT_CHAR)
                        && (wildCardType[i + 1] == UNDERSCORE_CHAR)) {
                    wildCardType[i] = UNDERSCORE_CHAR;
                    wildCardType[i + 1] = PERCENT_CHAR;
                }
            }
        }
    }

}

