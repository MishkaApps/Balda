package mikhailbolgov.balda.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import mikhailbolgov.balda.Fragments.Keyboard;
import mikhailbolgov.balda.Library;
import mikhailbolgov.balda.Modes;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.Settings;
import mikhailbolgov.balda.ThemeChangeable;
import mikhailbolgov.balda.ThemeChanger;

import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;


public class SettingsActivity extends FragmentActivity implements OnClickListener, Modes, CompoundButton.OnCheckedChangeListener, ThemeChangeable {

    private Button btnOk, btnChangeWord;
    private TextView tvFirstWord;
    private TextView tvPlayer1, tvPlayer2;
    private int fiveLetterWordCounter;
    private Random random;
    private Keyboard keyboard;
    private LinearLayout keyboardLayout;
    private TextView inFocus;
    private Button firstWordBackspace, player1Backspace, player2Backspace;
    private CheckBox pvcModeBox;
    private Spinner timeChooser, difficultyChooser;
    private String[] timeArray, difficultyArray;
    //    private LinearLayout underKeyboardLayout;
    private ViewSwitcher modeSwitcher;
    private Animation kbrdApAnim, kbrdDisapAnim, underKbrdApAnim, underKbrdDisapAnim;
    private LinearLayout timeChooserLayout, difficultyChooserLayout;
    private Animation timeChsrApAnim, timeChsrDisapAnim, difChsrApAnim, difChsrDisapAnim;
    private Animation bckspceApAnim, bckspceDisapAnim;
    private Animation chgWrdAnim;
    private Animation swapApAnim, swapDisapAnim;
    private File myFile;
    public Library library;
    private Button btnMenu;
    private ArrayList<View> underKeyboardViews;
    private Button btnSwap;
    private final int FIRST_WORD = 0, NAME1 = 1, NAME2 = 2;
    private ThemeChanger themeChanger;
    private TextView tvTimeChooserLabel, tvDifficultyChooserLabel;
    private int textViewBackground, textViewInFocus;

    @Override
    public void setTheme() {


        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(findViewById(R.id.lytSettingsHeader));
        headerNFooter.add(findViewById(R.id.btnSettingsOk));
        themeChanger.applyTheme(this, (ViewGroup) findViewById(R.id.lytGameSettingsBackground), headerNFooter);
    }

    private enum ORDER {COMP_FIRST, COMP_SECOND}

    private ORDER order;

    private class SpinnerAdapter extends ArrayAdapter<String> {
        private String[] objects;

