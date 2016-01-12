package asbridge.me.uk.MPhoto.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.adapter.TabsAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.tabs.TabFragment;

/**
 * Created by David on 02/12/2015.
 */
public class PhotosByDateActivity extends FragmentActivity {

    private static final String TAG = "PhotosByDateActivity";

    private TabsAdapter tabsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosbydate);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagertabs);
        //http://www.androidbegin.com/tutorial/android-viewpagertabstrip-fragments-tutorial/

        Button btnViewPhotos = (Button) findViewById(R.id.btnShowAlbum);
        if (AppConstant.ALLOW_VIEW_PHOTOS == false)
            btnViewPhotos.setVisibility(View.INVISIBLE);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsAdapter);
        btn0=(Button)findViewById(R.id.btn0);
        btn0.setMinimumWidth(0);
        btn1=(Button)findViewById(R.id.btn1);
        btn1.setMinimumWidth(0);
        btn2=(Button)findViewById(R.id.btn2);
        btn2.setMinimumWidth(0);
        btn3=(Button)findViewById(R.id.btn3);
        btn3.setMinimumWidth(0);
        btn4=(Button)findViewById(R.id.btn4);
        btn4.setMinimumWidth(0);
        btn5=(Button)findViewById(R.id.btn5);
        btn5.setMinimumWidth(0);

        setButtonSelected(btn0);
        setButtonUnselected(btn1);
        setButtonUnselected(btn2);
        setButtonUnselected(btn3);
        setButtonUnselected(btn4);
        setButtonUnselected(btn5);
        //https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout
        //https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
        // Give the TabLayout the ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                btnAction(position);
            }

        });
    }
    private Button btn0, btn1, btn2, btn3, btn4, btn5;

    private void setButton(Button btn, int h, int w){
        ViewGroup.LayoutParams params = btn.getLayoutParams();
        params.width = h;
        params.height = w;
        btn.setLayoutParams(params);
    }

    private void setButtonSelected(Button btn) {
        setButton(btn, 40,40);
    }

    private void setButtonUnselected(Button btn) {
        setButton(btn,20,20);
    }

    private void btnAction(int action){
        switch(action){
            case 0:
                setButtonSelected(btn0);
                setButtonUnselected(btn1);
                setButtonUnselected(btn2);
                setButtonUnselected(btn3);
                setButtonUnselected(btn4);
                setButtonUnselected(btn5);
                break;
            case 1:
            setButtonUnselected(btn0);
            setButtonSelected(btn1);
            setButtonUnselected(btn2);
            setButtonUnselected(btn3);
            setButtonUnselected(btn4);
            setButtonUnselected(btn5);
            break;

            case 2:
                setButtonUnselected(btn0);
                setButtonUnselected(btn1);
                setButtonSelected(btn2);
                setButtonUnselected(btn3);
                setButtonUnselected(btn4);
                setButtonUnselected(btn5);
                break;
            case 3:
                setButtonUnselected(btn0);
                setButtonUnselected(btn1);
                setButtonUnselected(btn2);
                setButtonSelected(btn3);
                setButtonUnselected(btn4);
                setButtonUnselected(btn5);
                break;
            case 4:
                setButtonUnselected(btn0);
                setButtonUnselected(btn1);
                setButtonUnselected(btn2);
                setButtonUnselected(btn3);
                setButtonSelected(btn4);
                setButtonUnselected(btn5);
                break;
            case 5:
                setButtonUnselected(btn0);
                setButtonUnselected(btn1);
                setButtonUnselected(btn2);
                setButtonUnselected(btn3);
                setButtonUnselected(btn4);
                setButtonSelected(btn5);
                break;
        }
    }
    public void btnShowSlideshowClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.doSlideshow();
    }

    public void btnShowAlbumClicked(View v) {
        TabFragment currentFragment = tabsAdapter.getCurrentFragment();
        currentFragment.viewAlbum();
   }

}