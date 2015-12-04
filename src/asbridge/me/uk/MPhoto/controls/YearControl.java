package asbridge.me.uk.MPhoto.controls;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import asbridge.me.uk.MPhoto.R;

import java.util.Calendar;

/**
 * Created by David on 03/12/2015.
 http://code.tutsplus.com/tutorials/creating-compound-views-on-android--cms-22889
 */
public class YearControl extends LinearLayout{
    private Button mPreviousButton;
    private Button mNextButton;
    private TextView mTextView;

    private int minYear;
    private int maxYear;

    private int currentYear;

    public void setMinYear(int i) {
        minYear = i;
        if (currentYear < i) currentYear = i;
    }

    public void setMaxYear(int i) {
        maxYear = i;
        if (currentYear > i) currentYear = i;
    }

    public void setYear(int i)
    {
        if (i < minYear) currentYear = minYear;
        else if (i > maxYear) currentYear = maxYear;
        else currentYear = i;

        mTextView.setText(Integer.toString(currentYear));
    }

    public int getYear() {
        return currentYear;
    }

    public YearControl(Context context) {
        super(context);
        initializeViews(context);
    }

    public YearControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public YearControl(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_control_year, this);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        Calendar c = Calendar.getInstance();

        minYear = 2000;
        maxYear = 2020;

        mTextView = (TextView) this.findViewById(R.id.sidespinner_view_current_value);
        setYear(c.get(Calendar.YEAR));
        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        mPreviousButton = (Button) this
                .findViewById(R.id.sidespinner_view_previous);
        mPreviousButton
                .setBackgroundResource(android.R.drawable.ic_media_previous);

        mNextButton = (Button)this
                .findViewById(R.id.sidespinner_view_next);
        mNextButton
                .setBackgroundResource(android.R.drawable.ic_media_next);

        // When the previous button is pressed, go to prev year
        mPreviousButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (currentYear > minYear) {
                    setYear(--currentYear);
                }
            }
        });

        // When the next button is pressed, select the next item in the
        // list.
        mNextButton = (Button)this
                .findViewById(R.id.sidespinner_view_next);
        mNextButton
                .setBackgroundResource(android.R.drawable.ic_media_next);
        mNextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (currentYear < maxYear) {
                    setYear(++currentYear);
                }
            }
        });
    }
}
