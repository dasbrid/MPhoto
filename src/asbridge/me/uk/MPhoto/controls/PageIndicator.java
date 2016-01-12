package asbridge.me.uk.MPhoto.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by AsbridgeD on 12/01/2016.
 */
public class PageIndicator extends LinearLayout {

    private static final String TAG = "PageIndicator";

    private int numButtons;
    private int ballResId;
    private View[] balls;

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
    }

    public void setPage(int page) {

        if (page <0 || page >= numButtons)
            return;
        Log.d(TAG, "setPage "+ page);
        View ball = balls[page];
        ViewGroup.LayoutParams params = ball.getLayoutParams();
        params.width = 40;
        params.height = 40;
        ball.setLayoutParams(params);


    }
    private View createBall() {
        View v;
        if (ballResId > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(ballResId, this, true);
        } else {
            Button btn = new Button(getContext());
            btn.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            btn.setBackground(getResources().getDrawable(R.drawable.rounded_cell));

            v = btn;
        }

        return v;
    }
}