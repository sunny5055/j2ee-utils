import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.code.jee.utils.image.ImageUtil;

public class Demo {



    public static void addInfosBanner(String image, List<String> infos) throws IOException {
        ImageUtil.addInfosBanner(image, infos, "Arial", 16, Color.white, Color.black);
    }

    public static void main(String[] args) throws IOException, ArrayIndexOutOfBoundsException {
        List<String> infos = new ArrayList<String>();
        infos.add("34 – Mulhouse (68) - A36");
        infos.add("A36 Visu direction All.");
        infos.add("Alt. 233m / PR 102+210");
        infos.add("Localisation : Mulhouse");
        infos.add("Etat : Active");

        BufferedImage bufferedImage = ImageUtil.addInfosBanner("src/test/resources/img_direst.jpg", infos, "Verdana", 12, Color.white, new Color(86,153,38));

        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream("src/test/resources/infos_"
                + "test.jpg"));
        ImageIO.write(bufferedImage, "jpg", bo);
    }
}
