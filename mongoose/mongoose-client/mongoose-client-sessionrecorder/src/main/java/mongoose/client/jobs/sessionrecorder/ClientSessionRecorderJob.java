package mongoose.client.jobs.sessionrecorder;

import mongoose.client.services.authn.MongooseUserPrincipal;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.framework.client.ui.uirouter.uisession.UiSession;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.framework.client.services.push.PushClientService;
import webfx.platform.client.services.storage.LocalStorage;
import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.bus.Bus;
import webfx.platform.shared.services.bus.BusHook;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.bus.Registration;
import webfx.platform.shared.services.log.Logger;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public final class ClientSessionRecorderJob implements ApplicationJob {

    private static ClientSessionRecorderJob INSTANCE;

    private final Bus bus;
    private Registration pushClientRegistration;

    public ClientSessionRecorderJob() {
        this(BusService.bus());
    }

    private ClientSessionRecorderJob(Bus bus) {
        this.bus = bus;
        INSTANCE = this;
    }

    private final UpdateStore store = UpdateStore.create(DataSourceModelService.getDefaultDataSourceModel());
    private Entity sessionAgent, sessionApplication, sessionProcess, sessionConnection, sessionUser;

    @Override
    public void onStart() {
        Logger.log("User Agent = " + getUserAgent());
        Logger.log("application.name = " + getApplicationName());
        Logger.log("application.version = " + getApplicationVersion());
        Logger.log("application.build.tool = " + getApplicationBuildTool());
        Logger.log("application.build.timestamp = " + getApplicationBuildTimestampString());
        Logger.log("application.build.number = " + getApplicationBuildNumberString());
        bus.setHook(new BusHook() {
            @Override
            public void handleOpened() {
                onConnectionOpened();
            }

            @Override
            public boolean handlePreClose() {
                onConnectionClosed();
                return true;
            }

            @Override
            public void handlePostClose() {
                onConnectionClosed();
            }
        });
        if (bus.isOpen())
            onConnectionOpened();
        UiSession.get().userPrincipalProperty().addListener((observable, oldValue, userPrincipal) -> {
            if (userPrincipal instanceof MongooseUserPrincipal && INSTANCE != null)
                INSTANCE.recordNewSessionUser(((MongooseUserPrincipal) userPrincipal).getUserPersonId());
        });
    }

    @Override
    public void onStop() {
        onShutdown();
    }

    private Entity getSessionAgent() {
        if (sessionAgent == null)
            loadOrInsertSessionAgent();
        return sessionAgent;
    }

    private void loadOrInsertSessionAgent() {
        String agentString = getUserAgent();
        if (agentString.length() > 1024)
            agentString = agentString.substring(0, 1024);
        if (agentString.equals(LocalStorage.getItem("sessionAgent.agentString")))
            sessionAgent = recreateSessionEntityFromLocalStorage("SessionAgent", "sessionAgent.id");
        else {
            sessionAgent = insertSessionEntity("SessionAgent", sessionAgent);
            sessionAgent.setFieldValue("agentString", agentString);
        }
    }

    private Entity getSessionApplication() {
        if (sessionApplication == null)
            loadOrInsertSessionApplication();
        return sessionApplication;
    }

    private void loadOrInsertSessionApplication() {
        String applicationName = getApplicationName();
        String applicationVersion = getApplicationVersion();
        String applicationBuildTool = getApplicationBuildTool();
        String applicationBuildNumberString = getApplicationBuildNumberString();
        String applicationBuildTimestampString = getApplicationBuildTimestampString();
        if (applicationName.equals(LocalStorage.getItem("sessionApplication.name"))
                && applicationVersion.equals(LocalStorage.getItem("sessionApplication.version"))
                && applicationBuildTool.equals(LocalStorage.getItem("sessionApplication.buildTool"))
                && applicationBuildNumberString.equals(LocalStorage.getItem("sessionApplication.buildNumberString"))
                && applicationBuildTimestampString.equals(LocalStorage.getItem("sessionApplication.buildTimestampString")))
            sessionApplication = recreateSessionEntityFromLocalStorage("SessionApplication", "sessionApplication.id");
        else {
            sessionApplication = insertSessionEntity("SessionApplication", sessionApplication);
            sessionApplication.setForeignField("agent", getSessionAgent());
            sessionApplication.setFieldValue("name", applicationName);
            sessionApplication.setFieldValue("version", applicationVersion);
            sessionApplication.setFieldValue("buildTool", applicationBuildTool);
            sessionApplication.setFieldValue("buildNumberString", applicationBuildNumberString);
            sessionApplication.setFieldValue("buildNumber", getApplicationBuildNumber());
            sessionApplication.setFieldValue("buildTimestampString", applicationBuildTimestampString);
            sessionApplication.setFieldValue("buildTimestamp", getApplicationBuildTimestamp());
        }
    }

    private Entity getSessionProcess() {
        if (sessionProcess == null)
            insertSessionProcess();
        return sessionProcess;
    }

    private void insertSessionProcess() {
        sessionProcess = insertSessionEntity("SessionProcess", sessionProcess);
        sessionProcess.setForeignField("application", getSessionApplication());
    }

    private void insertSessionConnection() {
        sessionConnection = insertSessionEntity("SessionConnection", sessionConnection);
        sessionConnection.setForeignField("process", getSessionProcess());
    }

    private void onConnectionOpened() {
        listenServerPushCallsIfReady();
        insertSessionConnection();
        executeUpdate();
    }

    private void onConnectionClosed() {
        stopListeningServerPushCalls();
        if (sessionConnection != null) {
            touchSessionEntityEnd(store.updateEntity(sessionConnection));
            executeUpdate();
        }
    }

    private void onShutdown() {
        stopListeningServerPushCalls();
        if (sessionProcess != null) {
            touchSessionEntityEnd(store.updateEntity(sessionProcess));
            if (sessionConnection != null)
                touchSessionEntityEnd(store.updateEntity(sessionConnection));
            if (sessionUser != null)
                touchSessionEntityEnd(store.updateEntity(sessionUser));
            executeUpdate();
        }
    }

    private void recordNewSessionUser(Object userId) {
        createNewSessionUser(userId);
        executeUpdate();
    }

    private void createNewSessionUser(Object userId) {
        sessionUser = insertSessionEntity("SessionUser", sessionUser);
        sessionUser.setForeignField("process", getSessionProcess());
        sessionUser.setForeignField("user", userId);
    }

    private void executeUpdate() {
        boolean newSessionAgent = Entities.isNew(sessionAgent);
        boolean newSessionApplication =  Entities.isNew(sessionApplication);
        store.submitChanges().setHandler(ar -> {
            if (ar.failed())
                Logger.log("Client Session Recorder error", ar.cause());
            else {
                if (newSessionAgent)
                    storeEntityToLocalStorage(sessionAgent, "sessionAgent", "agentString");
                if (newSessionApplication)
                    storeEntityToLocalStorage(sessionApplication, "sessionApplication", "name", "version", "buildTool", "buildNumberString", "buildTimestampString");
                listenServerPushCallsIfReady();
            }
        });
    }

    private void listenServerPushCallsIfReady() {
        if (pushClientRegistration == null && Entities.isNotNew(sessionProcess))
            pushClientRegistration = PushClientService.listenServerPushCalls(sessionProcess.getPrimaryKey());
    }

    private void stopListeningServerPushCalls() {
        if (pushClientRegistration != null && bus.isOpen())
            pushClientRegistration.unregister();
        pushClientRegistration = null;
        // Resetting the push client id property to null (will be reassigned when connected again). The purpose is to
        // make the reactive expression filters in push mode react when the connection is open again (this property
        // change should make them send the query push info sent to the server again).
        PushClientService.pushClientIdProperty().setValue(null);
    }

    private Entity insertSessionEntity(Object domainClassId, Entity previousEntity) {
        return chainSessionEntities(previousEntity, touchSessionEntityStart(store.insertEntity(domainClassId)));
    }

    private Entity recreateSessionEntityFromLocalStorage(Object domainClassId, String idKey) {
        return store.createEntity(EntityId.create(store.getDomainClass(domainClassId), Integer.parseInt(LocalStorage.getItem(idKey))));
    }

    private void storeEntityToLocalStorage(Entity entity, String entityName, Object... fields) {
        storeEntityFieldToLocalStorage(entityName, "id", entity.getPrimaryKey());
        for (Object field : fields)
            storeEntityFieldToLocalStorage(entityName, field, entity.getFieldValue(field));
    }

    private void storeEntityFieldToLocalStorage(String entityName, Object field, Object value) {
        LocalStorage.setItem(entityName + "." + field, value.toString());
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

    private static String getUserAgent() {
        return WebFxKitLauncher.getUserAgent();
    }

    private static String getApplicationName() {
        return System.getProperty("application.name", "?");
    }

    private static String getApplicationVersion() {
        return System.getProperty("application.version", "?");
    }

    private static String getApplicationBuildTool() {
        return System.getProperty("application.build.tool", "?");
    }

    private static String getApplicationBuildNumberString() {
        return System.getProperty("application.build.number", "0");
    }

    private static Number getApplicationBuildNumber() {
        try {
            return Integer.valueOf(getApplicationBuildNumberString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getApplicationBuildTimestampString() {
        String timestamp = System.getProperty("application.build.timestamp");
        if (timestamp == null)
            timestamp = Instant.now().toString();
        return timestamp;
    }

    private static Instant getApplicationBuildTimestamp() {
        try {
            return Instant.parse(getApplicationBuildTimestampString());
        } catch (Exception e) {
            return null;
        }
    }
}
