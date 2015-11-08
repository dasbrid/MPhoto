package asbridge.me.uk.MPhoto.Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by David on 04/11/2015.
 * see http://www.javacodegeeks.com/2013/04/android-tutorial-using-the-viewpager.html
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;


    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {

        super(fm);

        this.fragments = fragments;

    }

    @Override

    public Fragment getItem(int position) {

        return this.fragments.get(position);

    }


    @Override

    public int getCount() {

        return this.fragments.size();

    }

}

