package com.google.code.jee.utils.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings ("restriction")
public class Image {
    private BufferedImage img;
    private ImageType imageType = ImageType.UNKNOWN;

    /**
     * Load image from InputStream
     * 
     * @param input
     * @throws IOException
     */
    public Image(InputStream input, ImageType imageType) throws IOException {
        img = ImageIO.read(input);
        input.close();
        this.imageType = imageType == null ? ImageType.UNKNOWN : imageType;
    }

    /**
     * Constructor for taking a BufferedImage
     * 
     * @param img
     */
    public Image(BufferedImage img, ImageType imageType) {
        this.img = img;
        this.imageType = imageType == null ? ImageType.UNKNOWN : imageType;
    }

    /**
     * @return Source type of the image
     */
    public ImageType getSourceType() {
        return imageType;
    }

    /**
     * @return Width of the image in pixels
     */
    public int getWidth() {
        return img.getWidth();
    }

    /**
     * @return Height of the image in pixels
     */
    public int getHeight() {
        return img.getHeight();
    }

    /**
     * @return Aspect ratio of the image (width / height)
     */
    public double getAspectRatio() {
        return (double) getWidth() / (double) getHeight();
    }

    /**
     * Generate a new Image object resized to a specific width, maintaining the
     * same aspect ratio of the original
     * 
     * @param width
     * @return Image scaled to new width
     */
    public Image getResizedToWidth(int width) {
        final int nHeight = width * img.getHeight() / img.getWidth();
        final MultiStepRescaleOp rescale = new MultiStepRescaleOp(width, nHeight);
        rescale.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
        final BufferedImage resizedImage = rescale.filter(img, null);

        return new Image(resizedImage, imageType);
    }

    /**
     * Generate a new Image object cropped to a new size
     * 
     * @param x1 Starting x-axis position for crop area
     * @param y1 Starting y-axis position for crop area
     * @param x2 Ending x-axis position for crop area
     * @param y2 Ending y-axis position for crop area
     * @return Image cropped to new dimensions
     */
    public Image crop(int x1, int y1, int x2, int y2) {
        if (x1 < 0 || x2 <= x1 || y1 < 0 || y2 <= y1 || x2 > getWidth() || y2 > getHeight()) {
            throw new IllegalArgumentException("invalid crop coordinates");
        }

        final int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
        final int nNewWidth = x2 - x1;
        final int nNewHeight = y2 - y1;
        final BufferedImage cropped = new BufferedImage(nNewWidth, nNewHeight, type);
        final Graphics2D g = cropped.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setComposite(AlphaComposite.Src);

        g.drawImage(img, 0, 0, nNewWidth, nNewHeight, x1, y1, x2, y2, null);
        g.dispose();

        return new Image(cropped, imageType);
    }

    /**
     * Useful function to crop and resize an image to a square. This is handy
     * for thumbnail generation.
     * 
     * @param width Width of the resulting square
     * @param cropEdgesPct Specifies how much of an edge all around the square
     *            to crop, which creates a zoom-in effect on the center of the
     *            resulting square. This may be useful, given that when images
     *            are reduced to thumbnails, the detail of the focus of the
     *            image is reduced. Specifying a value such as 0.1 may help
     *            preserve this detail. You should experiment with it. The value
     *            must be between 0 and 0.5 (representing 0% to 50%)
     * @return Image cropped and resized to a square
     */
    public Image getResizedToSquare(int width, double cropEdgesPct) {
        if (cropEdgesPct < 0 || cropEdgesPct > 0.5) {
            throw new IllegalArgumentException("Crop edges pct must be between 0 and 0.5. " + cropEdgesPct
                    + " was supplied.");
        }
        // crop to square first. determine the coordinates.
        final int cropMargin = (int) Math.abs(Math.round(((img.getWidth() - img.getHeight()) / 2.0)));
        int x1 = 0;
        int y1 = 0;
        int x2 = getWidth();
        int y2 = getHeight();
        if (getWidth() > getHeight()) {
            x1 = cropMargin;
            x2 = x1 + y2;
        } else {
            y1 = cropMargin;
            y2 = y1 + x2;
        }

        // should there be any edge cropping?
        if (cropEdgesPct != 0) {
            final int cropEdgeAmt = (int) ((x2 - x1) * cropEdgesPct);
            x1 += cropEdgeAmt;
            x2 -= cropEdgeAmt;
            y1 += cropEdgeAmt;
            y2 -= cropEdgeAmt;
        }

        // generate the image cropped to a square
        final Image cropped = crop(x1, y1, x2, y2);

        // now resize. we do crop first then resize to preserve detail
        final Image resized = cropped.getResizedToWidth(width);
        cropped.dispose();

        return resized;
    }

