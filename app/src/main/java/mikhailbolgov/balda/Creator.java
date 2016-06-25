package mikhailbolgov.balda;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by mbolg on 04.04.2016.
 */
public class Creator extends AsyncTask<Integer, Void, Pair<Node, Node>> {

    private Resources resources;
    private final int THR1 = 1, THR2 = 2, THR3 = 3, THR4 = 4;
    private VocsCreatedListener vocsCreatedListener;

    public Creator(Resources resources) {
        this.resources = resources;
    }

    @Override
    protected Pair<Node, Node> doInBackground(Integer... params) {
        Vector<String> words = new Vector<>();
        XmlPullParser parser = resources.getXml(R.xml.comp_voc_large);
        int wordsAmount = 0;

        int lLimit = 0, uLimit = 0;
        int part = params[0];


        Node rootNode = new Node(false, null);
        Node invRootNode = new Node(true, rootNode);


        switch (part) {
            case THR1:
                lLimit = LIBRARY_CONSTANTS.COMP_VOC_1_BEG;
                uLimit = LIBRARY_CONSTANTS.COMP_VOC_1_END;
                break;
            case THR2:
                lLimit = LIBRARY_CONSTANTS.COMP_VOC_2_BEG;
                uLimit = LIBRARY_CONSTANTS.COMP_VOC_2_END;
                break;
            case THR3:
                lLimit = LIBRARY_CONSTANTS.COMP_VOC_3_BEG;
                uLimit = LIBRARY_CONSTANTS.COMP_VOC_3_END;
                break;
            case THR4:
                lLimit = LIBRARY_CONSTANTS.COMP_VOC_4_BEG;
                uLimit = LIBRARY_CONSTANTS.COMP_VOC_4_END;
                break;
        }

        long start = System.currentTimeMillis();


        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    if (wordsAmount > lLimit)
                        words.add(parser.getAttributeValue(0));
                    ++wordsAmount;
                }
                if (wordsAmount > uLimit)
                    break;
                if (isCancelled()) {
                    break;
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isCancelled()) {
            return null;
        }


        for (String word : words)
            rootNode.add(word, word);

        String invWord, invSubWord;
        for (String word : words) {
            invWord = inverse(word);
            for (int counter = 0; counter < word.length(); ) {
                invSubWord = invWord.substring(counter);
                invRootNode.add(invSubWord, invSubWord);
                ++counter;
            }
        }

        return new Pair<>(rootNode, invRootNode);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Pair<Node, Node> nodeNodePair) {
        super.onPostExecute(nodeNodePair);
        vocsCreatedListener.vocsCreated();
    }

    private static String inverse(String string) {
        String tempString = "";
        int counter = string.length();
        while (--counter >= 0)
            tempString += string.charAt(counter);
        return tempString;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void setVocsCreatedListener(VocsCreatedListener vocsCreatedListener) {
        this.vocsCreatedListener = vocsCreatedListener;
    }
}
