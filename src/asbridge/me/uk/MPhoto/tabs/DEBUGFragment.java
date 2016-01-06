package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoGridActivity;
import asbridge.me.uk.MPhoto.Activities.SlideshowActivity;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.helper.AppConstant;
import asbridge.me.uk.MPhoto.helper.Utils;
import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class DEBUGFragment extends TabFragment {

    private String TAG = "DAVE:GivenPeriodFragment";

    private View v;
    //RadioGroup radioGroup;
    Calendar c;
    String albumName;

    RadioGroup rg1;
    RadioGroup rg2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_debug, container, false);
        //radioGroup =(RadioGroup)v.findViewById(R.id.radioGroup);
        rg1 = (RadioGroup) v.findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) v.findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        RadioButton rbDefault = (RadioButton) v.findViewById(R.id.rbAllPhotos);
        rbDefault.setChecked(true);
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tvLastDisplayed = (TextView) v.findViewById(R.id.tvLastDisplayed);
        tvLastDisplayed.setText("image "+ Utils.getLastDisplayed(getContext())+" "+Utils.getImageFilename(getContext()));
    }
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                rg2.clearCheck(); // clear the second RadioGroup!
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
                Log.e("XXX2", "do the work");
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
            }
        }
    };

    public void doSlideshow() {
        int selectedId=rg1.getCheckedRadioButtonId();
        if (selectedId == -1)
            selectedId = rg2.getCheckedRadioButtonId();
        Log.d(TAG,"selectedId="+selectedId);
        Intent intent = new Intent(getActivity(), /*PhotoActivity*/SlideshowActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");

        if (selectedId == R.id.rbAllPhotos) {
            intent.putExtra("albumType", "allPhotos");
            intent.putExtra("albumName", "All photos");
            intent.putExtra("position", -1);
            intent.putExtra("month", -1);
            intent.putExtra("year", -1);
            this.startActivity(intent);
            return;
        } else if (selectedId == R.id.rbLastYear) {
            intent.putExtra("albumType", "lastYear");
            intent.putExtra("albumName", "Photos from last year");
            intent.putExtra("position", -1);
            intent.putExtra("month", -1);
            intent.putExtra("year", -1);
            this.startActivity(intent);
            return;
        }

        // all other options are photos from a given date to present
        getStartDateAndAlbumName(selectedId);

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        // start the slideshow activity

        intent.putExtra("albumType", "fromDate");
        intent.putExtra("albumName", albumName);
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        intent.putExtra("day", day);
        this.startActivity(intent);

        //TODO: Last Year, All Photos
    }

    public void getStartDateAndAlbumName(int selectedId) {
        c = Calendar.getInstance();

        albumName = "Photos";
        switch (selectedId) {
            case (R.id.rbInLastWeek): {
                c.add(Calendar.DATE, -7);
                albumName = "Photos in the last 7 days";
                break;
            }
            case (R.id.rbThisMonth): {
                c.set(Calendar.DAY_OF_MONTH, 1);
                albumName = "Photos taken this month";
                break;
            }
            case (R.id.rbInLastMonth): {
                c.add(Calendar.MONTH, -1);
                albumName = "Photos taken in the past month";
                break;
            }
            case (R.id.rbInLast3Months): {
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.MONTH, -3);
                albumName = "Photos taken in the past 3 months";
                break;
            }
            case (R.id.rbThisYear): {
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.MONTH, 0);
                albumName = "Photos taken this year";
                break;
            }
            case (R.id.rbInLastYear): {
                c.add(Calendar.YEAR, -1);
                albumName = "Photos during the past year";
                break;
            }
        }
    }

    public void viewAlbum() {
        int selectedId=rg1.getCheckedRadioButtonId();
        if (selectedId == -1)
            selectedId = rg2.getCheckedRadioButtonId();
        Log.d(TAG,"selectedId="+selectedId);
        Intent intent;
        if (AppConstant.USE_PHOTO_GRID_ACTIVITY)
            intent = new Intent(getActivity(), PhotoGridActivity.class);
        else
            intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("folderAbsolutePath", "not needed");

        if (selectedId == R.id.rbAllPhotos) {
            intent.putExtra("albumType", "allPhotos");
            intent.putExtra("albumName", "All photos");
            intent.putExtra("position", -1);
            intent.putExtra("month", -1);
            intent.putExtra("year", -1);
            this.startActivity(intent);
            return;
        } else if (selectedId == R.id.rbLastYear) {
            intent.putExtra("albumType", "lastYear");
            intent.putExtra("albumName", "Photos from last year");
            intent.putExtra("position", -1);
            intent.putExtra("month", -1);
            intent.putExtra("year", -1);
            this.startActivity(intent);
            return;
        }

        // all other options are photos from a given date to present
        getStartDateAndAlbumName(selectedId);

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        // start the slideshow activity
        intent.putExtra("albumType", "fromDate");
        intent.putExtra("albumName", albumName);
        intent.putExtra("position", -1);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        intent.putExtra("day", day);
        this.startActivity(intent);
    }
}