package mongoose.activities.tester.drive.model;

import naga.core.spi.platform.Platform;
import naga.core.type.PrimType;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;

import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet (int rowCount, List<ConnectionsChartData> connectionList){
        Platform.log("createDisplayResultSet(Connections)");
        DisplayColumn[] columns = new DisplayColumn[]{new DisplayColumn("Requested", PrimType.INTEGER),
                new DisplayColumn("Started", PrimType.INTEGER),
                new DisplayColumn("Connected", PrimType.INTEGER)};
        int columnCount = columns.length;
        Object[] values = new Object[rowCount * columnCount];
        int index = 0;
        for (int i=0 ; i<rowCount ; i++) {
            Object value = connectionList.get(i).getRequested();
            values[index++] = value;
            value = connectionList.get(i).getStarted();
            values[index++] = value;
            value = connectionList.get(i).getConnected();
            values[index++] = value;
        }
        DisplayResultSet displayResultSet = new DisplayResultSet(rowCount, values, columns);
        //Platform.log("Ok: " + displayResultSet);
        return displayResultSet;
    }
}
