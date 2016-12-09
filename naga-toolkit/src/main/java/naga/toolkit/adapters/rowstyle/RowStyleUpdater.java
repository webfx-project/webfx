package naga.toolkit.adapters.rowstyle;

import naga.commons.util.Objects;
import naga.commons.util.Strings;
import naga.commons.util.function.Function;
import naga.toolkit.adapters.grid.GridFiller;

/**
 * @author Bruno Salmon
 */
public class RowStyleUpdater {
    private final RowAdapter row;
    private final Function<Integer, Object[]> rowStyleClassesGetter;
    private Object[] styles;

    public RowStyleUpdater(RowAdapter row) {
        this(row,(Function<Integer, Object[]>) null);
    }

    public RowStyleUpdater(RowAdapter row, GridFiller gridFiller) {
        this(row, gridFiller::getRowStyleClasses);
    }

    public RowStyleUpdater(RowAdapter row, Function<Integer, Object[]> rowStyleClassesGetter) {
        this.row = row;
        this.rowStyleClassesGetter = rowStyleClassesGetter;
    }

    public void update() {
        update(rowStyleClassesGetter.apply(row.getRowIndex()));
    }

    public void update(Object[] newStyles) {
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
}
