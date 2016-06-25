package mikhailbolgov.balda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Node implements Serializable {
    static final long serialVersionUID = 4033L;
    protected HashMap<Character, Node> next;
    protected String word;
    private Node treeNodeRef;
    private boolean inv;
    private Node rootNode;


    public Node(boolean inv, Node rootNode) {
        next = new HashMap<>();
        word = null;
        this.inv = inv;
        treeNodeRef = null;
        if (rootNode == null)
            this.rootNode = this;
        else
            this.rootNode = rootNode;
    }

    public void getWords(ArrayList<String> storage) {
        if (word != null) {
            storage.add(word);
        }
        for (Character ch : next.keySet())
            next.get(ch).getWords(storage);
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
            next.put(wordEndBeg, new Node(inv, rootNode));
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
        if (word != null)
            return true;
        return false;
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
        else return false;


//        if (next.size() > 0)
//            return true;
//        return false;
    }

    public Node get(String prefix) {
        if (prefix.length() == 0)
            return this;

        if (next.containsKey(prefix.charAt(0)))
            return next.get(prefix.charAt(0)).get(prefix.substring(1));
        else return null;
    }

    public boolean delete(String wordEnd, String fullWord) {
        if (wordEnd.length() == 0) {
            if (word != null)
                if (word.equals(fullWord)) {
                    word = null;
                    if (next.keySet().size() == 0)
                        return true;
                    else return false;
                } else return false;
        } else {
            char ch = wordEnd.charAt(0);
            boolean deleteBranch = next.get(ch).delete(wordEnd.substring(1), fullWord);
            if (deleteBranch && !(next.size() > 1)) {

                next.remove(ch);
                return false;
            } else return deleteBranch;
        }
        return false;

    }

    private String inverse(String string) {
        String tempString = "";
        int counter = string.length();
        while (--counter >= 0)
            tempString += string.charAt(counter);
        return tempString;
    }

    public void changeRootNode(Node rootNode) {
        this.rootNode = rootNode;
        for (Character ch : next.keySet())
            next.get(ch).changeRootNode(rootNode);
    }

}
