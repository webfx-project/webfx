package naga.toolkit.cell;

import naga.commons.util.Objects;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
public class RowStyleUpdater {
    private final RowAdapter row;
    private final GridFiller gridFiller;
    private Object[] styles;

    public RowStyleUpdater(RowAdapter row) {
        this(row, null);
    }

    public RowStyleUpdater(RowAdapter row, GridFiller gridFiller) {
        this.row = row;
        this.gridFiller = gridFiller;
    }

    public void update() {
        update(gridFiller.getRowStyleClasses(row.getRowIndex()));
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
