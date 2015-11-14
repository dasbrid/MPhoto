package asbridge.me.uk.MPhoto.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by David on 10/11/2015.
 */
// http://www.androidhive.info/2013/09/android-fullscreen-image-slider-with-swipe-and-pinch-zoom-gestures/
public class Utils {

    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    // TODO: first image may be in a subfolder
    public static File getFirstImageInFolder(File folder)
    {
        if (!folder.isDirectory()) {
            return null;
        }
        File[] listFiles = folder.listFiles();
        // first look for a file in this folder
        for (File file : listFiles) {
            if (file.isFile() && isImageFile(file)) {
                return file;
            }
        }

        File file;
        for (File subfolder : listFiles) {
            if (subfolder.isDirectory()) {
                file = getFirstImageInFolder(subfolder);
                if (file != null)
                return file;
            }
        }
        return null;
    }

    public static boolean isImageFile(File file)
    {
        String filePath = file.getAbsolutePath();
        // Check supported file extensions
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    // get all FILES in path (including all files in subfolders)
    // TODO: only return compatible (image) files
    public static ArrayList<File> getAllFiles(String absolutePath)
    {
        ArrayList<File> files = new ArrayList<File>();
        File rootFolder = new File(absolutePath);
        if (!rootFolder.isDirectory())
            return files;

        addFilesToList(files, rootFolder);
        return files;
    }

    // recursion method for getting FILES in a folder
    private static void addFilesToList(ArrayList<File> fileList, File folder) {
        if (folder.isFile())
        {
            if (isImageFile(folder)) {
                fileList.add(folder);
            }
            return;
        }

        File[] listFiles = folder.listFiles();
        if (listFiles == null)
            return;

        for (File file : listFiles) {
            addFilesToList(fileList, file);
        }
    }

    // get all the FOLDERS (albums) (and their subfolders) from a folder
    public static ArrayList<File> getFolders() {
        ArrayList<File> folders = new ArrayList<File>();
        File rootFolder = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator + "MatthewsPhotos");
        addFoldersToList(folders, rootFolder);
        return folders;
    }

    // recursion method for getting subFOLDERS in a folder
    private static void addFoldersToList(ArrayList<File> folderList, File folder) {
        if (folder.isDirectory()) {
            // we have been passed a folder.
            // add it to the list
            folderList.add(folder);
            // and iterate it's contents
            // getting list of file paths
            File[] listFiles = folder.listFiles();
            if (listFiles == null)
                return;
            // Check for count
            // loop through all files
            for (File file : listFiles) {
                addFoldersToList(folderList, file);
            }
        }
    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
