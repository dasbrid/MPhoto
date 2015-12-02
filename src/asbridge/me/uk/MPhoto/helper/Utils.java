package asbridge.me.uk.MPhoto.helper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import android.app.Activity;
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
/*
    public static String getSlideshowDelay(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String folder = prefs.getString("slideshowDelay", "3");
        return folder;
    }
*/
    public static int getSlideshowDelay(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("Asbridge.Me.Uk.MPhoto",Context.MODE_PRIVATE);

        String ssd = sharedPref.getString("slideshowDelay", "3");
        return Integer.parseInt(ssd);
    }

    public static void setSlideshowDelay(Context context, int ssd)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("Asbridge.Me.Uk.MPhoto",Context.MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = sharedPref.edit();
        settingsEditor.putInt("slideshowDelay", ssd);
        settingsEditor.commit();
    }

    public static String getphotoDatePreference(Context context)
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String photoDate = prefs.getString("photoDate", "DateTaken");
        return photoDate;
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

    // Get all the media in specified bucket
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

    public static ArrayList<Album> getAlbumsFromMediaGroupedByMonth(Context context) {
        return getAlbumsFromMediaGrouped(context, true);
    }

    public static ArrayList<Album> getAlbumsFromMediaGroupedByYear(Context context) {
        return getAlbumsFromMediaGrouped(context, false);
    }

    public static ArrayList<Album> getAlbumsFromMediaGrouped(Context context, boolean groupbyMonth) {

        ArrayList<Album> albums = new ArrayList<>();

        String photoDatePreference;
        String storedpref;
        storedpref = Utils.getphotoDatePreference(context);
        Log.d("DAVE", storedpref);
        if (Utils.getphotoDatePreference(context).equals("DateTaken"))
            photoDatePreference = MediaStore.Images.Media.DATE_TAKEN;
        else
            photoDatePreference = MediaStore.Images.Media.DATE_ADDED;

        String[] PROJECTION_BUCKET = {
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                photoDatePreference,
                MediaStore.Images.Media.DATA };

        String BUCKET_GROUP_BY = null; // no group by


        String BUCKET_ORDER_BY = photoDatePreference + " DESC"; // oldest photo first

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        int numimages = 0;
        int numimagesWithDate = 0;

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
            int dateColumn = cur.getColumnIndex(photoDatePreference);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            int currentMonth = -1;
            int currentYear = -1;


            do {
                // Get the field values
                bucketname = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                bucketId = cur.getInt(bucketIdColumn);

                numimages++;

                if (date != null) {
                    long milliseconds = Long.parseLong(date); // since 1/1/1970
                    Calendar cl = Calendar.getInstance();
                    cl.setTimeInMillis(milliseconds);
                    int month = cl.get(Calendar.MONTH);
                    int year = cl.get(Calendar.YEAR);

                    numimagesWithDate++;

                    if (bucketname != null && bucketname.length() > 0) {
                        boolean newGroup = false;
                        if (groupbyMonth == false) {
                            newGroup = (year != currentYear);
                        } else {
                            newGroup = (month != currentMonth || year != currentYear);
                        }
                        if (newGroup) {
                            currentMonth = month;
                            currentYear = year;
                            String albumdate;
                            File f = new File(data);
                            int albumMonth;
                            if (groupbyMonth) {
                                albumdate = cl.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " + year;
                                albumMonth = month;
                            } else {
                                albumdate = Integer.toString(year);
                                albumMonth = -1;
                            }
                            Album album = new Album(albumdate, year, albumMonth, f, f.getParentFile());
                            albums.add(album);
                        }
                    }
                }
            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("DAVE", "using "+ photoDatePreference + " found " +numimages+" images in " + albums.size()+" albums. Of which "+numimagesWithDate+" images with date");
        return albums;
    }

    // Get media in a given month (of a given year)
    public static ArrayList<File> getMediaInMonth(Context context, int month, int year) {
        long minDate;
        long maxDate;

        Calendar c = Calendar.getInstance();
        c.clear(); // set all fields (HH:MM:SS) to 0
        c.set(year, month, 1);
        minDate = c.getTimeInMillis();
        c.add(Calendar.MONTH, 1);
        maxDate = c.getTimeInMillis();

        return getMediaInDateRange(context, minDate, maxDate);
    }

    // Get all media in specified year
    // Calculates min and max dates and gets data inbetween
    public static ArrayList<File> getMediaInYear(Context context, int year) {
        long minDate;
        long maxDate;

        Calendar c = Calendar.getInstance();
        c.set(year, 0, 1, 0, 0, 0);
        minDate = c.getTimeInMillis();
        c.set(year+1, 0, 1, 0, 0, 0);
        maxDate = c.getTimeInMillis();
        return getMediaInDateRange(context, minDate, maxDate);
    }

    // Just get all the media. No dates specified
    public static ArrayList<File> getAllMedia(Context context, int year) {
        return getMediaInDateRange(context, -1, -1);
    }

    // Get all media since a certain tima ago (e.g. one year)
    public static ArrayList<File> getRecentMedia(Context context) {
        long minDate;

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        minDate = c.getTimeInMillis();
        return getMediaInDateRange(context, minDate, -1);
    }

    // Get Media between two dates (using Query) from content provider
    // Used by other methods
    // If maxdate is -1 then no max date is used (up to present date)
    // If minDate and maxdate are both -1 then no clause is used and we get all media
    public static ArrayList<File> getMediaInDateRange(Context context, long minDate, long maxDate) {

        Log.d("DAVE", "searching between " + Long.toString(minDate) + " and " + Long.toString(maxDate));

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

        String photoDatePreference;
        if (Utils.getphotoDatePreference(context).equals("DateTaken"))
            photoDatePreference = MediaStore.Images.Media.DATE_TAKEN;
        else
            photoDatePreference = MediaStore.Images.Media.DATE_ADDED;

        if (minDate != -1 && maxDate != -1) {
            // get media in specified time range
            selectionArgs = new String[2];
            selectionArgs[0] = Long.toString(minDate); // min date
            selectionArgs[1] = Long.toString(maxDate); // max date
            selectionClause = photoDatePreference + ">=? and " + photoDatePreference + "<=?";
        }

        if (minDate != -1 && maxDate == -1) {
            // get media in from specified start time
            selectionArgs = new String[1];
            selectionArgs[0] = Long.toString(minDate); // min date
            selectionClause = photoDatePreference + ">=?";
            Log.d("DAVE", "recent files:"+selectionClause);
        }

        // Make the query.
        Cursor cur = context.getContentResolver().query(
                images,     // URI
                projection, // Which columns to return
                selectionClause,       // WHERE clause  (null = all rows)
                selectionArgs,       // Selection arguments (null = none)
                photoDatePreference + " desc"        // Ordering
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
                Log.d("DATE", dateTaken.toString());
                data = cur.getString(dataColumn);

                files.add(new File(data));

            } while (cur.moveToNext());
        }
        return files;
    }

    ///////////////////////////////////////////////////////////////////
    // Get all the different types of albums from the media store
    ///////////////////////////////////////////////////////////////////
    public static ArrayList<Album> getLotsOfAlbumsFromMedia(Context context) {
        ArrayList<Album> albums = new ArrayList<>();

        // get everything grouped by months
        ArrayList<Album> monthAlbums = new ArrayList<>();
        monthAlbums = getAlbumsFromMediaGroupedByMonth(context);

        // get everything grouped by years, (note the month is -1)
        ArrayList<Album> yearAlbums = new ArrayList<>();
        yearAlbums = getAlbumsFromMediaGroupedByYear(context);

        // the first file in the first album, a 'random' choice as the thumbnail for some esoteric albums
        File firstFile = monthAlbums.get(0).getFirstFile();

        // create a dummy album for all photos (note the year is 0)
        Album allPhotos = new Album("All Photos",0, 0, firstFile, null);

        // create an album of recent pictures (note the month and year are -2)
        Album recentPhotos = new Album("Recent Photos",-2, -2, firstFile, null);

        // add the albums to the list
        albums.add(allPhotos);
        albums.add(recentPhotos);
        albums.addAll(yearAlbums);
        albums.addAll(monthAlbums);
        return albums;
    }


}
