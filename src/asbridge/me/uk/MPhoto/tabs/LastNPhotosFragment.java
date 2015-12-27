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

/**
 * Created by David on 02/12/2015.
 */
public class LastNPhotosFragment extends TabFragment {

    private NumberControl ncLastNControl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_last_n_photos, container, false);

        ncLastNControl = (NumberControl) v.findViewById(R.id.ncLastNControl);
        ncLastNControl.setMinNumber(10);
        ncLastNControl.setMaxNumber(100);
        ncLastNControl.setNumber(25);
        return v;
    }

    public void doSlideshow() {
        // get the Year
        int numPhotos = ncLastNControl.getNumber();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumType", "lastNPhotos");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", -1);
        intent.putExtra("numPhotos", numPhotos);
        this.startActivity(intent);
    }

    public void viewAlbum() {
        // get the Year
        int numPhotos = ncLastNControl.getNumber();

        // start the slideshow activity
        Intent intent = new Intent(getActivity(), PhotoGridActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");
        intent.putExtra("albumName", "Most recent " + numPhotos + " photos");
        intent.putExtra("albumType", "lastNPhotos");
        intent.putExtra("position", -1);
        intent.putExtra("month", -1);
        intent.putExtra("year", -1);
        intent.putExtra("numPhotos", numPhotos);
        this.startActivity(intent);
    }
}