package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.Activities.MultiCheckablePhotoGridActivity;
import asbridge.me.uk.MPhoto.Activities.SlideshowActivity;
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
            Intent intent = new Intent(getActivity(), SlideshowActivity.class);
            intent.putExtra("albumType", "multipleBuckets");
            intent.putExtra("albumName", albumNames);
            intent.putExtra("position", -1);
            intent.putStringArrayListExtra("bucketIDs", selectedBucketIDs);
            this.startActivity(intent);
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
            Intent intent;

            intent = new Intent(getActivity(), MultiCheckablePhotoGridActivity.class);

            intent.putExtra("albumType", "multipleBuckets");
            intent.putExtra("albumName", albumNames);
            intent.putStringArrayListExtra("bucketIDs", selectedBucketIDs);
            this.startActivity(intent);
        }
    }
}