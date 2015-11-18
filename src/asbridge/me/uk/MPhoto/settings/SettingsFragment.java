package asbridge.me.uk.MPhoto.settings;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import asbridge.me.uk.MPhoto.R;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
/**
 * Created by AsbridgeD on 18/11/2015.
 */
public class SettingsFragment extends PreferenceFragment  {
    private SharedPreferences prefs;
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}