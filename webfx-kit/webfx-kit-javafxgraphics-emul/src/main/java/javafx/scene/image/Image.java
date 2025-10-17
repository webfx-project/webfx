package javafx.scene.image;

import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasHeightProperty;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasWidthProperty;
import javafx.beans.property.*;

import java.util.function.Supplier;

/**
 * The {@code Image} class represents graphical images and is used for loading
 * images from a specified URL.
 *
 * <p>
 * Supported image formats are:
 * <ul>
 * <li><a href="http://msdn.microsoft.com/en-us/library/dd183376(v=vs.85).aspx">BMP</a></li>
 * <li><a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF</a></li>
 * <li><a href="http://www.ijg.org">JPEG</a></li>
 * <li><a href="http://www.libpng.org/pub/png/spec/">PNG</a></li>
 * </ul>
 * </p>
 *
 * <p>
 * Images can be resized as they are loaded (for example to reduce the amount of
 * memory consumed by the image). The application can specify the quality of
 * filtering used when scaling, and whether or not to preserve the original
 * image's aspect ratio.
 * </p>
 *
 * <p>
 * All URLs supported by {@link URL} can be passed to the constructor.
 * If the passed string is not a valid URL, but a path instead, the Image is
 * searched on the classpath in that case.
 * </p>
 *
 * <p>Use {@link ImageView} for displaying images loaded with this
 * class. The same {@code Image} instance can be displayed by multiple
 * {@code ImageView}s.</p>
 *
 *<p>Example code for loading images.</p>

 <PRE>
 import javafx.scene.image.Image;

 // load an image in background, displaying a placeholder while it's loading
 // (assuming there's an ImageView node somewhere displaying this image)
 // The image is located in default package of the classpath
 Image image1 = new Image("/flower.png", true);

 // load an image and resize it to 100x150 without preserving its original
 // aspect ratio
 // The image is located in my.res package of the classpath
 Image image2 = new Image("my/res/flower.png", 100, 150, false, false);

 // load an image and resize it to width of 100 while preserving its
 // original aspect ratio, using faster filtering method
 // The image is downloaded from the supplied URL through http protocol
 Image image3 = new Image("http://sample.com/res/flower.png", 100, 0, false, false);

 // load an image and resize it only in one dimension, to the height of 100 and
 // the original width, without preserving original aspect ratio
 // The image is located in the current working directory
 Image image4 = new Image("file:flower.png", 0, 100, false, false);

 </PRE>
 * @since JavaFX 2.0
 */
