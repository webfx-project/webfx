package naga.framework.expression.terms.function.java;

import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.function.Function;
import naga.commons.type.PrimType;
import naga.commons.type.Type;

/**
 * @author Bruno Salmon
 */
public class DateIntervalFormat extends Function {

    public DateIntervalFormat() {
        super("dateIntervalFormat", new String[] {"date1", "date2"}, new Type[] {null, null}, PrimType.STRING, true);
    }

    @Override
    public Object evaluate(Object argument, DataReader dataReader) {
        /* doesn't compile with GWT
        try {
            Object[] arguments = (Object[]) argument;
            Date date1 = (Date) arguments[0];
            Date date2 = (Date) arguments[1];

            Calendar calendar = Calendar.getInstance();
            int nowYear = calendar.get(Calendar.YEAR);
            calendar.setTime(date1);
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            int month1 = calendar.get(Calendar.MONTH);
            String month1Name = null; // PB TeaVM calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            //int year1 = calendar.get(Calendar.YEAR);
            calendar.setTime(date2);
            int day2 = calendar.get(Calendar.DAY_OF_MONTH);
            int month2 = calendar.get(Calendar.MONTH);
            int year2 = calendar.get(Calendar.YEAR);
            StringBuffer sb = new StringBuffer();
            if (month1 == month2)
                sb.append(day1).append('-').append(day2).append(' ').append(month1Name);
            else
                sb.append(day1).append(' ').append(month1Name).append(" - ").append(day2).append(' '); //PB TEAVM .append(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
            if (year2 != nowYear)
                sb.append(' ').append(year2);
            return sb.toString();
        } catch (Exception e) {
            return argument;
        }
        */
        return argument;
    }
}
