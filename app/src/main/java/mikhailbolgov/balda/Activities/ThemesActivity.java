package mikhailbolgov.balda.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChanger;

public class ThemesActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvExtendedSettings;
    private CheckBox cbxBoldFont;
    private SharedPreferences preferences;
    private String sharedPrefsFontBoldKey;
    private ViewGroup lytTheme1, lytTheme2, lytTheme3, lytTheme4;
    private ThemeChanger themeChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        tvExtendedSettings = (TextView) findViewById(R.id.tv_extended_settings);
        cbxBoldFont = (CheckBox) findViewById(R.id.bold_font);

        themeChanger = new ThemeChanger(this);

        tvExtendedSettings.setOnClickListener(this);
        cbxBoldFont.setOnClickListener(this);

        sharedPrefsFontBoldKey = getResources().getString(R.string.shrdPrefsFontBold);
        preferences = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), this.MODE_PRIVATE);

        cbxBoldFont.setChecked(preferences.getBoolean(sharedPrefsFontBoldKey, false));

        lytTheme1 = (ViewGroup) findViewById(R.id.lyt_theme_1);
        lytTheme2 = (ViewGroup) findViewById(R.id.lyt_theme_2);
        lytTheme3 = (ViewGroup) findViewById(R.id.lyt_theme_3);
        lytTheme4 = (ViewGroup) findViewById(R.id.lyt_theme_4);

        lytTheme1.setOnClickListener(this);
        lytTheme2.setOnClickListener(this);
        lytTheme3.setOnClickListener(this);
        lytTheme4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvExtendedSettings) {
            Intent intent = new Intent(this, ExtendedSettingsActivity.class);
            ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(getResources().getInteger(R.integer.gameFieldVibrationDuration));
            startActivity(intent);
        }

        if (v == cbxBoldFont) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(sharedPrefsFontBoldKey, cbxBoldFont.isChecked());
            editor.apply();
        }

        if(v == lytTheme1 | v == lytTheme2 | v == lytTheme3 | v == lytTheme4){
            themeChanger.rememberPresetTheme(v.getTag().toString());

            ArrayList<View> headerNFooter = new ArrayList<>();
            headerNFooter.add(findViewById(R.id.lytAppSettingsFooterNHeader));

            themeChanger.applyTheme(this,
                    (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                    headerNFooter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(sharedPrefsFontBoldKey, cbxBoldFont.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.lytAppSettingsFooterNHeader));

        themeChanger.applyTheme(this,
                (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                headerNFooter);
    }
}
