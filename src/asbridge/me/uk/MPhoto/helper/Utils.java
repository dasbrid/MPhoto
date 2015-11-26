package asbridge.me.uk.MPhoto.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.Album;

/**
 * Created by David on 10/11/2015.
 */
// http://www.androidhive.info/2013/09/android-fullscreen-image-slider-with-swipe-and-pinch-zoom-gestures/
public class Utils {

    public static String getRootPhotosFolder(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context); //
        String folder = prefs.getString("rootPhotosFolder", "");
        return folder;
    }

    public static boolean getFromMediaPreference(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context); //
        boolean fromMedia = prefs.getBoolean("fromMedia", false);
        return fromMedia;
    }

    public static String getSlideshowDelay(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String folder = prefs.getString("slideshowDelay", "3");
        return folder;
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

    public static ArrayList<Album> getAlbumsFromFolders(String rootPhotosFolder)
    {
        ArrayList<Album> albums = new ArrayList<Album>();
        File rootFolder = new File(rootPhotosFolder);
        addAlbumsToList(albums, rootFolder);
        return albums;
    }

    public static int addAlbumsToList(ArrayList<Album> albumList, File folder) {
        int numfiles = 0;
        File[] listFiles = folder.listFiles();
        if (listFiles == null)
            return numfiles;
        // loop through all files
        for (File file : listFiles) {
            if (file.isFile()) {
                numfiles++;
            }
            if (file.isDirectory()) {
                numfiles=numfiles+addAlbumsToList(albumList, file);
            }
        }
        if (numfiles > 0) {
            File firstfile = getFirstImageInFolder(folder);
            Album a = new Album(folder.getName(), firstfile, folder);
            albumList.add(a);
        }
        return numfiles;
    }
/*
    public static ArrayList<Album> getAlbumsFromFolders(String rootPhotosFolder)
    {
        ArrayList<Album> albums = new ArrayList<Album>();
        File rootFolder = new File(rootPhotosFolder);
        addAlbumsToList(albums, rootFolder);
        return albums;
    }

    // recursion method for getting subFOLDERS in a folder
    private static void addAlbumsToList(ArrayList<Album> albumList, File folder) {
        //Toast.makeText(context, "processing "+folder.getName(), Toast.LENGTH_SHORT).show();
        if (folder.isDirectory()) {
            // we have been passed a folder.
            // add it to the list
            File firstfile = getFirstImageInFolder(folder);
            Album a = new Album(folder.getName(), firstfile, folder);
            Log.d("DAVE", "adding folder "+folder.getName()+", firstfile "+firstfile.getName());
            albumList.add(a);
            // and iterate it's contents
            // getting list of file paths
            File[] listFiles = folder.listFiles();
            if (listFiles == null)
                return;
            // loop through all files
            for (File file : listFiles) {
                addAlbumsToList(albumList, file);
            }
        }
    }

    // get all the FOLDERS (albums) (and their subfolders) from a folder
    public static ArrayList<File> getFolders(String rootPhotosFolder) {
        ArrayList<File> folders = new ArrayList<File>();
        File rootFolder = new File(rootPhotosFolder);
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
    */

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

    public static Bitmap decodeFileToSize(File f, int reqWidth, int reqHeight) {
        // Decode bitmap with inSampleSize set
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(f, reqWidth, reqHeight);

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



    public static Bitmap decodeFileToThumbnail(File f) {
        return decodeFileToSize(f,AppConstant.THUMB_SIZE, AppConstant.THUMB_SIZE );
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


    public static ArrayList<File> getMediaInBucket(Context context, String bucketDisplayName )
    {
        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

        // Get the base URI for ...
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] selectionArgs = null;
        String selectionClause = null;
        String OrderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        if (bucketDisplayName != null) {
            // get media in specific bucket
            selectionArgs = new String[1];
            selectionArgs[0] = bucketDisplayName;
            selectionClause = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        }

        // Make the query.
        Cursor cur = context.getContentResolver().query(
                images,     // URI
                projection, // Which columns to return
                selectionClause,       // WHERE clause  (null = all rows)
                selectionArgs,       // Selection arguments (null = none)
                null        // Ordering
        );

        if (cur.getCount() == 0)
            return null;

        ArrayList<File> files = new ArrayList<File>();

        if (cur.moveToFirst()) {
            String bucket;
            String dateTakenString;
            String data;

            Date dateTaken;
            Date dateAdded;

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateTakenColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                dateTakenString = cur.getString(dateTakenColumn);
                dateTaken = new Date(Long.parseLong(dateTakenString));

                data = cur.getString(dataColumn);

                files.add(new File(data));

            } while (cur.moveToNext());
        }
        return files;
    }

    public static ArrayList<Album> getAlbumsFromMedia(Context context) {

        ArrayList<Album> albums = new ArrayList<>();

        String[] PROJECTION_BUCKET = {
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA };

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2"; // this is really WHERE (1) GROUP BY 1,(2)
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = context.getContentResolver().query(
                images,
                PROJECTION_BUCKET,
                BUCKET_GROUP_BY,
                null,
                BUCKET_ORDER_BY);

        if (cur.moveToFirst()) {
            String bucketname;
            String date;
            String data;
            long bucketId;
            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            do {
                // Get the field values
                bucketname = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                bucketId = cur.getInt(bucketIdColumn);

                if (bucketname != null && bucketname.length() > 0) {
                    // Do something with the values.
                    File f = new File(data);

                    Album album = new Album(bucketname, f, f.getParentFile());
                    albums.add(album);
                }
            } while (cur.moveToNext());
        }
        cur.close();

        return albums;
    }
}
