package com.wx.show.wxnews.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.fragment.SettingFragment;


/**
 * Settings activity.
 */

public class AboutActivity extends AppCompatActivity {
    // TAG
//    private final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_about);
//        initToolbar();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean navigationBarColorSwitch = sharedPreferences.getBoolean(
//                getString(R.string.key_navigation_bar_color_switch), false);
//                if (navigationBarColorSwitch && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.lightPrimary_3));
//        }
//        SettingFragment settingFragment = new SettingFragment();
//        getFragmentManager().beginTransaction().replace(R.id.setting_container, settingFragment).commit();
    }
//
//    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
//        toolbar.setTitle(getString(R.string.settings));
//        this.setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }
}