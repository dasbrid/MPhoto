package asbridge.me.uk.MPhoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import asbridge.me.uk.MPhoto.Activities.SlideshowActivity;
import asbridge.me.uk.MPhoto.Classes.CheckedFile;
import asbridge.me.uk.MPhoto.Classes.ImageDownloader;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.controls.CheckableLinearLayout;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 10/11/2015.
 */
public class MultiCheckablePhotoGridAdapter extends BaseAdapter  {

    private final static String TAG = "PhotoGridAdapter";

    private int gridClickMode;

    public static class GridClickMode {
        public static final int MODE_SELECT = 0;
        public static final int MODE_VIEW = 1;
    }

    static class ViewHolder {
        CheckBox checkbox;
        ImageView image;
        CheckableLinearLayout layout;
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

    public void setGridClickMode(int gcm) {
        gridClickMode = gcm;
    }
    public int getGridClickMode() {
        return gridClickMode;
    }

    public MultiCheckablePhotoGridAdapter(Activity activity, ArrayList<CheckedFile> imageFiles, String albumFolder, int albumMonth, int albumYear, String albumName, String albumType, long albumBucketID, ArrayList<String> bucketIDStrings) {
        this._context = activity;
        this._files = imageFiles;
        this._albumFolder = albumFolder;
        this.albumMonth = albumMonth;
        this.albumYear = albumYear;
        this.albumName = albumName;
        this.albumType = albumType;
        this.albumBucketID = albumBucketID;
        this.bucketIDStrings = bucketIDStrings;

        gridClickMode = GridClickMode.MODE_SELECT;
    }

    public interface ISelectionChangedEventListener {
        void onSelectionChanged(int numItemsSelected);
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

    private final ImageDownloader imageDownloader = new ImageDownloader();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            Log.d(TAG, "getView " + position + " convertView == null");
            convertView = inflater.inflate(R.layout.checkable_photo_grid_item, null);
            holder = new ViewHolder();
            holder.layout = (CheckableLinearLayout) convertView.findViewById(R.id.layout);
            holder.image = (ImageView) convertView.findViewById(R.id.photo_grid_item_image);
            convertView.setTag(holder);
        } else {
            Log.d(TAG, "getView " + position + " convertView != null");
            holder = (ViewHolder) convertView.getTag();
        }
        // http://android-developers.blogspot.co.uk/2010/07/multithreading-for-performance.html
        imageDownloader.download(_files.get(position).getFile().getAbsolutePath(), (ImageView) holder.image);
        holder.layout.setChecked(_files.get(position).isChecked());
        // onclick for the image
//        holder.layout.setOnClickListener(new OnbtnViewPhotoClickListener(position, _albumFolder, albumMonth, albumYear, albumName, albumType, albumBucketID, bucketIDStrings));

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

    public void clearSelection() {
        for (int i = 0 ; i < _files.size(); i++)
            _files.get(i).setChecked(false);
        notifyDataSetChanged();
        if (mEventListener != null)
            mEventListener.onSelectionChanged(0);
    }

    public void selectAll() {
        for (int i = 0 ; i < _files.size(); i++)
            _files.get(i).setChecked(true);
        notifyDataSetChanged();
        if (mEventListener != null)
            mEventListener.onSelectionChanged(_files.size());

    }

    public int getNumSelectedItems() {
        int count = 0;
        for (CheckedFile cf : _files ) {
            if (cf.isChecked()) count++;
        }
        return count;
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
            if (gridClickMode == GridClickMode.MODE_SELECT) {
                boolean checked = ((CheckableLinearLayout) v).isChecked();
                ((CheckableLinearLayout) v).setChecked(!checked);
                _files.get(_position).setChecked(!checked);
                if (mEventListener != null) {
                    mEventListener.onSelectionChanged(getNumSelectedItems());
                }
            } else if (gridClickMode == GridClickMode.MODE_VIEW) {
                File f = _files.get(_position).getFile();
                Intent intent = new Intent(_context, SlideshowActivity.class);
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
}
