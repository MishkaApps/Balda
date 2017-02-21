package mikhailbolgov.balda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mikhailbolgov.balda.Balda;
import mikhailbolgov.balda.Player;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.ThemeChangeable;
import mikhailbolgov.balda.ThemeChanger;
import mikhailbolgov.balda.Word;

/**
 * Created by Михаил on 25.04.2015.
 */
public class ScoreFragment extends Fragment implements ThemeChangeable {

    private Balda balda;
    private View contentView;
    private final String wordTag = "WORD", wordPointsTag = "WORD_POINTS";
    private ArrayList<HashMap<String, String>> data1, data2;
    private SimpleAdapter adapter1, adapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            balda = (Balda) savedInstanceState.getSerializable("balda");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.score_fragment, container, false);


        final ListView lvPlayer1 = (ListView) contentView.findViewById(R.id.lvPlayer1);
        final ListView lvPlayer2 = (ListView) contentView.findViewById(R.id.lvPlayer2);

        TextView tvPlayer1Name = (TextView) contentView.findViewById(R.id.tv_player1_name);
        TextView tvPlayer2Name = (TextView) contentView.findViewById(R.id.tv_player2_name);


        ArrayList<Player> players = balda.getPlayers();

        tvPlayer1Name.setText(players.get(0).getName());
        tvPlayer2Name.setText(players.get(1).getName());

        ThemeChanger themeChanger = new ThemeChanger(getActivity());

        data1 = new ArrayList<>();

        HashMap<String, String> item;

        for (Word word : players.get(0).getWords()) {
            item = new HashMap<>();
            item.put(wordTag, word.toString());
            item.put(wordPointsTag, Integer.toString(word.getPoints()));
            data1.add(item);
        }

        String from[] = {wordTag, wordPointsTag};
        int to[] = {R.id.tv_word, R.id.tv_word_points};

        if (themeChanger.backgroundTextColorIsDark())
            adapter1 = new SimpleAdapter(contentView.getContext(), data1, R.layout.score_item_dark_text, from, to);
        else
            adapter1 = new SimpleAdapter(contentView.getContext(), data1, R.layout.score_item_white_text, from, to);

        lvPlayer1.setAdapter(adapter1);

        data2 = new ArrayList<>();
        for (Word word : players.get(1).getWords()) {
            item = new HashMap<>();
            item.put(wordTag, word.toString());
            item.put(wordPointsTag, Integer.toString(word.getPoints()));
            data2.add(item);
        }

        if (themeChanger.backgroundTextColorIsDark())
            adapter2 = new SimpleAdapter(contentView.getContext(), data2, R.layout.score_item_dark_text, from, to);
        else
            adapter2 = new SimpleAdapter(contentView.getContext(), data2, R.layout.score_item_white_text, from, to);

        lvPlayer2.setAdapter(adapter2);

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTheme();
    }

    public void receiveData(Balda balda) {
        this.balda = balda;
    }

    public void update() {
        final ListView lvPlayer1 = (ListView) contentView.findViewById(R.id.lvPlayer1);
        final ListView lvPlayer2 = (ListView) contentView.findViewById(R.id.lvPlayer2);

        final ArrayList<Player> players = balda.getPlayers();

        data1.clear();
        data2.clear();

        HashMap<String, String> item;

        int pointsCounter = 0;

        for (Word word : players.get(0).getWords()) {
            item = new HashMap<>();
            if (word.getSize() > 10)
                item.put(wordTag, word.toString().substring(0, 10) + "..");
            else
                item.put(wordTag, word.toString());
            pointsCounter += word.getPoints();
            item.put(wordPointsTag, Integer.toString(pointsCounter));
            data1.add(item);
        }

        adapter1.notifyDataSetChanged();

        pointsCounter = 0;
        data2.clear();
        for (Word word : players.get(1).getWords()) {
            item = new HashMap<>();
            item.put(wordTag, word.toString());
            pointsCounter += word.getPoints();
            item.put(wordPointsTag, Integer.toString(pointsCounter));
            data2.add(item);
        }

        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("balda", balda);
    }

    @Override
    public void setTheme() {
        ThemeChanger themeChanger = new ThemeChanger(getActivity());
        ArrayList<View> header = new ArrayList<>();
        header.add(getView().findViewById(R.id.tvScoreFragmentHeader));


        themeChanger.applyTheme(getContext(), (ViewGroup) getView().findViewById(R.id.lytScoreFragmentBackground), header);

    }
}

