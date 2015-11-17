package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.GridViewImageAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 10/11/2015.
 * An Activity for viewing (and later editing/sharing) all the photos in an album
 */
public class AlbumActivity extends Activity {
    private Utils utils;
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private String albumAbsolutePath;
    private ArrayList<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        gridView = (GridView) findViewById(R.id.grid_view);

        utils = new Utils(this);

        // Initilizing Grid View
//        InitilizeGridLayout();

        Bundle parameters = getIntent().getExtras();
        String albumFolder =parameters.getString("folderAbsolutePath");
        this.albumAbsolutePath = albumFolder;

        // get all files (in this folder and in subfolders)
        this.imageFiles = Utils.getAllFiles(albumAbsolutePath);

        // Gridview adapter
        adapter = new GridViewImageAdapter(AlbumActivity.this, imageFiles); //albumFolder);//, columnWidth);
        // setting grid view adapter
        gridView.setAdapter(adapter);
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    // button delete clicked. Delete selected images
    public void btnDeleteClicked(View v)
    {
        ArrayList<File> selectedFiles = adapter.getSelectedFiles();

        String msg = " delete files ";
        File fileToDelete;
        for (int i = 0; i < selectedFiles.size(); i++) {
            msg += selectedFiles.get(i).getName() + ",";
            fileToDelete = selectedFiles.get(i);
/// DONT DELETE            fileToDelete.delete();
            this.imageFiles.remove(fileToDelete);
        }
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
// NOT NEEDED       gridView.invalidateViews();
    }

    // button clicked, launch slideshow for this folder
    public void btnStartSlideshowClicked(View v)
    {
        Intent intent = new Intent(this, PhotoSlideshowActivity.class);
        intent.putExtra("folderAbsolutePath", this.albumAbsolutePath);
        this.startActivity(intent);
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