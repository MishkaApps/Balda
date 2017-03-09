package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import mikhailbolgov.balda.MyLog;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChanger;


public class ExtendedSettingsActivity extends Activity implements View.OnClickListener {

    private Color lytBackgroundColor1, lytBackgroundColor2, lytBackgroundColor3, lytBackgroundColor4;
    private Color lytHeaderNFooterColor1, lytHeaderNFooterColor2, lytHeaderNFooterColor3, lytHeaderNFooterColor4;
    private Color lytGameFieldsColor1, lytGameFieldsColor2, lytGameFieldsColor3, lytGameFieldsColor4;
    private ArrayList<Color> lytsBackgroundColor, lytsElementsColor, lytsGameFieldColor, lytsGameFieldTextColor;
    private Color selectedBackgroundColor, selectedHeaderNFooterColor, selectedGameFieldsColor;
    private Context context;
    private SharedPreferences preferences;
    private String sharedPrefsBackgroundKey, sharedPrefsHeaderNFooterKey, sharedPrefsGameFieldsKey, sharedPrefsBackgroundTextColorKey, sharedPrefsHeaderNFooterTextColorKey, sharedPrefsGameFieldsTextColorKey;
    private final int DEF_COLOR = 228;
    private ThemeChanger themeChanger;
    private int blackTextColor, whiteTextColor;
    private boolean autoclick;                     // Для определения источника нажатия на цвет. Если нажатие программное тема не сбрасывается.

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
        lytsGameFieldTextColor = new ArrayList<>();



        blackTextColor = getResources().getColor(R.color.textColor);
        whiteTextColor = getResources().getColor(R.color.textColorWhite);

        LinearLayout lytBackgroundColors = (LinearLayout) findViewById(R.id.lytAppSettingsBackgroundColors);
        for (int counter = 0; counter < lytBackgroundColors.getChildCount(); ++counter) {
            lytsBackgroundColor.add(new Color((RelativeLayout) lytBackgroundColors.getChildAt(counter)));
        }

        LinearLayout lytHeaderNFooterColors = (LinearLayout) findViewById(R.id.lytAppSettingsHeaderNFooterColors);
        for (int counter = 0; counter < lytHeaderNFooterColors.getChildCount(); ++counter) {
            lytsElementsColor.add(new Color((RelativeLayout) lytHeaderNFooterColors.getChildAt(counter)));
        }

        LinearLayout lytGameFieldColors = (LinearLayout) findViewById(R.id.lytAppSettingsGameFieldColors);
        Color color;
        for (int counter = 0; counter < lytGameFieldColors.getChildCount(); ++counter) {
            color = new Color((RelativeLayout) lytGameFieldColors.getChildAt(counter));
            lytsGameFieldColor.add(color);
            color.setLetterColor();
        }

        selectedBackgroundColor = lytsBackgroundColor.get(2);
        selectedHeaderNFooterColor = lytsElementsColor.get(2);
        selectedGameFieldsColor = lytsGameFieldColor.get(2);

        for (Color lyt : lytsBackgroundColor)
            lyt.setOnClickListener(this);


        for (Color lyt : lytsElementsColor)
            lyt.setOnClickListener(this);

        for (Color lyt : lytsGameFieldColor)
            lyt.setOnClickListener(this);

        sharedPrefsBackgroundKey = getResources().getString(R.string.shrdPrefsAppBackgroundColor);
        sharedPrefsHeaderNFooterKey = getResources().getString(R.string.shrdPrefsHeaderNFooterColor);
        sharedPrefsGameFieldsKey = getResources().getString(R.string.shrdPrefsGameFieldsColor);

        sharedPrefsBackgroundTextColorKey = getResources().getString(R.string.shrdPrefsAppBackgroundTextColor);
        sharedPrefsHeaderNFooterTextColorKey = getResources().getString(R.string.shrdPrefsHeaderNFooterTextColor);
        sharedPrefsGameFieldsTextColorKey = getResources().getString(R.string.shrdPrefsGameFieldsTextColor);

