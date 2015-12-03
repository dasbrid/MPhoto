package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by David on 02/12/2015.
 */
public class GivenMonthFragment extends TabFragment {

    private static final String[] MONTHS = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private Spinner spinnerMonth;
        private NumberPicker npYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_given_month, container, false);
        npYear = (NumberPicker) v.findViewById(R.id.numberpickerYearMonth);
        npYear.setMinValue(2011);
        npYear.setMaxValue(2016);

        spinnerMonth = (Spinner) v.findViewById(R.id.spinnerMonthMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, MONTHS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );
        // get the MONTH
        String spunMonth = spinnerMonth.getSelectedItem().toString();
        int month = spinnerMonth.getSelectedItemPosition();

        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
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
        String spunMonth = spinnerMonth.getSelectedItem().toString();
        int month = spinnerMonth.getSelectedItemPosition();

        // get the Year
        int year = npYear.getValue();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken in Month "+(month+1) + " in " + year);
        intent.putExtra("albumType", "givenMonth");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        this.startActivity(intent);
    }
}