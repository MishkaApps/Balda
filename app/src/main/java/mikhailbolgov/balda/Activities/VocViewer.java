package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import mikhailbolgov.balda.Library;
import mikhailbolgov.balda.Node;

public abstract class VocViewer extends Activity {
    protected ListView listView;
    protected Context context;
    protected ArrayList<String> words;
    protected ArrayAdapter<String> adapter;
    protected SimpleAdapter simpleAdapter;
    protected final String NUMBER_TAG = "number", WORD_TAG = "word";
    protected final String ITEM_DELETE = "Удалить";
    protected String lastSelectedItem;
    protected Node voc;
    protected Library library;
    protected ArrayList<HashMap<String, String>>  data;
    protected Button clearButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        words = new ArrayList<>();
        library = new Library();
    }
}
