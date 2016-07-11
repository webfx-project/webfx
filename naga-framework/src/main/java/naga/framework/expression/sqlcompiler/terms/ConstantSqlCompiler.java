package naga.framework.expression.sqlcompiler.terms;

import naga.framework.expression.terms.Constant;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
public class ConstantSqlCompiler extends AbstractTermSqlCompiler<Constant> {

    public ConstantSqlCompiler() {
        super(Constant.class);
    }

    @Override
    public void compileExpressionToSql(Constant e, Options o) {
        StringBuilder sb = o.build.prepareAppend(o);
        Object constantValue = e.getConstantValue();
        if (constantValue instanceof String) {
            sb.append(toSqlQuotedString((String) constantValue));
            if (o.separator != null) // Quick fix for Postgres when ordering by ISC transport field = (select 'ISC' from ...)
                sb.append("::text"); // casting to text to avoid the "failed to find conversion function from unknown to text" error
        } else
            sb.append(constantValue);
    }

    private static String toSqlQuotedString(String s) {
        return "'" + Strings.replaceAll(s, "'", "''") + "'";
    }

}
