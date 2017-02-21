package mikhailbolgov.balda.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import mikhailbolgov.balda.Node;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChangeable;
import mikhailbolgov.balda.ThemeChanger;

public class UserVocabulary extends VocViewer implements ThemeChangeable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_voc);
        listView = (ListView) findViewById(R.id.listView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        clearButton = (Button) findViewById(R.id.clearButton);
        backButton = (Button) findViewById(R.id.backButton);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (words.size() == 0)
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Вы уверены, что хотите удалить все слова?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.clear();
                        library.clearUserVoc(context);
                        simpleAdapter.notifyDataSetChanged();
                        changeVocState();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(getFilesDir(), context.getResources().getString(R.string.user_voc_file_name))));
            voc = (Node) objectInputStream.readObject();
            voc.getWords(words);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        data = new ArrayList<>();
        HashMap<String, String> item;
        int counter = 1;
        for (String word : words) {
            item = new HashMap<>();
            item.put(NUMBER_TAG, Integer.toString(counter++));
            item.put(WORD_TAG, word);
            data.add(item);
        }

        String[] tags = new String[]{NUMBER_TAG, WORD_TAG};

        ThemeChanger themeChanger = new ThemeChanger(this);
        if (themeChanger.backgroundTextColorIsDark()) {
            int[] textViews = new int[]{R.id.voc_item_number, R.id.voc_item};
            simpleAdapter = new SimpleAdapter(context, data, R.layout.voc_item_dark_text, tags, textViews);
        } else {
            int[] textViews = new int[]{R.id.voc_item_number_white, R.id.voc_item_white};
            simpleAdapter = new SimpleAdapter(context, data, R.layout.voc_item_white_text, tags, textViews);
        }

        registerForContextMenu(listView);


        if (words != null) {
            listView.setAdapter(simpleAdapter);
        }
        changeVocState();
    }

    private void changeVocState() {

        if (data.size() == 0) {
            listView.setVisibility(View.GONE);
            findViewById(R.id.emptyVocTextView).setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyVocTextView).setVisibility(View.GONE);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        HashMap<String, String> selectedItem = (HashMap<String, String>) listView.getItemAtPosition(info.position);


        lastSelectedItem = selectedItem.get(WORD_TAG);

        menu.add(Menu.NONE, 0, 0, ITEM_DELETE);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        voc.delete(lastSelectedItem, lastSelectedItem);
        library.updateUserVoc(context, voc);

        data.clear();

        words = new ArrayList<>();
        voc.getWords(words);

        HashMap<String, String> vocItem;
        int counter = 1;
        for (String word : words) {
            vocItem = new HashMap<>();
            vocItem.put(NUMBER_TAG, Integer.toString(counter++));
            vocItem.put(WORD_TAG, word);
            data.add(vocItem);
        }

        simpleAdapter.notifyDataSetChanged();


        changeVocState();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
    }

    @Override
    public void setTheme() {
        ThemeChanger themeChanger = new ThemeChanger(this);
        ArrayList<View> header = new ArrayList<>();
        header.add(findViewById(R.id.lytUserVocabularyHeader));
        themeChanger.applyTheme(this, (ViewGroup) findViewById(R.id.lytUserVocabularyBackground), header);

    }
}
