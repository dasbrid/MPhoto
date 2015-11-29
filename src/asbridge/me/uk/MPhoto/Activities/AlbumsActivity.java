package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.Album;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.GridViewAlbumAdapter;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

import android.widget.GridView;
import asbridge.me.uk.MPhoto.settings.SettingsActivity;
import android.view.MenuItem;
/**
 * Created by David on 10/11/2015.
 * Activity for showing all the albums
 */
public class AlbumsActivity extends Activity {
    private ArrayList<Album> albums = new ArrayList<Album>();
    private GridViewAlbumAdapter adapter;
    private GridView gridView;
    // stores whether we are currently loading from media or folders
    // used to detect change in this setting on resume
    private boolean fromMediaPreference;
    // Called after starting or when resuming (no saved instance state)
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first

        // If preferences have changed (or Creating activity)
        if (Utils.getFromMediaPreference(this) == fromMediaPreference)
            return; // no change in preference

        // change in preference ... reload the albums
        fromMediaPreference = Utils.getFromMediaPreference(this); // change the stored preference
        ArrayList<Album> listOfAlbums = new ArrayList<Album>();
        if (fromMediaPreference) {
            listOfAlbums = Utils.getAlbumsFromMedia(this);
        } else {
            String rootPhotosFolder = Utils.getRootPhotosFolder(this);

            if (rootPhotosFolder == "")
            {
                Toast.makeText(this,"photos folder is empty string",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            }

            if (!new File(rootPhotosFolder).isDirectory()) {
                Toast.makeText(this,"photos root folder is not a folder",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            }

            listOfAlbums = Utils.getAlbumsFromFolders(rootPhotosFolder);

        }

        if (listOfAlbums  == null) {
            Toast.makeText(this,"folders is null",Toast.LENGTH_SHORT).show();
            return;
        }

        albums.clear();
        for (Album a : listOfAlbums) {
            albums.add(a);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //
        getMenuInflater().inflate(R.menu.menu_albums, menu); //
        return true;
    }

    // Called every time user clicks on an menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        Toast.makeText(this,"onCreate",Toast.LENGTH_LONG).show();
        gridView = (GridView) findViewById(R.id.grid_view);

        // next line will trigger reading of files in onCreate
        fromMediaPreference = !Utils.getFromMediaPreference(this);

        adapter = new GridViewAlbumAdapter(AlbumsActivity.this, albums);
        gridView.setAdapter(adapter);

    }

}