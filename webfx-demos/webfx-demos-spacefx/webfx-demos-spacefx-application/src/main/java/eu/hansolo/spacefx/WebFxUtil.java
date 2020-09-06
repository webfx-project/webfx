package eu.hansolo.spacefx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import webfx.platform.shared.services.resource.ResourceService;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.shared.services.shutdown.Shutdown;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class WebFxUtil {

    private final static String RESOURCE_PATH = "/eu/hansolo/spacefx/";

    static String toResourceUrl(String resourceName) {
        return ResourceService.toUrl(RESOURCE_PATH + resourceName, WebFxUtil.class);
    }

    static Media newMedia(String resourceName) {
        return new Media(toResourceUrl(resourceName));
    }

    static AudioClip newAudioClip(String resourceName) {
        AudioClip audioClip = new AudioClip(toResourceUrl(resourceName));
        audioClip.setVolume(0.5);
        return audioClip;
    }

    static Image newImage(String resourceName) {
        return startLoadingImage(new Image(toResourceUrl(resourceName), true));
    }

    static Image newImage(String resourceName, double requestedWidth, double requestedHeight) {
        return startLoadingImage(new Image(toResourceUrl(resourceName), requestedWidth, requestedHeight, true, false, true));
    }

    private static final int MAX_SIMULTANEOUS_LOADING_IMAGES_COUNT = 3;
    private static int loadingImagesCount;
    private static GraphicsContext loadingContext;
    private static final List<Image> toLoadImages = new ArrayList<>();

    static boolean hasImageFinishedLoading(Image image) {
        return image == null || image.getProgress() >= 1;
    }

    private static Image startLoadingImage(Image image) {
        if (!hasImageFinishedLoading(image)) {
            if (loadingContext == null || loadingImagesCount >= MAX_SIMULTANEOUS_LOADING_IMAGES_COUNT) {
                if (!toLoadImages.contains(image))
                    toLoadImages.add(image);
            } else {
                toLoadImages.remove(image);
                loadingImagesCount++;
                //webfx.platform.shared.services.log.Logger.log(loadingImagesCount + " (+1 " + image.getUrl() + ")");
                image.progressProperty().addListener((observableValue, oldProgress, progress) -> {
                    if (progress.doubleValue() == 1) {
                        loadingImagesCount--;
                        //webfx.platform.shared.services.log.Logger.log(loadingImagesCount + " (-1 " + image.getUrl() + ")");
                        doNextLoadingAction();
                    }
                });
                loadingContext.drawImage(image, 50_000, 50_000);  // This will cause the image to load
            }
        }
        return image;
    }

    private static void doNextLoadingAction() {
        if (loadingImagesCount < MAX_SIMULTANEOUS_LOADING_IMAGES_COUNT) {
            for (Image image : new ArrayList<>(toLoadImages))
                startLoadingImage(image);
        }
        if (loadingImagesCount == 0)
            startWaitingPlayers();
    }

    public static void setLoadingContext(GraphicsContext loadingContext) {
        WebFxUtil.loadingContext = loadingContext;
        doNextLoadingAction();
    }

    static void onImageLoaded(Image image, Runnable runnable) {
        if (!onImageLoadedIfLoading(image, runnable))
            runnable.run();
    }

    static boolean onImageLoadedIfLoading(Image image, Runnable runnable) {
        if (hasImageFinishedLoading(image))
            return false;
        image.progressProperty().addListener((observableValue, oldProgress, progress) -> {
            if (progress.doubleValue() == 1)
                runnable.run();
        });
        startLoadingImage(image);
        return true;
    }

    private static final List<MediaPlayer> waitingPlayers = new ArrayList<>();

    static void playMusic(MediaPlayer mediaPlayer) {
        if (true)
            mediaPlayer.play();
        else if (!waitingPlayers.contains(mediaPlayer))
            waitingPlayers.add(mediaPlayer);
    }

    static void stopMusic(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        waitingPlayers.remove(mediaPlayer);
    }

    static void startWaitingPlayers() {
        for (MediaPlayer waitingPlayer : waitingPlayers)
            waitingPlayer.play();
        waitingPlayers.clear();
    }

    static void playSound(AudioClip audioClip) {
        if (true)
            audioClip.play();
    }

    static double getImageWidth(Image image) {
        if (image == null)
            return 0;
        double width = image.getWidth();
        if (width <= 0)
            width = image.getRequestedWidth();
        return width;
    }

    static double getImageHeight(Image image) {
        if (image == null)
            return 0;
        double height = image.getHeight();
        if (height <= 0)
            height = image.getRequestedHeight();
        return height;
    }

    static String loadAndGetSpaceBoyFontName() {
        return "Spaceboy";
/*
        try {
            return Font.loadFont(WebFxUtil.class.getResourceAsStream(RESOURCE_PATH + "spaceboy.ttf"), 10).getName();
        } catch (Exception exception) {
            return null;
        }
*/
    }

    static long nanoTime() {
        return Scheduler.nanoTime();
    }

    static void exit(int status) {
/*
        Platform.exit();
        System.exit(status);
*/
        Shutdown.softwareShutdown(true, status);
    }

}
