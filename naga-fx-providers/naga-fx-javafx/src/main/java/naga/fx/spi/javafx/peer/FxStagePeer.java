package naga.fx.spi.javafx.peer;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.fx.event.Event;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.JavaFxToolkit;
import naga.fx.spi.peer.StagePeer;
import naga.fx.stage.Window;
import naga.fx.stage.WindowEvent;
import naga.fx.sun.tk.TKStageListener;

/**
 * @author Bruno Salmon
 */
public class FxStagePeer implements StagePeer {

    private naga.fx.stage.Stage stage;
    protected Stage fxStage;
    private TKStageListener listener;

    public FxStagePeer(naga.fx.stage.Stage stage, Stage fxStage) {
        this.stage = stage;
        setFxStage(fxStage);
    }

    public void setFxStage(Stage fxStage) {
        this.fxStage = fxStage;
        if (fxStage != null) {
            onSceneRootChanged();
            Properties.runOnPropertiesChange(p -> {
                if (listener != null)
                    listener.changedLocation((float) fxStage.getX(), (float) fxStage.getY());
            }, fxStage.xProperty(), fxStage.yProperty());
            Properties.runOnPropertiesChange(p -> {
                if (listener != null)
                    listener.changedSize((float) fxStage.getWidth(), (float) fxStage.getHeight());
            }, fxStage.widthProperty(), fxStage.heightProperty());
            fxStage.setOnCloseRequest(e -> {
                WindowEvent we = new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST);
                Event.fireEvent(stage, we);
                if (e != null && we.isConsumed())
                    e.consume();
            });
        }
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
    }

    @Override
    public void setTitle(String title) {
        fxStage.setTitle(title);
    }

    @Override
    public Window getWindow() {
        return stage;
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //System.out.println("x = " + x +", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet)
            fxStage.setX(x);
        if (ySet)
            fxStage.setY(y);
        if (w < 0 && cw > 0)
            w = cw + (float) stagePaddingWidth();
        if (h < 0 && ch > 0)
            h = ch + (float) stagePaddingHeight();
        if (w > 0)
            fxStage.setWidth(w);
        if (h > 0)
            fxStage.setHeight(h);
    }

    protected double stagePaddingWidth() {
        Scene scene = fxStage.getScene();
        return (scene == null || Double.isNaN(fxStage.getWidth()) ? 0 : (fxStage.getWidth() - scene.getWidth()/*- scene.getX()*/));
    }

    protected double stagePaddingHeight() {
        Scene scene = fxStage.getScene();
        return (scene == null || Double.isNaN(fxStage.getHeight()) ? 0 : (fxStage.getHeight() - scene.getHeight()/*- scene.getY()*/));
    }

    private boolean showOnNewScene;

    @Override
    public void setVisible(boolean visible) {
        Toolkit.get().scheduler().runInUiThread(() -> {
            if (visible) {
                if (fxStage.getScene() != null)
                    fxStage.show();
                else
                    showOnNewScene = true;
            } else {
                fxStage.hide();
                showOnNewScene = false;
            }
        });
    }

    @Override
    public void onSceneRootChanged() {
        Toolkit.get().scheduler().scheduleDeferred(() -> {
            if (fxStage != null) {
                naga.fx.scene.Scene scene = getStage().getScene();
                if (scene != null) {
                    naga.fx.scene.Parent root = scene.getRoot();
                    if (root != null) {
                        FxNodePeer peer = (FxNodePeer) root.getOrCreateAndBindNodePeer();
                        Parent fxRoot = (Parent) peer.getFxNode();
                        if (fxRoot == null)
                            onSceneRootChanged();
                        else
                            setFxRoot(fxRoot);
                    }
                }
            }
        });
    }

    private void setFxRoot(Parent fxRoot) {
        Toolkit.get().scheduler().scheduleDeferred(() -> setFxRootNow(fxRoot));
    }

    protected void setFxRootNow(Parent fxRoot) {
        Scene fxScene = fxStage.getScene();
        if (fxScene != null)
            setFxSceneRoot(fxScene, fxRoot);
        else { // Creating the scene if not yet done
            naga.fx.scene.Scene scene = getStage().getScene();
            fxStage.setScene(fxScene = createFxScene(fxRoot, scene.getWidth(), scene.getHeight()));
            ((FxScenePeer) scene.impl_getPeer()).setFxScene(fxScene);
            // Calling the scene hook is specified
            Consumer<Scene> sceneHook = JavaFxToolkit.getSceneHook();
            if (sceneHook != null)
                sceneHook.accept(fxScene);
        }
        if (showOnNewScene) {
            fxStage.show();
            showOnNewScene = false;
        }
    }

    protected void setFxSceneRoot(Scene fxScene, Parent fxRoot) {
        fxScene.setRoot(fxRoot);
    }

    protected Scene createFxScene(Parent rootComponent, double width, double height) {
        return new Scene(rootComponent, width, height);
    }
}