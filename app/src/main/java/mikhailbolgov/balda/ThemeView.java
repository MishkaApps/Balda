package mikhailbolgov.balda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mbolg on 07.03.2017.
 */
public class ThemeView extends RelativeLayout {
    public ThemeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        MyLog.log("onSizeChanged");
        ViewGroup.LayoutParams newParams =  getLayoutParams();
        newParams.width = h;
        setLayoutParams(newParams);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MyLog.log("onMeasure");
        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }
}
