package naga.fxdata.displaydata;

import naga.commons.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class DisplaySelection {

    private final Unit[] units;

    private DisplaySelection(Unit unit) {
        this(new Unit[]{unit});
    }

    private DisplaySelection(Unit[] units) {
        this.units = units;
    }

    public Unit[] getUnits() {
        return units;
    }

    public int getUnitsCount() {
        return units.length;
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
        Builder selectionBuilder = DisplaySelection.createBuilder(rows.length);
        for (Integer row : rows)
            selectionBuilder.addSelectedRow(row);
        return selectionBuilder.build();
    }

    public static DisplaySelection createRowsSelection(List<Integer> rows) {
        Builder selectionBuilder = DisplaySelection.createBuilder(rows.size());
        for (Integer row : rows)
            selectionBuilder.addSelectedRow(row);
        return selectionBuilder.build();
    }

    public static Builder createBuilder(int expectedUnitsCount) {
        return new Builder(expectedUnitsCount);
    }

    public static Builder createBuilder() {
        return new Builder(10);
    }

    public static class Unit {
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

    public static class Builder {

        final ArrayList<Unit> units;

        private Builder(int expectedUnitsCount) {
            units = new ArrayList<>(expectedUnitsCount);
        }

        public DisplaySelection build() {
            return new DisplaySelection(units.toArray(new Unit[units.size()]));
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
    }
}
