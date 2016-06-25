package mikhailbolgov.balda.Activities;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import mikhailbolgov.balda.Balda;
import mikhailbolgov.balda.Fragments.GameFragment;
import mikhailbolgov.balda.Fragments.ScoreFragment;
import mikhailbolgov.balda.MyLog;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.Settings;


/**
 * Created by Михаил on 25.04.2015.
 */
public class GameActivity extends FragmentActivity implements View.OnClickListener, GameFragment.BaldaReceiver, GameFragment.OnAddWordListener, ViewPager.OnPageChangeListener, GameFragment.OnTimerPauseListener {

    private Settings settings;
    private GameFragment gameFragment;
    private ScoreFragment scoreFragment;
    private long lastBackPress;
    private Toast toast;
    private RelativeLayout demoLayout;
    private ImageView dragFinger;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        pager = (ViewPager) findViewById(R.id.game_score_view_pager);
        final int PAGE_NUMBER = 2;
        settings = (Settings) getIntent().getSerializableExtra("settings");
        lastBackPress = 0;
        toast = Toast.makeText(this, R.string.toast_tap_again, Toast.LENGTH_SHORT);

        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Object fragment = super.instantiateItem(container, position);
                switch (position) {
                    case 0:
                        gameFragment = (GameFragment) fragment;
                        break;
                    case 1:
                        scoreFragment = (ScoreFragment) fragment;
                }
                return fragment;

            }

            @Override
            public Fragment getItem(int i) {

                switch (i) {
                    case 0:
                        gameFragment = new GameFragment();
                        gameFragment.receiveSettings(settings);
                        return gameFragment;
                    case 1:
                        scoreFragment = new ScoreFragment();
                        return scoreFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return PAGE_NUMBER;
            }
        };


        pager.setAdapter(adapter);

        demoLayout = (RelativeLayout) findViewById(R.id.demo_layout);
        demoLayout.setVisibility(View.GONE);                                          // todo FINGER HIDE !!
        dragFinger = (ImageView) findViewById(R.id.image_view_drag_finger);

        Animation dragFingerAnim = AnimationUtils.loadAnimation(this, R.anim.finger_drag);
        dragFinger.startAnimation(dragFingerAnim);
        pager.setOnPageChangeListener(this);


    }

    @Override
    public void receiveBalda(Balda balda) {
        scoreFragment.receiveData(balda);
    }

    @Override
    public void onWordAdded() {
        scoreFragment.update();
    }


    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - lastBackPress < 1500) {
            toast.cancel();
            super.onBackPressed();
        } else {
            lastBackPress = System.currentTimeMillis();
            toast.show();
        }

    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                gameFragment.resumeTimer();
                break;
            case 1:
                if (demoLayout.getVisibility() != View.GONE)
                    demoLayout.setVisibility(View.GONE);
                gameFragment.pauseTimer();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void timerPause() {
        pager.setCurrentItem(1, true);
    }
}