        preferences = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), context.MODE_PRIVATE);

        setCurrentColors();

    }

    private void setCurrentColors() {
        int backgroundCounter = 0, headerNFooterCounter = 0, gameFieldCounter = 0;
        for (Color color : lytsBackgroundColor) {
            if (color.getColor() == getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(getResources().getString(R.string.shrdPrefsAppBackgroundColor), DEF_COLOR))
                break;
            ++backgroundCounter;
        }

        for (Color color : lytsElementsColor) {
            if (color.getColor() == getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(getResources().getString(R.string.shrdPrefsHeaderNFooterColor), DEF_COLOR))
                break;
            ++headerNFooterCounter;
        }

        for (Color color : lytsGameFieldColor) {
            if (color.getColor() == getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(getResources().getString(R.string.shrdPrefsGameFieldsColor), DEF_COLOR))
                break;
            ++gameFieldCounter;
        }


        selectedBackgroundColor = lytsBackgroundColor.get(backgroundCounter);
        selectedHeaderNFooterColor = lytsElementsColor.get(headerNFooterCounter);
        selectedGameFieldsColor = lytsGameFieldColor.get(gameFieldCounter);

        autoclick = true;

        onClick(selectedBackgroundColor.lyt);
        onClick(selectedHeaderNFooterColor.lyt);
        onClick(selectedGameFieldsColor.lyt);

        autoclick = false;

    }

    @Override
    public void onClick(View v) {

        if (!autoclick) {
            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), MODE_PRIVATE).edit();
            editor.putInt(getResources().getString(R.string.selectedTheme), 0);
            editor.apply();
        }

        for (Color color : lytsBackgroundColor) {
            if (v.getId() == color.getId()) {
                selectedBackgroundColor.hideTick();
                selectedBackgroundColor = color;
                selectedBackgroundColor.showTick();
            }
        }

        for (Color color : lytsElementsColor) {
            if (v.getId() == color.getId()) {
                selectedHeaderNFooterColor.hideTick();
                selectedHeaderNFooterColor = color;
                selectedHeaderNFooterColor.showTick();
            }
        }

        for (Color color : lytsGameFieldColor) {
            if (v.getId() == color.getId()) {
                selectedGameFieldsColor.hideTick();
                selectedGameFieldsColor = color;
                selectedGameFieldsColor.showTick();
            }
        }

        rememberColors();

    }

    private void rememberColors() {
        int backgroundColor = selectedBackgroundColor.getColor();
        int elementsColor = selectedHeaderNFooterColor.getColor();
        int gameFieldsColor = selectedGameFieldsColor.getColor();
        int backgroundTextColor;
        int headerNFooterTextColor;
        int gameFieldsTextColor = 0;

        if (selectedBackgroundColor.getTag().equals(getResources().getString(R.string.tagTextBlack)))
            backgroundTextColor = getResources().getColor(R.color.textColor);
        else backgroundTextColor = getResources().getColor(R.color.textColorWhite);

        if (selectedHeaderNFooterColor.getTag().equals(getResources().getString(R.string.tagTextBlack)))
            headerNFooterTextColor = getResources().getColor(R.color.textColor);
        else headerNFooterTextColor = getResources().getColor(R.color.textColorWhite);


        if (selectedGameFieldsColor.getTag().equals(getResources().getString(R.string.tagTextBlack)))
            gameFieldsTextColor = getResources().getColor(R.color.textColor);
        else gameFieldsTextColor = getResources().getColor(R.color.textColorWhite);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(sharedPrefsBackgroundKey, backgroundColor);
        editor.putInt(sharedPrefsHeaderNFooterKey, elementsColor);
        editor.putInt(sharedPrefsGameFieldsKey, gameFieldsColor);

        editor.putInt(sharedPrefsBackgroundTextColorKey, backgroundTextColor);
        editor.putInt(sharedPrefsHeaderNFooterTextColorKey, headerNFooterTextColor);
        editor.putInt(sharedPrefsGameFieldsTextColorKey, gameFieldsTextColor);
        editor.apply();

        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.lytAppSettingsFooterNHeader));

        themeChanger.applyTheme(this,
                (ViewGroup) findViewById(R.id.lytAppSettingsBackground),
                headerNFooter);

    }


    private class Color {
        private RelativeLayout lyt;
        private TickView tick;

        public Color(RelativeLayout lyt) {

            this.lyt = lyt;

            if (lyt.getChildAt(0).getTag().equals(getResources().getString(R.string.tagTextBlack)))
                tick = new TickView(context, true);
            else
                tick = new TickView(context, false);


        }

        public void showTick() {
            lyt.addView(tick);
        }

        public void hideTick() {
            lyt.removeView(tick);
        }

        public int getId() {
            return lyt.getId();
        }


        public void setOnClickListener(View.OnClickListener listener) {
            lyt.setOnClickListener(listener);
        }

        public int getColor() {
            return ((ColorDrawable) lyt.getChildAt(0).getBackground()).getColor();
        }

        public String getTag() {
            return lyt.getChildAt(0).getTag().toString();
        }

        public void setLetterColor() {
            if (lyt.getChildAt(0).getTag().equals(getResources().getString(R.string.tagTextBlack)))
                ((TextView) lyt.getChildAt(1)).setTextColor(blackTextColor);
            else
                ((TextView) lyt.getChildAt(1)).setTextColor(whiteTextColor);
        }

        private View getView(){
            return this.lyt;
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
