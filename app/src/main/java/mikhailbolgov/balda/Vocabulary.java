package mikhailbolgov.balda;

import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Михаил on 13.06.2015.
 */
public class Vocabulary implements Serializable {


    private static Resources resources;
    private final int lastInFirstPart;
    private final int lastInSecondPart;
    private final int lastInThirdPart;
    private int numberOfWords;

    public Vocabulary(Resources resources) {
        this.resources = resources;

        numberOfWords = 0;


        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word"))
                    ++numberOfWords;
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastInFirstPart = numberOfWords / 4;
        lastInSecondPart = numberOfWords / 2;
        lastInThirdPart = 3 * numberOfWords / 4;
//        MyLog.log("1 " + lastInFirstPart);
//        MyLog.log("2 " + lastInSecondPart);
//        MyLog.log("3 " + lastInThirdPart);

    }

    public boolean wordWithBeginning(String beg) {

        XmlPullParser parser = resources.getXml(R.xml.voc);
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {

                    String vocWordBeg = parser.getAttributeValue(0);
                    if (vocWordBeg.length() <= beg.length()) {
                        parser.next();
                        continue;
                    }
                    if (vocWordBeg.substring(0, beg.length()).equals(beg))
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
    }

    public boolean wordExist(String beg) {
        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {

                    String vocWordBeg = parser.getAttributeValue(0);
                    if (vocWordBeg.length() != beg.length()) {
                        parser.next();
                        continue;
                    }
                    if (vocWordBeg.substring(0, beg.length()).equals(beg))
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
    }

    public Vector<String> getWords() {

        Vector<String> wordsVector = new Vector<>();

        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word"))
                    wordsVector.add(parser.getAttributeValue(0));
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsVector;
    }

    public Vector<String> getFirstPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (wordCounter <= lastInFirstPart) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    wordsVector.add(parser.getAttributeValue(0));
                    ++wordCounter;
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MyLog.log("first part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getSecondPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && wordCounter < lastInFirstPart) {
                    ++wordCounter;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    ++wordCounter;
                    wordsVector.add(parser.getAttributeValue(0));
                }
                parser.next();
                if (wordCounter > lastInSecondPart)
                    break;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MyLog.log("second part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getThirdPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && wordCounter < lastInSecondPart) {
                    ++wordCounter;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    ++wordCounter;
                    wordsVector.add(parser.getAttributeValue(0));
                }
                parser.next();
                if (wordCounter > lastInThirdPart)
                    break;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MyLog.log("third part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getFourthPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        XmlPullParser parser = resources.getXml(R.xml.voc);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && wordCounter < lastInThirdPart) {
                    ++wordCounter;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    ++wordCounter;
                    wordsVector.add(parser.getAttributeValue(0));
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MyLog.log("fourth part: " + wordsVector.size());
        return wordsVector;
    }


    public Vector<String> mixVoc(Vector<String> voc) {

        Random random = new Random();
        String tempWord;
        Vector<String> mixedVoc = new Vector<>();
        while (voc.size() > 0) {
            tempWord = voc.get(random.nextInt(voc.size()));
            mixedVoc.add(tempWord);
            voc.remove(tempWord);
        }

        voc.addAll(mixedVoc);
        return voc;
    }

    public Vector<String> getFirstExtPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        Vector<Integer> resVocs = new Vector<>();
        resVocs.add(R.xml.voc_a);
        resVocs.add(R.xml.voc_b);
        resVocs.add(R.xml.voc_v);
        resVocs.add(R.xml.voc_g);
        resVocs.add(R.xml.voc_d);
        resVocs.add(R.xml.voc_e);
        resVocs.add(R.xml.voc_zh);
        resVocs.add(R.xml.voc_i);
        resVocs.add(R.xml.voc_j);



        for (Integer res : resVocs) {

            XmlPullParser parser = resources.getXml(res);

            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                        wordsVector.add(parser.getAttributeValue(0));
                        ++wordCounter;
                    }
                    parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        MyLog.log("first ext part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getSecondExtPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        Vector<Integer> resVocs = new Vector<>();

        resVocs.add(R.xml.voc_k);
        resVocs.add(R.xml.voc_l);
        resVocs.add(R.xml.voc_m);


        for (Integer res : resVocs) {

            XmlPullParser parser = resources.getXml(res);

            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                        wordsVector.add(parser.getAttributeValue(0));
                        ++wordCounter;
                    }
                    parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        MyLog.log("second ext part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getThirdExtPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        Vector<Integer> resVocs = new Vector<>();

        resVocs.add(R.xml.voc_n);
        resVocs.add(R.xml.voc_o);
        resVocs.add(R.xml.voc_p);


        for (Integer res : resVocs) {

            XmlPullParser parser = resources.getXml(res);

            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                        wordsVector.add(parser.getAttributeValue(0));
                        ++wordCounter;
                    }
                    parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        MyLog.log("third ext part: " + wordsVector.size());
        return wordsVector;
    }

    public Vector<String> getFourthExtPart() {

        Vector<String> wordsVector = new Vector<>();

        int wordCounter = 0;

        Vector<Integer> resVocs = new Vector<>();

        resVocs.add(R.xml.voc_r);
        resVocs.add(R.xml.voc_s);
        resVocs.add(R.xml.voc_t);
        resVocs.add(R.xml.voc_u);
        resVocs.add(R.xml.voc_f);
        resVocs.add(R.xml.voc_kh);
        resVocs.add(R.xml.voc_ts);
        resVocs.add(R.xml.voc_ch);
        resVocs.add(R.xml.voc_sh);
        resVocs.add(R.xml.voc_hs);
        resVocs.add(R.xml.voc_y);
        resVocs.add(R.xml.voc_ss);
        resVocs.add(R.xml.voc_re);
        resVocs.add(R.xml.voc_yu);
        resVocs.add(R.xml.voc_ya);

        for (Integer res : resVocs) {

            XmlPullParser parser = resources.getXml(res);

            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                        wordsVector.add(parser.getAttributeValue(0));
                        ++wordCounter;
                    }
                    parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        MyLog.log("fourth ext part: " + wordsVector.size());
        return wordsVector;
    }


}
