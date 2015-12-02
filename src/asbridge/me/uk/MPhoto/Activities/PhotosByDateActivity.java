package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.TabsAdapter;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class PhotosByDateActivity extends FragmentActivity {

    private static final String[] MONTHS = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private Spinner spinnerMonth;
    private NumberPicker npYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosbydate);

        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MONTHS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        npYear = (NumberPicker) findViewById(R.id.numberpickerYear);
        npYear.setMinValue(2011);
        npYear.setMaxValue(2016);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), 2 /*number of tabs*/);
        viewPager.setAdapter(tabsAdapter);
    }

    public void btnShowSlideshowClicked(View v) {
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

    }

    public void btnShowAlbumClicked(View v) {
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
    }

}