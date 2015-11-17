package asbridge.me.uk.MPhoto.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TooManyListenersException;

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
/*
    public class CheckedFile {
        private File file;
        private boolean checked;

        public CheckedFile(File file)
        {
            this.file = file;
            this.checked = false;
        }
        public File getFile() { return this.file;}
        public boolean isChecked() { return this.checked; }
    }
    */

    static class ViewHolder {
        CheckBox checkbox;
        ImageView image;
    }

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

    public void clearSelection() {
        for (int i = 0 ; i < thumbnailsselection.length; i++)
            thumbnailsselection[i] = false;
    }

    public void selectAll() {
        for (int i = 0 ; i < thumbnailsselection.length; i++)
            thumbnailsselection[i] = true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_grid_item, null);
            holder = new ViewHolder();
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.imageCheckBox);
            holder.image = (ImageView) convertView.findViewById(R.id.image_grid_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkbox.setChecked(isImageSelected(position));
        holder.checkbox.setOnClickListener(new OnCheckBoxClickListener(position));
        Bitmap bMap = BitmapFactory.decodeFile(_files.get(position).getAbsolutePath());
        holder.image.setImageBitmap(bMap);
        return convertView;
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
