package asbridge.me.uk.MPhoto.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.NonSwipeableViewPager;
import asbridge.me.uk.MPhoto.Classes.PhotoViewPager;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.MyStatePagerAdapter;
import asbridge.me.uk.MPhoto.adapter.PhotoPagerAdapter;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by David on 04/11/2015.
 * See http://developer.android.com/training/animation/screen-slide.html
 */
public class PhotoActivity extends FragmentActivity {
    private static String TAG=PhotoActivity.class.getName();
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
    private PhotoViewPager mPager;
    private Timer timer;
    private int page = 0;


    private PhotoPagerAdapter photoPagerAdapter;
    private PhotoViewPager pager;

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
            Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
            // http://developer.android.com/training/system-ui/visibility.html
            /*
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
*/
        setContentView(R.layout.activity_photo);

        photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), this);
        pager = (PhotoViewPager)findViewById(R.id.photopager);
        pager.setAdapter(photoPagerAdapter);


        Bundle parameters = getIntent().getExtras();
        String albumFolder =parameters.getString("folderAbsolutePath");

        ArrayList<File> filelist = Utils.getAllFiles(albumFolder);

        photoPagerAdapter.setFileList(filelist);
        photoPagerAdapter.notifyDataSetChanged();

        numPages = filelist.size();
        pager.setCurrentItemManual(page);
// Don't start the slideshow        handler.postDelayed(runnable, SLIDE_SHOW_DELAY);
    }

    public void btnControlSlideshowClicked(View v) {
        Toast.makeText(this, "control slideshow clicked", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, SLIDE_SHOW_DELAY);
    }

    public void btnPhotoClicked(View v) {
        Toast.makeText(this, "Photo touched", Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnableSwiping:
                PlayPauseBtnHandler();
                break;
        }
    }
*/

/*
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
*/
}