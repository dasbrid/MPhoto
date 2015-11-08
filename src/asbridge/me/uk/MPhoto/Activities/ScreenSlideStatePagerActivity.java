package asbridge.me.uk.MPhoto.Activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.MyStatePagerAdapter;
import asbridge.me.uk.MPhoto.Classes.NonSwipeableViewPager;
import asbridge.me.uk.MPhoto.R;

import java.io.File;
import java.util.*;

/**
 * Created by David on 04/11/2015.
 * See http://developer.android.com/training/animation/screen-slide.html
 */
public class ScreenSlideStatePagerActivity extends FragmentActivity   implements View.OnClickListener{
    private static String TAG=ScreenSlideStatePagerActivity.class.getName();
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private int numPages;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private Timer timer;
    private int page = 0;

    private Handler handler = new Handler();
    private static final int slideshowDelay = 5; // in seconds

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            pager.setCurrentItemManual(page++);
            if (page >= numPages)
                page = 0;

            /* and here comes the "trick" */
            handler.postDelayed(this, slideshowDelay*1000);
        }
    };

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
//    private PagerAdapter mPagerAdapter;
    private MyStatePagerAdapter myStatePageAdapter;
    private NonSwipeableViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        myStatePageAdapter = new MyStatePagerAdapter(getSupportFragmentManager(), this);
        pager = (NonSwipeableViewPager)findViewById(R.id.dynamicpager);
        pager.setAdapter(myStatePageAdapter);


        Button btnEnableSwiping = (Button)findViewById(R.id.btnEnableSwiping);
        btnEnableSwiping.setOnClickListener(this);



        ArrayList<File> filelist = getFilesToCopy("/mnt/sdcard/MatthewsPhotos");
        myStatePageAdapter.setFileList(filelist);
        myStatePageAdapter.notifyDataSetChanged();

        numPages = filelist.size();
        pager.setCurrentItemManual(page);
        handler.postDelayed(runnable, slideshowDelay);
    }

    private ArrayList<File> getMediaToCopy(Date lastSyncTime)
    {
        Log.v(TAG, "Getting media to copy");
        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

        // Get the base URI for ...
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.i(TAG, cur.getCount() + " media files found");
        if (cur.getCount() == 0)
            return null;

        ArrayList<File> filesToCopy = new ArrayList<File>();

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

                Log.v(TAG, "Testing media " + data + " taken on " + dateTaken.toString());
                if (lastSyncTime == null || (dateTaken.compareTo(lastSyncTime) > 0)) {
                    Log.v(TAG, "Added " + data + " to list");
                    /*
                    FileToCopy ftc = new FileToCopy();
                    ftc.file = new File(data);
                    ftc.dateTaken = dateTaken;
                    ftc.bucket = bucket;
*/
                    filesToCopy.add(new File(data));
                }


            } while (cur.moveToNext());
        }
        return filesToCopy;
    }

    private ArrayList<File> getFilesToCopy(String filepath)
    {
        ArrayList<File> filesToCopy = new ArrayList<File>();

        File picturesStorageDir  = new File(filepath);
        File[] fullFileList = picturesStorageDir.listFiles();
        if (fullFileList == null)
        {
            Toast.makeText(this, "Cannot read list of files", Toast.LENGTH_SHORT).show();
            return null;
        }
        Log.v(TAG, "File folder contains " + fullFileList.length + " files");

        Date filedate;
        for (File theFile : fullFileList) {
            if (theFile.isFile()) {
                Log.v(TAG, "Added " + theFile.getName() + " to list");
                filesToCopy.add(theFile);
            }
        }

        return filesToCopy;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnableSwiping:
                PlayPauseBtnHandler();
                break;
        }
    }

    private void PlayPauseBtnHandler()
    {
        pager.togglePlayPause();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

}