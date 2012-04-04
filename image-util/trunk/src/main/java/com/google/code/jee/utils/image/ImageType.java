package com.google.code.jee.utils.image;

import java.util.HashMap;
import java.util.Map;

import com.google.code.jee.utils.StringUtil;

/**
 * The Enum ImageType.
 * 
 * @author James
 */
public enum ImageType {
    JPG, GIF, PNG, UNKNOWN;

    private static final Map<String, ImageType> extensionMap = new HashMap<String, ImageType>();

    static {
        extensionMap.put("jpg", ImageType.JPG);
        extensionMap.put("jpeg", ImageType.JPG);
        extensionMap.put("gif", ImageType.GIF);
        extensionMap.put("png", ImageType.PNG);
    }

    /**
     * Gets the type.
     * 
     * @param extension the extension
     * @return the type
     */
    public static ImageType getType(String extension) {
        ImageType imageType = UNKNOWN;
        if (!StringUtil.isBlank(extension)) {
            extension = extension.toLowerCase();
            if (extensionMap.containsKey(extension)) {
                imageType = extensionMap.get(extension);
            }

        }
        return imageType;
    }
}
