package emul.java.util.regex;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/** Emulation of the {@link Matcher} class, uses {@link RegExp} as internal implementation.
 * @author hneuer */

public class Matcher {

    private final RegExp regExp;
    private final String input;
    private final MatchResult matchResult;

    Matcher (Pattern pattern, CharSequence input) {
        this.regExp = pattern.regExp;
        this.input = String.valueOf(input);
        matchResult = regExp.exec(this.input);
    }

    public boolean matches() {
        return regExp.test(input);
    }

    public int groupCount() {
        return matchResult.getGroupCount();
    }

    public String group(int group) {
        return matchResult.getGroup(group);
    }
}