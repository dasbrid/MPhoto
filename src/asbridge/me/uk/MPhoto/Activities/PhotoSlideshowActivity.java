package asbridge.me.uk.MPhoto.Activities;

import android.app.ActionBar;
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
import asbridge.me.uk.MPhoto.adapter.MyStatePagerAdapter;
import asbridge.me.uk.MPhoto.Classes.NonSwipeableViewPager;
import asbridge.me.uk.MPhoto.R;

import java.io.File;
import java.util.*;

import asbridge.me.uk.MPhoto.helper.Utils;

/**
 * Created by David on 04/11/2015.
 * See http://developer.android.com/training/animation/screen-slide.html
 */
public class PhotoSlideshowActivity extends FragmentActivity   implements View.OnClickListener{
    private static String TAG=PhotoSlideshowActivity.class.getName();
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private int numPages;

    // The time (in seconds) for which the navigation controls are visible after screen interaction.
    private static int SHOW_NAVIGATION_CONTROLS_TIME = 3;
    // Delay between slides
    private static final int SLIDE_SHOW_DELAY = 2; // in seconds
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private Timer timer;
    private int page = 0;

    private Handler handler = new Handler();


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pager.setCurrentItemManual(page++);
            if (page >= numPages)
                page = 0;
            // to keep the slideshow going, start te timer again
            handler.postDelayed(this, SLIDE_SHOW_DELAY * 1000);
        }
    };


    // Runnable called by handler x seconds after user interaction
    private Runnable hidenavigation = new  Runnable() {
        @Override
        public void run() {
            Button btnEnableSwiping = (Button)findViewById(R.id.btnEnableSwiping);
            btnEnableSwiping.setVisibility(View.INVISIBLE);
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(uiOptions);
            getActionBar().hide();
        }
    };

    private MyStatePagerAdapter myStatePageAdapter;
    private NonSwipeableViewPager pager;

    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first

        // Code to make layout fullscreen. In onResume, otherwise when activity comes back it will revert to non-fullscreen
        // http://developer.android.com/training/system-ui/status.html
        // hide the status bar and application bar (top of the screen) with SYSTEM_UI_FLAG_FULLSCREEN
        // hide the navigation bar (back, home at bottom of screen) with SYSTEM_UI_FLAG_HIDE_NAVIGATION
        // SYSTEM_UI_FLAG_LAYOUT_STABLE puts the navigation controls on top of the activity layout (stays fullscreen)
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        // You should never show the action bar (application bar) if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            Utils utils = new Utils(this);

            // http://developer.android.com/training/system-ui/visibility.html
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            // Note that system bars will only be "visible" if none of the
                            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            Button btnEnableSwiping = (Button)findViewById(R.id.btnEnableSwiping);
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                // Navigation is here, show our custom navigation buttons
                                btnEnableSwiping.setVisibility(View.VISIBLE);
                                // start timer to hide navigation after x seconds
                                Handler vishandler = new Handler();
                                vishandler.postDelayed(hidenavigation, SHOW_NAVIGATION_CONTROLS_TIME*1000); // milliseconds
                            } else {
                                // TODO: The system bars are NOT visible. Make any desired
                                btnEnableSwiping.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        setContentView(R.layout.activity_photo_slideshow);

        myStatePageAdapter = new MyStatePagerAdapter(getSupportFragmentManager(), this);
        pager = (NonSwipeableViewPager)findViewById(R.id.dynamicpager);
        pager.setAdapter(myStatePageAdapter);


        Button btnEnableSwiping = (Button)findViewById(R.id.btnEnableSwiping);
        btnEnableSwiping.setOnClickListener(this);

            Bundle parameters = getIntent().getExtras();
            String albumFolder =parameters.getString("folderAbsolutePath");

        ArrayList<File> filelist = utils.getAllFiles(albumFolder);


        myStatePageAdapter.setFileList(filelist);
        myStatePageAdapter.notifyDataSetChanged();

        numPages = filelist.size();
        pager.setCurrentItemManual(page);
        handler.postDelayed(runnable, SLIDE_SHOW_DELAY);
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
        Button btnEnableSwiping = (Button)findViewById(R.id.btnEnableSwiping);
        if (pager.IsSlideshowOn())
            btnEnableSwiping.setText("Pause");
        else
            btnEnableSwiping.setText("Play");

        btnEnableSwiping.setVisibility(View.INVISIBLE);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        getActionBar().hide();

    }
/*
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
*/
}