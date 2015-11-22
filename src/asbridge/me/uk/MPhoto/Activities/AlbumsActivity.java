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

        String rootPhotosFolder = Utils.getRootPhotosFolder(this);

        if (!Utils.isAlbumColumnWidthSet(this))
        {
            Toast.makeText(this,"Album column width not set",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }

        int colWidth = Utils.getAlbumColumnWidth(this);
        if (rootPhotosFolder == null)
        {
            Toast.makeText(this,"photos folder null",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }
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

        folders = Utils.getFolders(rootPhotosFolder);
        if (folders == null) {
            Toast.makeText(this,"folders is null",Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new GridViewAlbumAdapter(AlbumsActivity.this, folders);
//        gridView.setColumnWidth(colWidth);
        gridView.setAdapter(adapter);
    }

}