package mikhailbolgov.balda;

import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class VocTreeCreator implements Serializable{

    private Node rootNode, rootNodeInv;
    private Vector<String> testVoc;

    public VocTreeCreator(Resources resources) {

        Vector<String> words = new Vector<>();
        XmlPullParser parser = resources.getXml(R.xml.voc);

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

        rootNode = new Node(false);
        rootNodeInv = new Node(true);

        testVoc = words;

        for (String word : words)
            rootNode.add(word, word);

        String invWord, invSubWord;
        for (String word : words) {
            invWord = inverse(word);
            for (int counter = 0; counter < word.length(); ) {
                invSubWord = invWord.substring(counter);
                rootNodeInv.add(invSubWord, invSubWord);
                ++counter;
            }
        }
    }

    public Vector<String> getVoc() {
        return testVoc;
    }


    public Node getRootInvNode() {
        return this.rootNodeInv;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getNode(String prefix) {
        return rootNode.get(prefix);
    }

    public class Node implements Serializable{

        protected HashMap<Character, Node> next;
        protected String word;
        private Node treeNodeRef;
        private boolean inv;

        public Node(boolean inv) {
            next = new HashMap<>();
            word = null;
            this.inv = inv;
            treeNodeRef = null;
        }

        public void add(String wordEnd, String word) {
            if (wordEnd.length() == 0) {
                this.word = word;

                if (inv)
                    treeNodeRef = rootNode.getWordBegRef(inverse(word));

                return;
            }
            char wordEndBeg = wordEnd.charAt(0);
            if (next.containsKey(wordEndBeg))
                next.get(wordEndBeg).add(wordEnd.substring(1), word);
            else {
                next.put(wordEndBeg, new Node(inv));
                next.get(wordEndBeg).add(wordEnd.substring(1), word);
            }
        }

        protected Node getWordBegRef(String string) {
            if (string.length() == 0)
                return this;

            if (next.containsKey(string.charAt(0)))
                return getWordBegRef(string.substring(1));
            else
                return null;

        }

        public boolean wordExist() {
            if (word == null)
                return false;
            return true;
        }

        public boolean exist(String wordEnd, String word) {
            if (wordEnd.length() == 0) {
                if (this.word != null && this.word.equals(word))
                    return true;
                else return false;
            }
            char wordEndBeg = wordEnd.charAt(0);
            if (next.containsKey(wordEndBeg))
                return next.get(wordEndBeg).exist(wordEnd.substring(1), word);
            else return false;
        }

        public boolean maybeExistPrefix(String prefix) {
            if (prefix.length() == 0)
                return true;

            char wordEndBeg = prefix.charAt(0);
            if (next.containsKey(wordEndBeg))
                return next.get(wordEndBeg).maybeExistPrefix(prefix.substring(1));
            else return false;
        }


        public Node getNext(char letter) {

            if (next.containsKey(letter))
                return next.get(letter);
            else return null;
        }


        public boolean treeContinue() {
            if (next.size() > 0)
                return true;
            return false;
        }

        public Node get(String prefix) {
            if(prefix.length() == 0)
                return this;

            if(next.containsKey(prefix.charAt(0)))
                return next.get(prefix.charAt(0)).get(prefix.substring(1));
            else return null;
        }
    }

    public boolean oldSearch(String word) {
        for (String temp : testVoc)
            if (temp.equals(word))
                return true;
        return false;
    }

    private String inverse(String string) {
        String tempString = "";
        int counter = string.length();
        while (--counter >= 0)
            tempString += string.charAt(counter);
        return tempString;
    }
}

