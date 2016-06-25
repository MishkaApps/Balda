package mikhailbolgov.balda;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Михаил on 20.03.2015.
 */
public class Word implements Serializable {

    private Vector<Cell> word;

    public Word() {
        word = new Vector<>();
    }

    public Word(Vector<Cell> word) {
        this.word = word;
    }

    public boolean addLetter(Cell cell) {
        if (word.size() == 0 && cell.isSet()) {
            word.add(cell);
            return true;
        }
        for (Cell tempCell : word) {
            if (cell == tempCell) {
                return false;
            }
        }
        for (Cell tempCell : word)
            if (cell.isEqual(tempCell)) {
                return false;
            }
        if (word.size() != 0 && cell.isSet()) {

            Cell lastCell = word.lastElement();
            if ((Math.abs(cell.getRow() - lastCell.getRow()) == 1 & (cell.getCol() == lastCell.getCol()))
                    | (Math.abs(cell.getCol() - lastCell.getCol()) == 1 & (cell.getRow() == lastCell.getRow()))) {
                word.add(cell);
                return true;
            }
        }
        return false;
    }

    public boolean equals(Word word) {

        if (this.word.size() != word.getSize())
            return false;

        int counter = 0;
        while (counter < getSize()) {
            if (this.word.get(counter).getLetter() != word.getCells().get(counter).getLetter())
                return false;
            ++counter;
        }

        return true;

    }


    public Vector<Cell> getCells() {
        return word;
    }

    public String toString() {
        String string = "";
        for (Cell cell : word)
            string = string + cell.getLetter();
        return string;
    }


    public int getPoints() {
        return word.size();
    }

    public int getSize() {
        return word.size();
    }

