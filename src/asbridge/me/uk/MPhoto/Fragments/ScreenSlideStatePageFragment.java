package asbridge.me.uk.MPhoto.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;

/**
 * Created by David on 04/11/2015.
 * see http://developer.android.com/training/animation/screen-slide.html
 */
public class ScreenSlideStatePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_state_pager, container, false);

        Bundle args = getArguments();
        int pageNumber = args.getInt("page_position");
        String imagepath = args.getString("imagepath");

        ImageView iv = (ImageView) rootView.findViewById(R.id.imageView);

        Bitmap myBitmap = Utils.decodeFileToThumbnail(new File(imagepath));// BitmapFactory.decodeFile(imagepath);
        iv.setImageBitmap(myBitmap);

        return rootView;
    }

}