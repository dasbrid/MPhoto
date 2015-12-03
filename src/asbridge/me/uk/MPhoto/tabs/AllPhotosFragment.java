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

/**
 * Created by David on 02/12/2015.
 */
public class AllPhotosFragment extends TabFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_photos, container, false);
        return v;
    }

    public void doSlideshow() {
        Log.d("DAVE", "GivenYearFragment.doSlideshow" );

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "allPhotos");
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
        intent.putExtra("albumName", "All Photos");
        intent.putExtra("albumType", "allPhotos");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", -1);
        this.startActivity(intent);
    }
}