        public SpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down_item, null);
            TextView view = (TextView) convertView.findViewById(R.id.drop_down_item);
            view.setText(objects[position]);
            view.setTextColor(themeChanger.getBackgroundTextColor());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down_item, null);
            TextView view = (TextView) convertView.findViewById(R.id.drop_down_item);
            view.setText(objects[position]);
            view.setTextColor(themeChanger.getBackgroundTextColor());
            return convertView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        timeChooserLayout = (LinearLayout) findViewById(R.id.timeChooseLayout);
        difficultyChooserLayout = (LinearLayout) findViewById(R.id.difficultyChooseLayout);

        timeChsrApAnim = AnimationUtils.loadAnimation(this, R.anim.time_stgs_ap);
        timeChsrDisapAnim = AnimationUtils.loadAnimation(this, R.anim.time_stgs_disap);

        difChsrApAnim = AnimationUtils.loadAnimation(this, R.anim.dif_stgs_ap);
        difChsrDisapAnim = AnimationUtils.loadAnimation(this, R.anim.dif_stgs_disap);

        bckspceApAnim = AnimationUtils.loadAnimation(this, R.anim.bckspce_ap);
        bckspceDisapAnim = AnimationUtils.loadAnimation(this, R.anim.bckspce_disap);

        chgWrdAnim = AnimationUtils.loadAnimation(this, R.anim.update_word_anim);

        swapApAnim = AnimationUtils.loadAnimation(this, R.anim.swap_ap);
        swapDisapAnim = AnimationUtils.loadAnimation(this, R.anim.swap_disap);

        tvTimeChooserLabel = (TextView) findViewById(R.id.tvSettingsTimeChooser);
        tvDifficultyChooserLabel = (TextView) findViewById(R.id.tvSettingsDifficultyChooser);

        themeChanger = new ThemeChanger(this);


        textViewBackground = themeChanger.getTextViewBackgroundResource();
        textViewInFocus = themeChanger.getTextViewInFocusBackgroundResource();

        FragmentManager fragmentManager = getSupportFragmentManager();

        keyboard = (Keyboard) fragmentManager.findFragmentByTag("keyboard");
        keyboardLayout = (LinearLayout) findViewById(R.id.keyboardLayout);
        keyboardLayout.setVisibility(View.INVISIBLE);

        underKeyboardViews = new ArrayList<>();
        underKeyboardViews.add(findViewById(R.id.mode_checkbox));
        underKeyboardViews.add(findViewById(R.id.btnSettingsOk));
        underKeyboardViews.add(findViewById(R.id.gameModeSwitcher));

        random = new Random();

        btnSwap = (Button) findViewById(R.id.btn_swap);
        btnSwap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pvcModeBox.isChecked()) {
                    String firstName = tvPlayer1.getText().toString();
                    tvPlayer1.setText(tvPlayer2.getText());
                    tvPlayer2.setText(firstName);
                } else {
                    switch (order) {
                        case COMP_FIRST:
                            order = ORDER.COMP_SECOND;
                            break;
                        case COMP_SECOND:
                            order = ORDER.COMP_FIRST;
                            break;
                    }
                    updateCompNameState();
                }


            }
        });

        btnOk = (Button) findViewById(R.id.btnSettingsOk);
        btnOk.setOnClickListener(this);
        btnMenu = (Button) findViewById(R.id.button_menu);
        btnMenu.setOnClickListener(this);

        tvFirstWord = (TextView) findViewById(R.id.tvFirstWord);
        tvFirstWord.setText("БАЛДА");
        tvFirstWord.setOnClickListener(this);
        tvFirstWord.setBackgroundResource(textViewBackground);

        pvcModeBox = (CheckBox) findViewById(R.id.pvc_mode);
        pvcModeBox.setOnCheckedChangeListener(this);

        pvcModeBox.setChecked(false);

        timeArray = getResources().getStringArray(R.array.time_list);
        difficultyArray = getResources().getStringArray(R.array.difficulty_list);

//        timeChooser = (Spinner) findViewById(R.id.timeChooser);
//        ArrayAdapter<String> timeChooserAdapter = new ArrayAdapter(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, timeArray);
//        timeChooser.setAdapter(timeChooserAdapter);
//        timeChooser.setSelection(timeChooserAdapter.getCount() - 1);


        tvTimeChooserLabel.setTextColor(themeChanger.getBackgroundTextColor());
        tvDifficultyChooserLabel.setTextColor(themeChanger.getBackgroundTextColor());

        timeChooser = (Spinner) findViewById(R.id.timeChooser);
        SpinnerAdapter spinnerAdapterTimeChooser = new SpinnerAdapter(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, timeArray);
        timeChooser.setAdapter(spinnerAdapterTimeChooser);
        timeChooser.setSelection(spinnerAdapterTimeChooser.getCount() - 1);
        timeChooser.setPopupBackgroundDrawable(new ColorDrawable(themeChanger.getBackgroundColor()));


        difficultyChooser = (Spinner) findViewById(R.id.difficultyChooser);
        SpinnerAdapter spinnerAdapterDifficultyChooser = new SpinnerAdapter(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, difficultyArray);
        difficultyChooser.setAdapter(spinnerAdapterDifficultyChooser);
        difficultyChooser.setSelection(spinnerAdapterDifficultyChooser.getCount() / 2);
        difficultyChooser.setPopupBackgroundDrawable(new ColorDrawable(themeChanger.getBackgroundColor()));

