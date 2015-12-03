package asbridge.me.uk.MPhoto.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.TabsAdapter;
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
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
    }

    public void btnShowSlideshowClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.doSlideshow();
        /*
        // get the MONTH
        String spunMonth = spinnerMonth.getSelectedItem().toString();
        int month = spinnerMonth.getSelectedItemPosition();

        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        this.startActivity(intent);
*/
    }

    public void btnShowAlbumClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.viewAlbum();
        /*
        // get the MONTH
        String spunMonth = spinnerMonth.getSelectedItem().toString();
        int month = spinnerMonth.getSelectedItemPosition();

        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in Month "+(month+1) + " in " + year);
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        this.startActivity(intent);
        */
    }

}