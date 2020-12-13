package dev.webfx.platform.client.services.windowhistory.spi.impl;

import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryLocation;

/**
 * @author Bruno Salmon
 */
public final class SubBrowsingHistory extends BrowsingHistoryBase {

    private final BrowsingHistoryBase parentHistory;

    public SubBrowsingHistory(BrowsingHistory parentBrowsingHistory, String subMountPoint) {
        this.parentHistory = (BrowsingHistoryBase) parentBrowsingHistory;
        setMountPoint(subMountPoint);
    }

    @Override
    protected String mountToFullPath(String mountPath) {
        // Calling the super method will actually give the parentMountPath (= subMountPoint + mountPath)
        String parentMountPath = super.mountToFullPath(mountPath);
        // Then calling the parent method will give the final (parent) full path
        String fullPath = parentHistory.mountToFullPath(parentMountPath);
        //Logger.log("SubBrowsingHistory parentFullPath('" + mountPath + "') = " + fullPath);
        return fullPath;
    }

    @Override
    protected String fullToMountPath(String fullPath) {
        // Calling the parent method will give the parentMountPath (= subMountPoint + mountPath)
        String parentMountPath = parentHistory.fullToMountPath(fullPath);
        // Then calling the super method will actually give the final (sub) mount path
        String mountPath = super.fullToMountPath(parentMountPath);
        //Logger.log("SubBrowsingHistory fullToMountPath('" + fullPath + "') = " + mountPath);
        return mountPath;
    }

    @Override
    protected void doAcceptedPush(BrowsingHistoryLocationImpl historyLocation) {
        parentHistory.doAcceptedPush(historyLocation);
    }

    @Override
    protected void doAcceptedReplace(BrowsingHistoryLocationImpl historyLocation) {
        parentHistory.doAcceptedReplace(historyLocation);
    }

    @Override
    public BrowsingHistoryLocation getCurrentLocation() {
        return parentHistory.getCurrentLocation();
    }

    @Override
    public void transitionTo(BrowsingHistoryLocation location) {
        parentHistory.transitionTo(location);
    }

    @Override
    public void go(int offset) {
        parentHistory.go(offset);
    }

    @Override
    protected void fireLocationChanged(BrowsingHistoryLocation location) {
        parentHistory.fireLocationChanged(location);
    }
}
