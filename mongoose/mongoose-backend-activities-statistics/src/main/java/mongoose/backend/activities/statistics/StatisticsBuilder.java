package mongoose.backend.activities.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.visual.VisualStyle;
import webfx.extras.type.PrimType;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsBuilder {

    private ReactiveExpressionFilter<DocumentLine> leftDocumentLineFilter;
    private ReactiveExpressionFilter<Attendance> rightAttendanceFilter;
    private final ObjectProperty<VisualResult> finalVisualResultProperty;

    private final ObjectProperty<VisualResult> leftVisualResultProperty = new SimpleObjectProperty<VisualResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalVisualResultIfReady();
        }
    };
    private final ObjectProperty<VisualResult> rightVisualResultProperty = new SimpleObjectProperty<VisualResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            buildFinalVisualResultIfReady();
        }
    };

    private VisualResult lastLeftResult, lastRightResult;

    public StatisticsBuilder(ReactiveExpressionFilter<DocumentLine> leftDocumentLineFilter, ReactiveExpressionFilter<Attendance> rightAttendanceFilter, ObjectProperty<VisualResult> finalVisualResultProperty) {
        this.leftDocumentLineFilter = leftDocumentLineFilter;
        this.rightAttendanceFilter = rightAttendanceFilter;
        this.finalVisualResultProperty = finalVisualResultProperty;
    }

    public void start() {
        leftDocumentLineFilter.visualizeResultInto(leftVisualResultProperty).start();
        rightAttendanceFilter.visualizeResultInto(rightVisualResultProperty).start();
    }

    private void buildFinalVisualResultIfReady() {
        VisualResult leftResult  = leftVisualResultProperty.get();
        VisualResult rightResult = rightVisualResultProperty.get();
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
        VisualColumn[] columns = new VisualColumn[leftColCount + rightColCount];
        System.arraycopy(leftResult.getColumns(), 0, columns, 0, leftColCount);
        for (int col = 0; col < rightColCount; col++)
            columns[leftColCount + col] = VisualColumn.create(Dates.format(dates.get(col), "dd/MM"), PrimType.INTEGER, VisualStyle.RIGHT_STYLE); //, new DisplayStyleImpl(32d, "right"));
        VisualResultBuilder rsb = VisualResultBuilder.create(rowCount, columns);
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
        finalVisualResultProperty.set(rsb.build());
    }

}