//        difficultyChooser = (Spinner) findViewById(R.id.difficultyChooser);
//        ArrayAdapter<String> difficultyChooserAdapter = new ArrayAdapter<String>(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, difficultyArray);
//        difficultyChooser.setAdapter(difficultyChooserAdapter);
//        difficultyChooser.setSelection(difficultyChooserAdapter.getCount() / 2);


        inFocus = null;


        btnChangeWord = (Button) findViewById(R.id.button_change_word);
        btnChangeWord.setOnClickListener(this);
        btnChangeWord.setWidth(btnChangeWord.getHeight());


        File file = new File(getFilesDir(), getResources().getString(R.string.recent_names_filename));
        String name1 = "ИМЯ 1", name2 = "ИМЯ 2";
        if (!file.exists()) {
            try {
                file.createNewFile();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject("ИМЯ 1");
                objectOutputStream.writeObject("ИМЯ 2");
                objectOutputStream.flush();
                objectOutputStream.close();
                name1 = "ИМЯ 1";
                name2 = "ИМЯ 2";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                name1 = (String) objectInputStream.readObject();
                name2 = (String) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


        tvPlayer1 = (TextView) findViewById(R.id.player1);
        tvPlayer2 = (TextView) findViewById(R.id.player2);

        tvPlayer1.setText(name1);
        tvPlayer2.setText(name2);

        tvPlayer1.setOnClickListener(this);
        tvPlayer2.setOnClickListener(this);

        firstWordBackspace = (Button) findViewById(R.id.firstWordBackspace);
        player1Backspace = (Button) findViewById(R.id.player1Backspace);
        player2Backspace = (Button) findViewById(R.id.player2Backspace);

        firstWordBackspace.setOnClickListener(this);
        player1Backspace.setOnClickListener(this);
        player2Backspace.setOnClickListener(this);

        firstWordBackspace.setVisibility(View.INVISIBLE);
        player1Backspace.setVisibility(View.INVISIBLE);
        player2Backspace.setVisibility(View.INVISIBLE);

        kbrdApAnim = AnimationUtils.loadAnimation(this, R.anim.kbrd_ap);
        kbrdDisapAnim = AnimationUtils.loadAnimation(this, R.anim.kbrd_disap);

        underKbrdApAnim = AnimationUtils.loadAnimation(this, R.anim.under_kbrd_ap);
        underKbrdDisapAnim = AnimationUtils.loadAnimation(this, R.anim.under_kbrd_disap);

        modeSwitcher = (ViewSwitcher) findViewById(R.id.gameModeSwitcher);


        fiveLetterWordCounter = 0;

        XmlPullParser parser = getResources().getXml(R.xml.voc5);
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG)
                    ++fiveLetterWordCounter;
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeWord();

        order = ORDER.COMP_SECOND;

//        pvcModeBox.setButtonDrawable(themeChanger.getCheckBoxBackground());

    }


    @Override
    public void onBackPressed() {
        if (inFocus != null) {
            if (inFocus == tvFirstWord)
                hideKeyboard(FIRST_WORD);
            else if (inFocus == tvPlayer1)
                hideKeyboard(NAME1);
            else if (inFocus == tvPlayer2)
                hideKeyboard(NAME2);
            inFocus.setBackgroundResource(textViewBackground);


            if (player1Backspace.getVisibility() == VISIBLE) {
                player1Backspace.startAnimation(bckspceDisapAnim);
                player1Backspace.setVisibility(View.INVISIBLE);
            }

            if (player2Backspace.getVisibility() == VISIBLE) {
                player2Backspace.startAnimation(bckspceDisapAnim);
                player2Backspace.setVisibility(View.INVISIBLE);
            }

            if (firstWordBackspace.getVisibility() == VISIBLE) {
                firstWordBackspace.startAnimation(bckspceDisapAnim);
                firstWordBackspace.setVisibility(View.INVISIBLE);
            }

            inFocus = null;
        } else
            super.onBackPressed();
    }

    private void hideBtnSwap() {
        btnSwap.startAnimation(swapDisapAnim);
        btnSwap.setVisibility(View.GONE);
    }

    private void showBtnSwap() {
        btnSwap.startAnimation(swapApAnim);
        btnSwap.setVisibility(VISIBLE);
    }

    private void hideKeyboard(int source) {


        switch (source) {
            case NAME1:
                showBtnSwap();
                player1Backspace.setVisibility(View.GONE);
                break;
            case NAME2:
                showBtnSwap();
                player2Backspace.setVisibility(View.GONE);
                break;
        }


        keyboardLayout.startAnimation(kbrdDisapAnim);
        keyboardLayout.setVisibility(View.INVISIBLE);
        for (View view : underKeyboardViews) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(underKbrdApAnim);

        }

    }

    private void showKeyboard(int source) {

        switch (source) {
            case NAME1:
                hideBtnSwap();
                player1Backspace.setVisibility(View.VISIBLE);
                break;
            case NAME2:
                hideBtnSwap();
                player2Backspace.setVisibility(View.VISIBLE);
                break;

        }
        keyboardLayout.setVisibility(View.VISIBLE);
        for (View view : underKeyboardViews)
            view.startAnimation(underKbrdDisapAnim);
        keyboardLayout.startAnimation(kbrdApAnim);
        for (View view : underKeyboardViews)
            view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tvFirstWord.getId()) {
            if (inFocus == null || inFocus != tvFirstWord) {
                if (inFocus != null) {
                    if (inFocus == tvPlayer1) {
                        if (player1Backspace.getVisibility() == VISIBLE) {
                            player1Backspace.startAnimation(bckspceDisapAnim);
                            player1Backspace.setVisibility(View.INVISIBLE);
                        }
                        showBtnSwap();
                    } else if (inFocus == tvPlayer2) {
                        if (player2Backspace.getVisibility() == VISIBLE) {
                            player2Backspace.startAnimation(bckspceDisapAnim);
                            player2Backspace.setVisibility(View.INVISIBLE);
                        }
                        showBtnSwap();
                    }
                    inFocus.setBackgroundResource(textViewBackground);

                    inFocus = tvFirstWord;
                    if (tvFirstWord.getText().length() > 0) {
                        firstWordBackspace.setVisibility(View.VISIBLE);
                        firstWordBackspace.startAnimation(bckspceApAnim);
                    }
                    inFocus.setBackgroundResource(textViewInFocus);

                } else {
                    inFocus = tvFirstWord;

                    if (tvFirstWord.getText().length() == 5)
                        tvFirstWord.setText("");

                    if (tvFirstWord.getText().length() > 0) {
                        firstWordBackspace.setVisibility(View.VISIBLE);
                        firstWordBackspace.startAnimation(bckspceApAnim);
                    }


                    inFocus.setBackgroundResource(textViewInFocus);
                    showKeyboard(FIRST_WORD);
                }

            } else {

                if (tvFirstWord.getText().length() != 0) {
                    firstWordBackspace.startAnimation(bckspceDisapAnim);
                    firstWordBackspace.setVisibility(View.INVISIBLE);
                }

                inFocus.setBackgroundResource(textViewBackground);
                inFocus = null;
                hideKeyboard(FIRST_WORD);
            }
            return;
        }
        if (v.getId() == tvPlayer1.getId()) {
            if (inFocus == null || inFocus != tvPlayer1) {
                if (inFocus != null) {
                    if (inFocus == tvFirstWord) {
                        if (firstWordBackspace.getVisibility() == VISIBLE) {
                            firstWordBackspace.startAnimation(bckspceDisapAnim);
                            firstWordBackspace.setVisibility(View.INVISIBLE);
                        }
                        hideBtnSwap();
                    } else if (inFocus == tvPlayer2 && player2Backspace.getVisibility() == VISIBLE) {
                        player2Backspace.startAnimation(bckspceDisapAnim);
                        player2Backspace.setVisibility(View.INVISIBLE);
                    }
                    inFocus.setBackgroundResource(textViewBackground);

                    inFocus = tvPlayer1;
                    if (tvPlayer1.getText().length() > 0) {
                        player1Backspace.setVisibility(View.VISIBLE);
                        player1Backspace.startAnimation(bckspceApAnim);
                    }
                    inFocus.setBackgroundResource(textViewInFocus);
                } else {
                    inFocus = tvPlayer1;
                    if (tvPlayer1.getText().length() > 0) {
                        player1Backspace.setVisibility(View.VISIBLE);
                        player1Backspace.startAnimation(bckspceApAnim);
                    }
                    inFocus.setBackgroundResource(textViewInFocus);
                    showKeyboard(NAME1);
                }

            } else {
                if (tvPlayer1.getText().length() > 0) {
                    player1Backspace.startAnimation(bckspceDisapAnim);
                    player1Backspace.setVisibility(View.INVISIBLE);
                }
                inFocus.setBackgroundResource(textViewBackground);
                inFocus = null;
                hideKeyboard(NAME1);

            }
            return;
        }

        if (v.getId() == tvPlayer2.getId()) {
            if (inFocus == null || inFocus != tvPlayer2) {
                if (inFocus != null) {
                    if (inFocus == tvFirstWord) {
                        if (firstWordBackspace.getVisibility() == VISIBLE) {
                            firstWordBackspace.startAnimation(bckspceDisapAnim);
                            firstWordBackspace.setVisibility(View.INVISIBLE);
                        }
                        hideBtnSwap();
                    } else if (inFocus == tvPlayer1 && player1Backspace.getVisibility() == VISIBLE) {
                        player1Backspace.startAnimation(bckspceDisapAnim);
                        player1Backspace.setVisibility(View.INVISIBLE);
                    }
                    inFocus.setBackgroundResource(textViewBackground);

                    inFocus = tvPlayer2;
                    if (tvPlayer2.getText().length() > 0) {
                        player2Backspace.setVisibility(View.VISIBLE);
                        player2Backspace.startAnimation(bckspceApAnim);
                    }
                    inFocus.setBackgroundResource(textViewInFocus);
                } else {
                    inFocus = tvPlayer2;
                    if (tvPlayer2.getText().length() > 0) {
                        player2Backspace.setVisibility(View.VISIBLE);
                        player2Backspace.startAnimation(bckspceApAnim);
                    }
                    inFocus.setBackgroundResource(textViewInFocus);
                    showKeyboard(NAME2);
                }

            } else {
                if (tvPlayer2.getText().length() > 0) {
                    player2Backspace.startAnimation(bckspceDisapAnim);
                    player2Backspace.setVisibility(View.INVISIBLE);
                }
                inFocus.setBackgroundResource(textViewBackground);
                inFocus = null;
                hideKeyboard(NAME2);

            }
            return;
        }

        if (v == firstWordBackspace) {
            if (tvFirstWord.getText().length() == 0)
                return;
            tvFirstWord.setText(tvFirstWord.getText().subSequence(0, tvFirstWord.length() - 1));
            if (tvFirstWord.getText().length() == 0) {
                firstWordBackspace.startAnimation(bckspceDisapAnim);
                firstWordBackspace.setVisibility(View.INVISIBLE);
            }
            return;
        }

        if (v == player1Backspace) {
            if (tvPlayer1.getText().length() == 0)
                return;
            tvPlayer1.setText(tvPlayer1.getText().subSequence(0, tvPlayer1.length() - 1));
            if (tvPlayer1.getText().length() == 0) {
                player1Backspace.startAnimation(bckspceDisapAnim);
                player1Backspace.setVisibility(View.INVISIBLE);
            }
            return;
        }

        if (v == player2Backspace) {
            if (tvPlayer2.getText().length() == 0)
                return;
            tvPlayer2.setText(tvPlayer2.getText().subSequence(0, tvPlayer2.length() - 1));
            if (tvPlayer2.getText().length() == 0) {
                player2Backspace.startAnimation(bckspceDisapAnim);
                player2Backspace.setVisibility(View.INVISIBLE);
            }
            return;
        }

        if (keyboard.isKey(v)) {
            onKeyClick(((Button) v).getText().charAt(0));
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(getResources().getInteger(R.integer.gameFieldVibrationDuration));
            if (inFocus == tvFirstWord)
                if (tvFirstWord.getText().length() == 5) {
                    inFocus.setBackgroundResource(textViewBackground);
                    hideKeyboard(FIRST_WORD);
                    inFocus = null;
                    firstWordBackspace.startAnimation(bckspceDisapAnim);
                    firstWordBackspace.setVisibility(View.INVISIBLE);
                }
            return;
        }

        if (v == btnChangeWord) {
            btnChangeWord.startAnimation(chgWrdAnim);
            if (inFocus == tvFirstWord)
                tvFirstWord.performClick();
            changeWord();
        }
        if (v.getId() == btnOk.getId()) {

            if (tvFirstWord.getText().toString().length() != 5) {
                Toast.makeText(this, "Введите слово из 5 букв", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tvPlayer1.getText().toString().length() == 0) {
                Toast.makeText(this, "Введите имя первого игрока", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tvPlayer1.getText().toString().length() == 0) {
                Toast.makeText(this, "Введите имя второго игрока", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, GameActivity.class);
            ArrayList<String> players = new ArrayList<>();
            players.add(tvPlayer1.getText().toString());
            players.add(tvPlayer2.getText().toString());
            int mode = Modes.PLAYER_VS_COMPUTER;
            if (pvcModeBox.isChecked()) {
                switch (difficultyChooser.getSelectedItemPosition()) {
                    case 0:
                        switch (order) {
                            case COMP_FIRST:
                                mode = Modes.PLAYER_VS_COMPUTER_E;
                                break;
                            case COMP_SECOND:
                                mode = Modes.PLAYER_VS_COMPUTER_E_SECOND;
                                break;
                        }
                        break;
                    default:
                    case 1:
                        switch (order) {
                            case COMP_FIRST:
                                mode = Modes.PLAYER_VS_COMPUTER;
                                break;
                            case COMP_SECOND:
                                mode = Modes.PLAYER_VS_COMPUTER_SECOND;
                                break;
                        }
                        break;
                    case 2:
                        switch (order) {
                            case COMP_FIRST:
                                mode = Modes.PLAYER_VS_COMPUTER_H;
                                break;
                            case COMP_SECOND:
                                mode = Modes.PLAYER_VS_COMPUTER_H_SECOND;
                                break;
                        }
                        break;
                }
            } else {
                switch (timeChooser.getSelectedItemPosition()) {
                    case 0:
                        mode = Modes.PLAYER_VS_PLAYER_30;
                        break;
                    case 1:
                        mode = Modes.PLAYER_VS_PLAYER_1m;
                        break;
                    case 2:
                        mode = Modes.PLAYER_VS_PLAYER_2m;
                        break;
                    default:
                    case 4:
                        mode = Modes.PLAYER_VS_PLAYER;
                        break;

                }
            }

            File file = new File(getFilesDir(), getResources().getString(R.string.recent_names_filename));
            if (!pvcModeBox.isChecked()) {

                if (file.exists()) {

                    try {
                        file.delete();
                        file.createNewFile();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                        objectOutputStream.writeObject(tvPlayer1.getText().toString());
                        objectOutputStream.writeObject(tvPlayer2.getText().toString());
                        objectOutputStream.flush();
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    String name1 = (String) objectInputStream.readObject();
                    String name2 = (String) objectInputStream.readObject();

                    switch (order) {
                        case COMP_FIRST:
                            name2 = tvPlayer2.getText().toString();
                            break;
                        case COMP_SECOND:
                            name1 = tvPlayer1.getText().toString();
                            break;
                    }

                    file.delete();
                    file.createNewFile();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(name1);
                    objectOutputStream.writeObject(name2);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


            intent.putExtra("settings", new Settings(players, tvFirstWord.getText().toString(), mode));
            startActivity(intent);
            return;
        }
        if (v == btnMenu) {
            PopupMenu popupMenu = new PopupMenu(this, btnMenu);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
            final Activity thisActivity = this;
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Resources resources = getResources();
                    String title = (String) item.getTitle();
                    Intent intent = null;
                    if (title.equals(resources.getString(R.string.menu_item_user_voc))) {
                        intent = new Intent(thisActivity, UserVocabulary.class);
                    } else if (title.equals(resources.getString(R.string.menu_item_about_app))) {
                        intent = new Intent(thisActivity, AboutActivity.class);
                    } else if (title.equals(resources.getString(R.string.menu_item_app_rules))) {
                        intent = new Intent(thisActivity, RulesActivity.class);
                    } else if (title.equals(resources.getString(R.string.menu_item_app_settings))) {
                        intent = new Intent(thisActivity, ThemesActivity.class);
                    }
                    startActivity(intent);
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    private void changeWord() {
        XmlPullParser parser = getResources().getXml(R.xml.voc5);
        int counter = 0;
        int randomWord = random.nextInt(fiveLetterWordCounter - 1) + 1;


        while (true) {
            try {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (counter == randomWord)
                        break;
                    ++counter;
                }
                parser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tvFirstWord.setText(parser.getAttributeValue(0).toUpperCase());
    }

    private final int DEF_VALUE = 228;

    @Override
    protected void onResume() {
        super.onResume();
        changeWord();

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.shrdPrefsAppThemes), this.MODE_PRIVATE);
        int backgroundColor = preferences.getInt(getResources().getString(R.string.shrdPrefsAppBackgroundColor), DEF_VALUE),
                headerNFooterColor = preferences.getInt(getResources().getString(R.string.shrdPrefsHeaderNFooterColor), DEF_VALUE),
                gameFieldColor = preferences.getInt(getResources().getString(R.string.shrdPrefsGameFieldsColor), DEF_VALUE),
                backgroundTextColor = preferences.getInt(getResources().getString(R.string.shrdPrefsAppBackgroundTextColor), DEF_VALUE),
                headerNFooterTextColor = preferences.getInt(getResources().getString(R.string.shrdPrefsHeaderNFooterTextColor), DEF_VALUE),
                gameFieldTextColor = preferences.getInt(getResources().getString(R.string.shrdPrefsGameFieldsTextColor), DEF_VALUE);

        if (backgroundColor == DEF_VALUE || headerNFooterColor == DEF_VALUE || gameFieldColor == DEF_VALUE
                || backgroundTextColor == DEF_VALUE || headerNFooterTextColor == DEF_VALUE || gameFieldTextColor == DEF_VALUE) {

            themeChanger.rememberPresetTheme(getResources().getString(R.string.theme3));


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getResources().getString(R.string.shrdPrefsFontBold),true);
            editor.apply();


//
//            String sharedPrefsBackgroundKey = getResources().getString(R.string.shrdPrefsAppBackgroundColor);
//            String sharedPrefsHeaderNFooterKey = getResources().getString(R.string.shrdPrefsHeaderNFooterColor);
//            String sharedPrefsGameFieldsKey = getResources().getString(R.string.shrdPrefsGameFieldsColor);
//
//
//            String sharedPrefsBackgroundTextColorKey = getResources().getString(R.string.shrdPrefsAppBackgroundTextColor);
//            String sharedPrefsHeaderNFooterTextColorKey = getResources().getString(R.string.shrdPrefsHeaderNFooterTextColor);
//            String sharedPrefsGameFieldsTextColorKey = getResources().getString(R.string.shrdPrefsGameFieldsTextColor);
//
//            backgroundColor = getResources().getColor(R.color.app_background_color_1);
//            headerNFooterColor = getResources().getColor(R.color.app_elements_color_4);
//            gameFieldColor = getResources().getColor(R.color.game_fields_color_4);
//            backgroundTextColor = getResources().getColor(R.color.textColor);
//            headerNFooterTextColor = getResources().getColor(R.color.textColorWhite);
//            gameFieldTextColor = getResources().getColor(R.color.textColorWhite);
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putInt(sharedPrefsBackgroundKey, backgroundColor);
//            editor.putInt(sharedPrefsHeaderNFooterKey, headerNFooterColor);
//            editor.putInt(sharedPrefsGameFieldsKey, gameFieldColor);
//            editor.putInt(sharedPrefsBackgroundTextColorKey, backgroundTextColor);
//            editor.putInt(sharedPrefsHeaderNFooterTextColorKey, headerNFooterTextColor);
//            editor.putInt(sharedPrefsGameFieldsTextColorKey, gameFieldTextColor);
//            editor.apply();
        }


        timeChooser = (Spinner) findViewById(R.id.timeChooser);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, timeArray);
        timeChooser.setAdapter(spinnerAdapter);
        timeChooser.setSelection(spinnerAdapter.getCount() - 1); // todo должно выставляться значение сохраненное с предыдущего раза
        timeChooser.setPopupBackgroundDrawable(new ColorDrawable(themeChanger.getBackgroundColor()));

        difficultyChooser = (Spinner) findViewById(R.id.difficultyChooser);
        SpinnerAdapter spinnerAdapterDifficultyChooser = new SpinnerAdapter(this, R.layout.spinner_drop_down_item, R.id.drop_down_item, difficultyArray);
        difficultyChooser.setAdapter(spinnerAdapterDifficultyChooser);
        difficultyChooser.setSelection(spinnerAdapterDifficultyChooser.getCount() / 2); // todo аналогично пункту выше
        difficultyChooser.setPopupBackgroundDrawable(new ColorDrawable(themeChanger.getBackgroundColor()));

        tvTimeChooserLabel.setTextColor(themeChanger.getBackgroundTextColor());
        tvDifficultyChooserLabel.setTextColor(themeChanger.getBackgroundTextColor());

        textViewBackground = themeChanger.getTextViewBackgroundResource();
        textViewInFocus = themeChanger.getTextViewInFocusBackgroundResource();

//        pvcModeBox.setButtonDrawable(themeChanger.getCheckBoxBackground());

        setTheme();
    }

    private void onKeyClick(char ch) {

        if (inFocus != null) {
            if (inFocus == tvFirstWord) {
                if (inFocus.getText().length() < 5)
                    inFocus.setText(inFocus.getText().toString() + ch);
                if (tvFirstWord.getText().length() == 1) {
                    firstWordBackspace.startAnimation(bckspceApAnim);
                    firstWordBackspace.setVisibility(View.VISIBLE);
                }
                return;
            }
            if (inFocus == tvPlayer1) {
                if (pvcModeBox.isChecked() && (inFocus.getText().toString() + ch).equals("КОМПЬЮТЕР"))
                    return;

                if (tvPlayer1.getText().length() < 10)
                    inFocus.setText(inFocus.getText().toString() + ch);
                if (tvPlayer1.getText().length() == 1) {
                    player1Backspace.startAnimation(bckspceApAnim);
                    player1Backspace.setVisibility(View.VISIBLE);
                }
                return;
            }
            if (inFocus == tvPlayer2) {
                if (pvcModeBox.isChecked() && (inFocus.getText().toString() + ch).equals("КОМПЬЮТЕР"))
                    return;
                if (tvPlayer2.getText().length() < 10)
                    inFocus.setText(inFocus.getText().toString() + ch);
                if (tvPlayer2.getText().length() == 1) {
                    player2Backspace.startAnimation(bckspceApAnim);
                    player2Backspace.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
    }

    private void updateCompNameState() {

        tvPlayer1.setOnClickListener(this);
        tvPlayer2.setOnClickListener(this);

        File file = new File(getFilesDir(), getResources().getString(R.string.recent_names_filename));
        String name1 = "ИМЯ 1", name2 = "ИМЯ 2";
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                name1 = (String) objectInputStream.readObject();
                name2 = (String) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        if (pvcModeBox.isChecked()) {
            switch (order) {
                case COMP_FIRST:
                    tvPlayer2.setText(name2);
                    tvPlayer1.setText("КОМПЬЮТЕР");
                    tvPlayer1.setOnClickListener(null);
                    break;
                case COMP_SECOND:
                    tvPlayer1.setText(name1);
                    tvPlayer2.setText("КОМПЬЮТЕР");
                    tvPlayer2.setOnClickListener(null);
                    break;
            }
        } else {
            switch (order) {
                case COMP_FIRST:
                    tvPlayer1.setText(name1);
                    tvPlayer1.setOnClickListener(this);
                    break;
                case COMP_SECOND:
                    tvPlayer2.setText(name2);
                    tvPlayer2.setOnClickListener(this);
                    break;
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView != pvcModeBox)
            return;

        updateCompNameState();

        if (isChecked && modeSwitcher.getCurrentView() != difficultyChooserLayout) {
            timeChooserLayout.startAnimation(timeChsrDisapAnim);
            difficultyChooserLayout.startAnimation(difChsrApAnim);
            modeSwitcher.showNext();
        } else if (!isChecked && modeSwitcher.getCurrentView() == difficultyChooserLayout) {
            difficultyChooserLayout.startAnimation(difChsrDisapAnim);
            timeChooserLayout.startAnimation(timeChsrApAnim);
            modeSwitcher.showNext();
        }

    }

}
