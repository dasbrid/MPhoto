package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private ArrayList<File> folders = new ArrayList<File>();
    private GridViewAlbumAdapter adapter;
    private GridView gridView;

    public void btnSlideshowClicked(View v) {
        Toast.makeText(this, "slideshow",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //
// Inflate the menu items to the action bar.
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

        gridView = (GridView) findViewById(R.id.grid_view);

        boolean fromMediaPreference = Utils.getFromMediaPreference(this);

        ArrayList<Album> albums;
        if (fromMediaPreference) {
            albums = Utils.getAlbumsFromMedia(this);
            Toast.makeText(this,"albums "+albums.size(),Toast.LENGTH_SHORT).show();

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
            albums = Utils.getAlbumsFromFolders(rootPhotosFolder);
            Toast.makeText(this,"folders "+albums.size(),Toast.LENGTH_SHORT).show();

        }

        /*
        folders = new ArrayList<File>();
        for(Album a : albums) {
            folders.add(a.getFolder().getParentFile());
        }

//        folders = Utils.getFolders(rootPhotosFolder);
*/
        if (albums  == null) {
            Toast.makeText(this,"folders is null",Toast.LENGTH_SHORT).show();
            return;
        }

        //adapter = new GridViewAlbumAdapter(AlbumsActivity.this, folders);
        adapter = new GridViewAlbumAdapter(AlbumsActivity.this, albums);
        gridView.setAdapter(adapter);

    }

}