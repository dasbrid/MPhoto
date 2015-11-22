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
import android.widget.Toast;
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
    private static int SLIDE_SHOW_DELAY;
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
            // to keep the slideshow going, start the timer again
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

            SLIDE_SHOW_DELAY = Integer.parseInt(Utils.getSlideshowDelay(this)); // in seconds

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

        ArrayList<File> filelist = Utils.getAllFiles(albumFolder);


        myStatePageAdapter.setFileList(filelist);
        myStatePageAdapter.notifyDataSetChanged();

        numPages = filelist.size();
        pager.setCurrentItemManual(page);
        handler.postDelayed(runnable, SLIDE_SHOW_DELAY);
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

}