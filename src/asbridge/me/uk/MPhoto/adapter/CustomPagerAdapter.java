package asbridge.me.uk.MPhoto.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.helper.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 03/01/2016.
 * http://codetheory.in/android-image-slideshow-using-viewpager-pageradapter/
 */
public class CustomPagerAdapter extends PagerAdapter {

    private ArrayList<File> fileList = null;
    public void setFileList(ArrayList<File> fileList)
    {
        this.fileList = fileList;
    }

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.slideshow_page, container, false);

        ImageView iv = (ImageView) itemView.findViewById(R.id.slideshowImage);
        // get the imageview size and scale the image to fit
        Bitmap myBitmap = Utils.decodeFileToSize(new File(fileList.get(position).getAbsolutePath()), AppConstant.SLIDESHOW_WIDTH,AppConstant.SLIDESHOW_HEIGHT);
        iv.setImageBitmap(myBitmap);
 /*
        ImageView imageView = (ImageView) itemView.findViewById(R.id.slideshowImage);
        imageView.setImageResource(mResources[position]);
*/
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
