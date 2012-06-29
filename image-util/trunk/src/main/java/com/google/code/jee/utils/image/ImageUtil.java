package com.google.code.jee.utils.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * The Class ImageUtil.
 */
public final class ImageUtil {

    /**
     * The Constructor.
     */
    private ImageUtil() {
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

    /**
     * Resize image.
     * 
     * @param originalImage the original image
     * @param width the width
     * @param height the height
     * @param type the type
     * @return the buffered image
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * Resize image with hint.
     * 
     * @param originalImage the original image
     * @param width the width
     * @param height the height
     * @param type the type
     * @return the buffered image
     */
    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height, int type) {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    /**
     * Checks if is line bigger than borders.
     * 
     * @param graphics the graphics
     * @param line the line
     * @param img the img
     * @return true, if is line bigger than borders
     */
    public static boolean isLineBiggerThanBorders(Graphics graphics, String line, ImageIcon img) {
        return graphics.getFontMetrics().getStringBounds(line, graphics).getWidth() > img.getIconWidth();
    }

    /**
     * Split string from list.
     *
     * @param infos the infos
     * @param line the line
     * @param i the i
     * @return the string
     */
    private static String splitStringFromList(List<String> infos, String line, int i) {
        String firstPart = line.substring(0, line.length() / 2);
        String secondPart = line.substring(line.length() / 2, line.length());
        infos.remove(i);
        infos.add(i, firstPart);
        infos.add(i + 1, secondPart);
        return firstPart;
    }

    /**
     * Draw string.
     *
     * @param g the g
     * @param s the s
     * @param x the x
     * @param y the y
     * @param width the width
     */
    public static void drawString(Graphics g, String s, int x, int y, int width)
    {
            // FontMetrics gives us information about the width,
            // height, etc. of the current Graphics object's Font.
            FontMetrics fm = g.getFontMetrics();

            int lineHeight = fm.getHeight();

            int curX = x;
            int curY = y;

            String[] words = s.split(" ");

            for (String word : words)
            {
                    // Find out thw width of the word.
                    int wordWidth = fm.stringWidth(word + " ");

                    // If text exceeds the width, then move to next line.
                    if (curX + wordWidth >= x + width)
                    {
                            curY += lineHeight;
                            curX = x;
                    }

                    g.drawString(word, curX, curY);

                    // Move over to the right for next word.
                    curX += wordWidth;
            }
    }

    
    /**
     * Adds the infos banner.
     * 
     * @param image the image
     * @param infos the infos
     * @param policeName the police name
     * @param policeSize the police size
     * @param policeColor the police color
     * @param backgroundColor the background color
     * @return the buffered image
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static BufferedImage addInfosBanner(String image, String logoPath,
            List<String> infos, String policeName, int policeSize, Color policeColor, Color backgroundColor)
            throws IOException {
        BufferedImage bufferedImage = null;
        if (image != null && !StringUtil.isEmpty(policeName) && policeColor != null
                && backgroundColor != null) {
            ImageIcon img = new ImageIcon(image);
            int textPaneHeight = 0;
            if(!CollectionUtil.isEmpty(infos)) {
                textPaneHeight = (int) (policeSize * (infos.size() + 1) - ((policeSize * 20) / 100));
            }

            bufferedImage = new BufferedImage(img.getIconWidth(), img.getIconHeight() + textPaneHeight,
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

            g2d.drawImage(img.getImage(), 0, textPaneHeight, null);

//            BufferedImage direstImage = ImageIO.read(new File(direstImagePath));
//            int type = direstImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : direstImage.getType();
//            BufferedImage direstImageResized = resizeImageWithHint(direstImage, 50, 62, type);
//            ImageIO.write(direstImageResized, "jpg", new File(direstResizedPath));

            g2d.setColor(backgroundColor);

            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font(policeName, Font.PLAIN, policeSize));

            FontMetrics fontMetrics = g2d.getFontMetrics();
            
            Rectangle2D rect;
            
            if(CollectionUtil.isEmpty(infos)) {
                rect = fontMetrics.getStringBounds("", g2d);
            } else {
                rect = fontMetrics.getStringBounds(infos.get(0), g2d);
            }

            int y = 0;
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2d.fillRect(0, y, img.getIconWidth(), (int) rect.getHeight() * (infos.size()));

            ImageIcon direstIcon = null;
            if(!StringUtil.isEmpty(logoPath)) {
                direstIcon = new ImageIcon(logoPath);
                g2d.drawImage(direstIcon.getImage(), 10, 5, direstIcon.getIconWidth(), direstIcon.getIconHeight(), null);
            }

            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

            g2d.setColor(policeColor);
            for (int i = 0; i < infos.size(); i++) {
                String line = infos.get(i);
                while (isLineBiggerThanBorders(g2d, line, img)) {
                    line = splitStringFromList(infos, line, i);
                }
                if(!StringUtil.isEmpty(logoPath) && direstIcon != null) {
                    g2d.drawString(line, direstIcon.getIconWidth() + 20, (int) (y + rect.getHeight()));
                } else {
                    g2d.drawString(line, 20, (int) (y + rect.getHeight()));
                }
                //drawString(g2d, line, direstIcon.getIconWidth() + 20, (int) (y + rect.getHeight()), (int) rect.getWidth());
                y += rect.getHeight();
            }
            //g2d.drawString("", direstIcon.getIconWidth() + 20, 15);
            // Free graphic resources
            g2d.dispose();

        }
        return bufferedImage;
    }
    
    public static BufferedImage addInfosBanner(String image, String logo, List<String> infos) throws IOException {
        return ImageUtil.addInfosBanner(image, logo, infos, "Verdana", 12, Color.white, new Color(86, 153, 38));
    }
}
