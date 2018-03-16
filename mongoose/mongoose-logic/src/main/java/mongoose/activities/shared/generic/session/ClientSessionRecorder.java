package mongoose.activities.shared.generic.session;

import javafx.beans.property.Property;
import mongoose.authn.MongooseUserPrincipal;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.UpdateStore;
import naga.fx.spi.Toolkit;
import naga.platform.bus.BusHook;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.shutdown.spi.Shutdown;
import naga.platform.spi.Platform;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class ClientSessionRecorder {

    private final static ClientSessionRecorder INSTANCE = new ClientSessionRecorder();

    public static ClientSessionRecorder get() {
        return INSTANCE;
    }

    static {
        Platform.bus().setHook(new BusHook() {
            @Override
            public void handleOpened() {
                get().recordSessionConnectionStart();
            }

            @Override
            public void handlePostClose() {
                get().recordSessionConnectionEnd();
            }
        });
        Shutdown.addShutdownHook(get()::recordSessionProcessEnd);
        Logger.log("User Agent = " + getUserAgent());
        Logger.log("application.name = " + getApplicationName());
        Logger.log("application.version = " + getApplicationVersion());
        Logger.log("application.build.tool = " + getApplicationBuildTool());
        Logger.log("application.build.timestamp = " + getApplicationBuildTimestampString());
        Logger.log("application.build.number = " + getApplicationBuildNumberString());
    }

    private final UpdateStore store = UpdateStore.create(DomainModelSnapshotLoader.getDataSourceModel());
    private Entity sessionAgent, sessionApplication, sessionProcess, sessionConnection, sessionUser;

    public void setUserPrincipalProperty(Property<Object> userPrincipalProperty) {
        userPrincipalProperty.addListener((observable, oldValue, userPrincipal) -> {
            if (userPrincipal instanceof MongooseUserPrincipal)
                recordNewSessionUser(((MongooseUserPrincipal) userPrincipal).getUserPersonId());
        });
    }

    private Entity getSessionAgent() {
        if (sessionAgent == null)
            createNewSessionAgent();
        return sessionAgent;
    }

    private void createNewSessionAgent() {
        sessionAgent = createSessionEntity("SessionAgent", sessionAgent);
        String agentString = getUserAgent();
        if (agentString.length() > 1024)
            agentString = agentString.substring(0, 1024);
        sessionAgent.setFieldValue("agentString", agentString);
    }

    private Entity getSessionApplication() {
        if (sessionApplication == null)
            createNewSessionApplication();
        return sessionApplication;
    }

    private void createNewSessionApplication() {
        sessionApplication = createSessionEntity("SessionApplication", sessionApplication);
        sessionApplication.setForeignField("agent", getSessionAgent());
        sessionApplication.setFieldValue("name", getApplicationName());
        sessionApplication.setFieldValue("version", getApplicationVersion());
        sessionApplication.setFieldValue("buildTool", getApplicationBuildTool());
        sessionApplication.setFieldValue("buildNumberString", getApplicationBuildNumberString());
        sessionApplication.setFieldValue("buildNumber", getApplicationBuildNumber());
        sessionApplication.setFieldValue("buildTimestampString", getApplicationBuildTimestampString());
        sessionApplication.setFieldValue("buildTimestamp", getApplicationBuildTimestamp());
    }

    private Entity getSessionProcess() {
        if (sessionProcess == null)
            createNewSessionProcess();
        return sessionProcess;
    }

    private void createNewSessionProcess() {
        sessionProcess = createSessionEntity("SessionProcess", sessionProcess);
        sessionProcess.setForeignField("application", getSessionApplication());
    }

    private void recordSessionConnectionStart() {
        createNewSessionConnection();
        executeUpdate();
    }

    private void createNewSessionConnection() {
        sessionConnection = createSessionEntity("SessionConnection", sessionConnection);
        sessionConnection.setForeignField("process", getSessionProcess());
    }

    private void recordSessionConnectionEnd() {
        if (sessionConnection != null) {
            touchSessionEntityEnd(store.updateEntity(sessionConnection));
            executeUpdate();
        }
    }

    private void recordSessionProcessEnd() {
        if (sessionProcess != null) {
            touchSessionEntityEnd(store.updateEntity(sessionProcess));
            if (sessionConnection != null)
                touchSessionEntityEnd(store.updateEntity(sessionConnection));
            if (sessionUser != null)
                touchSessionEntityEnd(store.updateEntity(sessionUser));
            executeUpdate();
        }
    }

    public void recordNewSessionUser(Object userId) {
        createNewSessionUser(userId);
        executeUpdate();
    }

    private void createNewSessionUser(Object userId) {
        sessionUser = createSessionEntity("SessionUser", sessionUser);
        sessionUser.setForeignField("process", getSessionProcess());
        sessionUser.setForeignField("user", userId);
    }

    private void executeUpdate() {
        store.executeUpdate().setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
        });
    }

    private Entity createSessionEntity(Object domainClassId, Entity previousEntity) {
        return chainSessionEntities(previousEntity, touchSessionEntityStart(store.insertEntity(domainClassId)));
    }

    private static Entity touchSessionEntityStart(Entity entity) {
        return touchSessionEntity(entity, true);
    }

    private static Entity touchSessionEntityEnd(Entity entity) {
        return touchSessionEntity(entity, false);
    }

    private static Entity touchSessionEntity(Entity entity, boolean start) {
        entity.setFieldValue(start ? "start" : "end", Instant.now());
        return entity;
    }

    private Entity chainSessionEntities(Entity previousEntity, Entity nextEntity) {
        if (previousEntity != null && nextEntity != null) {
            previousEntity = store.updateEntity(previousEntity);
            previousEntity.setForeignField("next", nextEntity);
            nextEntity.setForeignField("previous", previousEntity);
            touchSessionEntityEnd(previousEntity);
        }
        return nextEntity;
    }

    public static String getUserAgent() {
        return Toolkit.get().getUserAgent();
    }

    public static String getApplicationName() {
        return System.getProperty("application.name");
    }

    public static String getApplicationVersion() {
        return System.getProperty("application.version");
    }

    public static String getApplicationBuildTool() {
        return System.getProperty("application.build.tool");
    }

    public static String getApplicationBuildNumberString() {
        return System.getProperty("application.build.number", "0");
    }

    public static Number getApplicationBuildNumber() {
        try {
            return Integer.valueOf(getApplicationBuildNumberString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getApplicationBuildTimestampString() {
        String timestamp = System.getProperty("application.build.timestamp");
        if (timestamp == null)
            timestamp = Instant.now().toString();
        return timestamp;
    }

    public static Instant getApplicationBuildTimestamp() {
        try {
            return Instant.parse(getApplicationBuildTimestampString());
        } catch (Exception e) {
            return null;
        }
    }
}
