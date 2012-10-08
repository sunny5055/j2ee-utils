package com.googlecode.jutils.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * The Class ImageUtil.
 */
public final class ImageScalingUtil {

    /**
     * The Constructor.
     */
    private ImageScalingUtil() {
        super();
    }

    /**
     * Load image from URL string.
     * 
     * @param url URL of image
     * @return the image
     * @throws IOException If there is a problem loading the image from the URL
     */
    public static Image fromUrl(String url) throws IOException {
        Image image = null;
        if (!StringUtil.isBlank(url)) {
            image = fromUrl(new URL(url));
        }
        return image;
    }

    /**
     * Load image from URL.
     * 
     * @param url URL of image
     * @return the image
     * @throws IOException If there is a problem loading the image from the URL
     */
    public static Image fromUrl(URL url) throws IOException {
        Image image = null;
        if (url != null) {
            final ImageType imageType = extensionToImageType(url.getPath());
            image = fromStream(url.openStream(), imageType);
        }
        return image;
    }

    /**
     * Load image from file string.
     * 
     * @param file the file
     * @return the image
     * @throws IOException If there is a problem loading the image from the file
     */
    public static Image fromFile(String file) throws IOException {
        Image image = null;
        if (!StringUtil.isBlank(file)) {
            image = fromFile(new File(file));
        }
        return image;
    }

    /**
     * Load image from file.
     * 
     * @param file the file
     * @return the image
     * @throws IOException If there is a problem loading the image from the file
     */
    public static Image fromFile(File file) throws IOException {
        Image image = null;
        if (file != null) {
            final ImageType imageType = extensionToImageType(file.getPath());
            image = fromStream(new FileInputStream(file), imageType);
        }
        return image;
    }

    /**
     * Load image from byte array.
     * 
     * @param data image in the form of a byte array
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image fromBytes(byte[] data) throws IOException {
        Image image = null;
        if (!ArrayUtil.isEmpty(data)) {
            image = fromBytes(data, ImageType.UNKNOWN);
        }
        return image;
    }

    /**
     * Load image from byte array.
     * 
     * @param data the data
     * @param extension the extension
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image fromBytes(byte[] data, String extension) throws IOException {
        Image image = null;
        if (!ArrayUtil.isEmpty(data) && !StringUtil.isBlank(extension)) {
            image = fromBytes(data, getImageType(extension));
        }
        return image;
    }

    /**
     * Load image from byte array.
     * 
     * @param data image in the form of a byte array
     * @param imageType hint that may be used when you eventually write the
     *            image
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image fromBytes(byte[] data, ImageType imageType) throws IOException {
        Image image = null;
        if (!ArrayUtil.isEmpty(data)) {
            image = fromStream(new ByteArrayInputStream(data), imageType);
        }
        return image;
    }

    /**
     * Load image from an input stream.
     * 
     * @param inputStream image in the form of an input stream
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image fromStream(InputStream inputStream) throws IOException {
        Image image = null;
        if (inputStream != null) {
            image = fromStream(inputStream, ImageType.UNKNOWN);
        }
        return image;
    }

    /**
     * Load image from an input stream.
     * 
     * @param inputStream image in the form of an input stream
     * @param imageType hint that may be used when you eventually write the
     *            image
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image fromStream(InputStream inputStream, ImageType imageType) throws IOException {
        Image image = null;
        if (inputStream != null) {
            image = new Image(inputStream, imageType);
        }
        return image;
    }

    /**
     * Gets the image type.
     * 
     * @param extension the extension
     * @return the image type
     */
    public static ImageType getImageType(String extension) {
        ImageType imageType = null;
        if (!StringUtil.isBlank(extension)) {
            imageType = ImageType.getType(extension);
        }
        return imageType;
    }

    /**
     * Square image.
     * 
     * @param imageStream the image stream
     * @param squareWidth the square width
     * @param crop the crop
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image squareImage(InputStream imageStream, int squareWidth, double crop) throws IOException {
        Image reduceImage = null;

        if (imageStream != null) {
            final Image img = fromStream(imageStream);
            if (img != null) {
                reduceImage = img.getResizedToSquare(squareWidth, crop);
            }
        }

        return reduceImage;
    }

    /**
     * Square image.
     * 
     * @param image the image
     * @param squareWidth the square width
     * @param crop the crop
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image squareImage(Image image, int squareWidth, double crop) throws IOException {
        Image reduceImage = null;
        if (image != null) {
            reduceImage = image.getResizedToSquare(squareWidth, crop);
        }
        return reduceImage;
    }

    /**
     * Reduce image with ratio.
     * 
     * @param imageStream the image stream
     * @param squareWidth the square width
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image reduceImageWithRatio(InputStream imageStream, int squareWidth) throws IOException {
        Image reduceImage = null;

        if (imageStream != null) {
            final Image img = fromStream(imageStream);
            if (img != null) {
                reduceImage = img.getResizedToWidth(squareWidth);
            }
        }

        return reduceImage;
    }

    /**
     * Reduce image with ratio.
     * 
     * @param image the image
     * @param squareWidth the square width
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image reduceImageWithRatio(Image image, int squareWidth) throws IOException {
        Image reduceImage = null;
        if (image != null) {
            reduceImage = image.getResizedToWidth(squareWidth);
        }
        return reduceImage;
    }

    /**
     * Reduce image.
     * 
     * @param imageStream the image stream
     * @param imageWidth the image width
     * @param imageHeight the image height
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image reduceImage(InputStream imageStream, int imageWidth, int imageHeight) throws IOException {
        Image reduceImage = null;
        if (imageStream != null) {
            final ResampleOp resampleOp = new ResampleOp(imageWidth, imageHeight);
            final BufferedImage imgTmp = ImageIO.read(imageStream);
            if (imgTmp != null) {
                final BufferedImage rescaledImage = resampleOp.filter(imgTmp, null);
                reduceImage = new Image(rescaledImage, null);
            }
        }

        return reduceImage;
    }

    /**
     * Reduce image.
     * 
     * @param image the image
     * @param imageWidth the image width
     * @param imageHeight the image height
     * @return the image
     * @throws IOException the IO exception
     */
    public static Image reduceImage(Image image, int imageWidth, int imageHeight) throws IOException {
        Image reduceImage = null;
        if (image != null) {
            final ResampleOp resampleOp = new ResampleOp(imageWidth, imageHeight);
            final BufferedImage imgTmp = image.getBufferedImage();
            if (imgTmp != null) {
                final BufferedImage rescaledImage = resampleOp.filter(imgTmp, null);
                reduceImage = new Image(rescaledImage, null);
            }
        }

        return reduceImage;
    }

    /**
     * Extension to image type.
     * 
     * @param path the path
     * @return the image type
     */
    private static ImageType extensionToImageType(String path) {
        ImageType imageType = ImageType.UNKNOWN;
        if (!StringUtil.isBlank(path)) {
            final int index = StringUtil.lastIndexOf(path, ".");
            if (index != -1) {
                final String extension = StringUtil.substring(path, index + 1);
                imageType = ImageType.getType(extension);
            }
        }
        return imageType;
    }
}
