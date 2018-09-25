package webfx.framework.expression.sqlcompiler.terms;

import webfx.framework.expression.terms.Constant;
import webfx.platforms.core.util.Dates;
import webfx.platforms.core.util.Strings;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public final class ConstantSqlCompiler extends AbstractTermSqlCompiler<Constant> {

    public ConstantSqlCompiler() {
        super(Constant.class);
    }

    @Override
    public void compileExpressionToSql(Constant e, Options o) {
        Object constantValue = e.getConstantValue();
        StringBuilder sb = o.build.prepareAppend(o).append(toSqlConstant(constantValue));
        // Quick fix for Postgres when ordering by ISC transport field = (select 'ISC' from ...)
        // casting to text to avoid the "failed to find conversion function from unknown to text" error
        if (o.separator != null && constantValue instanceof String)
            sb.append("::text");
    }

    public static String toSqlConstant(Object constant) {
        if (constant instanceof String)
            return toSqlString((String) constant);
        if (constant instanceof LocalDate)
            return toSqlDate((LocalDate) constant);
        return String.valueOf(constant);
    }

    public static String toSqlString(String s) {
        return toSqlQuotedString(s);
    }

    public static String toSqlDate(LocalDate date) {
        return Dates.format(date, "'yyyy-MM-dd'");
    }

    private static String toSqlQuotedString(String s) {
        return "'" + Strings.replaceAll(s, "'", "''") + "'";
    }
}