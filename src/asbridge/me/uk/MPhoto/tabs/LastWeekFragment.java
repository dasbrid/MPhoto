package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class LastWeekFragment extends TabFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_this_year, container, false);
        return v;
    }

    private Calendar getDateOneWeekAgo() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        return c;
    }

    public void doSlideshow() {
        Log.d("DAVE", "LastWeekFragment.doSlideshow" );
        Calendar c = getDateOneWeekAgo();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "fromDate");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        intent.putExtra("day", day);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        Log.d("DAVE", "LastWeekFragment.viewAlbum" );
        Calendar c = getDateOneWeekAgo();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        // start the slideshow activity
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Photos taken after " + day + "/" + (month+1) + "/" + year);
        intent.putExtra("albumType", "fromDate");
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        intent.putExtra("day", day);
        this.startActivity(intent);
    }
}