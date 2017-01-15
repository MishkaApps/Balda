package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import mikhailbolgov.balda.MyLog;
import mikhailbolgov.balda.R;


public class AppSettingsActivity extends Activity implements View.OnClickListener {

    private RelativeLayout lytBackgroundColor1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        context = this;

        lytBackgroundColor1 = (RelativeLayout)findViewById(R.id.app_background_color_1_lyt);
        lytBackgroundColor1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == lytBackgroundColor1){
            MyLog.log("On rl tap");
            lytBackgroundColor1.addView(new ColorSelectedView(this));
        }
    }

    private class ColorSelectedView extends RelativeLayout{

        public ColorSelectedView(Context context) {
            super(context);
            inflate(context, R.layout.color_selected_dark, this);
        }
    }
}
