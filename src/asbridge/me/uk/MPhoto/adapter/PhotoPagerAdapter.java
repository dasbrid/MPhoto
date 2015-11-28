package asbridge.me.uk.MPhoto.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import asbridge.me.uk.MPhoto.Fragments.PhotoPagerFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 04/11/2015.
 * see http://www.javacodegeeks.com/2013/04/android-tutorial-using-the-viewpager.html
 */
public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<File> fileList = null;

    public PhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public File getFileAtPosition(int position)
    {
        return fileList.get(position);
    }

    public void setFileList(ArrayList<File> fileList)
    {
        this.fileList = fileList;
    }

    @Override
    // This method returns the fragment associated with
    // the specified position.
    //
    // It is called when the Adapter needs a fragment
    // and it does not exists.
    public Fragment getItem(int position) {
        Fragment fragment = new PhotoPagerFragment();
        // Attach some data to it that we'll
        // use to populate our fragment layouts
        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);
        args.putString("imagepath", fileList.get(position).getAbsolutePath());
        // Set the arguments on the fragment
        // that will be fetched in DemoFragment@onCreateView
        fragment.setArguments(args);

        return fragment;
    }

    // returning position non probably means we get new fragment every time
    // might not be as efficient, but works when removing pages
    @Override
    public int getItemPosition(Object object) {
        //if (object instanceof FirstPageFragment && mFragmentAtPos0 instanceof NextFragment)
        // position none means recreate this page
        return POSITION_NONE;
        // position unchanged means use existing cached fragment
        //return POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        if (this.fileList == null)
            return 0;
        return this.fileList.size();

    }
}

