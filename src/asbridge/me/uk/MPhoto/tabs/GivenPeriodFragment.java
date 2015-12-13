package asbridge.me.uk.MPhoto.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import asbridge.me.uk.MPhoto.Activities.AlbumActivity;
import asbridge.me.uk.MPhoto.Activities.PhotoActivity;
import asbridge.me.uk.MPhoto.R;

import java.util.Calendar;

/**
 * Created by David on 02/12/2015.
 */
public class GivenPeriodFragment extends TabFragment {

    RadioGroup radioGroup;
    Calendar c;
    String albumName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_given_period, container, false);
        radioGroup =(RadioGroup)v.findViewById(R.id.radioGroup);
        return v;
    }

    public void doSlideshow() {
        int selectedId=radioGroup.getCheckedRadioButtonId();

        Intent intent = new Intent(getActivity(), PhotoActivity.class);
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
        int selectedId=radioGroup.getCheckedRadioButtonId();

        Intent intent = new Intent(getActivity(), AlbumActivity.class);
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