public class Image implements
        HasWidthProperty,
        HasHeightProperty {

    /**
     * The string representing the URL to use in fetching the pixel data.
     *
     * @defaultValue empty string
     */
    private final String url;

    public String getUrl() {
        return url;
    }

    // PENDING_DOC_REVIEW
    /**
     * The width of the bounding box within which the source image is
     * resized as necessary to fit. If set to a value {@code <= 0}, then the
     * intrinsic width of the image will be used.
     * <p/>
     * See {@link #preserveRatio} for information on interaction between image's
     * {@code requestedWidth}, {@code requestedHeight} and {@code preserveRatio}
     * attributes.
     *
     * @defaultValue 0
     */
    private final double requestedWidth;

    /**
     * Gets the width of the bounding box within which the source image is
     * resized as necessary to fit. If set to a value {@code <= 0}, then the
     * intrinsic width of the image will be used.
     * <p/>
     * See {@link #preserveRatio} for information on interaction between image's
     * {@code requestedWidth}, {@code requestedHeight} and {@code preserveRatio}
     * attributes.
     *
     * @return The requested width
     */
    public final double getRequestedWidth() {
        return requestedWidth;
    }
    // PENDING_DOC_REVIEW
    /**
     * The height of the bounding box within which the source image is
     * resized as necessary to fit. If set to a value {@code <= 0}, then the
     * intrinsic height of the image will be used.
     * <p/>
     * See {@link #preserveRatio} for information on interaction between image's
     * {@code requestedWidth}, {@code requestedHeight} and {@code preserveRatio}
     * attributes.
     *
     * @defaultValue 0
     */
    private final double requestedHeight;

    /**
     * Gets the height of the bounding box within which the source image is
     * resized as necessary to fit. If set to a value {@code <= 0}, then the
     * intrinsic height of the image will be used.
     * <p/>
     * See {@link #preserveRatio} for information on interaction between image's
     * {@code requestedWidth}, {@code requestedHeight} and {@code preserveRatio}
     * attributes.
     *
     * @return The requested height
     */
    public final double getRequestedHeight() {
        return requestedHeight;
    }
    // PENDING_DOC_REVIEW
    /**
     * Indicates whether to preserve the aspect ratio of the original image
     * when scaling to fit the image within the bounding box provided by
     * {@code width} and {@code height}.
     * <p/>
     * If set to {@code true}, it affects the dimensions of this {@code Image}
     * in the following way:
     * <ul>
     *  <li> If only {@code width} is set, height is scaled to preserve ratio
     *  <li> If only {@code height} is set, width is scaled to preserve ratio
     *  <li> If both are set, they both may be scaled to get the best fit in a
     *  width by height rectangle while preserving the original aspect ratio
     * </ul>
     * The reported {@code width} and {@code height} may be different from the
     * initially set values if they needed to be adjusted to preserve aspect
     * ratio.
     *
     * If unset or set to {@code false}, it affects the dimensions of this
     * {@code ImageView} in the following way:
     * <ul>
     *  <li> If only {@code width} is set, the image's width is scaled to
     *  match and height is unchanged;
     *  <li> If only {@code height} is set, the image's height is scaled to
     *  match and height is unchanged;
     *  <li> If both are set, the image is scaled to match both.
     * </ul>
     * </p>
     *
     * @defaultValue false
     */
    private final boolean preserveRatio;

    /**
     * Indicates whether to preserve the aspect ratio of the original image
     * when scaling to fit the image within the bounding box provided by
     * {@code width} and {@code height}.
     * <p/>
     * If set to {@code true}, it affects the dimensions of this {@code Image}
     * in the following way:
     * <ul>
     *  <li> If only {@code width} is set, height is scaled to preserve ratio
     *  <li> If only {@code height} is set, width is scaled to preserve ratio
     *  <li> If both are set, they both may be scaled to get the best fit in a
     *  width by height rectangle while preserving the original aspect ratio
     * </ul>
     * The reported {@code width} and {@code height} may be different from the
     * initially set values if they needed to be adjusted to preserve aspect
     * ratio.
     *
     * If unset or set to {@code false}, it affects the dimensions of this
     * {@code ImageView} in the following way:
     * <ul>
     *  <li> If only {@code width} is set, the image's width is scaled to
     *  match and height is unchanged;
     *  <li> If only {@code height} is set, the image's height is scaled to
     *  match and height is unchanged;
     *  <li> If both are set, the image is scaled to match both.
     * </ul>
     * </p>
     *
     * @return true if the aspect ratio of the original image is to be
     *               preserved when scaling to fit the image within the bounding
     *               box provided by {@code width} and {@code height}.
     */
    public final boolean isPreserveRatio() {
        return preserveRatio;
    }

    /**
     * Indicates whether to use a better quality filtering algorithm or a faster
     * one when scaling this image to fit within the
     * bounding box provided by {@code width} and {@code height}.
     *
     * <p>
     * If not initialized or set to {@code true} a better quality filtering
     * will be used, otherwise a faster but lesser quality filtering will be
     * used.
     * </p>
     *
     * @defaultValue true
     */
    private final boolean smooth;

    /**
     * Indicates whether to use a better quality filtering algorithm or a faster
     * one when scaling this image to fit within the
     * bounding box provided by {@code width} and {@code height}.
     *
     * <p>
     * If not initialized or set to {@code true} a better quality filtering
     * will be used, otherwise a faster but lesser quality filtering will be
     * used.
     * </p>
     *
     * @return true if a better quality (but slower) filtering algorithm
     *              is used for scaling to fit within the
     *              bounding box provided by {@code width} and {@code height}.
     */
    public final boolean isSmooth() {
        return smooth;
    }

    /**
     * Indicates whether the image is being loaded in the background.
     *
     * @defaultValue false
     */
    private final boolean backgroundLoading;

    /**
     * Indicates whether the image is being loaded in the background.
     * @return true if the image is loaded in the background
     */
    public final boolean isBackgroundLoading() {
        return backgroundLoading;
    }

    public Image(String url) {
        this(url, false);
    }

    public Image(String url, boolean backgroundLoading) {
        this(url, 0, 0, false, false, backgroundLoading);
    }

    public Image(String url, double requestedWidth, double requestedHeight,
                 boolean preserveRatio, boolean smooth) {
        this(url, requestedWidth, requestedHeight, preserveRatio, smooth, true);
    }

        /**
         * Construct a new {@code Image} with the specified parameters.
         *
         * @param url the string representing the URL to use in fetching the pixel
         *      data
         * @param requestedWidth the image's bounding box width
         * @param requestedHeight the image's bounding box height
         * @param preserveRatio indicates whether to preserve the aspect ratio of
         *      the original image when scaling to fit the image within the
         *      specified bounding box
         * @param smooth indicates whether to use a better quality filtering
         *      algorithm or a faster one when scaling this image to fit within
         *      the specified bounding box
         * @throws NullPointerException if URL is null
         * @throws IllegalArgumentException if URL is invalid or unsupported
         */
    public Image(String url, double requestedWidth, double requestedHeight,
                 boolean preserveRatio, boolean smooth, boolean backgroundLoading) {
        this.url = url;
        this.requestedWidth = requestedWidth;
        this.requestedHeight = requestedHeight;
        this.preserveRatio = preserveRatio;
        this.smooth = smooth;
        this.backgroundLoading = backgroundLoading;
        if (backgroundLoading)
            new ImageView(this).getOrCreateAndBindNodePeer(); // The ImageView peer should do the background loading (see HtmlImageViewPeer)
    }

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(-1d);
    @Override
    public DoubleProperty widthProperty() {
        return widthProperty;
    }

    private final DoubleProperty heightProperty = new SimpleDoubleProperty(-1d);
    @Override
    public DoubleProperty heightProperty() {
        return heightProperty;
    }

    /**
     * The approximate percentage of image's loading that
     * has been completed. A positive value between 0 and 1 where 0 is 0% and 1
     * is 100%.
     *
     * @defaultValue 0
     */
    private DoubleProperty progress;


    /**
     * This is package private *only* for the sake of testing. We need a way to feed fake progress
     * values. It would be better if Image were refactored to be testable (for example, by allowing
     * the test code to provide its own implementation of background loading), but this is a simpler
     * and safer change for now.
     *
     * @param value should be 0-1.
     */
    public final void setProgress(double value) {
        progressPropertyImpl().set(value);
    }

    public final double getProgress() {
        return progress == null ? 0.0 : progress.get();
    }

    public final ReadOnlyDoubleProperty progressProperty() {
        return progressPropertyImpl();
    }

    private DoubleProperty progressPropertyImpl() {
        if (progress == null) {
            progress = new SimpleDoubleProperty(this, "progress");
        }
        return progress;
    }

    /**
     * Indicates whether an error was detected while loading an image.
     *
     * @defaultValue false
     */
    private BooleanProperty error;


    public void setError(boolean value) {
        errorPropertyImpl().set(value);
    }

    public final boolean isError() {
        return error != null && error.get();
    }

    public final BooleanProperty errorProperty() {
        return errorPropertyImpl();
    }

    private BooleanProperty errorPropertyImpl() {
        if (error == null) {
            error = new SimpleBooleanProperty(this, "error");
        }
        return error;
    }

    /**
     * The exception which caused image loading to fail. Contains a non-null
     * value only if the {@code error} property is set to {@code true}.
     *
     * @since JavaFX 8.0
     */
    private ObjectProperty<Exception> exception;

    public void setException(Exception value) {
        exceptionPropertyImpl().set(value);
    }

    public final Exception getException() {
        return exception == null ? null : exception.get();
    }

    public final ObjectProperty<Exception> exceptionProperty() {
        return exceptionPropertyImpl();
    }

    private ObjectProperty<Exception> exceptionPropertyImpl() {
        if (exception == null) {
            exception = new SimpleObjectProperty<>(this, "exception");
        }
        return exception;
    }

    private Object peerImage;

    public Object getPeerImage() {
        return peerImage;
    }

    public void setPeerImage(Object peerImage) {
        this.peerImage = peerImage;
    }

    private Object peerImageData; // Can be used by the peer to store the underlying image, such as ImageData

    public Object getPeerImageData() {
        return peerImageData;
    }

    private String peerUrl;

    public String getPeerUrl() {
        return peerUrl;
    }

    public void setPeerUrl(String peerUrl) {
        this.peerUrl = peerUrl;
    }

    public void setPeerImageData(Object peerImageData) {
        this.peerImageData = peerImageData;
    }

    private Object peerCanvas; // Can be used by the peer to render the image

    public Object getPeerCanvas() {
        return peerCanvas;
    }

    public void setPeerCanvas(Object peerCanvas) {
        this.peerCanvas = peerCanvas;
    }

    private boolean peerCanvasDirty;

    public boolean isPeerCanvasDirty() {
        return peerCanvasDirty;
    }

    public void setPeerCanvasDirty(boolean peerCanvasDirty) {
        this.peerCanvasDirty = peerCanvasDirty;
    }

    private PixelReader pixelReader;
    private Supplier<PixelReader> pixelReaderFactory;

    public PixelReader getPixelReader() {
        if (pixelReader == null && pixelReaderFactory != null)
            pixelReader = pixelReaderFactory.get();
        return pixelReader;
    }

    public void setPixelReader(PixelReader pixelReader) {
        this.pixelReader = pixelReader;
    }

    public void setPixelReaderFactory(Supplier<PixelReader> pixelReaderFactory) {
        this.pixelReaderFactory = pixelReaderFactory;
    }
}
