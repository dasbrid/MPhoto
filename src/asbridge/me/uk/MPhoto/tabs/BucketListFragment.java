package asbridge.me.uk.MPhoto.tabs;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.AlbumsActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.Classes.Album;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.BucketListAdapter;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.util.ArrayList;

/**
 * Created by David on 02/12/2015.
 */
public class BucketListFragment extends TabFragment {

    private ArrayList<Album> bucketList;
    ListView lvBucketList;
    BucketListAdapter bucketAdapter;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bucket_list, container, false);

        lvBucketList = (ListView)v.findViewById(R.id.lvBucketList);

        lvBucketList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        bucketList = Utils.getAlbumsFromMedia(getContext());
        bucketAdapter = new BucketListAdapter(getContext(), bucketList);
        lvBucketList.setAdapter(bucketAdapter);

        return v;
    }

    public void bucketClicked(View v) {
        Toast.makeText(getContext(), "clicked",Toast.LENGTH_LONG).show();
    }

    public void doSlideshow() {
        // start the slideshow activity for the selected bucket
        SparseBooleanArray checked = lvBucketList.getCheckedItemPositions();
        String albumNames = "";
        ArrayList<Album> selectedItems = new ArrayList<Album>();
        ArrayList<String> selectedBucketIDs = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            if (checked.valueAt(i)) {
                Album selectedItem = bucketAdapter.getAlbum(position);
                if (!albumNames.isEmpty())
                    albumNames = albumNames + ", ";
                albumNames = albumNames + selectedItem.getName();
                selectedItems.add(selectedItem);
                selectedBucketIDs.add(Long.toString(selectedItem.getBucketID()));
            }
        }

        if (selectedItems.size() > 0 ) {
            Context context = getContext();
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("albumType", "multipleBuckets");
            intent.putExtra("albumName", albumNames);
            intent.putStringArrayListExtra("bucketIDs", selectedBucketIDs);
            context.startActivity(intent);
        }
    }

    public void viewAlbum() {
        SparseBooleanArray checked = lvBucketList.getCheckedItemPositions();
        String albumNames = "";
        ArrayList<Album> selectedItems = new ArrayList<Album>();
        ArrayList<String> selectedBucketIDs = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            if (checked.valueAt(i)) {
                Album selectedItem = bucketAdapter.getAlbum(position);
                if (albumNames.isEmpty())
                    albumNames = albumNames + ", ";
                albumNames = albumNames + selectedItem.getName();
                selectedItems.add(selectedItem);
                selectedBucketIDs.add(Long.toString(selectedItem.getBucketID()));
            }
        }

        if (selectedItems.size() > 0 ) {
            Context context = getContext();
            Intent intent = new Intent(context, AlbumActivity.class);
            //intent.putExtra("folderAbsolutePath", firstAlbum.getFolder() == null ? null : firstAlbum.getFolder().getAbsolutePath());
            intent.putExtra("albumType", "multipleBuckets");
            intent.putExtra("albumName", albumNames);
            intent.putStringArrayListExtra("bucketIDs", selectedBucketIDs);
            //intent.putExtra("albumBucketID", firstAlbum.getBucketID());
            //intent.putExtra("month", firstAlbum.getMonth());
            //intent.putExtra("year", firstAlbum.getYear());
            context.startActivity(intent);
        }
    }
}