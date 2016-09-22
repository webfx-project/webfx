package naga.providers.toolkit.swing.util;

import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author Bruno Salmon
 */
class BatikSvgIcon extends UserAgentAdapter implements Icon {

    /**
     * The BufferedImage generated from the SVG document.
     */
    private BufferedImage bufferedImage;

    /**
     * The width of the rendered image.
     */
    private int width;

    /**
     * The height of the rendered image.
     */
    private int height;

    /**
     * Create a new BatikSvgIcon object.
     * @param is The input stream to read the SVG document from.
     */
    BatikSvgIcon(InputStream is) throws TranscoderException {
        this(is, 0, 0);
    }

    /**
     * Create a new BatikSvgIcon object.
     * @param is The input stream to read the SVG document from.
     * @param w The width of the icon.
     * @param h The height of the icon.
     */
    BatikSvgIcon(InputStream is, int w, int h) throws TranscoderException {
        generateBufferedImage(new TranscoderInput(is), w, h);
    }

    /**
     * Generate the BufferedImage.
     */
    private void generateBufferedImage(TranscoderInput in, int w, int h)
            throws TranscoderException {
        BufferedImageTranscoder t = new BufferedImageTranscoder();
        if (w != 0 && h != 0) {
            t.setDimensions(w, h);
        }
        t.transcode(in, null);
        bufferedImage = t.getBufferedImage();
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
    }

    /**
     * A transcoder that generates a BufferedImage.
     */
    private class BufferedImageTranscoder extends ImageTranscoder {

        /**
         * The BufferedImage generated from the SVG document.
         */
        private BufferedImage bufferedImage;

        /**
         * Creates a new ARGB image with the specified dimension.
         * @param width the image width in pixels
         * @param height the image height in pixels
         */
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Writes the specified image to the specified output.
         * @param img the image to write
         * @param output the output where to store the image
         * @throws  TranscoderException if an error occured while storing the image
         */
        public void writeImage(BufferedImage img, TranscoderOutput output)
                throws TranscoderException {
            bufferedImage = img;
        }

        /**
         * Returns the BufferedImage generated from the SVG document.
         */
        private BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        /**
         * Set the dimensions to be used for the image.
         */
        private void setDimensions(int w, int h) {
            hints.put(KEY_WIDTH, (float) w);
            hints.put(KEY_HEIGHT, (float) h);
        }
    }

    // Icon //////////////////////////////////////////////////////////////////

    /**
     * Returns the icon's width.
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * Returns the icon's height.
     */
    public int getIconHeight() {
        return height;
    }

    /**
     * Draw the icon at the specified location.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.drawImage(bufferedImage, x, y, null);
    }

    // UserAgent /////////////////////////////////////////////////////////////

    /**
     * Returns the default size of this user agent.
     */
    public Dimension2D getViewportSize() {
        return new Dimension(width, height);
    }
}