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

/**
 * Created by David on 02/12/2015.
 */
public class ThisYearFragment extends TabFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_this_year, container, false);
        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "thisYear");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", -1);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        Log.d("DAVE", "GivenYearFragment.viewAlbum" );

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "All Photos from this year");
        intent.putExtra("albumType", "thisYear");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", -1);
        this.startActivity(intent);
    }
}