package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Classes.CheckedFile;
import asbridge.me.uk.MPhoto.Classes.DeleteConfirmDialog;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.GridViewImageAdapter;
import asbridge.me.uk.MPhoto.adapter.PhotoGridAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 10/11/2015.
 * An Activity for viewing (and later editing/sharing) all the photos in an album
 */
public class PhotoGridActivity extends Activity
//        implements DeleteConfirmDialog.DeleteDialogOKListener
{
    private PhotoGridAdapter adapter;
    private GridView gridView;
    private String albumAbsolutePath;
    private ArrayList<CheckedFile> imageFiles;
    private String albumName;
    private int albumMonth;
    private int albumYear;
    private int albumDay;
    private String albumType;
    private long albumBucketID;
    private ArrayList<String> bucketIDstrings;
    private int numPhotos;

    private Button btnSelectPhotos;

    private boolean modified;

    private int clickMode;
    private static final int MODE_SELECT = 0;
    private static final int MODE_VIEW = 1;

    private final String TAG = "DAVE:PhotoGridActivity";
    // Called after starting or when resuming (no saved instance state)
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first

        if (modified) {
            Log.d(TAG,"modified");
            this.imageFiles.clear();
            ArrayList<File> files;

            // TOTD: Check OnResume !!! WHAT ABOUT ALL THE OTHER ALBUM TYPES !!!
            // CHECK THIS !!! WHAT ABOUT ALL THE OTHER ALBUM TYPES !!!!!!!!!!!!!!!!!
            // get all files (in this folder and in subfolders)
            files = Utils.getMediaInBucket(this, this.albumName);

            for (int i = 0; i < files.size(); i++) {
                this.imageFiles.add(new CheckedFile(files.get(i)));
            }
            adapter.notifyDataSetChanged();
        }

    }

    private void setButtonVisibility () {
        if (clickMode == MODE_VIEW) {
//            btnSelectPhotos.setVisibility(View.VISIBLE);
            btnSelectPhotos.setText("Select photos");
        } else {
//            btnSelectPhotos.setVisibility(View.INVISIBLE);
            btnSelectPhotos.setText("Cancel select");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setButtonVisibility();

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
        setContentView(R.layout.activity_photo_grid);
        gridView = (GridView) findViewById(R.id.photo_grid_view);

        clickMode = MODE_VIEW;

        btnSelectPhotos = (Button) findViewById(R.id.btnSelectPhotos);

        Bundle parameters = getIntent().getExtras();
        String albumFolder = parameters.getString("folderAbsolutePath");
        this.albumType = parameters.getString("albumType");
        this.albumName = parameters.getString("albumName");

        if (albumType.equals("bucket")) {
            this.albumBucketID = parameters.getLong("albumBucketID");
        } else if (albumType.equals("multipleBuckets")) {
            this.bucketIDstrings = parameters.getStringArrayList("bucketIDs");
        } else if (albumType.equals("lastNPhotos")) {
            this.numPhotos = parameters.getInt("numPhotos");
        }

        this.albumMonth = parameters.getInt("month");
        this.albumYear = parameters.getInt("year");
        if (this.albumType.equals("fromDate")) {
            this.albumDay = parameters.getInt("day");
        } else {
            this.albumDay = -1;
        }
        this.albumAbsolutePath = albumFolder;
        modified = false;

        getActionBar().setTitle(albumName);

        ArrayList<File> files;

        // files = Utils.getMediaInBucket(this, albumName);
        Log.d("DAVE", "displaying album for " + albumMonth +"/" + albumYear);

        if (albumType.equals("lastYear")) {
            files = Utils.getPhotosLastYear(this);
        } else if (albumType.equals("lastNPhotos")) {
            files = Utils.getLastNPhotosinMedia(this, numPhotos);
        } else if (albumType.equals("multipleBuckets")) {
            files = Utils.getMediaInListofBuckets(this, bucketIDstrings);
        } else if (albumType.equals("bucket")) {
            files = Utils.getMediaInBucketID(this, albumBucketID);
        } else if (albumType.equals("thisYear")) {
            files = Utils.getMediaInCurrentYear(this);
        } else if (albumType.equals("fromDate")) {
            files = Utils.getMediaFromDate(this,albumDay, albumMonth, albumYear);
        } else if (albumType.equals("allPhotos")) {
            // ALL files
            files = Utils.getAllMedia(this);
        } else if (albumMonth == -1 && albumYear != -1) {
            // Year but no month ... Get all for this year
            files = Utils.getMediaInYear(this, albumYear);
        } else if (albumMonth == -2 && albumYear == -2) {
            // Get RECENT files
            files = Utils.getRecentMedia(this);
        } else {
            // Year and month specified ... get for this month
            files = Utils.getMediaInMonth(this, albumMonth, albumYear);
        }


        this.imageFiles = new ArrayList<CheckedFile>();
        if (files != null && files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                this.imageFiles.add(new CheckedFile(files.get(i)));
            }
        } else {
            Toast.makeText(this, "No files found", Toast.LENGTH_SHORT).show();
            Button btnStartSlideshow = (Button)findViewById(R.id.btnStartSlideshow);
            btnStartSlideshow.setEnabled(false);
        }
        // Gridview adapter
        adapter = new PhotoGridAdapter(PhotoGridActivity.this, imageFiles, albumFolder, albumMonth, albumYear, albumName, albumType, albumBucketID, bucketIDstrings);
        // setting grid view adapter
        gridView.setAdapter(adapter);
/*
        adapter.setEventListener(new PhotoGridAdapter.ISelectionChangedEventListener() {

            @Override
            public void onSelectionChanged() {
                enableDisableButtons();
            }
        });

        enableDisableButtons();
*/
    }