    /**
     * Soften the image to reduce pixelation. Helps JPGs look better after
     * resizing.
     * 
     * @param softenFactor Strength of softening. 0.08 is a good value
     * @return New Image object post-softening, unless softenFactor == 0, in
     *         which case the same object is returned
     * @throws NotImplementedException Not implemented in this class
     */
    public Image soften(float softenFactor) {
        if (softenFactor == 0f) {
            return this;
        } else {
            final float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - softenFactor * 4, softenFactor, 0,
                    softenFactor, 0 };
            final Kernel kernel = new Kernel(3, 3, softenArray);
            final ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            return new Image(cOp.filter(img, null), imageType);
        }
    }

    /**
     * Write image to a file. If the supplied File argument includes an
     * extension, this method will attempt to write the image of that type, IF
     * it is supported. Otherwise JPG is the default. If the supplied File
     * argument does not include an extension, this method will attempt to write
     * the image in the type of the original source image, IF it is supported.
     * Otherwise JPG is the default. This method will overwrite a file that
     * exists with the same name
     * 
     * @see #getWriterFormatNames()
     * @param file File to write image to
     * @returns File object representing the image, which will have the proper
     *          extension appended if none was supplied (i.e. if the method
     *          chose the image type), or will have ".jpg" appended if the
     *          supplied extension is not supported
     * @throws IOException
     */
    public File writeToFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File argument was null");
        }

        File writeto = null;

        // extract extension
        final String name = file.getName();
        String ext = null;
        final int dot = name.lastIndexOf(".");
        if (dot != -1) {
            ext = name.substring(dot + 1).toLowerCase();
        }

        // the presence of an extension in the file name tells us to
        // attempt to write the image of that type, if it is supported
        if (ext != null) {
            if (Arrays.asList(getWriterFormatNames()).contains(ext)) {
                writeto = file;
            } else {
                // failing that, default to jpg
                ext = "jpg";
                writeto = new File(file.getPath() + ".jpg");
            }
        } else {
            // if no extension is supplied, try to use the image type of the
            // source image
            if (Arrays.asList(getWriterFormatNames()).contains(getSourceType().toString().toLowerCase())) {
                ext = getSourceType().toString().toLowerCase();
                writeto = new File(file.getPath() + "." + getSourceType().toString().toLowerCase());
            } else {
                // failing that, default to jpg
                ext = "jpg";
                writeto = new File(file.getPath() + ".jpg");
            }
        }
        writeToFile(writeto, ext);
        return writeto;
    }

    /**
     * Write image to a file, specify image type This method will overwrite a
     * file that exists with the same name
     * 
     * @see #getWriterFormatNames()
     * @param file File to write image to
     * @param type jpg, gif, etc.
     * @throws IOException
     */
    public void writeToFile(File file, String type) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File argument was null");
        }
        ImageIO.write(img, type, file);
    }

    /**
     * @return Array of supported image types for writing to file
     */
    public String[] getWriterFormatNames() {
        return ImageIO.getWriterFormatNames();
    }

    /**
     * Writes to JPG using Sun's JPEGCodec
     * 
     * @param file File to write image to
     * @param quality The image quality
     * @throws IOException
     */
    public void writeToJPG(File file, float quality) throws IOException {
        final FileOutputStream out = new FileOutputStream(file);

        // Encodes image as a JPEG data stream
        final JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

        final JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);

        param.setQuality(quality, true);

        encoder.setJPEGEncodeParam(param);
        encoder.encode(img);
    }

    /**
     * Free up resources associated with this image
     */
    public void dispose() {
        img.flush();
    }

    /**
     * @return Internal representation of the image
     */
    public BufferedImage getBufferedImage() {
        return img;
    }

}
