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
public class PhotoActivity extends FragmentActivity implements PhotoViewPager.OnTouchedListener {

    private int numPages;

    private boolean slideshowOn;
    private boolean slideshowSharedState;

    // The time (in seconds) for which the navigation controls are visible after screen interaction.
    private static int SHOW_NAVIGATION_CONTROLS_TIME = 3;
    // Delay between slides
    private static int SLIDE_SHOW_DELAY;

    private Timer timer;
    private int page = 0;

    private Button btnStartSlideshow;

    private PhotoPagerAdapter photoPagerAdapter;
    private PhotoViewPager pager;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (slideshowOn == false)
                return;
            // TODO: don't keep the page here, use a method on the adapter or pager or whatever e.g. showNextPage()
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

    // button clicked - restart the slideshow
    public void btnPhotoStartSlideshowClicked(View v) {
        btnStartSlideshow.setVisibility(View.INVISIBLE);
        page = pager.getCurrentItem();
        startSlideshow();
    }

    // called back from the pager when touched
    public void onTouched() {
        stopSlideshow();
        btnStartSlideshow.setVisibility(View.VISIBLE);
    }

    private void stopSlideshow() {
        slideshowOn = false;
    }

    private void startSlideshow() {
        slideshowOn = true;
        handler.postDelayed(runnable, SLIDE_SHOW_DELAY);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);        // Save the slideshow status
        savedInstanceState.putBoolean("slideshowOn", slideshowOn);
        savedInstanceState.putInt("currentPage", pager.getCurrentItem());
    }

    @Override
    public void onStop() {
        super.onStop();
        slideshowSharedState= slideshowOn;
        slideshowOn = false;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        slideshowSharedState = savedInstanceState.getBoolean("slideshowOn");
        page = savedInstanceState.getInt("currentPage");
    }

    // Called after starting or when resuming (no saved instance state)
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        // get the saved (in memory state of the slideshow)
        slideshowOn = slideshowSharedState;
        pager.setCurrentItemManual(page);
        Toast.makeText(this,"onResume set page to = "+page, Toast.LENGTH_SHORT).show();

        if (slideshowOn) {
            startSlideshow();
            btnStartSlideshow.setVisibility(View.INVISIBLE);
        } else {
            btnStartSlideshow.setVisibility(View.VISIBLE);
        }
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

        // if we are starting for first time (not restarting)
        if (savedInstanceState == null) {
            slideshowSharedState = true;
            slideshowOn = true;
        }

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
        btnStartSlideshow = (Button) findViewById(R.id.btnPhotoStartSlideshow);
        photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), this);
        pager = (PhotoViewPager)findViewById(R.id.photopager);
        pager.setAdapter(photoPagerAdapter);
        pager.setOnTouchedListener(this);

        Bundle parameters = getIntent().getExtras();
        String albumFolder = parameters.getString("folderAbsolutePath");
        Integer positionParameter = parameters.getInt("position");
        Toast.makeText(this,"position = "+positionParameter == null? "null":positionParameter.toString()+" path="+albumFolder, Toast.LENGTH_SHORT).show();

        ArrayList<File> filelist = Utils.getAllFiles(albumFolder);

        photoPagerAdapter.setFileList(filelist);
        photoPagerAdapter.notifyDataSetChanged();

        numPages = filelist.size();
    }


/*
    public void btnPhotoClicked(View v) {
        Toast.makeText(this, "Photo touched", Toast.LENGTH_SHORT).show();
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