package mikhailbolgov.balda;

import java.util.ArrayList;

/**
 * Created by Михаил on 19.06.2015.
 */
public class Alphabet {
    private static final ArrayList<Character> alphabet = new ArrayList<>();

    public static void Alphabet() {
        alphabet.add(new Character('А'));
        alphabet.add(new Character('Б'));
        alphabet.add(new Character('В'));
        alphabet.add(new Character('Г'));
        alphabet.add(new Character('Д'));
        alphabet.add(new Character('Е'));
        alphabet.add(new Character('Ж'));
        alphabet.add(new Character('З'));
        alphabet.add(new Character('И'));
        alphabet.add(new Character('Й'));
        alphabet.add(new Character('К'));
        alphabet.add(new Character('Л'));
        alphabet.add(new Character('М'));
        alphabet.add(new Character('Н'));
        alphabet.add(new Character('О'));
        alphabet.add(new Character('П'));
        alphabet.add(new Character('Р'));
        alphabet.add(new Character('С'));
        alphabet.add(new Character('Т'));
        alphabet.add(new Character('У'));
        alphabet.add(new Character('Ф'));
        alphabet.add(new Character('Х'));
        alphabet.add(new Character('Ц'));
        alphabet.add(new Character('Ч'));
        alphabet.add(new Character('Ш'));
        alphabet.add(new Character('Щ'));
        alphabet.add(new Character('Ь'));
        alphabet.add(new Character('Ы'));
        alphabet.add(new Character('Ъ'));
        alphabet.add(new Character('Э'));
        alphabet.add(new Character('Ю'));
        alphabet.add(new Character('Я'));
    }

    synchronized public static ArrayList<Character> getAlphabet() {    // сомтаймс эта функция крошит мой код, т.к. ее пыта
        if (alphabet.size() == 0) {
            alphabet.add(new Character('А'));
            alphabet.add(new Character('Б'));
            alphabet.add(new Character('В'));
            alphabet.add(new Character('Г'));
            alphabet.add(new Character('Д'));
            alphabet.add(new Character('Е'));
            alphabet.add(new Character('Ж'));
            alphabet.add(new Character('З'));
            alphabet.add(new Character('И'));
            alphabet.add(new Character('Й'));
            alphabet.add(new Character('К'));
            alphabet.add(new Character('Л'));
            alphabet.add(new Character('М'));
            alphabet.add(new Character('Н'));
            alphabet.add(new Character('О'));
            alphabet.add(new Character('П'));
            alphabet.add(new Character('Р'));
            alphabet.add(new Character('С'));
            alphabet.add(new Character('Т'));
            alphabet.add(new Character('У'));
            alphabet.add(new Character('Ф'));
            alphabet.add(new Character('Х'));
            alphabet.add(new Character('Ц'));
            alphabet.add(new Character('Ч'));
            alphabet.add(new Character('Ш'));
            alphabet.add(new Character('Щ'));
            alphabet.add(new Character('Ь'));
            alphabet.add(new Character('Ы'));
            alphabet.add(new Character('Ъ'));
            alphabet.add(new Character('Э'));
            alphabet.add(new Character('Ю'));
            alphabet.add(new Character('Я'));
        }
        return alphabet;
    }

    public static String getLatin(char ch) {
        switch (ch) {
            case 'А':
            case 'а':
                return "a";

            case 'Б':
            case 'б':
                return "b";

            case 'В':
            case 'в':
                return "v";

            case 'Г':
            case 'г':
                return "g";

            case 'Д':
            case 'д':
                return "d";

            case 'Е':
            case 'е':
            case 'Ё':
            case 'ё':
                return "e";

            case 'Ж':
            case 'ж':
                return "zh";

            case 'З':
            case 'з':
                return "z";

            case 'И':
            case 'и':
                return "i";

            case 'Й':
            case 'й':
                return "j";

            case 'К':
            case 'к':
                return "k";

            case 'Л':
            case 'л':
                return "l";

            case 'М':
            case 'м':
                return "m";

            case 'Н':
            case 'н':
                return "n";

            case 'О':
            case 'о':
                return "o";

            case 'П':
            case 'п':
                return "p";

            case 'Р':
            case 'р':
                return "r";

            case 'С':
            case 'с':
                return "s";

            case 'Т':
            case 'т':
                return "t";

            case 'У':
            case 'у':
                return "u";

            case 'Ф':
            case 'ф':
                return "f";

            case 'Х':
            case 'х':
                return "kh";

            case 'Ц':
            case 'ц':
                return "ts";

            case 'Ч':
            case 'ч':
                return "ch";

            case 'Ш':
            case 'ш':
                return "sh";

            case 'Щ':
            case 'щ':
                return "shch";

            case 'Ь':
            case 'ь':
                return "ss";

            case 'Ы':
            case 'ы':
                return "y";

            case 'Ъ':
            case 'ъ':
                return "hs";

            case 'Э':
            case 'э':
                return "re";

            case 'Ю':
            case 'ю':
                return "yu";

            case 'Я':
            case 'я':
                return "ya";

        }
        return null;
    }
}
