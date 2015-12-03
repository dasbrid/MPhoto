package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by David on 02/12/2015.
 */
public class YearFragment extends TabFragment {

    private NumberPicker npYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_year, container, false);
        npYear = (NumberPicker) v.findViewById(R.id.numberpickerYearYear);
        npYear.setMinValue(2011);
        npYear.setMaxValue(2016);
        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "YearFragment.doSlideshow" );
        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        // do some shit
        Log.d("DAVE", "YearFragment.viewAlbum" );

        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in " + year);
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }
}