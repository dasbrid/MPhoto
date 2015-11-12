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

    // get all files in path (including all subfolders)
    public ArrayList<File> GetAllFiles(String absolutePath)
    {
        ArrayList<File> files = new ArrayList<File>();
        File rootFolder = new File(absolutePath);
        if (!rootFolder.isDirectory())
            return files;

        AddFilesToList(files, rootFolder);
        return files;
    }

    private void AddFilesToList(ArrayList<File> fileList, File folder) {
        if (folder.isFile())
        {
            fileList.add(folder);
            return;
        }

        File[] listFiles = folder.listFiles();
        if (listFiles == null)
            return;

        for (File file : listFiles) {
            AddFilesToList(fileList, file);
        }
    }

    // get all the folders (and their subfolders) from a folder
    public ArrayList<File> GetFolders() {
        ArrayList<File> folders = new ArrayList<File>();
        File rootFolder = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator + "MatthewsPhotos");
        AddFoldersToList(folders, rootFolder);
        return folders;
    }

    private void AddFoldersToList(ArrayList<File> folderList, File folder) {
        if (folder.isDirectory()) {
            // we have been passed a folder.
            // add it to the list
            folderList.add(folder);
            Toast.makeText(_context, folder.getName(), Toast.LENGTH_SHORT).show();
            // and iterate it's contents
            // getting list of file paths
            File[] listFiles = folder.listFiles();
            if (listFiles == null)
                return;
            Toast.makeText(_context, "numfiles=" + Integer.toString(listFiles.length), Toast.LENGTH_SHORT).show();
            // Check for count
            // loop through all files
            for (File file : listFiles) {
                AddFoldersToList(folderList, file);
            }
        }
    }

    // get files in the root path
    public ArrayList<File> GetFiles() {
        return GetFilesInFolder(".");
    }


    // Reading file paths from SDCard
    public ArrayList<File> GetFilesInFolder(String foldername) {
        ArrayList<File> files = new ArrayList<File>();

        File directory = new File(
                android.os.Environment.getExternalStorageDirectory()
                        + File.separator + AppConstant.PHOTO_ALBUM + File.separator + foldername);

        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();

            // Check for count
            if (listFiles.length > 0) {

                // loop through all files
                for (int i = 0; i < listFiles.length; i++) {
                    File file = listFiles[i];

                    // get file path
                    String filePath = listFiles[i].getAbsolutePath();

                    // check for supported file extension
                    if (IsSupportedFile(file)) {
                        // Add image path to array list
                        files.add(file);
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(
                        _context,
                        AppConstant.PHOTO_ALBUM
                                + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(AppConstant.PHOTO_ALBUM
                    + " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return files;
    }

    private  boolean IsSupportedFile(File file) {
        return IsSupportedFile(file.getAbsolutePath());
    }

        // Check supported file extensions
    private  boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

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
