package mikhailbolgov.balda;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mbolg on 18.01.2017.
 */
public class ThemeChanger {
    private final int DEF_COLOR = 0;
    private int backgroundTextColor, headerNFooterTextColor;
    private int textColorDark, textColorWhite;
    private Resources resources;
    private Context context;

    public ThemeChanger(Context context) {
        resources = context.getResources();
        this.context = context;

        textColorDark = resources.getColor(R.color.textColor);
        textColorWhite = resources.getColor(R.color.textColorWhite);

    }

    public int getBackgroungLinesColor() {
        if (getBackgroundTextColor() == textColorDark)
            return resources.getColor(R.color.absolutelyBlack);
        else
            return resources.getColor(R.color.absolutelyWhite);
    }

    public void applyTheme(Context context, ViewGroup background, ArrayList<View> headerNFooter) {
        SharedPreferences preferences = context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE);
        int backgroundColor = preferences.getInt(resources.getString(R.string.shrdPrefsAppBackgroundColor), DEF_COLOR);
        int headerNFooterColor = preferences.getInt(resources.getString(R.string.shrdPrefsHeaderNFooterColor), DEF_COLOR);


        background.setBackgroundColor(backgroundColor);

        ArrayList<View> backgroundChildren = new ArrayList<>();
        ArrayList<View> headerNFooterChildren = new ArrayList<>();

        getChildren(background, backgroundChildren);

        for (View view : headerNFooter) {
            view.setBackgroundColor(headerNFooterColor);
            getChildren(view, headerNFooterChildren);
        }


        setTextColor(backgroundChildren, headerNFooterChildren);

        for (View view : headerNFooterChildren) {
            if (view.getClass() == Button.class)
                setButton((Button) view);
            if (view.getClass() == Timer.class)
                view.setBackgroundColor(headerNFooterColor);

            if (view.getClass() == Button.class)
                setButton((Button) view);


            if (view.getTag() == resources.getString(R.string.tagLinefulColor)) {
                if (getHeaderNFooterTextColor() == textColorDark)
                    view.setBackgroundColor(resources.getColor(R.color.absolutelyBlack));
                else view.setBackgroundColor(resources.getColor(R.color.absolutelyWhite));
            }
        }

