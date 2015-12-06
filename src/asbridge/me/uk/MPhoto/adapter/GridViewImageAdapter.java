package asbridge.me.uk.MPhoto.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.Classes.CheckedFile;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.Utils;

/**
 * Created by David on 10/11/2015.
 */
public class GridViewImageAdapter extends BaseAdapter  {

    static class ViewHolder {
        CheckBox checkbox;
        ImageView image;
        Button btnViewPhoto;
    }

    private Activity _context;
    private ArrayList<CheckedFile> _files;
    private String _albumFolder;
    private int albumMonth;
    private int albumYear;
    private String albumName;
    private String albumType;
    private long albumBucketID;
    private ArrayList<String> bucketIDStrings;

    public GridViewImageAdapter(Activity activity, ArrayList<CheckedFile> imageFiles, String albumFolder, int albumMonth, int albumYear, String albumName, String albumType, long albumBucketID, ArrayList<String> bucketIDStrings) {
        this._context = activity;
        this._files = imageFiles;
        this._albumFolder = albumFolder;
        this.albumMonth = albumMonth;
        this.albumYear = albumYear;
        this.albumName = albumName;
        this.albumType = albumType;
        this.albumBucketID = albumBucketID;
        this.bucketIDStrings = bucketIDStrings;
    }

    public interface ISelectionChangedEventListener {
        public void onSelectionChanged();
    }

    private ISelectionChangedEventListener mEventListener;

    public void setEventListener(ISelectionChangedEventListener mEventListener) {
        this.mEventListener = mEventListener;
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
        for (int i = 0 ; i < _files.size(); i++)
            _files.get(i).setChecked(false);
        mEventListener.onSelectionChanged();
    }

    public void selectAll() {
        for (int i = 0 ; i < _files.size(); i++)
            _files.get(i).setChecked(true);

        mEventListener.onSelectionChanged();

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
            holder.btnViewPhoto = (Button) convertView.findViewById(R.id.btnViewPhoto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkbox.setChecked(isImageSelected(position));
        holder.checkbox.setOnClickListener(new OnCheckBoxClickListener(position));
        holder.btnViewPhoto.setOnClickListener(new OnbtnViewPhotoClickListener(position, _albumFolder, albumMonth, albumYear, albumName, albumType, albumBucketID, bucketIDStrings));
        Bitmap bMap = Utils.decodeFileToThumbnail(_files.get(position).getFile());
        holder.image.setImageBitmap(bMap);

        return convertView;
    }

    public ArrayList<CheckedFile> getSelectedFiles() {
        ArrayList<CheckedFile> selectedFiles = new ArrayList<CheckedFile>();
        for (int i = 0; i < getCount(); i++) {
            if (isImageSelected(i)) {
                selectedFiles.add(_files.get(i));
            }
        }
        return selectedFiles;
    }

    public CheckedFile getImageFile(int position)
    {
        return _files.get(position);
    }

    public boolean isImageSelected(int position)
    {
        return _files.get(position).isChecked();
    }

    class OnbtnViewPhotoClickListener implements OnClickListener {

        int _position;
        String _albumFolder;
        int albumMonth;
        int albumYear;
        String albumName;
        String albumType;
        long albumBucketID;
        ArrayList<String> albumBucketIDStrings;

        // constructor
        public OnbtnViewPhotoClickListener(int position, String albumFolder, int albumMonth, int albumYear , String albumName, String albumType, long albumBucketID, ArrayList<String> bucketIDStrings) {
            this._position = position;
            this._albumFolder = albumFolder;
            this.albumMonth = albumMonth;
            this.albumYear = albumYear;
            this.albumName = albumName;
            this.albumType = albumType;
            this.albumBucketID = albumBucketID;
            this.albumBucketIDStrings = bucketIDStrings;
        }
        // on click listener for view button
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.btnViewPhoto) {
                File f = _files.get(_position).getFile();
                Intent intent = new Intent(_context, PhotoActivity.class);
                intent.putExtra("folderAbsolutePath", this._albumFolder);
                intent.putExtra("albumName",this.albumName);
                intent.putExtra("albumType",this.albumType);
                intent.putExtra("albumBucketID", albumBucketID);
                intent.putStringArrayListExtra("bucketIDs", albumBucketIDStrings);
                intent.putExtra("position", _position);
                intent.putExtra("month", albumMonth);
                intent.putExtra("year", albumYear);
                _context.startActivityForResult(intent,100);
            }
        }
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
           if (_files.get(_position).isChecked()){
               _files.get(_position).setChecked(false);
            } else {
               _files.get(_position).setChecked(true);
            }
            mEventListener.onSelectionChanged();
        }
    }
}
