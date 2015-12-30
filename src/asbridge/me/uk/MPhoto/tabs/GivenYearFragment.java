package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoGridActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.controls.NumberControl;
import asbridge.me.uk.MPhoto.helper.AppConstant;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class GivenYearFragment extends TabFragment {

    private NumberControl ycYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_given_year, container, false);

        ycYear= (NumberControl) v.findViewById(R.id.yearControl);
        ycYear.setMinNumber(2011);
        ycYear.setMaxNumber(2018);
        Calendar c = Calendar.getInstance();
        ycYear.setNumber(c.get(Calendar.YEAR));

        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );
        // get the Year
        int year = ycYear.getNumber();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "givenYear");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        // do some shit
        Log.d("DAVE", "GivenYearFragment.viewAlbum" );

        // get the Year
        int year = ycYear.getNumber();

        Intent intent;
        if (AppConstant.USE_PHOTO_GRID_ACTIVITY)
            intent = new Intent(getActivity(), PhotoGridActivity.class);
        else
            intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in " + year);
        intent.putExtra("albumType", "givenYear");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }
}