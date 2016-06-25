package mikhailbolgov.balda;

import android.app.Activity;
import android.content.res.Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import mikhailbolgov.balda.Fragments.GameFragment;


public class Balda implements Serializable, Modes {
    private String firstWord;
    private ArrayList<Player> players;
    protected final int N = 5;
    private int currentPlayer;
    private int numberOfPlayers;
    protected Vector<Word> usedWords;
    private Settings settings;
    private int mode;

    protected Cell cells[][];
    protected Vector<Cell> usedCells;

    public Balda(Settings settings) {
        this.settings = settings;

        mode = PLAYER_VS_PLAYER;

        usedCells = new Vector<>();

        usedWords = new Vector<>();

        firstWord = settings.getFirstWord();

        ArrayList<String> tempNames = settings.getNames();

        players = new ArrayList<>(tempNames.size());
        for (String name : tempNames)
            players.add(new Player(name, tempNames.indexOf(name)));


        cells = new Cell[N][N];

        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
                cells[row][col] = new Cell(row, col);
                ++col;
            }
            ++row;
        }

        currentPlayer = 0;
        numberOfPlayers = players.size();
    }

    public boolean isModeTimeLimited() {
        if (mode == Modes.PLAYER_VS_PLAYER_30
                | mode == Modes.PLAYER_VS_PLAYER_1m
                | mode == Modes.PLAYER_VS_PLAYER_2m)
            return true;
        else return false;
    }

    public Balda(Settings settings, Resources resources) {

        usedCells = new Vector<>();
        usedWords = new Vector<>();

        ArrayList<String> tempNames = settings.getNames();
        this.mode = settings.getMode();
        switch (mode) {
            case PLAYER_VS_COMPUTER:
            case PLAYER_VS_COMPUTER_E:
            case PLAYER_VS_COMPUTER_H:
                this.settings = settings;
                firstWord = settings.getFirstWord();
                players = new ArrayList<>(tempNames.size());
                players.add(new Computer(0, this, resources, mode));
                players.add(new Player(tempNames.get(1), 1));
                break;
            case PLAYER_VS_COMPUTER_SECOND:
            case PLAYER_VS_COMPUTER_E_SECOND:
            case PLAYER_VS_COMPUTER_H_SECOND:
                this.settings = settings;
                firstWord = settings.getFirstWord();
                players = new ArrayList<>(tempNames.size());
                players.add(new Player(tempNames.get(0), 0));
                players.add(new Computer(1, this, resources, mode));
                break;
            case PLAYER_VS_PLAYER:
            case PLAYER_VS_PLAYER_30:
            case PLAYER_VS_PLAYER_1m:
            case PLAYER_VS_PLAYER_2m:
                this.settings = settings;
                firstWord = settings.getFirstWord();

                players = new ArrayList<>(tempNames.size());
                for (String name : tempNames)
                    players.add(new Player(name, tempNames.indexOf(name)));

                break;
        }

        cells = new Cell[N][N];

        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
                cells[row][col] = new Cell(row, col);
                ++col;
            }
            ++row;
        }


        currentPlayer = 0;
        numberOfPlayers = players.size();

    }

    public String getFirstWord() {
        return firstWord;
    }


    public Cell getCell(int row, int col) {
        return cells[row][col];
    }


    public Vector<Cell> getAvailableToChange() {

        Vector<Cell> cellVector = new Vector<>();
        boolean flag = false;
        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
                if (!cells[row][col].isSet()) {
                    try {
                        flag = flag | isCellUsed(row + 1, col);
                    } catch (Exception ignored) {
                    }
                    try {
                        flag = flag | isCellUsed(row - 1, col);
                    } catch (Exception ignored) {
                    }
                    try {
                        flag = flag | isCellUsed(row, col + 1);
                    } catch (Exception ignored) {
                    }
                    try {
                        flag = flag | isCellUsed(row, col - 1);
                    } catch (Exception ignored) {
                    }
                    if (flag)
                        cellVector.add(cells[row][col]);
                    flag = false;
                }
                ++col;
            }
            ++row;
        }
        return cellVector;
    }

    public boolean isAvailableToChange(Cell cell) {
        for (Cell tempCell : getAvailableToChange()) {
            if (cell == tempCell)
                return true;
        }
        return false;
    }

    public void setFirstWord(Word word) {

        for (Cell cell : word.getCells()) {
            cells[cell.getRow()][cell.getCol()].setLetter(cell.getLetter());
            usedCells.add(cells[cell.getRow()][cell.getCol()]);
        }
        usedWords.add(word);

    }

    public boolean isCellUsed(int row, int col) {
        for (Cell tempCell : usedCells) {
            if ((tempCell.getRow() == row && tempCell.getCol() == col)) return true;
//                    && tempCell == cells[row][col]) {
//                MyLog.log("isCellUsed " + tempCell.string());
//            }

//            if (tempCell == cells[row][col]){
//                return true;}

        }
        return false;
    }


    public char getCellLetter(int row, int col) {
        return cells[row][col].getLetter();
    }


    public boolean isCellUsed(Cell cell) {
        return isCellUsed(cell.getRow(), cell.getCol());                        // симс лайк ит ворк аналагично, но если что сотри и раскоменть
//        for (Cell tempCell : usedCells)
//            if (tempCell == cells[cell.getRow()][cell.getCol()])
//                return true;
//        return false;


    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addWord(Word newWord) {
        if (newWord == null) {
            skipMove();
        } else {
            for (Cell letter : newWord.getCells()) {
                cells[letter.getRow()][letter.getCol()].setLetter(letter.getLetter());
                if (!isCellUsed(letter))
                    usedCells.add(letter);
            }
            usedWords.add(newWord);
            players.get(currentPlayer).addWord(newWord);
            currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        }
    }

    public boolean isWordUsed(Word word) {
        int size = word.getSize();
        for (Word tempWord : usedWords) {
            if (tempWord.getSize() != word.getSize())
                continue;
            if (word.equals(tempWord))
                return true;

        }
        return false;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public boolean isGameOver() {
        if(this.usedCells.size() == 25)
            return true;
        int wordNumber1 = players.get(0).wordNumber(),
                wordNumber2 = players.get(1).wordNumber();
        if ((wordNumber1 + wordNumber2) % 2 == 0)
            if (players.get(0).wordNumber() == 10 | players.get(1).wordNumber() == 10)
                return true;
        return false;
    }

    public Player getWinner() {
        if (players.get(0).getScore() > players.get(1).getScore())
            return players.get(0);
        else if (players.get(0).getScore() < players.get(1).getScore())
            return players.get(1);
        return null;
    }

    public boolean winnerExist() {
        return players.get(0).getScore() != players.get(1).getScore();
    }

    public Vector<Cell> getUsedCells() {
        return usedCells;
    }

    public Settings getSettings() {
        return settings;
    }

    public Resources getResources() {
        return null;//resources;
    }

    public int getMode() {
        return mode;
    }

    public boolean isCurrentPlayerComputer() {
        if (players.get(currentPlayer).getClass() == Computer.class)
            return true;
        else return false;
    }

    public Computer getComputer() {
        for (Player player : players)
            if (player.getClass() == Computer.class)
                return (Computer) player;
        return null;
    }

    public boolean isWordUsed(String word) {
        for (Word usedWord : usedWords)
            if (usedWord.toString().equals(word))
                return true;
        return false;
    }

    public Player getNotActivePlayer() {
        for (Player player : players)
            if (player.getNumber() != currentPlayer)
                return player;
        return null;
    }

    public int getMoveTime() {
        switch (mode) {
            case Modes.PLAYER_VS_PLAYER_30:
                return 30;
            case Modes.PLAYER_VS_PLAYER_1m:
                return 60;
            case Modes.PLAYER_VS_PLAYER_2m:
                return 120;
        }
        return 0;
    }

    public void skipMove() {
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
    }

    public boolean isModeGameWithComputer() {
        if (mode == Modes.PLAYER_VS_COMPUTER
                | mode == Modes.PLAYER_VS_COMPUTER_E
                | mode == Modes.PLAYER_VS_COMPUTER_H
                | mode == Modes.PLAYER_VS_COMPUTER_SECOND
                | mode == Modes.PLAYER_VS_COMPUTER_E_SECOND
                | mode == Modes.PLAYER_VS_COMPUTER_H_SECOND)
            return true;
        else return false;
    }

    public void computerMove(GameFragment gameFragment) {
        ((Computer) getCurrentPlayer()).move(gameFragment, this);
    }


}
