package asbridge.me.uk.MPhoto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import asbridge.me.uk.MPhoto.tabs.*;

/**
 * Created by David on 02/12/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    TabFragment currentFragment;
    public TabsAdapter(FragmentManager fm) {
        super(fm);
        this.mNumOfTabs = 6;
    }

    public TabFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        if (currentFragment != object) {
            Log.d("DAVE", "change");
            currentFragment = (TabFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GivenMonthFragment tab0 = new GivenMonthFragment();
                return tab0;
            case 1:
                GivenYearFragment tab1 = new GivenYearFragment();
                return tab1;
            case 2:
                FromDateFragment tab2 = new FromDateFragment();
                return tab2;
            case 3:
                AllPhotosFragment tab3 = new AllPhotosFragment();
                return tab3;
            case 4:
                ThisYearFragment tab4 = new ThisYearFragment();
                return tab4;
            case 5:
                BucketListFragment tab5 = new BucketListFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}