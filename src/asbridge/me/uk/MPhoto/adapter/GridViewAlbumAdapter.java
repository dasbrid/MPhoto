package asbridge.me.uk.MPhoto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoSlideshowActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by David on 10/11/2015.
 * FOr displaying a grid of ALBUMS
 */
public class GridViewAlbumAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<File> _folders = new ArrayList<File>();

    public GridViewAlbumAdapter(Context context, ArrayList<File> folders) {
        this._context = context;
        this._folders = folders;
    }

    @Override
    public int getCount() {
        return this._folders.size();
    }

    @Override
    public Object getItem(int position) {
        return this._folders.get(position);
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
            gridView = inflater.inflate( R.layout.album_grid_item , null);


            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            // image is the first image in the folder
            File folder = _folders.get(position);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(_folders.get(position).getName());

            File imageFile = Utils.getFirstImageInFolder(folder);

            if (imageFile == null)
            {
                Toast.makeText(_context, "imageFile is null",Toast.LENGTH_SHORT).show();
            }
            if (imageFile.canRead() == false)
            {
                Toast.makeText(_context, "imageFile cannot read",Toast.LENGTH_SHORT).show();
            }
            //TODO: what if folder is empty (no images) or not actually a folder
            Bitmap bMap = Utils.decodeFileToThumbnail(imageFile);
            //Bitmap bMap = BitmapFactory.decodeFile(imageFile.getAbsolutePath()); // out of memory

            if (bMap == null)
            {
                Toast.makeText(_context, "bmap is null",Toast.LENGTH_SHORT).show();
                return gridView;
            }

            imageView.setImageBitmap(bMap);

            Button btnSlideshow = (Button) gridView.findViewById(R.id.btnSlideshow);
            btnSlideshow.setOnClickListener(new OnSlideshowButtonClickListener(position));

            Button btnAlbum = (Button) gridView.findViewById(R.id.btnAlbum);
            btnAlbum.setOnClickListener(new OnAlbumButtonClickListener(position));
        } else {
            gridView = convertView;
        }
        return gridView;
    }


    class OnAlbumButtonClickListener implements OnClickListener {

        int _position;

        // constructor
        public OnAlbumButtonClickListener(int position) {
            this._position = position;
        }
        @Override
        public void onClick(View v) {
            // button clicked, launch slideshow for this folder
            File folder = _folders.get(_position);
            Intent intent = new Intent(_context, AlbumActivity.class);
            intent.putExtra("folderAbsolutePath", folder.getAbsolutePath());
            _context.startActivity(intent);
        }
    }

    class OnSlideshowButtonClickListener implements OnClickListener {

        int _position;

        // constructor
        public OnSlideshowButtonClickListener(int position) {
            this._position = position;
        }
        @Override
        public void onClick(View v) {
            // button clicked, launch slideshow for this folder
            File folder = _folders.get(_position);
            Intent intent = new Intent(_context, PhotoActivity.class);
            intent.putExtra("folderAbsolutePath", folder.getAbsolutePath());
            intent.putExtra("position", -1);
            _context.startActivity(intent);
        }
    }



}
