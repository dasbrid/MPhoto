package asbridge.me.uk.MPhoto.Classes;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import asbridge.me.uk.MPhoto.adapter.PhotoPagerAdapter;

import java.io.File;

/**
 * Created by David on 07/11/2015.
 * For disabling swipe see
 * http://stackoverflow.com/questions/9650265/how-do-disable-paging-by-swiping-with-finger-in-viewpager-but-still-be-able-to-s
 */
public class PhotoViewPager extends ViewPager {

    OnTouchedListener mCallback = null;
    // implement this interface to listen
    public interface OnTouchedListener {
        public void onTouched();
    }

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentItemManual(int pageNumber) {
            setCurrentItem(pageNumber);
    }

    public void getCurrentFile()
    {
        int currentIndex = getCurrentItem();
        PhotoPagerAdapter adapter = (PhotoPagerAdapter) getAdapter();
        File currentFile = adapter.getFileAtPosition(currentIndex);
        Log.d("DAVE","file = "+currentFile.getName());
    }

    public void setOnTouchedListener(OnTouchedListener otl) {
        mCallback = otl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCallback != null)
            mCallback.onTouched();
        return super.onTouchEvent(event);
    }
}
