package asbridge.me.uk.MPhoto.Classes;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by David on 07/11/2015.
 * For disabling swipe see
 * http://stackoverflow.com/questions/9650265/how-do-disable-paging-by-swiping-with-finger-in-viewpager-but-still-be-able-to-s
 */
public class PhotoViewPager extends ViewPager {

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentItemManual(int pageNumber) {
            setCurrentItem(pageNumber);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
            return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
    }
}
