package asbridge.me.uk.MPhoto.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by AsbridgeD on 12/01/2016.
 */
public class PageIndicator extends LinearLayout {

    private static final String TAG = "PageIndicator";

    private int numButtons;
    private int ballResId;
    private View[] balls;
    private int currentPage = -1;

    public PageIndicator(Context context) {
        super(context);
        initializeViews(context);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public PageIndicator(Context context,
                         AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_control_page_indicator, this);

    }

    public void setNumButtons(int n) {
        if (n == numButtons)
            return;
        numButtons = n;
        balls = new View[numButtons];
        View ball;
        removeAllViews();
        for (int i = 0; i < numButtons; i++) {
            ball = createBall();
            balls[i]=ball;
            addView(ball);
        }
        changePage(0);
    }


    private void setPage(int page, int size) {

        if (page <0 || page >= numButtons)
            return;
        Log.d(TAG, "setPage "+ page);
        View ball = balls[page];
        ViewGroup.LayoutParams params = ball.getLayoutParams();
        params.width = size;
        params.height = size;
        ball.setLayoutParams(params);


    }

    private void setPage(int page) {
        setPage(page, 40);
    }

    private void resetPage(int page) {
        setPage(page, 20);
    }

    public void changePage(int page) {
        setPage(page);
        resetPage(currentPage);
        currentPage = page;
    }

    private View createBall() {
        View v;
        if (ballResId > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(ballResId, this, true);
        } else {
            Button btn = new Button(getContext());
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(20, 20);
            ll.gravity = Gravity.CENTER_VERTICAL;
            ll.leftMargin = 5;
            ll.rightMargin = 5;
            btn.setLayoutParams(ll);
            btn.setBackground(getResources().getDrawable(R.drawable.rounded_cell));

            v = btn;
        }
        return v;
    }
}