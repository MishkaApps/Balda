package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChanger;

public class RulesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ThemeChanger themeChanger = new ThemeChanger(this);
        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.tvRulesHeader));
        themeChanger.applyTheme(this, (ViewGroup) findViewById(R.id.lytRulesBackground), headerNFooter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ThemeChanger themeChanger = new ThemeChanger(this);
        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.tvRulesHeader));
        themeChanger.applyTheme(this, (ViewGroup) findViewById(R.id.lytRulesBackground), headerNFooter);
    }
}


