package mikhailbolgov.balda;

import java.io.Serializable;

/**
 * Created by Михаил on 22.03.2015.
 */
public class Cell  implements Serializable {
    private int row, col;
    private char letter;
    private boolean set;

    public String string(){
        return "cell[" + row + "][" + col + "] = " + letter;
    }

    public Cell(int row, int col, char letter){
        this.row = row;
        this.col = col;
        this.letter = letter;
        set = true;
    }

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        set = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public char getLetter() {
        return letter;
    }

    public boolean isSet(){
        return set;
    }

    public void set() {
        set = true;
    }


    public void setLetter(char letter) {
        this.letter = letter;
        set();
    }


    public void unset() {
        set = false;
    }
    public void unset(boolean flag) {
        letter = ' ';
        set = false;
    }

    public boolean isEqual(Cell cell) {
        return (row == cell.getRow() && col == cell.getCol());
    }

    public Cell copy(){
        return new Cell(row, col, letter);
    }

    public String toCell() {
        return "[" + row + "]" + "[" + col + "] = " + letter ;
    }
}
