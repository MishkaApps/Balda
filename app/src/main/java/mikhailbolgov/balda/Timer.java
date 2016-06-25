package mikhailbolgov.balda;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Михаил on 02.07.2015.
 */
public class Timer extends TextView implements View.OnClickListener{
    private CountDownTimer timer;
    private TimerEventListener timerEventListener;
    private int moveTime;
    private int timeToFinish;

    public Timer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("");
        setOnClickListener(this);
    }

    public void createTimer(final TimerEventListener timerEventListener, int moveTime) {
        this.timerEventListener = timerEventListener;
        this.moveTime = moveTime;

        if (moveTime < 60)
            if (moveTime >= 10)
                setText((Integer.valueOf(moveTime)).toString());
            else setText("0" + (Integer.valueOf(moveTime)).toString());
        else if (moveTime % 60 != 0)
            setText(moveTime / 60 + ":" + moveTime % 60);
        else setText(moveTime / 60 + ":" + moveTime % 60 + "0");

        timer = new CountDownTimer(moveTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeToFinish = (int) millisUntilFinished / 1000;
                if (millisUntilFinished / 1000 < 60)
                    if ((Integer.valueOf((int) millisUntilFinished / 1000)) >= 10)
                        setText((Integer.valueOf((int) millisUntilFinished / 1000)).toString());
                    else
                        setText("0" + (Integer.valueOf((int) millisUntilFinished / 1000)).toString());
                else if ((int) (millisUntilFinished / 1000) % 60 >= 10)
                    setText((int) millisUntilFinished / (1000 * 60) + ":" + (int) (millisUntilFinished / 1000) % 60);
                else
                    setText((int) millisUntilFinished / (1000 * 60) + ":0" + (int) (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                setText("00");
                timerEventListener.moveEnd();
            }
        };
    }

    public void createTimer() {
        if (timeToFinish < 60)
            if (timeToFinish >= 10)
                setText((Integer.valueOf(timeToFinish)).toString());
            else setText("0" + (Integer.valueOf(timeToFinish)).toString());
        else if (timeToFinish % 60 != 0)
            setText(timeToFinish / 60 + ":" + timeToFinish % 60);
        else setText(timeToFinish / 60 + ":" + timeToFinish % 60 + "0");

        timer = new CountDownTimer(timeToFinish * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Timer.this.timeToFinish = (int) millisUntilFinished / 1000;
                if (millisUntilFinished / 1000 < 60)
                    if ((Integer.valueOf((int) millisUntilFinished / 1000)) >= 10)
                        setText((Integer.valueOf((int) millisUntilFinished / 1000)).toString());
                    else
                        setText("0" + (Integer.valueOf((int) millisUntilFinished / 1000)).toString());
                else if ((int) (millisUntilFinished / 1000) % 60 >= 10)
                    setText((int) millisUntilFinished / (1000 * 60) + ":" + (int) (millisUntilFinished / 1000) % 60);
                else
                    setText((int) millisUntilFinished / (1000 * 60) + ":0" + (int) (millisUntilFinished / 1000) % 60);
            }

            @Override
            public void onFinish() {
                setText("00");
                timerEventListener.moveEnd();
            }
        };
    }

    public void pause() {

        timer.cancel();
    }

    public void resumeCountdown() {
        createTimer();
        timer.start();
    }

    @Override
    public void onClick(View v) {
        timerEventListener.pause();
    }

    public interface TimerEventListener {
        void moveEnd();
        void pause();
    }

    public void reset() {
        timer.cancel();
        if (moveTime < 60)
            if (moveTime >= 10)
                setText((Integer.valueOf(moveTime)).toString());
            else setText("0" + (Integer.valueOf(moveTime)).toString());
        else if (moveTime % 60 != 0)
            setText(moveTime / 60 + ":" + moveTime % 60);
        else setText(moveTime / 60 + ":" + moveTime % 60 + "0");

    }

    public void start() {
        timer.cancel();
        createTimer(timerEventListener, moveTime);
        timer.start();
    }

}
