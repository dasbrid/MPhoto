package asbridge.me.uk.MPhoto.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.TabsAdapter;
import asbridge.me.uk.MPhoto.controls.PageIndicator;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.tabs.TabFragment;

/**
 * Created by David on 02/12/2015.
 */
public class PhotosByDateActivity extends FragmentActivity {

    private static final String TAG = "PhotosByDateActivity";

    private TabsAdapter tabsAdapter;
    private PageIndicator pageIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosbydate);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagertabs);
        //http://www.androidbegin.com/tutorial/android-viewpagertabstrip-fragments-tutorial/

        Button btnViewPhotos = (Button) findViewById(R.id.btnShowAlbum);
        if (AppConstant.ALLOW_VIEW_PHOTOS == false)
            btnViewPhotos.setVisibility(View.INVISIBLE);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsAdapter);
        //https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout
        //https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
        // Give the TabLayout the ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                pageIndicator.changePage(position);
            }

        });
        pageIndicator = (PageIndicator) findViewById(R.id.pageindicator);
        pageIndicator.setNumButtons(6);
    }

    public void btnShowSlideshowClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.doSlideshow();
    }

    public void btnShowAlbumClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.viewAlbum();
   }

}