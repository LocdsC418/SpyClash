package src.util;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Assets {

    private static final Map<String, Image> imageCache = new HashMap<>();

    private Assets() {}

    public static Image loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        URL url = Assets.class.getResource(path);
        if (url == null) {
            System.out.println("Image not found: " + path);
            return null;
        }

        Image img = new ImageIcon(url).getImage();
        imageCache.put(path, img);
        return img;
    }
}