    public boolean isCorrect(Cell cell) {
        for (Cell tempCell : word)
            if (tempCell.isEqual(cell)) {
                return true;
            }

        return false;

    }
/*
    public boolean exist(Resources resources) {

        int id;
        switch (word.firstElement().getLetter()) {
            case 'а':
            case 'А':
                id = R.xml.voc_a.xml;
                break;
            case 'б':
            case 'Б':
                id = R.xml.voc_b.xml;
                break;
            case 'в':
            case 'В':
                id = R.xml.voc_v.xml;
                break;
            case 'г':
            case 'Г':
                id = R.xml.voc_gg;
                break;
            case 'д':
            case 'Д':
                id = R.xml.voc_d.xml;
                break;
            case 'е':
            case 'Е':
            case 'ё':
            case 'Ё':
                id = R.xml.voc_e.xml;
                break;
            case 'ж':
            case 'Ж':
                id = R.xml.voc_g.xml;
                break;
            case 'з':
            case 'З':
                id = R.xml.voc_z.xml;
                break;
            case 'и':
            case 'И':
                id = R.xml.voc_i.xml;
                break;
            case 'й':
            case 'Й':
                id = R.xml.voc_y.xml;
                break;
            case 'к':
            case 'К':
                id = R.xml.voc_k.xml;
                break;
            case 'л':
            case 'Л':
                id = R.xml.voc_l.xml;
                break;
            case 'м':
            case 'М':
                id = R.xml.voc_m.xml;
                break;
            case 'н':
            case 'Н':
                id = R.xml.voc_n.xml;
                break;
            case 'о':
            case 'О':
                id = R.xml.voc_o.xml;
                break;
            case 'п':
            case 'П':
                id = R.xml.voc_p.xml;
                break;
            case 'р':
            case 'Р':
                id = R.xml.voc_r.xml;
                break;
            case 'с':
            case 'С':
                id = R.xml.voc_s.xml;
                break;
            case 'т':
            case 'Т':
                id = R.xml.voc_t.xml;
                break;
            case 'у':
            case 'У':
                id = R.xml.voc_u.xml;
                break;
            case 'ф':
            case 'Ф':
                id = R.xml.voc_f.xml;
                break;
            case 'х':
            case 'Х':
                id = R.xml.voc_h;
                break;
            case 'ц':
            case 'Ц':
                id = R.xml.voc_c;
                break;
            case 'ч':
            case 'Ч':
                id = R.xml.voc_ch.xml;
                break;
            case 'ш':
            case 'Ш':
                id = R.xml.voc_sh.xml;
                break;
            case 'щ':
            case 'Щ':
                id = R.xml.voc_ssh;
                break;
            case 'э':
            case 'Э':
                id = R.xml.voc_ee;
                break;
            case 'ю':
            case 'Ю':
                id = R.xml.voc_yu.xml;
                break;
            case 'я':
            case 'Я':
                id = R.xml.voc_ya.xml;
                break;
            default:
                return false;
        }

        XmlPullParser parser = resources.getXml(id);
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {

                    if (equals(parser.getAttributeValue(1)))
                        return true;
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    private boolean equals(String string) {
        if (toString().length() != string.length()) {
            return false;
        }
        int counter = 0;
        while (counter < string.length()) {
            if (toString().toLowerCase().charAt(counter) != string.charAt(counter))
                return false;
            ++counter;
        }
        return true;
    }

    private String toLowerCase() {
        String string = "";
        char ch = ' ';
        for (Cell cell : word) {
            switch (cell.getLetter()) {
                case 'А':
                    ch = 'a';
                    break;
                case 'Б':
                    ch = 'б';
                    break;
                case 'В':
                    ch = 'в';
                    break;
                case 'Г':
                    ch = 'г';
                    break;
                case 'Д':
                    ch = 'д';
                    break;
                case 'Е':
                    ch = 'е';
                    break;
                case 'Ж':
                    ch = 'ж';
                    break;
                case 'З':
                    ch = 'з';
                    break;
                case 'И':
                    ch = 'и';
                    break;
                case 'Й':
                    ch = 'й';
                    break;
                case 'К':
                    ch = 'к';
                    break;
                case 'Л':
                    ch = 'л';
                    break;
                case 'М':
                    ch = 'м';
                    break;
                case 'Н':
                    ch = 'н';
                    break;
                case 'О':
                    ch = 'о';
                    break;
                case 'П':
                    ch = 'п';
                    break;
                case 'Р':
                    ch = 'р';
                    break;
                case 'С':
                    ch = 'с';
                    break;
                case 'Т':
                    ch = 'т';
                    break;
                case 'У':
                    ch = 'у';
                    break;
                case 'Ф':
                    ch = 'ф';
                    break;
                case 'Х':
                    ch = 'х';
                    break;
                case 'Ц':
                    ch = 'ц';
                    break;
                case 'Ч':
                    ch = 'ч';
                    break;
                case 'Ш':
                    ch = 'ш';
                    break;
                case 'Щ':
                    ch = 'щ';
                    break;
                case 'Ь':
                    ch = 'ь';
                    break;
                case 'Ъ':
                    ch = 'ъ';
                    break;
                case 'Э':
                    ch = 'э';
                    break;
                case 'Ю':
                    ch = 'ю';
                    break;
                case 'Я':
                    ch = 'я';
                    break;

            }
            string += ch;
        }
        return string;
    }


    public Word copy() {
        Vector<Cell> newCellVector = new Vector<>();
        for (Cell cell : word)
            newCellVector.add(cell.copy());
        Word newWord = new Word(newCellVector);
        return newWord;
    }

    public Word inverse() {
        Vector<Cell> tempWord = new Vector<>();
        int counter = word.size();
        while (--counter >= 0)
            tempWord.add(word.elementAt(counter));
        return new Word(tempWord);
    }

    public void removeLastCell() {
        word.remove(word.size() - 1);
    }

    public String toWord() {
        String res = "";
        for (Cell cell : word)
            res += "[" + cell.getLetter() + "] ";
        return res;
    }
}
