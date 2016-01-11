package asbridge.me.uk.MPhoto.controls;

import android.content.Context;
import android.widget.Checkable;
import android.widget.FrameLayout;
import asbridge.me.uk.MPhoto.R;


public class CheckableLayout extends FrameLayout { //} implements Checkable {
    private boolean mChecked;

    public CheckableLayout(Context context) {
        super(context);
    }
/*
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ?
                getResources().getDrawable(R.drawable.slideshow)
                : null);
    }


    public boolean isChecked() {
        return mChecked;
    }



    public void toggle() {
        setChecked(!mChecked);
    }
*/
}