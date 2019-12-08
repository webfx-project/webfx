package webfx.framework.shared.orm.dql.sqlcompiler.terms;

import webfx.framework.shared.orm.expression.terms.Constant;
import webfx.platform.shared.util.Dates;
import webfx.platform.shared.util.Strings;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        if (constant instanceof LocalDateTime)
            return toSqlDate((LocalDateTime) constant);
        return String.valueOf(constant);
    }

    public static String toSqlString(String s) {
        return toSqlQuotedString(s);
    }

    public static String toSqlDate(LocalDate date) {
        return Dates.format(date, "'yyyy-MM-dd'");
    }

    public static String toSqlDate(LocalDateTime date) {
        return Dates.format(date, "'yyyy-MM-dd hh:mm:ss'");
    }

    private static String toSqlQuotedString(String s) {
        return "'" + Strings.replaceAll(s, "'", "''") + "'";
    }
}
