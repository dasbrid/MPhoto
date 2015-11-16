package asbridge.me.uk.MPhoto.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import asbridge.me.uk.MPhoto.Activities.PhotoSlideshowActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.Utils;

/**
 * Created by David on 10/11/2015.
 */
public class GridViewImageAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<File> _files;
    private boolean[] thumbnailsselection;


    public GridViewImageAdapter(Activity activity, ArrayList<File> imageFiles) { //}, String albumAbsolutePath) {
        this._context = activity;
        this._files = imageFiles; // Utils.getAllFiles(albumAbsolutePath);
        thumbnailsselection = new boolean[getCount()];
    }

    @Override
    public int getCount() {
        return this._files.size();
    }

    @Override
    public Object getItem(int position) {
        return this._files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate( R.layout.image_grid_item , null);
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.image_grid_item_image);

            Bitmap bMap = BitmapFactory.decodeFile(_files.get(position).getAbsolutePath());
            imageView.setImageBitmap(bMap);

            CheckBox checkbox = (CheckBox) gridView.findViewById(R.id.imageCheckBox);
            checkbox.setId(position);
            checkbox.setOnClickListener(new OnCheckBoxClickListener(position));
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }

    public ArrayList<File> getSelectedFiles() {
        ArrayList<File> selectedFiles = new ArrayList<File>();
        for (int i = 0; i < getCount(); i++) {
            if (isImageSelected(i)) {
                selectedFiles.add(_files.get(i));
            }
        }
        return selectedFiles;
    }

    public File getImageFile(int position)
    {
        return _files.get(position);
    }

    public boolean isImageSelected(int position)
    {
        return thumbnailsselection[position];
    }

    class OnCheckBoxClickListener implements OnClickListener {

        int _position;

        // constructor
        public OnCheckBoxClickListener(int position) {
            this._position = position;
        }
        @Override
        public void onClick(View v) {
            // checkbox clicked
           if (thumbnailsselection[_position]){
                thumbnailsselection[_position] = false;
            } else {
                thumbnailsselection[_position] = true;
            }
        }
    }
}
