package asbridge.me.uk.MPhoto.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by David on 10/11/2015.
 */
// http://www.androidhive.info/2013/09/android-fullscreen-image-slider-with-swipe-and-pinch-zoom-gestures/
public class Utils {

    private Context _context;

    public static String getRootPhotosFolder(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context); //
        String folder = prefs.getString("rootPhotosFolder", "");
        return folder;
    }


    public static String getSlideshowDelay(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String folder = prefs.getString("slideshowDelay", "3");
        return folder;
    }
/*
    public static boolean isAlbumColumnWidthSet(Context context) {
        String s = getAlbumColumnWidthString(context);
        if (s==null || s == "")
            return false;
        else return isInteger(s);
    }

    public static int getAlbumColumnWidth(Context context) {
        return Integer.parseInt(getAlbumColumnWidthString(context));
    }

    private static String getAlbumColumnWidthString(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String w = prefs.getString("albumColumnWidth", "90");
        return w;
    }
*/
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
    public static ArrayList<File> getFolders(String rootPhotosFolder) {
        ArrayList<File> folders = new ArrayList<File>();
        File rootFolder = new File(rootPhotosFolder);
                //android.os.Environment.getExternalStorageDirectory()+ File.separator + "MatthewsPhotos");
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


    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private static int calculateInSampleSize(File f, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // just do the decode, don't load into memory

        BitmapFactory.decodeFile(f.getAbsolutePath(),options); // decode results into options

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        String imageType = options.outMimeType;

        int inSampleSize = 1;

        if (actualHeight > reqHeight || actualWidth > reqWidth) {

            final int halfHeight = actualHeight / 2;
            final int halfWidth = actualWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeFileToThumbnail(File f) {
        // Decode bitmap with inSampleSize set
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(f, AppConstant.THUMB_SIZE, AppConstant.THUMB_SIZE);

        Bitmap scaledBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);


        ExifInterface exif = null;
        try
        {
            exif = new ExifInterface(f.getAbsolutePath());
        }
        catch (IOException e)
        {
            //Error
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
        int rotationInDegrees = exifToDegrees(orientation);
        Matrix matrix = new Matrix();
        if (orientation != 0f) {matrix.preRotate(rotationInDegrees);}
        Bitmap adjustedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,  scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return adjustedBitmap;
    }


    private static boolean isInteger(String s) {
        int radix = 10;
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }


}