        for (View view : backgroundChildren) {
            if (view.getClass() == Button.class)
                setButton((Button) view);

            if (view.getTag() == resources.getString(R.string.tagLinefulDrawable)) {
                if (getBackgroundTextColor() == textColorDark)
                    view.setBackground(resources.getDrawable(R.drawable.text_view_background_dark_text));
                else
                    view.setBackground(resources.getDrawable(R.drawable.text_view_background_white_text));
            }

            if (view.getTag() == resources.getString(R.string.tagLinefulColor)) {
                if (getBackgroundTextColor() == textColorDark)
                    view.setBackgroundColor(resources.getColor(R.color.absolutelyBlack));
                else view.setBackgroundColor(resources.getColor(R.color.absolutelyWhite));
            }
        }


    }

    public int getBackgroundColor() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsAppBackgroundColor), DEF_COLOR);
    }

    public int getHeaderNFooterColor() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsHeaderNFooterColor), DEF_COLOR);
    }

    private void getChildren(View view, ArrayList<View> children) {
        children.add(view);
        if (view.getClass().getSuperclass() == ViewGroup.class
                || (view.getTag() != null && view.getTag().equals(resources.getString(R.string.tagInScrollView)))
                || view.getClass() == Timer.class) {
            View tempView;
            for (int counter = 0; counter < ((ViewGroup) view).getChildCount(); counter++) {
                tempView = ((ViewGroup) view).getChildAt(counter);
                children.add(tempView);

                if (tempView.getClass().getSuperclass() == ViewGroup.class)
                    getChildren(tempView, children);
            }
        }

    }

    public int getBackgroundTextColor() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsAppBackgroundTextColor), DEF_COLOR);
    }

    public int getHeaderNFooterTextColor() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsHeaderNFooterTextColor), DEF_COLOR);
    }


    private void setTextColor(ArrayList<View> backgroundChildren, ArrayList<View> headerNFooterChildren) {


        for (View view : backgroundChildren) {
//            if(view.getTag() != null && view.getTag().equals(resources.getString(R.string.tag_exception)))
//                continue;

            if (view.getClass() == TextView.class)
                ((TextView) view).setTextColor(getBackgroundTextColor());

            if (view.getClass() == CheckBox.class)
                ((CheckBox) view).setTextColor(getBackgroundTextColor());
        }

        for (View view : headerNFooterChildren) {
            if (view.getClass() == TextView.class)
                ((TextView) view).setTextColor(getHeaderNFooterTextColor());
            if (view.getClass() == Button.class)
                ((Button) view).setTextColor(getHeaderNFooterTextColor());
        }


    }

    public void setGameFieldColor(Context context, ArrayList<CellView> cellViews) {
        SharedPreferences preferences = context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE);
        int cellColor = preferences.getInt(resources.getString(R.string.shrdPrefsGameFieldsColor), DEF_COLOR);
        int textColor = preferences.getInt(resources.getString(R.string.shrdPrefsGameFieldsTextColor), DEF_COLOR);

        for (CellView cellView : cellViews) {
            cellView.setBackgroundColor(cellColor);
            cellView.setTextColor(textColor);
        }
    }

    public int getCellViewBackground() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsGameFieldsColor), DEF_COLOR);
    }

    public int getCellInFocusBackground() {
        if (getHeaderNFooterTextColor() == textColorDark)
            return resources.getColor(R.color.cell_in_focus_background_dark_text);
        else
            return resources.getColor(R.color.cell_in_focus_background_white_text);
    }

    public int getNewLetterBackground() {
        if (getHeaderNFooterTextColor() == textColorDark)
            return resources.getColor(R.color.new_letter_dark_text);
        else
            return resources.getColor(R.color.new_letter_white_text);
    }

    public int getLetterAddedInWordBackground() {
        if (getHeaderNFooterTextColor() == textColorDark)
            return resources.getColor(R.color.letter_added_in_word_dark_text);
        else
            return resources.getColor(R.color.letter_added_in_word_white_text);
    }

    public int getActivePlayerBackground() {
        if (getHeaderNFooterTextColor() == textColorDark)
            return resources.getColor(R.color.active_player_background_dark_text);
        else return resources.getColor(R.color.active_player_background_white_text);

    }

    public int getNotActivePlayerBackground() {
        return context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE).getInt(resources.getString(R.string.shrdPrefsHeaderNFooterColor), DEF_COLOR);
    }

    private void setButton(Button button) {
        String type;
        if (button.getTag() != null)
            type = button.getTag().toString();
        else return;

        if (type.equals(resources.getString(R.string.iconTagMenu))) {
            if (getHeaderNFooterTextColor() == textColorDark) {
                button.setBackgroundResource(R.drawable.menu_black);
            } else {
                button.setBackgroundResource(R.drawable.menu_white);
            }
            return;
        }

        if (type.equals(resources.getString(R.string.iconTagBackspace))) {
            if (getBackgroundTextColor() == textColorDark)
                button.setBackgroundResource(R.drawable.backspace_black);
            else
                button.setBackgroundResource(R.drawable.backspace_white);

            return;
        }

        if (type.equals(resources.getString(R.string.iconTagUpdateWord))) {
            if (getBackgroundTextColor() == textColorDark)
                button.setBackgroundResource(R.drawable.update_word_black);
            else
                button.setBackgroundResource(R.drawable.update_word_white);

            return;
        }

        if (type.equals(resources.getString(R.string.iconTagSwap))) {
            if (getBackgroundTextColor() == textColorDark)
                button.setBackgroundResource(R.drawable.swap_black);
            else
                button.setBackgroundResource(R.drawable.swap_white);

            return;
        }

        if (type.equals(resources.getString(R.string.tagEraseButton)))
            button.setTextColor(getBackgroundTextColor());

        if (type.equals(resources.getString(R.string.iconTagSkipMove))) {
            if (getBackgroundTextColor() == textColorDark)
                button.setBackgroundResource(R.drawable.skip_black);
            else
                button.setBackgroundResource(R.drawable.skip_white);

            return;
        }


        if (type.equals(resources.getString(R.string.tagDeleteVoc))) {
            if (getHeaderNFooterTextColor() == textColorDark)
                button.setBackgroundResource(R.drawable.delete_black);
            else
                button.setBackgroundResource(R.drawable.delete_white);

            return;
        }


    }

    public int getTextViewBackgroundResource() {
        if (getBackgroundTextColor() == textColorDark)
            return R.drawable.text_view_background_dark_text;
        else
            return R.drawable.text_view_background_white_text;
    }

    public int getTextViewInFocusBackgroundResource() {
        if (getBackgroundTextColor() == textColorDark)
            return R.drawable.text_view_in_focus_background_dark_text;
        else
            return R.drawable.text_view_in_focus_background_white_text;
    }

    public int getCheckBoxBackground() {
        if (getBackgroundTextColor() == textColorDark)
            return R.drawable.checkbox_black;
        else
            return R.drawable.checkbox_white;
    }

    public boolean backgroundTextColorIsDark() {
        return getBackgroundTextColor() == textColorDark;
    }

    public boolean boldText() {
        SharedPreferences preferences = context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE);
        return preferences.getBoolean(resources.getString(R.string.shrdPrefsFontBold), false);

    }

    public void rememberPresetTheme(String tag) {
        String sharedPrefsBackgroundKey = resources.getString(R.string.shrdPrefsAppBackgroundColor);
        String sharedPrefsHeaderNFooterKey = resources.getString(R.string.shrdPrefsHeaderNFooterColor);
        String sharedPrefsGameFieldsKey = resources.getString(R.string.shrdPrefsGameFieldsColor);

        String sharedPrefsBackgroundTextColorKey = resources.getString(R.string.shrdPrefsAppBackgroundTextColor);
        String sharedPrefsHeaderNFooterTextColorKey = resources.getString(R.string.shrdPrefsHeaderNFooterTextColor);
        String sharedPrefsGameFieldsTextColorKey = resources.getString(R.string.shrdPrefsGameFieldsTextColor);


        int backgroundColor = 0;
        int elementsColor = 0;
        int gameFieldsColor = 0;
        int backgroundTextColor = 0;
        int headerNFooterTextColor = 0;
        int gameFieldsTextColor = 0;
        int selectedTheme = 0;


        if (tag.equals(resources.getString(R.string.theme1))) {
            backgroundColor = resources.getColor(R.color.theme1_background);
            elementsColor = resources.getColor(R.color.theme1_main);
            gameFieldsColor = resources.getColor(R.color.theme1_fields);

            backgroundTextColor = resources.getColor(R.color.textColor);
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
            gameFieldsTextColor = resources.getColor(R.color.textColorWhite);

            selectedTheme = 1;

        } else if (tag.equals(resources.getString(R.string.theme2))) {

            backgroundColor = resources.getColor(R.color.theme2_background);
            elementsColor = resources.getColor(R.color.theme2_main);
            gameFieldsColor = resources.getColor(R.color.theme2_fields);

            backgroundTextColor = resources.getColor(R.color.textColorWhite);
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
            gameFieldsTextColor = resources.getColor(R.color.textColorWhite);

            selectedTheme = 2;
        } else if (tag.equals(resources.getString(R.string.theme3))) {

            backgroundColor = resources.getColor(R.color.theme3_background);
            elementsColor = resources.getColor(R.color.theme3_main);
            gameFieldsColor = resources.getColor(R.color.theme3_fields);


            backgroundTextColor = resources.getColor(R.color.textColorWhite);
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
            gameFieldsTextColor = resources.getColor(R.color.textColorWhite);

            selectedTheme = 3;
        } else if (tag.equals(resources.getString(R.string.theme4))) {

            backgroundColor = resources.getColor(R.color.theme4_background);
            elementsColor = resources.getColor(R.color.theme4_main);
            gameFieldsColor = resources.getColor(R.color.theme4_fields);


            backgroundTextColor = resources.getColor(R.color.textColor);
            headerNFooterTextColor = resources.getColor(R.color.textColor);
            gameFieldsTextColor = resources.getColor(R.color.textColor);


            selectedTheme = 4;
        } else if (tag.equals(resources.getString(R.string.theme5))) {

            backgroundColor = resources.getColor(R.color.theme5_background);
            elementsColor = resources.getColor(R.color.theme5_main);
            gameFieldsColor = resources.getColor(R.color.theme5_fields);


            backgroundTextColor = resources.getColor(R.color.textColorWhite);
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
            gameFieldsTextColor = resources.getColor(R.color.textColorWhite);


            selectedTheme = 5;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), context.MODE_PRIVATE).edit();
        editor.putInt(sharedPrefsBackgroundKey, backgroundColor);
        editor.putInt(sharedPrefsHeaderNFooterKey, elementsColor);
        editor.putInt(sharedPrefsGameFieldsKey, gameFieldsColor);

        editor.putInt(sharedPrefsBackgroundTextColorKey, backgroundTextColor);
        editor.putInt(sharedPrefsHeaderNFooterTextColorKey, headerNFooterTextColor);
        editor.putInt(sharedPrefsGameFieldsTextColorKey, gameFieldsTextColor);

        editor.putInt(resources.getString(R.string.selectedTheme), selectedTheme);
        editor.apply();

    }
}