package objects;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageHelper {
    static Map<String, Image> imgMap = new HashMap<String, Image>();

    public static Image getPicture(String fileName) {
	Image img = null;
	if (imgMap.containsKey(fileName)) {
	    img = imgMap.get(fileName);
	} else {
	    try {
		img = ImageIO.read(ImageHelper.class
			.getResourceAsStream("/img/" + fileName));

		imgMap.put(fileName, img);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return img;
    }
}
