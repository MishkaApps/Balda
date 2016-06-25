package mikhailbolgov.balda;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import mikhailbolgov.balda.Fragments.GameFragment;

public class Computer extends Player implements Serializable {
    public Intellect intellect1, intellect2, intellect3, intellect4;
    private Balance balance;
    private Word lastWord;
    private Creator creator1,
            creator2,
            creator3,
            creator4;
    private int mode;

    private enum THREAD {FIRST, SECOND, THIRD, FOURTH}

    private boolean vocsCreated;


    public void vocsCreated() {
        vocsCreated = true;
    }

    public Computer(int number, Balda balda, Resources resources, int mode) {
        super(resources.getString(R.string.computer_name), number);
        creator1 = new Creator(resources);
        creator2 = new Creator(resources);
        creator3 = new Creator(resources);
        creator4 = new Creator(resources);

        creator1.execute(1);
        creator2.execute(2);
        creator3.execute(3);
        creator4.execute(4);

        intellect1 = null;
        intellect2 = null;
        intellect3 = null;
        intellect4 = null;

        this.mode = mode;
        vocsCreated = false;
    }

    public void refresh(Resources resources, VocsCreatedListener vocsCreatedListener) {


        creator1 = new Creator(resources);
        creator2 = new Creator(resources);
        creator3 = new Creator(resources);
        creator4 = new Creator(resources);

        creator1.setVocsCreatedListener(vocsCreatedListener);
        creator2.setVocsCreatedListener(vocsCreatedListener);
        creator3.setVocsCreatedListener(vocsCreatedListener);
        creator4.setVocsCreatedListener(vocsCreatedListener);

        creator1.execute(1);
        creator2.execute(2);
        creator3.execute(3);
        creator4.execute(4);

        vocsCreated = false;
    }

    public void setVocsCreatedListener(VocsCreatedListener vocsCreatedListener) {
        creator1.setVocsCreatedListener(vocsCreatedListener);
        creator2.setVocsCreatedListener(vocsCreatedListener);
        creator3.setVocsCreatedListener(vocsCreatedListener);
        creator4.setVocsCreatedListener(vocsCreatedListener);
    }


    public void clear() {
        if (creator1 != null)
            creator1.cancel(true);
        if (creator2 != null)
            creator2.cancel(true);
        if (creator3 != null)
            creator3.cancel(true);
        if (creator4 != null)
            creator4.cancel(true);

        intellect1 = null;
        intellect2 = null;
        intellect3 = null;
        intellect4 = null;

        creator1 = null;
        creator2 = null;
        creator3 = null;
        creator4 = null;
    }

