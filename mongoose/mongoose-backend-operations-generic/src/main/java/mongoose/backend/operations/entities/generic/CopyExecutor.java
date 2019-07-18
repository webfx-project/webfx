package mongoose.backend.operations.entities.generic;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.util.formatter.Formatter;
import webfx.fxkit.extra.displaydata.DisplayColumn;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class CopyExecutor {

    static Future<Void> executeRequest(CopyRequest rq) {
        return execute(rq.getEntities(), rq.getColumns());
    }

    private static Future<Void> execute(Collection<? extends Entity> entities, ExpressionColumn... columns) {
        StringBuilder clipboardString = new StringBuilder();
        List<ExpressionColumn> textColumns = new ArrayList<>();
        for (ExpressionColumn column : columns) {
            DisplayColumn displayColumn = column.getDisplayColumn();
            if (displayColumn.getRole() == null) {
                Expression displayExpression = column.getDisplayExpression();
                Expression textExpression = ExportHelper.getTextExpression(displayExpression, false, false);
                if (textExpression != null) {
                    textColumns.add(textExpression == displayExpression ? column : ExpressionColumn.create(textExpression));
                    clipboardString.append(displayColumn.getName()).append('\t');
                }
            }
        }
        clipboardString.append('\n');
        for (Entity entity : entities) {
            for (ExpressionColumn textColumn : textColumns) {
                Object value = entity.evaluate(textColumn.getDisplayExpression());
                Formatter displayFormatter = textColumn.getDisplayFormatter();
                if (displayFormatter != null)
                    value = displayFormatter.format(value);
                clipboardString.append(ExportHelper.getTextExpressionValue(value)).append('\t');
            }
            clipboardString.append('\n');
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
        return Future.future();
    }
}
