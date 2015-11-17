package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.GridViewAlbumAdapter;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

import android.widget.GridView;

/**
 * Created by David on 10/11/2015.
 * Activity for showing all the albums
 */
public class AlbumsActivity extends Activity {
    private Utils utils;
    private ArrayList<File> folders = new ArrayList<File>();
    private GridViewAlbumAdapter adapter;
    private GridView gridView;
    private int columnWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        gridView = (GridView) findViewById(R.id.grid_view);

        // get all folders + all sub(and subsub) folders
        //TODO: use static method
        folders = Utils.getFolders();

        Toast.makeText(this,android.os.Environment.getExternalStorageDirectory()
                + File.separator + "MatthewsPhotos",Toast.LENGTH_LONG).show();
        // Gridview adapter
        adapter = new GridViewAlbumAdapter(AlbumsActivity.this, folders,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);
    }

}