package mongoose.backend.activities.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.fxkit.extra.displaydata.DisplayColumn;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplayResultBuilder;
import webfx.fxkit.extra.displaydata.DisplayStyle;
import webfx.fxkit.extra.type.PrimType;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsBuilder {

    private ReactiveExpressionFilter<DocumentLine> leftDocumentLineFilter;
    private ReactiveExpressionFilter<Attendance> rightAttendanceFilter;
    private final ObjectProperty<DisplayResult> finalDisplayResultProperty;

    private final ObjectProperty<DisplayResult> leftDisplayResultProperty = new SimpleObjectProperty<DisplayResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalDisplayResultIfReady();
        }
    };
    private final ObjectProperty<DisplayResult> rightDisplayResultProperty = new SimpleObjectProperty<DisplayResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalDisplayResultIfReady();
        }
    };

    private DisplayResult lastLeftResult, lastRightResult;

    public StatisticsBuilder(ReactiveExpressionFilter<DocumentLine> leftDocumentLineFilter, ReactiveExpressionFilter<Attendance> rightAttendanceFilter, ObjectProperty<DisplayResult> finalDisplayResultProperty) {
        this.leftDocumentLineFilter = leftDocumentLineFilter;
        this.rightAttendanceFilter = rightAttendanceFilter;
        this.finalDisplayResultProperty = finalDisplayResultProperty;
    }

    public void start() {
        leftDocumentLineFilter.displayResultInto(leftDisplayResultProperty).start();
        rightAttendanceFilter.displayResultInto(rightDisplayResultProperty).start();
    }

    private void buildFinalDisplayResultIfReady() {
        DisplayResult leftResult  = leftDisplayResultProperty.get();
        DisplayResult rightResult = rightDisplayResultProperty.get();
        if (leftResult == lastLeftResult || rightResult == lastRightResult)
            return;
        lastLeftResult = leftResult;
        lastRightResult = rightResult;
        int rowCount = leftResult.getRowCount();
        int leftColCount = leftResult.getColumnCount();
        List<LocalDate> dates = new ArrayList<>();
        EntityList<Attendance> rightAttendances = rightAttendanceFilter.getCurrentEntityList();
        rightAttendances.forEach(a -> {
            LocalDate date = a.getDate();
            if (dates.isEmpty() || !date.equals(dates.get(dates.size() - 1)))
                dates.add(date);
        });
        int rightColCount = dates.size();
        DisplayColumn[] columns = new DisplayColumn[leftColCount + rightColCount];
        System.arraycopy(leftResult.getColumns(), 0, columns, 0, leftColCount);
        for (int col = 0; col < rightColCount; col++)
            columns[leftColCount + col] = DisplayColumn.create(Dates.format(dates.get(col), "dd/MM"), PrimType.INTEGER, DisplayStyle.RIGHT_STYLE); //, new DisplayStyleImpl(32d, "right"));
        DisplayResultBuilder rsb = DisplayResultBuilder.create(rowCount, columns);
        for (int row = 0; row < rowCount; row++)
            for (int col = 0; col < leftColCount; col++)
                rsb.setValue(row, col, leftResult.getValue(row, col));
        EntityList<DocumentLine> leftDocumentLines = leftDocumentLineFilter.getCurrentEntityList();
        ExpressionColumn[] leftColumns = leftDocumentLineFilter.getExpressionColumns();
        rightAttendances.forEach(a -> {
            LocalDate date = a.getDate();
            DocumentLine rightDocumentLine = a.getDocumentLine();
            for (int row = 0; row < rowCount; row++) {
                DocumentLine leftDocumentLine = leftDocumentLines.get(row);
                boolean match = true;
                for (int col = 0; col < leftColumns.length - 1; col++) {
                    Expression<DocumentLine> expression = leftColumns[col].getExpression();
                    if (!Objects.equals(leftDocumentLine.evaluate(expression), rightDocumentLine.evaluate(expression))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    rsb.setValue(row, leftColCount + dates.indexOf(date), a.getFieldValue("count"));
                    break;
                }
            }
        });
        finalDisplayResultProperty.set(rsb.build());
    }

}
