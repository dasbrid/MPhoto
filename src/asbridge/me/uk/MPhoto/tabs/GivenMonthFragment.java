package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import asbridge.me.uk.MPhoto.Activities.CheckablePhotoGridActivity;
import asbridge.me.uk.MPhoto.Activities.SlideshowActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.controls.MonthControl;
import asbridge.me.uk.MPhoto.controls.NumberControl;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class GivenMonthFragment extends TabFragment {

    private static final String[] MONTHS = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private NumberControl ycYear;
    private MonthControl mcMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_given_month, container, false);
        ycYear= (NumberControl) v.findViewById(R.id.yearControlMonth);
        ycYear.setMinNumber(2011);
        ycYear.setMaxNumber(2018);
        Calendar c = Calendar.getInstance();
        ycYear.setNumber(c.get(Calendar.YEAR));

        mcMonth = (MonthControl) v.findViewById(R.id.monthControlMonth);

        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );
        // get the MONTH
        int month = mcMonth.getMonth();

        // get the Year
        int year = ycYear.getNumber();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), SlideshowActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "givenMonth");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        // do some shit
        Log.d("DAVE", "GivenYearFragment.viewAlbum" );
        // get the MONTH
        int month = mcMonth.getMonth();

        // get the Year
        int year = ycYear.getNumber();

        Intent intent;

        intent = new Intent(getActivity(), CheckablePhotoGridActivity.class);

        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in "+mcMonth.getMonthText() + " in " + year);
        intent.putExtra("albumType", "givenMonth");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }
}