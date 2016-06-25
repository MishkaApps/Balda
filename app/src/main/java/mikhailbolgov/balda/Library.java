package mikhailbolgov.balda;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Library implements Serializable {
    private VocTreeCreator vocTreeCreator;
    private Node rootNodeTest, invRootNodeTest;
    static final long serialVersionUID = LIBRARY_CONSTANTS.SERIAL_VERSION_UID;

    public Library() {
        vocTreeCreator = new VocTreeCreator();

        rootNodeTest = new Node(false, null);
        invRootNodeTest = new Node(true, rootNodeTest);
    }


    public boolean exist(Context context, Resources resources, String word) {   // можно сохранять уже созданные словари
        Character firstChar = word.charAt(0);

        boolean exist = vocTreeCreator.createVoc(resources, firstChar).get(firstChar).exist(word, word);

        if (!exist)
            try {
                exist = existInUserVoc(context, word);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        return exist;
    }

    private boolean existInUserVoc(Context context, String word) throws IOException, ClassNotFoundException {
        File userVocFile = new File(context.getFilesDir(), context.getResources().getString(R.string.user_voc_file_name));
        if (userVocFile.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(userVocFile));
            Node userVoc = (Node) objectInputStream.readObject();
            return userVoc.exist(word, word);
        } else return false;
    }

    public void addUserWord(Context context, String word) {
        File userVocFile = new File(context.getFilesDir(), context.getResources().getString(R.string.user_voc_file_name));

        try {
            if (!userVocFile.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(userVocFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                Node node = new Node(false, rootNodeTest);
                objectOutputStream.writeObject(node);
                objectOutputStream.flush();
                objectOutputStream.close();
            }

            FileInputStream fileInputStream = new FileInputStream(userVocFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Node node = (Node) objectInputStream.readObject();         // если че не так, может дело в отсутствии метода node.setRootNode()
            node.add(word, word);

            FileOutputStream fileOutputStream = new FileOutputStream(userVocFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(node);
            objectOutputStream.flush();
            objectOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public class VocTreeCreator implements Serializable {

        private int thread;
        private Node rootNode, invRootNode;
        private Resources resources;

        public VocTreeCreator(int thread, Node rootNode, Node invRootNode) {
            this.thread = thread;
            this.rootNode = rootNode;
            this.invRootNode = invRootNode;
        }

        public VocTreeCreator() {
        }




        public HashMap<Character, Node> createVoc(Resources resources, char ch) {
            Vector<String> words = new Vector<>();
            XmlPullParser parser = null;
            Character.toUpperCase(ch);
            HashMap<Character, Node> vocs = new HashMap<>();

            switch (ch) {
                case 'А':
                case 'а':
                    parser = resources.getXml(R.xml.voc_a);
                    break;
                case 'Б':
                case 'б':
                    parser = resources.getXml(R.xml.voc_b);
                    break;
                case 'В':
                case 'в':
                    parser = resources.getXml(R.xml.voc_v);
                    break;
                case 'Г':
                case 'г':
                    parser = resources.getXml(R.xml.voc_g);
                    break;
                case 'Д':
                case 'д':
                    parser = resources.getXml(R.xml.voc_d);
                    break;
                case 'Е':
                case 'е':
                case 'Ё':
                case 'ё':
                    parser = resources.getXml(R.xml.voc_e);
                    break;
                case 'Ж':
                case 'ж':
                    parser = resources.getXml(R.xml.voc_zh);
                    break;
                case 'З':
                case 'з':
                    parser = resources.getXml(R.xml.voc_z);
                    break;
                case 'И':
                case 'и':
                    parser = resources.getXml(R.xml.voc_i);
                    break;
                case 'Й':
                case 'й':
                    parser = resources.getXml(R.xml.voc_j);
                    break;
                case 'К':
                case 'к':
                    parser = resources.getXml(R.xml.voc_k);
                    break;
                case 'Л':
                case 'л':
                    parser = resources.getXml(R.xml.voc_l);
                    break;
                case 'М':
                case 'м':
                    parser = resources.getXml(R.xml.voc_m);
                    break;
                case 'Н':
                case 'н':
                    parser = resources.getXml(R.xml.voc_n);
                    break;
                case 'О':
                case 'о':
                    parser = resources.getXml(R.xml.voc_o);
                    break;
                case 'П':
                case 'п':
                    parser = resources.getXml(R.xml.voc_p);
                    break;
                case 'Р':
                case 'р':
                    parser = resources.getXml(R.xml.voc_r);
                    break;
                case 'С':
                case 'с':
                    parser = resources.getXml(R.xml.voc_s);
                    break;
                case 'Т':
                case 'т':
                    parser = resources.getXml(R.xml.voc_t);
                    break;
                case 'У':
                case 'у':
                    parser = resources.getXml(R.xml.voc_u);
                    break;
                case 'Ф':
                case 'ф':
                    parser = resources.getXml(R.xml.voc_f);
                    break;
                case 'Х':
                case 'х':
                    parser = resources.getXml(R.xml.voc_kh);
                    break;
                case 'Ц':
                case 'ц':
                    parser = resources.getXml(R.xml.voc_ts);
                    break;
                case 'Ч':
                case 'ч':
                    parser = resources.getXml(R.xml.voc_ch);
                    break;
                case 'Ш':
                case 'ш':
                    parser = resources.getXml(R.xml.voc_sh);
                    break;
                case 'Щ':
                case 'щ':
                    parser = resources.getXml(R.xml.voc_shch);
                    break;
                case 'Ь':
                case 'ь':
                    parser = resources.getXml(R.xml.voc_ss);
                    break;
                case 'Ы':
                case 'ы':
                    parser = resources.getXml(R.xml.voc_y);
                    break;
                case 'Ъ':
                case 'ъ':
                    parser = resources.getXml(R.xml.voc_hs);
                    break;
                case 'Э':
                case 'э':
                    parser = resources.getXml(R.xml.voc_re);
                    break;
                case 'Ю':
                case 'ю':
                    parser = resources.getXml(R.xml.voc_yu);
                    break;
                case 'Я':
                case 'я':
                    parser = resources.getXml(R.xml.voc_ya);
                    break;
            }

            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word"))
                        words.add(parser.getAttributeValue(0));
                    parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Node node = new Node(false, rootNode);
            vocs.put(ch, node);

            for (String word : words)
                node.add(word, word);

            return vocs;
        }

        public void setResouces(Resources resources) {
            this.resources = resources;
        }
    }

    private static String inverse(String string) {
        String tempString = "";
        int counter = string.length();
        while (--counter >= 0)
            tempString += string.charAt(counter);
        return tempString;
    }

    public void clearUserVoc(Context context) {
        File userVocFile = new File(context.getFilesDir(), context.getResources().getString(R.string.user_voc_file_name));
        userVocFile.delete();
    }

    public void updateUserVoc(Context context, Node newVoc) {
        File userVocFile = new File(context.getFilesDir(), context.getResources().getString(R.string.user_voc_file_name));

        try {
            userVocFile.delete();

            FileOutputStream fileOutputStream = new FileOutputStream(userVocFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            Node node = newVoc;
            objectOutputStream.writeObject(node);
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
