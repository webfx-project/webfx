package webfx.fxkit.extra.displaydata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class DisplaySelection {

    private final Unit[] units;
    private final int hitRow;

    private DisplaySelection(Unit unit) {
        this(new Unit[]{unit});
    }

    private DisplaySelection(Unit[] units) {
        this(units, null);
    }

    public DisplaySelection(Unit[] units, Integer hitRow) {
        this.units = units;
        this.hitRow = hitRow != null ? hitRow : units.length == 0 ? -1 : units[0].row;
    }

    public Unit[] getUnits() {
        return units;
    }

    public int getUnitsCount() {
        return units.length;
    }

    public boolean isEmpty() {
        return units.length == 0;
    }

    public boolean isMultiple() {
        return units.length > 1;
    }

    public int getSelectedRow() {
        for (Unit unit: units)
            if (unit.getRow() != null)
                return unit.getRow();
        return -1;
    }

    public List<Integer> getSelectedRows() {
        List<Integer> selectedRows = new ArrayList<>();
        for (Unit unit: units)
            if (unit.getRow() != null)
                selectedRows.add(unit.getRow());
        return selectedRows;
    }

    public void forEachRow(Consumer<Integer> rowConsumer) {
        for (Unit unit : units)
            if (unit.getRow() != null)
                rowConsumer.accept(unit.getRow());
    }

    public static DisplaySelection createSingleRowSelection(int row) {
        return new DisplaySelection(Unit.createSelectedRow(row));
    }

    public static DisplaySelection createSingleColumnSelection(int column) {
        return new DisplaySelection(Unit.createSelectedColumn(column));
    }

    public static DisplaySelection createSingleCellSelection(int row, int column) {
        return new DisplaySelection(Unit.createSelectedCell(row, column));
    }

    public static DisplaySelection createRowsSelection(int[] rows) {
        DisplaySelection.Builder selectionBuilder = DisplaySelection.createBuilder(rows.length);
        for (Integer row : rows)
            selectionBuilder.addSelectedRow(row);
        return selectionBuilder.build();
    }

    public static DisplaySelection createRowsSelection(List<Integer> rows) {
        return createRowsSelection(rows, null);
    }

    public static DisplaySelection createRowsSelection(List<Integer> rows, Integer hitRow) {
        DisplaySelection.Builder selectionBuilder = DisplaySelection.createBuilder(rows.size()).setHitRow(hitRow);
        for (Integer row : rows)
            selectionBuilder.addSelectedRow(row);
        return selectionBuilder.build();
    }

    public static DisplaySelection updateRowsSelection(DisplaySelection displaySelection, SelectionMode selectionMode, int hitRow, boolean mainButton, boolean ctrlKey, boolean shiftKey) {
        if (selectionMode == SelectionMode.DISABLED)
            return null;
        if (displaySelection == null || displaySelection.isEmpty() || mainButton && !ctrlKey && !shiftKey)
            return createSingleRowSelection(hitRow);
        List<Integer> selectedRows = displaySelection.getSelectedRows();
        if (!mainButton && selectedRows.contains(hitRow))
            return displaySelection;
        if (!ctrlKey && !shiftKey)
            return createSingleRowSelection(hitRow);
        if (ctrlKey) {
            if (selectedRows.contains(hitRow)) {
                selectedRows.remove((Object) hitRow); // Calling remove(Object) and not remove(index)
                if (!selectedRows.isEmpty())
                    hitRow = selectedRows.get(0);
            } else {
                selectedRows.add(hitRow);
                selectedRows.sort(Integer::compareTo);
                hitRow = displaySelection.hitRow;
            }
        } else { // shift key
            selectedRows.clear();
            int rangeStart = Math.min(hitRow, displaySelection.hitRow);
            int rangeEnd = Math.max(hitRow, displaySelection.hitRow);
            for (int i = rangeStart; i <= rangeEnd; i++)
                selectedRows.add(i);
            hitRow = displaySelection.hitRow;
        }
        return createRowsSelection(selectedRows, hitRow);
    }

    public static Builder createBuilder(int expectedUnitsCount) {
        return new Builder(expectedUnitsCount);
    }

    public static Builder createBuilder() {
        return new Builder(10);
    }

    public static final class Unit {
        private final Integer row;
        private final Integer column;

        private Unit(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }

        public Integer getRow() {
            return row;
        }

        public Integer getColumn() {
            return column;
        }

        public static Unit createSelectedRow(int row) {
            return new Unit(row, null);
        }

        public static Unit createSelectedColumn(int column) {
            return new Unit(null, column);
        }

        public static Unit createSelectedCell(int row, int column) {
            return new Unit(row, column);
        }
    }

    public static final class Builder {

        final Collection<Unit> units;
        Integer hitRow;

        private Builder(int expectedUnitsCount) {
            units = new ArrayList<>(expectedUnitsCount);
        }

        public Builder setHitRow(Integer hitRow) {
            this.hitRow = hitRow;
            return this;
        }

        public Builder addSelectedRow(int row) {
            units.add(Unit.createSelectedRow(row));
            return this;
        }

        public Builder addSelectedColumn(int column) {
            units.add(Unit.createSelectedColumn(column));
            return this;
        }

        public Builder addSelectedCell(int row, int column) {
            units.add(Unit.createSelectedCell(row, column));
            return this;
        }

        public DisplaySelection build() {
            return new DisplaySelection(units.toArray(new Unit[units.size()]), hitRow);
        }
    }
}
