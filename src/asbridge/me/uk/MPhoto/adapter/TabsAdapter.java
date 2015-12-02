package asbridge.me.uk.MPhoto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import asbridge.me.uk.MPhoto.tabs.MonthFragment;
import asbridge.me.uk.MPhoto.tabs.YearFragment;

/**
 * Created by David on 02/12/2015.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MonthFragment tab1 = new MonthFragment();
                return tab1;
            case 1:
                YearFragment tab2 = new YearFragment();
                return tab2;
/*
            case 2:
*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}