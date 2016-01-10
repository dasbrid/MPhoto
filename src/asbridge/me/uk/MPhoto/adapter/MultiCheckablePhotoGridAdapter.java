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

    static class ViewHolder {
        ImageView image;
        CheckableLinearLayout layout;
    }

    private Activity _context;
    private ArrayList<File> _files;
    /*
    private String _albumFolder;
    private int albumMonth;
    private int albumYear;
    private String albumName;
    private String albumType;
    private long albumBucketID;
    private ArrayList<String> bucketIDStrings;
    */


    public MultiCheckablePhotoGridAdapter(Activity activity, ArrayList<File> imageFiles /*, String albumFolder, int albumMonth, int albumYear, String albumName, String albumType, long albumBucketID, ArrayList<String> bucketIDStrings*/) {
        this._context = activity;
        this._files = imageFiles;
/*
        this._albumFolder = albumFolder;
        this.albumMonth = albumMonth;
        this.albumYear = albumYear;
        this.albumName = albumName;
        this.albumType = albumType;
        this.albumBucketID = albumBucketID;
        this.bucketIDStrings = bucketIDStrings;
*/
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
        imageDownloader.download(_files.get(position).getAbsolutePath(), (ImageView) holder.image);

        return convertView;
    }

    public File getImageFile(int position)
    {
        return _files.get(position);
    }






}
