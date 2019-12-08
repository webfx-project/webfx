package mongoose.shared.domainmodel.functions;

import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.function.Function;
import webfx.extras.type.PrimType;
import webfx.platform.shared.util.Strings;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class AbcNames extends Function {

    public AbcNames() {
        super("abcNames");
    }

    public AbcNames(String name) {
        super(name, null, null, PrimType.STRING, true);
    }

    @Override
    public Object evaluate(Object argument, DomainReader domainReader) {
        return evaluate((String) argument, false);
    }

    public static String evaluate(String s, boolean like) {
        if (s == null)
            return null;
        String[] tokens = Strings.split(Strings.replaceAll(s.toLowerCase(), "-", " "), " "); // PB TeaVM s.toLowerCase().split("[\\s,-]");
        Arrays.sort(tokens);
        String start = like ? "% " : " ";
        StringBuilder sb = new StringBuilder(start);
        for (String token : tokens) {
            if (sb.length() > start.length())
                sb.append(' ');
            sb.append(token);
            if (like)
                sb.append('%');
        }
        return sb.toString();
    }
}
