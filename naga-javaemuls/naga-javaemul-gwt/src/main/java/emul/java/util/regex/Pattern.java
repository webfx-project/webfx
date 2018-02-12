package emul.java.util.regex;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.regexp.shared.RegExp;

/** Emulation of the {@link Pattern} class, uses {@link RegExp} as internal implementation.
 * @author hneuer */

public final class Pattern {

/* Implementation using standard JavaScript RegExp
 Commented as not all browsers implement named groups (only Chrome 64 for now)

    final RegExp regExp;

    private Pattern (String regExp) {
        this.regExp = RegExp.compile(regExp);
    }

*/

    public Matcher matcher(CharSequence input) {
        return new Matcher(this, input);
    }

    public static Pattern compile(String regExp) {
        return new Pattern(regExp);
    }

    // Using XRegExp instead

    final JavaScriptObject xRegExp;

    private Pattern (String regExp) {
        xRegExp = newXRegExp(regExp);
    }

    private static native JavaScriptObject newXRegExp(String regExp) /*-{
        return $wnd.XRegExp(regExp);
    }-*/;

}
