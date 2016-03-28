package com.wx.show.wxnews.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.wx.show.wxnews.R;

/**
 * Created by Luka on 2016/3/28.
 * E-mail:397308937@qq.com
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(getString(R.string.key_sava_traffic).equals(preference.getKey())){
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
