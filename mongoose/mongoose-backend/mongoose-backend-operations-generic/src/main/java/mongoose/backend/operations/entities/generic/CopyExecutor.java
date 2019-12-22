package mongoose.backend.operations.entities.generic;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumnFactory;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class CopyExecutor {

    static Future<Void> executeRequest(CopyRequest rq) {
        return execute(rq.getEntities(), rq.getColumns());
    }

    private static <E extends Entity> Future<Void> execute(Collection<E> entities, EntityColumn<E>... columns) {
        StringBuilder clipboardString = new StringBuilder();
        List<EntityColumn<E>> textColumns = new ArrayList<>();
        for (EntityColumn<E> column : columns) {
            if (column.isVisible()) {
                Expression<E> displayExpression = column.getDisplayExpression();
                Expression<E> textExpression = ExportHelper.getTextExpression(displayExpression, false, false);
                if (textExpression != null) {
                    textColumns.add(textExpression == displayExpression ? column : EntityColumnFactory.get().create(textExpression));
                    clipboardString.append(column.getName()).append('\t');
                }
            }
        }
        clipboardString.append('\n');
        for (Entity entity : entities) {
            for (EntityColumn<E> textColumn : textColumns) {
                Object value = entity.evaluate(textColumn.getDisplayExpression());
                ValueFormatter displayFormatter = textColumn.getDisplayFormatter();
                if (displayFormatter != null)
                    value = displayFormatter.formatValue(value);
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