    public void move(GameFragment gameFragment, Balda balda) {

        if (intellect1 == null
                | intellect2 == null
                | intellect3 == null
                | intellect4 == null) {

            try {
                intellect1 = new Intellect(balda, creator1.get(), THREAD.FIRST);
                intellect2 = new Intellect(balda, creator1.get(), THREAD.SECOND);
                intellect3 = new Intellect(balda, creator1.get(), THREAD.THIRD);
                intellect4 = new Intellect(balda, creator2.get(), THREAD.FOURTH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        Thread thread1 = new Thread(intellect1);
        Thread thread2 = new Thread(intellect2);
        Thread thread3 = new Thread(intellect3);
        Thread thread4 = new Thread(intellect4);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Word> words = new ArrayList<>();
        words.addAll(intellect1.getWords());
        words.addAll(intellect2.getWords());
        words.addAll(intellect3.getWords());
        words.addAll(intellect4.getWords());

        int length = 0;
        Word maxWord = null;
        int lengthLimit = new Balance(mode).getLength();
        for (Word word : words)
            if (word.getSize() > length) {
                if (balda.isWordUsed(word) | word.getSize() > lengthLimit)
                    continue;
                maxWord = word;
                length = maxWord.getSize();
            }

        intellect1.clear();
        intellect2.clear();
        intellect3.clear();
        intellect4.clear();

        lastWord = maxWord;

        if (maxWord != null)
            gameFragment.input(maxWord);
        else {
            gameFragment.passMove();
        }
    }


    public Word getLastWord() {
        return lastWord;
    }

    public boolean isVocsCreated() {
        return vocsCreated;
    }


    public class Intellect implements Serializable, Runnable {
        private Balda balda;
        private final int N = 5;
        private TestBalda testBalda;
        private ArrayList<Word> fullWords;
        private ArrayList<Word> prefixes;
        private Node voc;
        private Node invVoc;
        private final int forecastAmount = 15;
        private final int WORD_SEARCH_STAGES = 3;
        private THREAD thread;

        public Word getWord() {
            int length = 0;
            Word maxWord = null;
            int lengthLimit = balance.getLength();

            for (Word word : fullWords)
                if (word.getSize() > length) {
                    if (balda.isWordUsed(word) | word.getSize() > lengthLimit)
                        continue;
                    maxWord = word;
                    length = maxWord.getSize();
                }
            fullWords = new ArrayList<>();
            prefixes = new ArrayList<>();
            return maxWord;
        }

        public Intellect(Balda balda, Pair<Node, Node> vocs, THREAD thread) {
            this.balda = balda;
            this.voc = vocs.first;
            this.invVoc = vocs.second;
            fullWords = new ArrayList<>();
            prefixes = new ArrayList<>();
            this.thread = thread;
        }

        @Override
        public void run() {
            wordSearch();
        }

        public void wordSearch() {
            testBalda = new TestBalda(balda);
            double startTime, finishTime;
            double startTimeForecast, finishTimeForecast;

            startTime = startTimeForecast = System.currentTimeMillis();

            finishTimeForecast = System.currentTimeMillis();

            testBalda = new TestBalda(balda);
            Vector<Cell> availCells = testBalda.getAvailableToChange();

            for (Cell availCell : availCells) {
                for (Character letter : Alphabet.getAlphabet()) {
                    testBalda.addTestCell(new Cell(availCell.getRow(), availCell.getCol(), letter));
                    findPrefixes(prefixes, fullWords, availCell, new Word(), invVoc, availCell);
                    findWords(fullWords, availCell, new Word(), voc);

                    testBalda.deleteTestCell();
                }
            }

            finishTime = System.currentTimeMillis();
        }

        public void findWords(ArrayList<Word> fullWords, Cell currentCell, Word currentWord, Node treeNode) {

            if (!currentWord.addLetter(currentCell)) {
                return;
            }

            treeNode = treeNode.getNext(currentCell.getLetter());

            if (treeNode == null) {
                return;
            }

            if (treeNode.wordExist()) {
                fullWords.add(currentWord.copy());
            }

            if (!treeNode.treeContinue()) {
                return;
            }


            if (currentCell.getCol() < N - 1)
                if (testBalda.getCell(currentCell.getRow(), currentCell.getCol() + 1).isSet()) {
                    Cell cellR = testBalda.getCell(currentCell.getRow(), currentCell.getCol() + 1);
                    findWords(fullWords, cellR, currentWord.copy(), treeNode);
                }

            if (currentCell.getRow() < N - 1)
                if (testBalda.getCell(currentCell.getRow() + 1, currentCell.getCol()).isSet()) {
                    Cell cellB = testBalda.getCell(currentCell.getRow() + 1, currentCell.getCol());
                    findWords(fullWords, cellB, currentWord.copy(), treeNode);
                }

            if (currentCell.getRow() > 0)
                if (testBalda.getCell(currentCell.getRow() - 1, currentCell.getCol()).isSet()) {
                    Cell cellA = testBalda.getCell(currentCell.getRow() - 1, currentCell.getCol());
                    findWords(fullWords, cellA, currentWord.copy(), treeNode);
                }
            if (currentCell.getCol() > 0) {
                if (testBalda.getCell(currentCell.getRow(), currentCell.getCol() - 1).isSet()) {
                    Cell cellL = testBalda.getCell(currentCell.getRow(), currentCell.getCol() - 1);
                    findWords(fullWords, cellL, currentWord.copy(), treeNode);
                }
            }


            return;
        }

        public void findPrefixes(ArrayList<Word> prefixes, ArrayList<Word> fullWords, Cell currentCell, Word currentPrefix, Node nextInvNode, Cell startCell) {

            if (!currentPrefix.addLetter(currentCell)) {
                return;
            }

            nextInvNode = nextInvNode.getNext(currentCell.getLetter());

            if (nextInvNode == null)
                return;

            if (nextInvNode.wordExist()) {
                prefixes.add(currentPrefix.copy());
                Word newCurrentPrefix = currentPrefix.inverse();
                newCurrentPrefix.removeLastCell();
                findWords(fullWords, startCell, newCurrentPrefix, voc.get(newCurrentPrefix.toString()));
            }

            if (!nextInvNode.treeContinue()) {
                return;
            }


            if (currentCell.getCol() < N - 1)
                if (testBalda.getCell(currentCell.getRow(), currentCell.getCol() + 1).isSet()) {
                    Cell cellR = testBalda.getCell(currentCell.getRow(), currentCell.getCol() + 1);
                    findPrefixes(prefixes, fullWords, cellR, currentPrefix.copy(), nextInvNode, startCell);
                }

            if (currentCell.getRow() < N - 1)
                if (testBalda.getCell(currentCell.getRow() + 1, currentCell.getCol()).isSet()) {
                    Cell cellB = testBalda.getCell(currentCell.getRow() + 1, currentCell.getCol());
                    findPrefixes(prefixes, fullWords, cellB, currentPrefix.copy(), nextInvNode, startCell);
                }

            if (currentCell.getRow() > 0)
                if (testBalda.getCell(currentCell.getRow() - 1, currentCell.getCol()).isSet()) {
                    Cell cellA = testBalda.getCell(currentCell.getRow() - 1, currentCell.getCol());
                    findPrefixes(prefixes, fullWords, cellA, currentPrefix.copy(), nextInvNode, startCell);
                }
            if (currentCell.getCol() > 0) {
                if (testBalda.getCell(currentCell.getRow(), currentCell.getCol() - 1).isSet()) {
                    Cell cellL = testBalda.getCell(currentCell.getRow(), currentCell.getCol() - 1);
                    findPrefixes(prefixes, fullWords, cellL, currentPrefix.copy(), nextInvNode, startCell);
                }
            }
            return;

        }

        public ArrayList<Word> getWords() {
            return fullWords;
        }

        public void clear() {
            fullWords = new ArrayList<>();
            prefixes = new ArrayList<>();
        }


        private class TestBalda extends Balda {

            private Cell testCell;

            public TestBalda(Balda parBalda) {
                super(parBalda.getSettings());

                Cell tempCells[][] = new Cell[N][N];

                for (int row = 0; row < N; ) {
                    for (int col = 0; col < N; ) {
                        if (balda.cells[row][col].isSet())
                            tempCells[row][col] = new Cell(row, col, parBalda.cells[row][col].getLetter());
                        else
                            tempCells[row][col] = new Cell(row, col);
                        ++col;
                    }
                    ++row;
                }

                cells = tempCells;
                Vector<Cell> tempUsedCells = new Vector<>();
                for (Cell cell : parBalda.usedCells)
                    tempUsedCells.add(cells[cell.getRow()][cell.getCol()]);
                super.usedCells = tempUsedCells;

                for (Word word : parBalda.usedWords)
                    usedWords.add(word.copy());


            }

            public void addTestCell(Cell cell) {
                testCell = cell;
                cells[cell.getRow()][cell.getCol()].setLetter(cell.getLetter());
                usedCells.add(cell);
            }

            public void deleteTestCell() {
                cells[testCell.getRow()][testCell.getCol()].unset(true);
                usedCells.remove(testCell);
            }

            public Cell getTestCell() {
                return testCell;
            }
        }

    }
}
