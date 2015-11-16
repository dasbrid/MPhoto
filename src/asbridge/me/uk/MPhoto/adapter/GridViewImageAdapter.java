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
    private ArrayList<File> _files = new ArrayList<File>();
    private boolean[] thumbnailsselection;
    private int imageWidth;
    private String albumAbsolutePath;

    public GridViewImageAdapter(Activity activity, String albumAbsolutePath, int imageWidth) {
        this._context = activity;
        this.albumAbsolutePath = albumAbsolutePath;
        // get all files (in this folder and in subfolders)
        this._files = Utils.getAllFiles(albumAbsolutePath);
        thumbnailsselection = new boolean[getCount()];
        this.imageWidth = imageWidth;
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
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        // get screen dimensions
        Bitmap image = decodeFile(_files.get(position).getAbsolutePath(), imageWidth,
                imageWidth);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        imageView.setImageBitmap(image);

        // image view click listener
        imageView.setOnClickListener(new OnImageClickListener(position));

        return imageView;
    }
*/
    /*
    class OnImageClickListener implements OnClickListener {

        int _position;

        // constructor
        public OnImageClickListener(int position) {
            this._position = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
//            File folder = _files.get(_position);
            Intent intent = new Intent(_activity, PhotoSlideshowActivity.class);
            intent.putExtra("position", _position);
            intent.putExtra("folderAbsolutePath", albumAbsolutePath);
            _activity.startActivity(intent);
        }

    }
*/
    /*
     * Resizing image size
     */
    /*
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
}
