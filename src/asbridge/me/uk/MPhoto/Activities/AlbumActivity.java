package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.CheckedFile;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.GridViewImageAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.helper.Utils;
import asbridge.me.uk.MPhoto.settings.SettingsActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 10/11/2015.
 * An Activity for viewing (and later editing/sharing) all the photos in an album
 */
public class AlbumActivity extends Activity {
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private String albumAbsolutePath;
    private ArrayList<CheckedFile> imageFiles;
    private String albumname;
    private boolean modified;

    // Called after starting or when resuming (no saved instance state)
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        if (modified) {
            this.imageFiles.clear();
            ArrayList<File> files;
            if (Utils.getFromMediaPreference(this)) {
                // get all files (in this folder and in subfolders)
                files = Utils.getMediaInBucket(this, this.albumname);
            } else {
                files = Utils.getAllFiles(albumAbsolutePath);
            }
            for (int i = 0; i < files.size(); i++) {
                this.imageFiles.add(new CheckedFile(files.get(i)));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 100)
        {
            if (resultCode == RESULT_OK) {
                // The activity ended sucessfully
                // The Intent's data Uri identifies which contact was selected.
                modified = resultData.getBooleanExtra("modified", false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toast.makeText(this,"onCreate",Toast.LENGTH_LONG).show();
        gridView = (GridView) findViewById(R.id.grid_view);

        Bundle parameters = getIntent().getExtras();
        String albumFolder =parameters.getString("folderAbsolutePath");
        this.albumAbsolutePath = albumFolder;
        modified = false;
        if (albumAbsolutePath == null)
        {
            Toast.makeText(this,"photos folder null",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }
        if (albumAbsolutePath == "")
        {
            Toast.makeText(this,"photos folder is empty string",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }

        if (!new File(albumAbsolutePath).isDirectory()) {
            Toast.makeText(this,"photos root folder is not a folder",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }

        this.albumname = new File (albumAbsolutePath).getName();
        getActionBar().setTitle(albumname);

        ArrayList<File> files;
        if (Utils.getFromMediaPreference(this)) {
            // get all files (in this folder and in subfolders)
            files = Utils.getMediaInBucket(this, albumname);
        } else {
            files = Utils.getAllFiles(albumAbsolutePath);
        }

        this.imageFiles = new ArrayList<CheckedFile>();
        for (int i=0;i<files.size();i++)
        {
            this.imageFiles.add(new CheckedFile(files.get(i)));
        }

        // Gridview adapter
        adapter = new GridViewImageAdapter(AlbumActivity.this, imageFiles, albumFolder); //albumFolder);//, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);

        adapter.setEventListener(new GridViewImageAdapter.ISelectionChangedEventListener() {

            @Override
            public void onSelectionChanged() {
                enableDisableButtons();
            }
        });

        enableDisableButtons();
    }

    private void enableDisableButtons()
    {
        int numImages = adapter.getCount();
        int numSelected = adapter.getSelectedFiles().size();

        Button btnSelectNone = (Button) findViewById(R.id.btnSelectNone);
        Button btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnShare = (Button) findViewById(R.id.btnShare);

        btnSelectNone.setEnabled(numSelected != 0);
        btnDelete.setEnabled(numSelected != 0);
        btnShare.setEnabled(numSelected != 0);
        btnSelectAll.setEnabled(numImages != numSelected);
    }


    // button share clicked. Share selected image
    public void btnShareClicked(View v) {
        ArrayList<CheckedFile> selectedFiles = adapter.getSelectedFiles();
        File fileToShare = selectedFiles.get(0).getFile();

        // many files
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> attachmentUris = new ArrayList<Uri>();

        for (CheckedFile checkedFile : selectedFiles)
        {

            Uri u = Uri.fromFile(checkedFile.getFile());
            attachmentUris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentUris);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Photos");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I hope you enjoy these photos");

        startActivity(Intent.createChooser(emailIntent, "Send mail:"));
    }

     // button delete clicked. Delete selected images
    public void btnDeleteClicked(View v)
    {
        ArrayList<CheckedFile> selectedFiles = adapter.getSelectedFiles();

        File fileToDelete;
        for (int i = 0; i < selectedFiles.size(); i++) {
            fileToDelete = selectedFiles.get(i).getFile();
            // Only actually delete if deletion enabled
            if (AppConstant.ALLOW_DELETE)
                fileToDelete.delete();
            this.imageFiles.remove(selectedFiles.get(i));
        }
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
    }

    // button clicked, launch slideshow for this folder
    public void btnStartSlideshowClicked(View v)
    {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", this.albumAbsolutePath);
        intent.putExtra("position", -1);
        this.startActivityForResult(intent,100);
    }

    public void btnSelectAllClicked(View v)
    {
        adapter.selectAll();
        adapter.notifyDataSetChanged();
    }


    public void btnSelectNoneClicked(View v)
    {
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
    }

}