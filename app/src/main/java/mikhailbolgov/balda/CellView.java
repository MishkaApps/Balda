package mikhailbolgov.balda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Михаил on 10.06.2015.
 */
public class CellView extends TextView {
    private Cell cell;

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    public void hide(){
        setText("");
    }

    public void show() {
        setText("" + cell.getLetter());
    }
}