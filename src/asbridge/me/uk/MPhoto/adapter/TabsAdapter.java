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
        this.mNumOfTabs = 7;
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
                LastNPhotosFragment tab0 = new LastNPhotosFragment();
                return tab0;
            case 1:
                BucketListFragment tab1 = new BucketListFragment();
                return tab1;
            case 2:
                GivenYearFragment tab2 = new GivenYearFragment();
                return tab2;
            case 3:
                ThisYearFragment tab3 = new ThisYearFragment();
                return tab3;
            case 4:
                AllPhotosFragment tab4 = new AllPhotosFragment();
                return tab4;
            case 5:
                GivenMonthFragment tab5 = new GivenMonthFragment();
                return tab5;
            case 6:
                FromDateFragment tab6 = new FromDateFragment();
                return tab6;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}