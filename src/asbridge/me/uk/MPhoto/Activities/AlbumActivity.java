package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.DialogInterface;
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
public class AlbumActivity extends Activity implements View.OnClickListener {
    private Utils utils;
    private ArrayList<File> images = new ArrayList<File>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private String albumAbsolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        gridView = (GridView) findViewById(R.id.grid_view);

        utils = new Utils(this);

        // Initilizing Grid View
        InitilizeGridLayout();

        Bundle parameters = getIntent().getExtras();
        String albumFolder =parameters.getString("folderAbsolutePath");
        this.albumAbsolutePath = albumFolder;


        // Gridview adapter
        adapter = new GridViewImageAdapter(AlbumActivity.this, albumFolder, columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);

        Button btnEnableSwiping = (Button)findViewById(R.id.btnSelect);
        btnEnableSwiping.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelect:
                Toast.makeText(getApplicationContext(),
                        "You've selected Total " + " image(s).",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}