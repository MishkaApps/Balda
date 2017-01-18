package mikhailbolgov.balda;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mbolg on 18.01.2017.
 */
public class ThemeChanger {
    private final int DEF_COLOR = 0;
    private int backgroundTextColor, headerNFooterTextColor;
    private Resources resources;

    public ThemeChanger(Context context) {
        resources = context.getResources();
    }

    public void applyTheme(Context context, ViewGroup background, ArrayList<ViewGroup> headerNFooter) {
        SharedPreferences preferences = context.getSharedPreferences(resources.getString(R.string.shrdPrefsAppThemes), Context.MODE_PRIVATE);
        int backgroundColor = preferences.getInt(resources.getString(R.string.shrdPrefsAppBackgroundColor), DEF_COLOR);
        int headerNFooterColor = preferences.getInt(resources.getString(R.string.shrdPrefsHeaderNFooterColor), DEF_COLOR);



        background.setBackgroundColor(backgroundColor);

        ArrayList<View> backgroundChildren = new ArrayList<>();
        ArrayList<View> headerNFooterChildren = new ArrayList<>();

        getChildren(background, backgroundChildren);

        for (ViewGroup viewGroup : headerNFooter) {
            viewGroup.setBackgroundColor(headerNFooterColor);
            getChildren(viewGroup, headerNFooterChildren);
        }



        setTextColor(backgroundColor, headerNFooterColor, backgroundChildren, headerNFooterChildren);

    }

    private void getChildren(ViewGroup viewGroup, ArrayList<View> children) {
        View tempView;
        for (int counter = 0; counter < viewGroup.getChildCount(); counter++) {
            tempView = viewGroup.getChildAt(counter);
            if(tempView.getTag() == null)
//            if(!tempView.getTag().equals(resources.getString(R.string.permanentTextColorTag)))
                children.add(tempView);

            if (tempView.getClass().getSuperclass() == ViewGroup.class)
                getChildren((ViewGroup) tempView, children);
        }

    }

    private void setTextColor(int backgroundColor, int headerNFooterColor, ArrayList<View> backgroundChildren, ArrayList<View> headerNFooterChildren) {

        int constBackgroundColor1 = resources.getColor(R.color.app_background_color_1),
                constBackgroundColor2 = resources.getColor(R.color.app_background_color_2),
                constBackgroundColor3 = resources.getColor(R.color.app_background_color_3),
                constBackgroundColor4 = resources.getColor(R.color.app_background_color_4);

        if (backgroundColor == constBackgroundColor1) {
            backgroundTextColor = resources.getColor(R.color.textColor);
        } else if (backgroundColor == constBackgroundColor2) {
            backgroundTextColor = resources.getColor(R.color.textColor);
        } else if (backgroundColor == constBackgroundColor3) {
            backgroundTextColor = resources.getColor(R.color.textColorWhite);
        } else if (backgroundColor == constBackgroundColor4) {
            backgroundTextColor = resources.getColor(R.color.textColorWhite);
        }

        int constHeaderNFooterColor1 = resources.getColor(R.color.app_elements_color_1),
                constHeaderNFooterColor2 = resources.getColor(R.color.app_elements_color_2),
                constHeaderNFooterColor3 = resources.getColor(R.color.app_elements_color_3),
                constHeaderNFooterColor4 = resources.getColor(R.color.app_elements_color_4);

        if (headerNFooterColor == constHeaderNFooterColor1) {
            headerNFooterTextColor = resources.getColor(R.color.textColor);
            MyLog.log("col 1");
        } else if (headerNFooterColor == constHeaderNFooterColor2) {
            MyLog.log("col 2");
            headerNFooterTextColor = resources.getColor(R.color.textColor);
        } else if (headerNFooterColor == constHeaderNFooterColor3) {
            MyLog.log("col 3");
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
        } else if (headerNFooterColor == constHeaderNFooterColor4) {
            MyLog.log("col 4");
            headerNFooterTextColor = resources.getColor(R.color.textColorWhite);
        }

        for(View view: backgroundChildren){
            if(view.getClass() == TextView.class)
                ((TextView)view).setTextColor(backgroundTextColor);
        }

        for(View view: headerNFooterChildren){
            if(view.getClass() == TextView.class)
                ((TextView)view).setTextColor(headerNFooterTextColor);
        }


    }


}
