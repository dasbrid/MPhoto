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
public class NonSwipeableViewPager extends ViewPager {

    private boolean swipable = false;
    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentItemManual(int pageNumber) {
        if (!this.swipable)
            setCurrentItem(pageNumber);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipable) {
            return super.onInterceptTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipable) {
            return super.onTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    public void setPagingEnabled(boolean swipable) {
        this.swipable = swipable;
    }

    public void togglePlayPause()
    {
        this.swipable = !this.swipable;
    }
}