/*
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
*/
/*
    // button share clicked. Share selected image
    public void btnShareClicked(View v) {
        ArrayList<CheckedFile> selectedFiles = adapter.getSelectedFiles();
        File fileToShare = selectedFiles.get(0).getFile();

        // many files
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> attachmentUris = new ArrayList<Uri>();

        for (CheckedFile checkedFile : selectedFiles)
        {

            Uri u = Uri.fromFile(checkedFile.getFile());
            attachmentUris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentUris);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Photos");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I hope you enjoy these photos");

        startActivity(Intent.createChooser(emailIntent, "Send mail:"));
    }

    // Delete dialog button clicked (callback)
    public void onDeleteDialogOK() {
        Toast.makeText(this, "Delete files", Toast.LENGTH_LONG).show();
        ArrayList<CheckedFile> selectedFiles = adapter.getSelectedFiles();
        File fileToDelete;
        for (int i = 0; i < selectedFiles.size(); i++) {
            fileToDelete = selectedFiles.get(i).getFile();
            // Only actually delete if deletion enabled
            if (AppConstant.ALLOW_DELETE) {
                // DON'T DELETE   fileToDelete.delete();
            }
            imageFiles.remove(selectedFiles.get(i));
        }
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
    }

     // button delete clicked. Delete selected images
    public void btnDeleteClicked(View v)
    {
        // show confirm dialog. OK will call back to OnDeleteDialogOK
        FragmentManager fm = getFragmentManager();
        DeleteConfirmDialog deleteDialog = new DeleteConfirmDialog();
        Bundle args = new Bundle();
        args.putString("title", "Delete Pictures");
        args.putString("message", "Are you sure you want to delete the selected pictures?");
        deleteDialog.setArguments(args);
        deleteDialog.show(fm, "fragment_delete_dialog");
    }
*/

    // button clicked, launch slideshow for this folder
    public void btnSelectPhotosClicked(View v)
    {
        Log.d("DAVE", "Select Photos");
        if (clickMode == MODE_SELECT)
            clickMode = MODE_VIEW;
        else
            clickMode = MODE_SELECT;
        setButtonVisibility();
    }

    public void startSlideshow() {

    }

    // button clicked, launch slideshow for this folder
    public void btnStartSlideshowClicked(View v)
    {
        if (clickMode == MODE_VIEW) {
        Log.d("DAVE", "start slideshow for type "+this.albumType+" name = "+this.albumName);
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("folderAbsolutePath", this.albumAbsolutePath);
        intent.putExtra("albumType", this.albumType);
        intent.putExtra("albumName", this.albumName);
        intent.putExtra("albumBucketID", this.albumBucketID);
        intent.putStringArrayListExtra("bucketIDs", this.bucketIDstrings);
        intent.putExtra("numPhotos", this.numPhotos);
        intent.putExtra("position", -1);
        intent.putExtra("month", this.albumMonth);
        intent.putExtra("year", this.albumYear);
        intent.putExtra("day", this.albumDay);
        this.startActivityForResult(intent,100);
        }
    }
/*
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
*/
}