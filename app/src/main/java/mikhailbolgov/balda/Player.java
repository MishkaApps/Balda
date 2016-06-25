package mikhailbolgov.balda;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Михаил on 20.03.2015.
 */
public class Player implements Serializable{
    private String name;
    private int score;
    private ArrayList<Word> words;
    private int number;

    public Player(String name, int number){
        this.name = name;
        score = 0;
        words = new ArrayList<>();
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addWord(Word newWord) {
        words.add(newWord);
        score += newWord.getPoints();
    }

    public int getNumber(){return number;}

    public ArrayList<Word> getWords() {
        return words;
    }
    public int wordNumber(){
        return words.size();
    }

}
