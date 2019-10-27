package webfx.extras.cell.rowstyle;

import javafx.scene.paint.Paint;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.Strings;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class RowStyleUpdater {
    private final RowAdapter row;
    private final Function<Integer, Object[]> rowStyleClassesGetter;
    private final Function<Integer, Paint> rowFillGetter;
    private Object[] styles;

    public RowStyleUpdater(RowAdapter row, Function<Integer, Object[]> rowStyleClassesGetter, Function<Integer, Paint> rowFillGetter) {
        this.row = row;
        this.rowStyleClassesGetter = rowStyleClassesGetter;
        this.rowFillGetter = rowFillGetter;
    }

    public void update() {
        if (rowStyleClassesGetter != null)
            updateRowStyle(rowStyleClassesGetter.apply(row.getRowIndex()));
        if (rowFillGetter != null)
            updateRowFill(rowFillGetter.apply(row.getRowIndex()));
    }

    private void updateRowStyle(Object[] newStyles) {
        if (newStyles != null)
            for (int i = 0; i < newStyles.length; i++) {
                String newStyleClass = Strings.toString(newStyles[i]);
                String oldStyleClass = styles == null ? null : Strings.toString(styles[i]);
                if (!Objects.areEquals(newStyleClass, oldStyleClass)) {
                    if (oldStyleClass != null)
                        row.removeStyleClass(oldStyleClass);
                    if (newStyleClass != null)
                        row.addStyleClass(newStyleClass);
                }
            }
        styles = newStyles;
    }

    private void updateRowFill(Paint fill) {
        row.applyBackground(fill);
    }
}
