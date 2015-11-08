package asbridge.me.uk.MPhoto.Classes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import asbridge.me.uk.MPhoto.Fragments.ScreenSlideStatePageFragment;
import asbridge.me.uk.MPhoto.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 04/11/2015.
 * see http://www.javacodegeeks.com/2013/04/android-tutorial-using-the-viewpager.html
 */
public class MyStatePagerAdapter extends FragmentStatePagerAdapter {

    protected Context mContext;

    private ArrayList<File> fileList = null;

    public MyStatePagerAdapter(FragmentManager fm,
                               Context context) {
        super(fm);
        this.mContext = context;
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
        Fragment fragment = new ScreenSlideStatePageFragment();
        // Attach some data to it that we'll
        // use to populate our fragment layouts
        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);
        args.putString("imagepath", fileList.get(position).getAbsolutePath() /*"/mnt/sdcard/MatthewsPhotos/DSC00272.JPG"*/);
        // Set the arguments on the fragment
        // that will be fetched in DemoFragment@onCreateView
        fragment.setArguments(args);

        return fragment;
    }


    @Override

    public int getCount() {
        if (this.fileList == null)
            return 0;
        return this.fileList.size();

    }

}

