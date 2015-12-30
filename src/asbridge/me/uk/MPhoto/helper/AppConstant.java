package asbridge.me.uk.MPhoto.helper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by David on 10/11/2015.
 */
public class AppConstant {

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    // SD card image directory
    public static final String PHOTO_ALBUM = "MatthewsPhotos";

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
            "png");

    // size (pixels) of images used for thumbnails
    // NOT the size on the screen (specified in guiconstants)
    public static int THUMB_SIZE= 128;

    public static int SLIDESHOW_WIDTH= 1280;
    public static int SLIDESHOW_HEIGHT= 800;

    public static final boolean ALLOW_DELETE = true;
    public static final boolean ALLOW_VIEW_PHOTOS = true;
    public static final boolean USE_PHOTO_GRID_ACTIVITY = true;

}
