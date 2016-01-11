package asbridge.me.uk.MPhoto.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.TabsAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.tabs.TabFragment;

/**
 * Created by David on 02/12/2015.
 */
public class PhotosByDateActivity extends FragmentActivity {

    private TabsAdapter tabsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosbydate);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagertabs);

        Button btnViewPhotos = (Button) findViewById(R.id.btnShowAlbum);
        if (AppConstant.ALLOW_VIEW_PHOTOS == false)
            btnViewPhotos.setVisibility(View.INVISIBLE);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        // http://developer.android.com/training/implementing-navigation/lateral.html
        final ActionBar actionBar = getActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
                // show the given tab
            }

            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < tabsAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(tabListener));
        }

        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });


        viewPager.setAdapter(tabsAdapter);

        //https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout
        //https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
        // Give the TabLayout the ViewPager
        android.support.design.widget.TabLayout tabLayout = (android.support.design.widget.TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
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