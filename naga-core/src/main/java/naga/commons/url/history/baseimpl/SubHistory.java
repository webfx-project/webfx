package naga.commons.url.history.baseimpl;

import naga.commons.url.history.spi.History;
import naga.commons.url.history.spi.HistoryLocation;

/**
 * @author Bruno Salmon
 */
public class SubHistory extends HistoryBase {

    private final HistoryBase parentHistory;

    public SubHistory(History parentHistory, String subMountPoint) {
        this.parentHistory = (HistoryBase) parentHistory;
        setMountPoint(subMountPoint);
    }

    @Override
    protected String mountToFullPath(String mountPath) {
        // Calling the super method will actually give the parentMountPath (= subMountPoint + mountPath)
        String parentMountPath = super.mountToFullPath(mountPath);
        // Then calling the parent method will give the final (parent) full path
        String fullPath = parentHistory.mountToFullPath(parentMountPath);
        //Platform.log("SubHistory parentFullPath('" + mountPath + "') = " + fullPath);
        return fullPath;
    }

    @Override
    protected String fullToMountPath(String fullPath) {
        // Calling the parent method will give the parentMountPath (= subMountPoint + mountPath)
        String parentMountPath = parentHistory.fullToMountPath(fullPath);
        // Then calling the super method will actually give the final (sub) mount path
        String mountPath = super.fullToMountPath(parentMountPath);
        //Platform.log("SubHistory fullToMountPath('" + fullPath + "') = " + mountPath);
        return mountPath;
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl historyLocation) {
        parentHistory.doAcceptedPush(historyLocation);
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl historyLocation) {
        parentHistory.doAcceptedReplace(historyLocation);
    }

    @Override
    public HistoryLocation getCurrentLocation() {
        return parentHistory.getCurrentLocation();
    }

    @Override
    public void transitionTo(HistoryLocation location) {
        parentHistory.transitionTo(location);
    }

    @Override
    public void go(int offset) {
        parentHistory.go(offset);
    }

    @Override
    protected void fireLocationChanged(HistoryLocation location) {
        parentHistory.fireLocationChanged(location);
    }
}
