package asbridge.me.uk.MPhoto.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import asbridge.me.uk.MPhoto.R;

/**
 * Created by David on 04/11/2015.
 * see http://developer.android.com/training/animation/screen-slide.html
 */
public class ScreenSlidePageFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final ScreenSlidePageFragment newInstance(String message)

    {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String message = getArguments().getString(EXTRA_MESSAGE);


        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        TextView messageTextView = (TextView)rootView.findViewById(R.id.textView);
        messageTextView.setText(message);

        return rootView;
    }
}