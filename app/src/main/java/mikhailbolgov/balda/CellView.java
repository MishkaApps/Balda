package mikhailbolgov.balda;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Михаил on 10.06.2015.
 */
public class CellView extends TextView {
    private Cell cell;

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.game_field_letter_size));
    }
    public void setCell(Cell cell){
        this.cell = cell;
    }
    public Cell getCell() {
        return cell;
    }
    public void setLetter(char letter){
        String string = "";
        string += letter;
        setText(string);
        cell.setLetter(letter);
    }

    public void clean() {
        setText("");
        cell.unset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewGroup.LayoutParams newParams =  getLayoutParams();
        newParams.width = h;
        setLayoutParams(newParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }

    public void hide(){
        setText("");
    }

    public void show() {
        setText("" + cell.getLetter());
    }
}
