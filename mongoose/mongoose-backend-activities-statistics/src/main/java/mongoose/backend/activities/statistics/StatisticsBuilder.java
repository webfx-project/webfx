package mongoose.backend.activities.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.DocumentLine;
import webfx.extras.type.PrimType;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.visual.VisualStyle;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.expression.Expression;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsBuilder {

    private final ReactiveVisualMapper<DocumentLine> leftDocumentLineVisualMapper;
    private final ReactiveVisualMapper<Attendance> rightAttendanceVisualMapper;
    private final Property<VisualResult> finalVisualResultProperty;

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
    private boolean settingFinalResult;

    public StatisticsBuilder(ReactiveVisualMapper<DocumentLine> leftDocumentLineVisualMapper, ReactiveVisualMapper<Attendance> rightAttendanceVisualMapper, Property<VisualResult> finalVisualResultProperty) {
        this.leftDocumentLineVisualMapper = leftDocumentLineVisualMapper;
        this.rightAttendanceVisualMapper = rightAttendanceVisualMapper;
        this.finalVisualResultProperty = finalVisualResultProperty;
    }

    public StatisticsBuilder start() {
        leftDocumentLineVisualMapper.visualizeResultInto(leftVisualResultProperty).start();
        rightAttendanceVisualMapper.visualizeResultInto(rightVisualResultProperty).start();
        return this;
    }

    private void buildFinalVisualResultIfReady() {
        VisualResult leftResult  = leftVisualResultProperty.get();
        VisualResult rightResult = rightVisualResultProperty.get();
        int rowCount = leftResult == null ? -1 : leftResult.getRowCount();
        int leftColCount = leftResult == null ? 0 : leftResult.getColumnCount();
        EntityList<Attendance> rightAttendances = rightAttendanceVisualMapper.getCurrentEntities();
        if (settingFinalResult || leftResult == lastLeftResult || rightResult == lastRightResult || rowCount == 0 || leftColCount == 0 || rightAttendances.isEmpty())
            return;
        lastLeftResult = leftResult;
        lastRightResult = rightResult;
        List<LocalDate> dates = new ArrayList<>();
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
        EntityList<DocumentLine> leftDocumentLines = leftDocumentLineVisualMapper.getCurrentEntities();
        EntityColumn<DocumentLine>[] leftColumns = leftDocumentLineVisualMapper.getEntityColumns();
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
        settingFinalResult = true;
        finalVisualResultProperty.setValue(rsb.build());
        settingFinalResult = false;
    }
}
