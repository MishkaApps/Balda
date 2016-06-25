package mikhailbolgov.balda;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Михаил on 10.04.2015.
 */
public class Settings implements Serializable, Modes {
    private ArrayList<String> names;
    private String firstWord;
    private int mode;

    public Settings(ArrayList<String> names, String firstWord, int mode) {
        this.names = names;
        this.firstWord = firstWord;
        this.mode = mode;
    }


    public ArrayList<String> getNames() {
        return names;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public int getMode() {
        return mode;
    }
}
