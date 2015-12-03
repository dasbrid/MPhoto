package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.AlbumsActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by David on 02/12/2015.
 */
public class ByBucketsFragment extends TabFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_by_buckets, container, false);

        Button button = (Button) v.findViewById(R.id.btnShowByBucket);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAlbums();
            }
        });

        return v;
    }

    public void showAlbums() {
        // start the albums activity
        Intent intent = new Intent(getActivity(), AlbumsActivity.class);
        this.startActivity(intent);
    }

    public void doSlideshow() {
    }

    public void viewAlbum() {
    }
}