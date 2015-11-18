package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
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
    private int columnWidth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //
// Inflate the menu items to the action bar.
        getMenuInflater().inflate(R.menu.menu_albums, menu); //
        return true;
    }

    // Called every time user clicks on an action
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

        // get all folders + all sub(and subsub) folders

        String rootPhotosFolder = Utils.getRootPhotosFolder(this);
        if (rootPhotosFolder == "")
        {
            Toast.makeText(this,"root folder not set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
        }
        //rootPhotosFolder = android.os.Environment.getExternalStorageDirectory()                + File.separator + "MatthewsPhotos";
        folders = Utils.getFolders(rootPhotosFolder);

        Toast.makeText(this,rootPhotosFolder,Toast.LENGTH_LONG).show();
        // Gridview adapter
        adapter = new GridViewAlbumAdapter(AlbumsActivity.this, folders);

        // setting grid view adapter
        gridView.setAdapter(adapter);
    }

}