package mikhailbolgov.balda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

public class SquareButton extends Button {
    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, h, oldw, oldh);
        ViewGroup.LayoutParams newParams = getLayoutParams();
        newParams.width = h;
        setLayoutParams(newParams);
    }
}
