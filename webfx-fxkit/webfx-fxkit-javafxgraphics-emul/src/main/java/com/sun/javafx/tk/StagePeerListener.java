package com.sun.javafx.tk;

import javafx.stage.Stage;
import com.sun.javafx.stage.WindowPeerListener;

public class StagePeerListener extends WindowPeerListener {
    private final Stage stage;
    private final StageAccessor stageAccessor;

    public interface StageAccessor {
        void setIconified(Stage stage, boolean iconified);

        void setMaximized(Stage stage, boolean maximized);

        void setResizable(Stage stage, boolean resizable);

        void setFullScreen(Stage stage, boolean fs);

        void setAlwaysOnTop(Stage stage, boolean aot);
    }

    public StagePeerListener(Stage stage, StageAccessor stageAccessor) {
        super(stage);
        this.stage = stage;
        this.stageAccessor = stageAccessor;
    }


    @Override
    public void changedIconified(boolean iconified) {
        stageAccessor.setIconified(stage, iconified);
    }

    @Override
    public void changedMaximized(boolean maximized) {
        stageAccessor.setMaximized(stage, maximized);
    }

    @Override
    public void changedResizable(boolean resizable) {
        stageAccessor.setResizable(stage, resizable);
    }

    @Override
    public void changedFullscreen(boolean fs) {
        stageAccessor.setFullScreen(stage, fs);
    }

    @Override
    public void changedAlwaysOnTop(boolean aot) {
        stageAccessor.setAlwaysOnTop(stage, aot);
    }
}
