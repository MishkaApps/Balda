package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import mikhailbolgov.balda.MyLog;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChanger;


public class AppSettingsActivity extends Activity implements View.OnClickListener {

    private Color lytBackgroundColor1, lytBackgroundColor2, lytBackgroundColor3, lytBackgroundColor4;
    private Color lytHeaderNFooterColor1, lytHeaderNFooterColor2, lytHeaderNFooterColor3, lytHeaderNFooterColor4;
    private Color lytGameFieldsColor1, lytGameFieldsColor2, lytGameFieldsColor3, lytGameFieldsColor4;
    private ArrayList<Color> lytsBackgroundColor, lytsElementsColor, lytsGameFieldColor;
    private Color selectedBackgroundColor, selectedHeaderNFooterColor, selectedGameFieldsColor;
    private Context context;
    private SharedPreferences preferences;
    private String sharedPrefsBackgroundKey, sharedPrefsHeaderNFooterKey, sharedPrefsGameFieldsKey;

    private ThemeChanger themeChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        themeChanger = new ThemeChanger(this);

        context = this;
        lytsBackgroundColor = new ArrayList<>();
        lytsElementsColor = new ArrayList<>();
        lytsGameFieldColor = new ArrayList<>();

        lytBackgroundColor1 = new Color((RelativeLayout) findViewById(R.id.app_background_color_1_lyt), true);
        lytBackgroundColor2 = new Color((RelativeLayout) findViewById(R.id.app_background_color_2_lyt), true);
        lytBackgroundColor3 = new Color((RelativeLayout) findViewById(R.id.app_background_color_3_lyt), false);
        lytBackgroundColor4 = new Color((RelativeLayout) findViewById(R.id.app_background_color_4_lyt), false);

        lytsBackgroundColor.add(lytBackgroundColor1);
        lytsBackgroundColor.add(lytBackgroundColor2);
        lytsBackgroundColor.add(lytBackgroundColor3);
        lytsBackgroundColor.add(lytBackgroundColor4);

        lytHeaderNFooterColor1 = new Color((RelativeLayout) findViewById(R.id.app_elements_color_1_lyt), true);
        lytHeaderNFooterColor2 = new Color((RelativeLayout) findViewById(R.id.app_elements_color_2_lyt), true);
        lytHeaderNFooterColor3 = new Color((RelativeLayout) findViewById(R.id.app_elements_color_3_lyt), false);
        lytHeaderNFooterColor4 = new Color((RelativeLayout) findViewById(R.id.app_elements_color_4_lyt), false);


        lytsElementsColor.add(lytHeaderNFooterColor1);
        lytsElementsColor.add(lytHeaderNFooterColor2);
        lytsElementsColor.add(lytHeaderNFooterColor3);
        lytsElementsColor.add(lytHeaderNFooterColor4);


        lytGameFieldsColor1 = new Color((RelativeLayout) findViewById(R.id.lytGameFieldColor1), true);
        lytGameFieldsColor2 = new Color((RelativeLayout) findViewById(R.id.lytGameFieldColor2), true);
        lytGameFieldsColor3 = new Color((RelativeLayout) findViewById(R.id.lytGameFieldColor3), false);
        lytGameFieldsColor4 = new Color((RelativeLayout) findViewById(R.id.lytGameFieldColor4), false);


        lytsGameFieldColor.add(lytGameFieldsColor1);
        lytsGameFieldColor.add(lytGameFieldsColor2);
        lytsGameFieldColor.add(lytGameFieldsColor3);
        lytsGameFieldColor.add(lytGameFieldsColor4);

        selectedBackgroundColor = lytBackgroundColor1;
        selectedHeaderNFooterColor = lytHeaderNFooterColor1;
        selectedGameFieldsColor = lytGameFieldsColor1;

        for (Color lyt : lytsBackgroundColor)
            lyt.setOnClickListener(this);


        for (Color lyt : lytsElementsColor)
            lyt.setOnClickListener(this);

        for (Color lyt : lytsGameFieldColor)
            lyt.setOnClickListener(this);

        sharedPrefsBackgroundKey = getResources().getString(R.string.shrdPrefsAppBackgroundColor);
        sharedPrefsHeaderNFooterKey = getResources().getString(R.string.shrdPrefsHeaderNFooterColor);
        sharedPrefsGameFieldsKey = getResources().getString(R.string.shrdPrefsGameFieldsColor);

        preferences = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {
        for (Color color : lytsBackgroundColor) {
            if (v.getId() == color.getId()) {
                selectedBackgroundColor.hideTick();
                selectedBackgroundColor = color;
                selectedBackgroundColor.showTick();

                rememberColors();

            }
        }

        for (Color color : lytsElementsColor) {
            if (v.getId() == color.getId()) {
                selectedHeaderNFooterColor.hideTick();
                selectedHeaderNFooterColor = color;
                selectedHeaderNFooterColor.showTick();

                rememberColors();
            }
        }

        for (Color color : lytsGameFieldColor) {
            if (v.getId() == color.getId()) {
                selectedGameFieldsColor.hideTick();
                selectedGameFieldsColor = color;
                selectedGameFieldsColor.showTick();

                rememberColors();
            }
        }

    }

    private void rememberColors(){
        int backgroundColor = selectedBackgroundColor.getColor();
        int elementsColor = selectedHeaderNFooterColor.getColor();
        int gameFieldsColor = selectedGameFieldsColor.getColor();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(sharedPrefsBackgroundKey, backgroundColor);
        editor.putInt(sharedPrefsHeaderNFooterKey, elementsColor);
        editor.putInt(sharedPrefsGameFieldsKey, gameFieldsColor);
        editor.apply();

        ArrayList<ViewGroup> headerNFooter = new ArrayList<>();
        headerNFooter.add((ViewGroup)findViewById(R.id.lytAppSettingsFooterNHeader));

        themeChanger.applyTheme(this,
                (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                headerNFooter);

    }


    private class Color {
        private RelativeLayout lyt;
        private TickView tick;

        public Color(RelativeLayout lyt, boolean darkTick) {

            this.lyt = lyt;

            tick = new TickView(context, darkTick);

        }

        public void showTick() {
            lyt.addView(tick);
        }

        public void hideTick() {
            lyt.removeView(tick);
        }

        public int getId(){
            return lyt.getId();
        }


        public void setOnClickListener(View.OnClickListener listener) {
            lyt.setOnClickListener(listener);
        }

        public int getColor(){
            return ((ColorDrawable)lyt.getChildAt(0).getBackground()).getColor();
        }
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
