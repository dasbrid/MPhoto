package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.controls.YearControl;

/**
 * Created by David on 02/12/2015.
 */
public class GivenYearFragment extends TabFragment {

    private YearControl ycYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_given_year, container, false);

        ycYear= (YearControl) v.findViewById(R.id.yearControl);
        ycYear.setMinYear(2011);
        ycYear.setMaxYear(2018);

        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );
        // get the Year
        int year = ycYear.getYear();

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
        int year = ycYear.getYear();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in " + year);
        intent.putExtra("albumType", "givenYear");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }
}