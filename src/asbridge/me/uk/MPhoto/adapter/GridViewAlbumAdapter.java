package asbridge.me.uk.MPhoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.Classes.Album;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 10/11/2015.
 * For displaying a grid of ALBUMS
 */
public class GridViewAlbumAdapter extends BaseAdapter {

    static class AlbumViewHolder {
        TextView gridItemLabel;
        ImageView image;
        Button btnSlideshow;
        Button btnAlbum;
    }


    private Context _context;
//    private ArrayList<File> _folders = new ArrayList<File>();
    private ArrayList<Album> _albums = new ArrayList<Album>();

    public GridViewAlbumAdapter(Context context, ArrayList<Album> albums) {
        this._context = context;
        this._albums = albums;
    }

    @Override
    public int getCount() {
        return this._albums.size();
    }

    @Override
    public Object getItem(int position) {
        return this._albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlbumViewHolder holder;
        //View gridView;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.album_grid_item , null);
            holder = new AlbumViewHolder();
            holder.btnAlbum = (Button) convertView.findViewById(R.id.btnAlbum);
            holder.image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            holder.btnSlideshow = (Button) convertView.findViewById(R.id.btnSlideshow);
            holder.gridItemLabel = (TextView) convertView.findViewById(R.id.grid_item_label);
            convertView.setTag(holder);
        } else {
            //gridView = convertView;
            holder = (AlbumViewHolder) convertView.getTag();
        }

//        ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

        Album album = _albums.get(position);

        //TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
        holder.gridItemLabel.setText(album.getName());

        File imageFile;
        imageFile = album.getFirstFile();
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
        if (bMap == null)
        {
            Toast.makeText(_context, "bmap is null",Toast.LENGTH_SHORT).show();
            return convertView;
        }

        holder.image.setImageBitmap(bMap);

        //Button btnSlideshow = (Button) gridView.findViewById(R.id.btnSlideshow);
        holder.btnSlideshow.setOnClickListener(new OnSlideshowButtonClickListener(position));

        //Button btnAlbum = (Button) gridView.findViewById(R.id.btnAlbum);
        holder.btnAlbum.setOnClickListener(new OnAlbumButtonClickListener(position));
        return convertView;
    }


    class OnAlbumButtonClickListener implements OnClickListener {

        int _position;

        public OnAlbumButtonClickListener(int position) {
            this._position = position;
        }
        @Override
        public void onClick(View v) {
            Album album = _albums.get(_position);
            Intent intent = new Intent(_context, AlbumActivity.class);
            intent.putExtra("folderAbsolutePath", album.getFolder().getAbsolutePath());
            intent.putExtra("albumName", album.getName());
            intent.putExtra("month", album.getMonth());
            intent.putExtra("year", album.getYear());
            _context.startActivity(intent);
        }
    }

    class OnSlideshowButtonClickListener implements OnClickListener {

        int _position;

        public OnSlideshowButtonClickListener(int position) {
            this._position = position;
        }
        @Override
        public void onClick(View v) {
            // button clicked, launch slideshow for this folder
            Album album = _albums.get(_position);
            Intent intent = new Intent(_context, PhotoActivity.class);
            intent.putExtra("folderAbsolutePath", album.getFolder().getAbsolutePath()); //folder.getAbsolutePath());
            intent.putExtra("position", -1);
            intent.putExtra("month", album.getMonth());
            intent.putExtra("year", album.getYear());
            _context.startActivity(intent);
        }
    }



}
