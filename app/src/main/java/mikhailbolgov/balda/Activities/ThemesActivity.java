package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChanger;

public class ThemesActivity extends Activity implements View.OnClickListener {
    private Button btnMenu;
    private CheckBox cbxBoldFont;
    private SharedPreferences preferences;
    private String sharedPrefsFontBoldKey;
    private ViewGroup lytTheme1, lytTheme2, lytTheme3, lytTheme4, lytTheme5;
    private ThemeChanger themeChanger;
    private TickView currentTick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        cbxBoldFont = (CheckBox) findViewById(R.id.bold_font);

        themeChanger = new ThemeChanger(this);

        cbxBoldFont.setOnClickListener(this);


        btnMenu = (Button) findViewById(R.id.button_menu);
        btnMenu.setOnClickListener(this);

        sharedPrefsFontBoldKey = getResources().getString(R.string.shrdPrefsFontBold);
        preferences = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), this.MODE_PRIVATE);

        cbxBoldFont.setChecked(preferences.getBoolean(sharedPrefsFontBoldKey, false));


        lytTheme1 = (ViewGroup) findViewById(R.id.lyt_theme_1);
        lytTheme2 = (ViewGroup) findViewById(R.id.lyt_theme_2);
        lytTheme3 = (ViewGroup) findViewById(R.id.lyt_theme_3);
        lytTheme4 = (ViewGroup) findViewById(R.id.lyt_theme_4);
        lytTheme5 = (ViewGroup) findViewById(R.id.lyt_theme_5);


        lytTheme1.setOnClickListener(this);
        lytTheme2.setOnClickListener(this);
        lytTheme3.setOnClickListener(this);
        lytTheme4.setOnClickListener(this);
        lytTheme5.setOnClickListener(this);

        int currentTheme = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(getResources().getString(R.string.selectedTheme), 0);

        switch (currentTheme) {
            case 1:
                onClick(lytTheme1);
                break;
            case 2:
                onClick(lytTheme2);
                break;
            case 3:
                onClick(lytTheme3);
                break;
            case 4:
                onClick(lytTheme4);
                break;
            case 5:
                onClick(lytTheme5);
                break;
            case 0:
            default:
                if (currentTick != null) {
                    lytTheme1.removeView(currentTick);
                    lytTheme2.removeView(currentTick);
                    lytTheme3.removeView(currentTick);
                    lytTheme4.removeView(currentTick);
                    lytTheme5.removeView(currentTick);
                }
        }

    }

    @Override
    public void onClick(View v) {

        if (v == cbxBoldFont) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(sharedPrefsFontBoldKey, cbxBoldFont.isChecked());
            editor.apply();
        }

        if (v == lytTheme1 | v == lytTheme2 | v == lytTheme3 | v == lytTheme4 | v == lytTheme5) {
            themeChanger.rememberPresetTheme(v.getTag().toString());

            if (currentTick != null) {
                lytTheme1.removeView(currentTick);
                lytTheme2.removeView(currentTick);
                lytTheme3.removeView(currentTick);
                lytTheme4.removeView(currentTick);
                lytTheme5.removeView(currentTick);
            }

            if (((ViewGroup) v).getChildAt(0).getTag().equals(getResources().getString(R.string.tagTextBlack)))
                currentTick = new TickView(this, true);
            else currentTick = new TickView(this, false);
            ((ViewGroup) v).addView(currentTick);

            ArrayList<View> headerNFooter = new ArrayList<>();
            headerNFooter.add(findViewById(R.id.lytAppSettingsFooterNHeader));

            themeChanger.applyTheme(this,
                    (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                    headerNFooter);
        }

        if (v == btnMenu) {
            PopupMenu popupMenu = new PopupMenu(this, btnMenu);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.themes_menu, popupMenu.getMenu());
            final Activity thisActivity = this;
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(thisActivity, ExtendedSettingsActivity.class);

                    startActivity(intent);
                    return false;
                }
            });
            popupMenu.show();
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


        int currentTheme = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(getResources().getString(R.string.selectedTheme), 0);
        if (currentTheme == 0)
            if (currentTick != null) {
                lytTheme1.removeView(currentTick);
                lytTheme2.removeView(currentTick);
                lytTheme3.removeView(currentTick);
                lytTheme4.removeView(currentTick);
                lytTheme5.removeView(currentTick);
            }


        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.lytAppSettingsFooterNHeader));

        themeChanger.applyTheme(this,
                (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                headerNFooter);
    }

    private class TickView extends RelativeLayout {

        public TickView(Context context, boolean darkTick) {
            super(context);
            if (darkTick)
                inflate(context, R.layout.color_selected_dark, this);
            else
                inflate(context, R.layout.color_selected_white, this);
        }
    }
}
