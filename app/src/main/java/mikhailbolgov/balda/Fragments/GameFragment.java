package mikhailbolgov.balda.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import mikhailbolgov.balda.Balda;
import mikhailbolgov.balda.Cell;
import mikhailbolgov.balda.CellView;
import mikhailbolgov.balda.Library;
import mikhailbolgov.balda.MyLog;
import mikhailbolgov.balda.Player;
import mikhailbolgov.balda.R;
import mikhailbolgov.balda.Settings;
import mikhailbolgov.balda.ThemeChangeable;
import mikhailbolgov.balda.ThemeChanger;
import mikhailbolgov.balda.Timer;
import mikhailbolgov.balda.VocsCreatedListener;
import mikhailbolgov.balda.Word;

public class GameFragment extends Fragment implements View.OnClickListener, Timer.TimerEventListener, VocsCreatedListener, ThemeChangeable {

    private CellView cells[][];
    private Button keyboard[];
    private CellView selectedCell;
    private CellView newCell;
    private TextView newWordView;
    private final int N = 5;
    private Balda balda;
    private Word newWord;
    private Button btnDeleteWord;
    private Button btnAddWord;
    private HashMap<Player, TextView> playersScore;
    private Settings settings;
    private LinearLayout controlLayout;
    private View keyboardView;
    private FrameLayout keyboardLayout;
    private Keyboard keyboardFragment;
    private LinearLayout cellRows[];
    private ViewSwitcher viewSwitcher;
    private Animation kbrdDisapAnim;
    private Animation kbrdApAnim;
    private Animation cntrlPnlDisapAnim;
    private Animation cntrlPnlApAnim;
    public int moveTime;
    private int mode;
    private Library library;
    private Resources resources;
    private AlertDialog.Builder builder;
    private final int GAME_STAGE_BEGINNING = 0, GAME_STAGE_NEXT_PLAYER = 1, GAME_STAGE_TIME_ENDS = 2;
    private Timer timer;
    OnAddWordListener listener;
    private Context context;
    private GameFragment gameFragment;
    private HashMap<Player, TextView> playersNames;
    private HashMap<Player, ViewGroup> playersInfo;
    private ViewGroup waitLayout;
    private ImageView ivWait;
    private boolean created;
    private Button btnPassMove;
    private boolean passedOnce;
    private int createdVocsNumber;

    private int cellViewBackground, activePlayerBackground, notActivePlayerBackground, newLetterBackground, letterAddedInWordBackground, cellInFocusBackground;

    private void removeAllListeners() {
        for (int rowCtr = 0; rowCtr < N; ++rowCtr)
            for (int colCtr = 0; colCtr < N; ++colCtr)
                cells[rowCtr][colCtr].setOnClickListener(null);

        btnAddWord.setOnClickListener(null);
        btnDeleteWord.setOnClickListener(null);
        btnPassMove.setOnClickListener(null);

    }

    private void restoreAllListeners() {
        for (int rowCtr = 0; rowCtr < N; ++rowCtr)
            for (int colCtr = 0; colCtr < N; ++colCtr)
                cells[rowCtr][colCtr].setOnClickListener(this);

        btnAddWord.setOnClickListener(this);
        btnDeleteWord.setOnClickListener(this);
        btnPassMove.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (library == null)
            library = new Library();
        if (balda.isModeGameWithComputer() & created) {
            if (balda.isCurrentPlayerComputer()) {
                removeAllListeners();
                waitLayout.setVisibility(View.VISIBLE);
                ivWait.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wait_anim));
            }
            balda.getComputer().refresh(resources, this);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (savedInstanceState == null) {
            balda = new Balda(settings, getResources());
            if (balda.isModeGameWithComputer()) {
                balda.getComputer().setVocsCreatedListener(this);
            }
            BaldaReceiver receiver = (BaldaReceiver) context;
            receiver.receiveBalda(balda);
        } else {
            balda = (Balda) savedInstanceState.getSerializable("balda");
        }


        kbrdDisapAnim = AnimationUtils.loadAnimation(context, R.anim.kbrd_disap);
        kbrdApAnim = AnimationUtils.loadAnimation(context, R.anim.kbrd_ap);

        cntrlPnlDisapAnim = AnimationUtils.loadAnimation(context, R.anim.under_kbrd_disap);
        cntrlPnlApAnim = AnimationUtils.loadAnimation(context, R.anim.under_kbrd_ap);

        library = new Library();
        resources = getResources();

        listener = (OnAddWordListener) context;
        mode = balda.getMode();

        gameFragment = this;

        createdVocsNumber = 0;

        ThemeChanger themeChanger = new ThemeChanger(context);

        cellViewBackground = themeChanger.getCellViewBackground();
        activePlayerBackground = themeChanger.getActivePlayerBackground();
        notActivePlayerBackground = themeChanger.getNotActivePlayerBackground();
        cellInFocusBackground = themeChanger.getCellInFocusBackground();
        letterAddedInWordBackground = themeChanger.getLetterAddedInWordBackground();
        newLetterBackground = themeChanger.getNewLetterBackground();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (balda.isModeGameWithComputer()) {
            if (printer != null)
                printer.cancel();
            restoreAllListeners();
        }

        if (balda.isCurrentPlayerComputer()) {
            newWord = null;                        //    данная залупа должна ресетить поле, тип на него не нажимали
            newWordView.setText("");               //
            newCell = null;                        //
            setLetters();                          //
            resetCellsBackground();                //
            removeAllListeners();
        }
        library = null;
        created = true;
        if (balda.isModeGameWithComputer())
            balda.getComputer().clear();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("balda", balda);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.game_fragment, container, false);


        controlLayout = (LinearLayout) contentView.findViewById(R.id.lytControl);
        keyboardView = contentView.findViewById(R.id.game_keyboard);

        keyboardLayout = (FrameLayout) contentView.findViewById(R.id.keyboard_layout);
        hideKeyboard();


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        keyboardFragment = (Keyboard) fragmentManager.findFragmentByTag("keyboard");

        cellRows = new LinearLayout[N];

        cellRows[0] = (LinearLayout) contentView.findViewById(R.id.row_0);
        cellRows[1] = (LinearLayout) contentView.findViewById(R.id.row_1);
        cellRows[2] = (LinearLayout) contentView.findViewById(R.id.row_2);
        cellRows[3] = (LinearLayout) contentView.findViewById(R.id.row_3);
        cellRows[4] = (LinearLayout) contentView.findViewById(R.id.row_4);

        playersScore = new HashMap<>();
        playersNames = new HashMap<>();
        playersInfo = new HashMap<>();
        cells = new CellView[N][N];
        keyboard = new Button[32];
        selectedCell = null;
        newCell = null;
        newWordView = (TextView) contentView.findViewById(R.id.text_view_new_word);
        btnDeleteWord = (Button) contentView.findViewById(R.id.button_delete_word);


        newWord = null;
        btnAddWord = (Button) contentView.findViewById(R.id.btnGameOk);
//        btnAddWord.setEnabled(false);
        btnAddWord.setVisibility(View.INVISIBLE);
        btnAddWord.setEnabled(false);

        cells[0][0] = (CellView) contentView.findViewById(R.id.cell00);
        cells[0][1] = (CellView) contentView.findViewById(R.id.cell01);
        cells[0][2] = (CellView) contentView.findViewById(R.id.cell02);
        cells[0][3] = (CellView) contentView.findViewById(R.id.cell03);
        cells[0][4] = (CellView) contentView.findViewById(R.id.cell04);

        cells[1][0] = (CellView) contentView.findViewById(R.id.cell10);
        cells[1][1] = (CellView) contentView.findViewById(R.id.cell11);
        cells[1][2] = (CellView) contentView.findViewById(R.id.cell12);
        cells[1][3] = (CellView) contentView.findViewById(R.id.cell13);
        cells[1][4] = (CellView) contentView.findViewById(R.id.cell14);

        cells[2][0] = (CellView) contentView.findViewById(R.id.cell20);
        cells[2][1] = (CellView) contentView.findViewById(R.id.cell21);
        cells[2][2] = (CellView) contentView.findViewById(R.id.cell22);
        cells[2][3] = (CellView) contentView.findViewById(R.id.cell23);
        cells[2][4] = (CellView) contentView.findViewById(R.id.cell24);

        cells[3][0] = (CellView) contentView.findViewById(R.id.cell30);
        cells[3][1] = (CellView) contentView.findViewById(R.id.cell31);
        cells[3][2] = (CellView) contentView.findViewById(R.id.cell32);
        cells[3][3] = (CellView) contentView.findViewById(R.id.cell33);
        cells[3][4] = (CellView) contentView.findViewById(R.id.cell34);

        cells[4][0] = (CellView) contentView.findViewById(R.id.cell40);
        cells[4][1] = (CellView) contentView.findViewById(R.id.cell41);
        cells[4][2] = (CellView) contentView.findViewById(R.id.cell42);
        cells[4][3] = (CellView) contentView.findViewById(R.id.cell43);
        cells[4][4] = (CellView) contentView.findViewById(R.id.cell44);



        boolean boldText = new ThemeChanger(context).boldText();

        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
                cells[row][col].setCell(balda.getCell(row, col));

                if(boldText)
                    cells[row][col].setTypeface(null, Typeface.BOLD);
                else cells[row][col].setTypeface(null, Typeface.NORMAL);

                ++col;
            }
            ++row;
        }


        setFirstWord(balda.getFirstWord());

        keyboard[0] = (Button) contentView.findViewById(R.id.key_a);
        keyboard[1] = (Button) contentView.findViewById(R.id.key_b);
        keyboard[2] = (Button) contentView.findViewById(R.id.key_v);
        keyboard[3] = (Button) contentView.findViewById(R.id.key_g);
        keyboard[4] = (Button) contentView.findViewById(R.id.key_d);
        keyboard[5] = (Button) contentView.findViewById(R.id.key_e);
        keyboard[6] = (Button) contentView.findViewById(R.id.key_zh);
        keyboard[7] = (Button) contentView.findViewById(R.id.key_z);

        keyboard[8] = (Button) contentView.findViewById(R.id.key_i);
        keyboard[9] = (Button) contentView.findViewById(R.id.key_j);
        keyboard[10] = (Button) contentView.findViewById(R.id.key_k);
        keyboard[11] = (Button) contentView.findViewById(R.id.key_l);
        keyboard[12] = (Button) contentView.findViewById(R.id.key_m);
        keyboard[13] = (Button) contentView.findViewById(R.id.key_n);
        keyboard[14] = (Button) contentView.findViewById(R.id.key_o);
        keyboard[15] = (Button) contentView.findViewById(R.id.key_p);

        keyboard[16] = (Button) contentView.findViewById(R.id.key_r);
        keyboard[17] = (Button) contentView.findViewById(R.id.key_s);
        keyboard[18] = (Button) contentView.findViewById(R.id.key_t);
        keyboard[19] = (Button) contentView.findViewById(R.id.key_u);
        keyboard[20] = (Button) contentView.findViewById(R.id.key_f);
        keyboard[21] = (Button) contentView.findViewById(R.id.key_kh);
        keyboard[22] = (Button) contentView.findViewById(R.id.key_ts);
        keyboard[23] = (Button) contentView.findViewById(R.id.key_ch);

        keyboard[24] = (Button) contentView.findViewById(R.id.key_sh);
        keyboard[25] = (Button) contentView.findViewById(R.id.key_shch);
        keyboard[26] = (Button) contentView.findViewById(R.id.key_ss);
        keyboard[27] = (Button) contentView.findViewById(R.id.key_y);
        keyboard[28] = (Button) contentView.findViewById(R.id.key_hs);
        keyboard[29] = (Button) contentView.findViewById(R.id.key_re);
        keyboard[30] = (Button) contentView.findViewById(R.id.key_yu);
        keyboard[31] = (Button) contentView.findViewById(R.id.key_ya);


        for (int rowCtr = 0; rowCtr < N; ++rowCtr)
            for (int colCtr = 0; colCtr < N; ++colCtr)
                cells[rowCtr][colCtr].setOnClickListener(this);

        for (int keyCtr = 0; keyCtr < 32; ++keyCtr)
            keyboard[keyCtr].setOnClickListener(this);


        for (Player tempPlayer : balda.getPlayers()) {

            switch (tempPlayer.getNumber()) {
                case 0:
                    playersNames.put(tempPlayer, (TextView) contentView.findViewById(R.id.text_view_player1));
                    playersNames.get(tempPlayer).setText(tempPlayer.getName());
                    playersScore.put(tempPlayer, (TextView) contentView.findViewById(R.id.text_view_player1_score));
                    playersScore.get(tempPlayer).setText("" + tempPlayer.getScore());
                    playersInfo.put(tempPlayer, (ViewGroup) contentView.findViewById(R.id.layout_player1));
                    break;
                case 1:
                    playersNames.put(tempPlayer, (TextView) contentView.findViewById(R.id.text_view_player2));
                    playersNames.get(tempPlayer).setText(tempPlayer.getName());
                    playersScore.put(tempPlayer, (TextView) contentView.findViewById(R.id.text_view_player2_score));
                    playersScore.get(tempPlayer).setText("" + tempPlayer.getScore());
                    playersInfo.put(tempPlayer, (ViewGroup) contentView.findViewById(R.id.layout_player2));
                    break;
            }


        }


        btnDeleteWord.setOnClickListener(this);
        btnAddWord.setOnClickListener(this);

        timer = (Timer) contentView.findViewById(R.id.timer);

        if (!balda.isModeTimeLimited())        {         // кажется код избыточен и крив. сделай че нить
            timer.setVisibility(View.GONE);
            contentView.findViewById(R.id.lytGameInfoSeparator1).setVisibility(View.GONE);
        }
        else
            timer.setVisibility(View.VISIBLE);

        if (balda.isModeTimeLimited())
            timer.createTimer(this, balda.getMoveTime());
//         else
//            timer.setVisibility(View.GONE);

        if (balda.isModeTimeLimited())
            showTimerWarning(GAME_STAGE_BEGINNING);

//        playersInfo.get(balda.getCurrentPlayer()).setBackgroundResource(R.color.active_player_background);
        playersInfo.get(balda.getCurrentPlayer()).setBackgroundColor(activePlayerBackground);


        waitLayout = (ViewGroup) contentView.findViewById(R.id.wait_layout);

        ivWait = (ImageView) contentView.findViewById(R.id.wait);


        if (balda.isModeGameWithComputer()) {
            if (!balda.getComputer().isVocsCreated() & balda.isCurrentPlayerComputer()) {
                waitLayout.setVisibility(View.VISIBLE);
                ivWait.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wait_anim));
                removeAllListeners();
            }
        }

        btnPassMove = (Button) contentView.findViewById(R.id.btn_pass);
        btnPassMove.setOnClickListener(this);

        contentView.invalidate();

        return contentView;
    }

    public void showTimerWarning(int stage) {
        if (!balda.isModeTimeLimited())
            return;

        hideGameField();
        AlertDialog dialog;
        builder = new AlertDialog.Builder(context);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                timer.start();
                showGameField();
            }
        });
        switch (stage) {
            case GAME_STAGE_BEGINNING:
                builder.setMessage(balda.getCurrentPlayer().getName() + " приготовтесь начть ход")
                        .setPositiveButton(resources.getString(R.string.alert_dialog_move_beginning_button_start), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.start();
                                showGameField();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                break;
            case GAME_STAGE_NEXT_PLAYER:
                builder.setMessage(balda.getCurrentPlayer().getName() + ", готовы начать?")
                        .setPositiveButton(resources.getString(R.string.alert_dialog_move_beginning_button_start), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.start();
                                showGameField();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                break;
            case GAME_STAGE_TIME_ENDS:
                if (passMoveDialog != null) {
                    if (passMoveDialog.isShowing())
                        passMoveDialog.dismiss();
                }
                builder.setMessage(balda.getNotActivePlayer().getName() + " пропускает ход \n" + balda.getCurrentPlayer().getName() + ", вы готовы?")
                        .setPositiveButton(resources.getString(R.string.alert_dialog_move_beginning_button_start), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.start();
                                showGameField();
                            }
                        });
                dialog = builder.create();
                if (!((Activity) context).isFinishing())
                    dialog.show();
                break;
            default:
                return;
        }

        passedOnce = false;
    }


    private void setFirstWord(String word) {

        Word tempWord = new Word();
        final int row = 2;

        for (int counter = 0; counter < N; ++counter)
            tempWord.addLetter(new Cell(row, counter, word.charAt(counter)));

        balda.setFirstWord(tempWord);
        setLetters();
    }


    @Override
    public void onClick(View v) {
        if (balda.isModeGameWithComputer()
                & !balda.isCurrentPlayerComputer()
                & newWordView.getText().length() != 0 & newWord != null) {
            if (newWord.getPoints() == 0)
                newWordView.setText("");
        }


        if (newWord != null & newCell != null) {
            if (newWord.isCorrect(newCell.getCell())) {
//                btnAddWord.setEnabled(true);
                showOkButton();
            } else {
//                btnAddWord.setEnabled(false);
                hideOkButton();
            }
        } else {
//            btnAddWord.setEnabled(false);
            hideOkButton();
        }

        for (int rowCtr = 0; rowCtr < N; ++rowCtr)
            for (int colCtr = 0; colCtr < N; ++colCtr)
                if (v.getId() == cells[rowCtr][colCtr].getId()) {
                    ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(resources.getInteger(R.integer.gameFieldVibrationDuration));
                    onTextViewClick(cells[rowCtr][colCtr]);
                    rowCtr = N + 1;
                    break;
                }

        for (int keyCtr = 0; keyCtr < 32; ++keyCtr)
            if (v.getId() == keyboard[keyCtr].getId()) {
                ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(resources.getInteger(R.integer.keyboardVibrationDuration));
                onKeyClick(keyboard[keyCtr]);
                break;
            }

        if (v == btnDeleteWord) {
//            btnAddWord.setEnabled(false);
            hideOkButton();
            newWord = null;
            newWordView.setText("");
            newCell = null;
            setLetters();
            resetCellsBackground();
        }

        if (v == btnAddWord)
            onAddWordClick();


        if (newWord != null & newCell != null) {
            if (newWord.isCorrect(newCell.getCell())) {
//                btnAddWord.setEnabled(true);
                showOkButton();
            } else {
//                btnAddWord.setEnabled(false);
                hideOkButton();
            }
        } else {
//            btnAddWord.setEnabled(false);
            hideOkButton();
        }

        if (v == btnPassMove)
            passMove();

    }

    // todo при выборе поля и выключении экрана, не закрывается клавиатура

    private void onAddWordClick() {
        removeAllListeners();

        if (!newWord.isCorrect(newCell.getCell())) {
            restoreAllListeners();
            return;
        }
        if (balda.isWordUsed(newWord)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage("Слово уже использованно").setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (balda.isModeTimeLimited())
                        timer.resumeCountdown();
                }
            });
            if (balda.isModeTimeLimited())
                timer.pause();
            builder.create().show();
//            btnAddWord.setEnabled(false);
            hideOkButton();
            newWord = null;
            newWordView.setText("");
            newCell = null;
            setLetters();
            resetCellsBackground();
            restoreAllListeners();
            return;
        }

        if (newWord == null || newWord.getSize() == 0) {
            restoreAllListeners();
            return;
        }

        if (!library.exist(context, getResources(), newWord.toString()) && !balda.isCurrentPlayerComputer()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.alert_dialog_msg_word_dont_exist).setNegativeButton(R.string.alert_dialog_btn_neg_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
//                    btnAddWord.setEnabled(false);
                    hideOkButton();
                    newWord = null;
                    newWordView.setText("");
                    newCell = null;
                    setLetters();
                    resetCellsBackground();
                    restoreAllListeners();
                }
            }).setPositiveButton(R.string.alert_dialog_btn_pos_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {                 // пользователь добавляет новое словов словарь, игра продолжается
                    dialog.cancel();
                    library.addUserWord(context, newWord.toString());

                    addWord();
                    restoreAllListeners();

                    if (balda.isGameOver()) {
                        removeAllListeners();
                        if (balda.isModeTimeLimited())
                            timer.pause();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        if (balda.winnerExist())
                            builder.setTitle(R.string.alert_dialog_game_over_title).setMessage(balda.getWinner().getName() + " одержал победу!");
                        else
                            builder.setTitle(R.string.alert_dialog_game_over_title).setMessage("Ничья");


                        AlertDialog inDialog = builder.create();
                        inDialog.show();


                    } else {
                        if (balda.isModeTimeLimited()) {
                            timer.reset();
                            showTimerWarning(GAME_STAGE_NEXT_PLAYER);
                        }


                        if (balda.isModeGameWithComputer() && balda.isCurrentPlayerComputer()) {
                            if (!balda.getComputer().isVocsCreated()) {
                                removeAllListeners();
                                waitLayout.setVisibility(View.VISIBLE);
                                ivWait.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wait_anim));
                            } else {
                                balda.computerMove(gameFragment);
                            }
                        }
                    }
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!balda.isGameOver() && !balda.isCurrentPlayerComputer())
                        restoreAllListeners();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        addWord();
        restoreAllListeners();

        if (balda.isGameOver()) {
            removeAllListeners();
            if (balda.isModeTimeLimited())
                timer.pause();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (balda.winnerExist()) {
                builder.setTitle(R.string.alert_dialog_game_over_title).setMessage(balda.getWinner().getName() + " одержал победу!");
            } else
                builder.setTitle(R.string.alert_dialog_game_over_title).setMessage("Ничья");

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (balda.isModeTimeLimited()) {
                timer.reset();
                showTimerWarning(GAME_STAGE_NEXT_PLAYER);
            }


            if (balda.isModeGameWithComputer() && balda.isCurrentPlayerComputer()) {
                if (!balda.getComputer().isVocsCreated()) {
                    removeAllListeners();
                    waitLayout.setVisibility(View.VISIBLE);
                    ivWait.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wait_anim));
                } else {
                    balda.computerMove(gameFragment);
                }
            }
        }


    }

    private void addWord() {
        Player currentPlayer = balda.getCurrentPlayer();
//        playersInfo.get(currentPlayer).setBackgroundResource(R.color.not_active_player_background);
//        playersInfo.get(balda.getNotActivePlayer()).setBackgroundResource(R.color.active_player_background);
        playersInfo.get(currentPlayer).setBackgroundColor(notActivePlayerBackground);
        playersInfo.get(balda.getNotActivePlayer()).setBackgroundColor(activePlayerBackground);

        if (!balda.isCurrentPlayerComputer())
            newWordView.setText("");

        if (balda.isCurrentPlayerComputer()) {
            balda.addWord(balda.getComputer().getLastWord());
            restoreAllListeners();
        } else balda.addWord(newWord);
        newWord = null;
        newCell = null;
        selectedCell = null;
//        btnAddWord.setEnabled(false);
        hideOkButton();
        playersScore.get(currentPlayer).setText("" + currentPlayer.getScore());
        setLetters();
        resetCellsBackground();

        listener.onWordAdded();

        passedOnce = false;


    }

    private void onKeyClick(Button key) {
        if (selectedCell == null)
            return;

        selectedCell.setLetter(key.getText().charAt(0));
        newCell = selectedCell;
//        newCell.setBackgroundResource(R.color.newLetter);
        newCell.setBackgroundColor(newLetterBackground);
        newWord = null;
        newWordView.setText("");
        hideKeyboard();

    }

    private void hideGameField() {
        for (int row = 0; row < N; ++row)
            for (int col = 0; col < N; ++col)
                cells[row][col].hide();
    }

    private void showGameField() {
        for (Cell cell : balda.getUsedCells())
            cells[cell.getRow()][cell.getCol()].show();
    }

    private void onTextViewClick(CellView cellView) {


        if (newCell != null)
            if (cellView.getCell() == newCell.getCell() | balda.isCellUsed(cellView.getCell())) {
                selectedCell = null;
                if (newWord == null)
                    newWord = new Word();
                if (newWord.addLetter(cellView.getCell())) {
//                    cellView.setBackgroundResource(R.color.letterAddedInWord);
                    cellView.setBackgroundColor(letterAddedInWordBackground);
                    String string = "";
                    string = (String) newWordView.getText() + cellView.getCell().getLetter();
                    newWordView.setText(string);
                }
                return;
            }
        if (cellView != selectedCell & balda.isAvailableToChange(cellView.getCell()))
            setSelectedCell(cellView);
        else if (cellView == selectedCell)
            setSelectedCell(null);

    }

    private void setSelectedCell(CellView cell) {

        if (newCell != null) {
            newCell.clean();
//            newCell.setBackgroundResource(R.color.cellBackground);       test of new way to change cell color
            newCell.setBackgroundColor(cellViewBackground);
            newCell = null;
        }
        newWord = null;
        newWordView.setText("");
        if (cell != null) {

            showKeyboard();

            selectedCell = cell;
            resetCellsBackground();
//            selectedCell.setBackgroundResource(R.color.inFocusBackground);
            selectedCell.setBackgroundColor(cellInFocusBackground);
        } else {
            hideKeyboard();
            selectedCell = null;
            resetCellsBackground();
        }
    }

    private void resetCellsBackground() {
        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
//                cells[row][col].setBackgroundResource(R.color.cellBackground);
                cells[row][col].setBackgroundColor(cellViewBackground);
                ++col;

            }
            ++row;
        }
    }


    private void setLetters() {

        for (int row = 0; row < N; ) {
            for (int col = 0; col < N; ) {
                if (!balda.isCellUsed(row, col)) {
                    cells[row][col].clean();
                    ++col;
                    continue;
                }
                cells[row][col].setLetter(balda.getCellLetter(row, col));
                ++col;
            }
            ++row;
        }
    }

    public void receiveSettings(Settings settings) {
        this.settings = settings;
    }

    private AlertDialog passMoveDialog;

    public void passMove() {

        if (balda.isGameOver())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (balda.isModeTimeLimited()) {
            builder.setMessage("Вы уверены, что хотите пропустить ход?").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Player currentPlayer = balda.getCurrentPlayer();
//                    playersInfo.get(currentPlayer).setBackgroundResource(R.color.not_active_player_background);
//                    playersInfo.get(balda.getNotActivePlayer()).setBackgroundResource(R.color.active_player_background);
                    playersInfo.get(currentPlayer).setBackgroundColor(notActivePlayerBackground);
                    playersInfo.get(balda.getNotActivePlayer()).setBackgroundColor(activePlayerBackground);
                    balda.skipMove();
                    newWord = null;
                    newCell = null;
                    selectedCell = null;
                    hideOkButton();
                    newWordView.setText("");
                    playersScore.get(currentPlayer).setText("" + currentPlayer.getScore());
                    setLetters();
                    resetCellsBackground();

                    listener.onWordAdded();


                    if (balda.isModeTimeLimited()) {
                        timer.reset();
                        showTimerWarning(GAME_STAGE_TIME_ENDS);
                    }


                    if (passedOnce) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.alert_dialog_game_over_title).setMessage("Ничья");
                        builder.create().show();
                        removeAllListeners();
                        return;

                    } else passedOnce = true;

                }
            }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            passMoveDialog = builder.create();
            passMoveDialog.show();
        } else {
            if (balda.isModeGameWithComputer() & balda.isCurrentPlayerComputer()) {
                builder.setMessage("Компьютер пропускает ход").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Player currentPlayer = balda.getCurrentPlayer();
//                playersInfo.get(currentPlayer).setBackgroundResource(R.color.not_active_player_background);
//                playersInfo.get(balda.getNotActivePlayer()).setBackgroundResource(R.color.active_player_background);
                playersInfo.get(currentPlayer).setBackgroundColor(notActivePlayerBackground);
                playersInfo.get(balda.getNotActivePlayer()).setBackgroundColor(activePlayerBackground);

                if (!balda.isCurrentPlayerComputer())
                    newWordView.setText("");
                balda.addWord(null);

                newWord = null;
                newCell = null;
                selectedCell = null;
//            btnAddWord.setEnabled(false);
                hideOkButton();
                playersScore.get(currentPlayer).setText("" + currentPlayer.getScore());
                setLetters();
                resetCellsBackground();

                builder.create().show();

            } else {

                builder.setMessage("Вы уверены, что хотите пропустить ход?").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Player currentPlayer = balda.getCurrentPlayer();
//                        playersInfo.get(currentPlayer).setBackgroundResource(R.color.not_active_player_background);
//                        playersInfo.get(balda.getNotActivePlayer()).setBackgroundResource(R.color.active_player_background);
                        playersInfo.get(currentPlayer).setBackgroundColor(notActivePlayerBackground);
                        playersInfo.get(balda.getNotActivePlayer()).setBackgroundColor(activePlayerBackground);

                        if (!balda.isCurrentPlayerComputer())
                            newWordView.setText("");
                        balda.addWord(null);

                        newWord = null;
                        newCell = null;
                        selectedCell = null;
                        hideOkButton();
                        playersScore.get(currentPlayer).setText("" + currentPlayer.getScore());
                        setLetters();
                        resetCellsBackground();
                        dialog.cancel();

                        if (balda.isModeGameWithComputer() && balda.isCurrentPlayerComputer()) {
                            if (!balda.getComputer().isVocsCreated()) {
                                removeAllListeners();
                                waitLayout.setVisibility(View.VISIBLE);
                                ivWait.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wait_anim));
                            } else {
                                balda.computerMove(gameFragment);
                            }
                        }

                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        }
    }

    //todo проконтролировать, что происходит если пропускаешь ход в конце игры, тип что б она заканчивалась
    //todo  скрывать буквы в ячейках, когда в игре на время ставится пауза

    @Override
    public void moveEnd() {
        Player currentPlayer = balda.getCurrentPlayer();
//        playersInfo.get(currentPlayer).setBackgroundResource(R.color.active_player_background);
//        playersInfo.get(balda.getNotActivePlayer()).setBackgroundResource(R.color.not_active_player_background);
        playersInfo.get(currentPlayer).setBackgroundColor(activePlayerBackground);
        playersInfo.get(balda.getNotActivePlayer()).setBackgroundColor(notActivePlayerBackground);
        balda.skipMove();
        newWord = null;
        newCell = null;
        selectedCell = null;
//        btnAddWord.setEnabled(false);
        hideOkButton();
        newWordView.setText("");
        playersScore.get(currentPlayer).setText("" + currentPlayer.getScore());
        setLetters();
        resetCellsBackground();

        listener.onWordAdded();
        if (keyboardLayout.getVisibility() == View.VISIBLE)
            hideKeyboard();


        if (balda.isGameOver()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            removeAllListeners();
            if (balda.winnerExist()) {
                String part1 = getResources().getString(R.string.alert_dialog_msg_player_wins_part1);
                String part2 = getResources().getString(R.string.alert_dialog_msg_player_wins_part2);
                builder.setTitle(R.string.alert_dialog_game_over_title).setMessage(part1 + balda.getWinner().getName() + part2);
            } else
                builder.setTitle(R.string.alert_dialog_game_over_title).setMessage(R.string.alert_dialog_msg_nobody_wins);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (balda.isModeTimeLimited()) {
                timer.reset();
                showTimerWarning(GAME_STAGE_TIME_ENDS);
            }
        }

    }

    @Override
    public void pause() {
        ((OnTimerPauseListener) context).timerPause();

    }


    @Override
    public void vocsCreated() {
        if (/*((Activity) context).isDestroyed() || */((Activity) context).isFinishing())
            return;

        createdVocsNumber++;

        if (createdVocsNumber == 4) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    waitLayout.setVisibility(View.GONE);
                    if (balda.isCurrentPlayerComputer()) {
                        balda.computerMove(gameFragment);
                    }
                    balda.getComputer().vocsCreated();
                }
            });
            createdVocsNumber = 0;
        }

    }

    @Override
    public void setTheme() {
        ThemeChanger themeChanger = new ThemeChanger(getContext());

        ArrayList<View> headerNFooter = new ArrayList<>();
        headerNFooter.add(getView().findViewById(R.id.lytGameHeader));
        headerNFooter.add(getView().findViewById(R.id.btnGameOk));

        ArrayList<CellView> cellViews = new ArrayList<>(Arrays.asList(cells[0]));
        cellViews.addAll(Arrays.asList(cells[1]));
        cellViews.addAll(Arrays.asList(cells[2]));
        cellViews.addAll(Arrays.asList(cells[3]));
        cellViews.addAll(Arrays.asList(cells[4]));



        themeChanger.applyTheme(context, (ViewGroup) getView().findViewById(R.id.lytGameBackground), headerNFooter);
        themeChanger.setGameFieldColor(context, cellViews);
    }

    public interface OnTimerPauseListener {
        void timerPause();
    }

    public void pauseTimer() {
        if (balda.isModeTimeLimited())
            timer.pause();
    }

    public void resumeTimer() {
        if (balda.isModeTimeLimited())
            timer.resumeCountdown();
    }

    private Printer printer;

    public void input(Word word) {
        removeAllListeners();

        Cell cell = null;
        for (Cell tempCell : word.getCells())
            if (!balda.isCellUsed(tempCell)) {
                cell = tempCell;
                break;
            }


        if (cell == null)
            for (Cell tempCell : word.getCells())
                if (!balda.isCellUsed(tempCell)) {
                    cell = tempCell;
                    break;
                }


        onClick(cells[cell.getRow()][cell.getCol()]);
        char letter = cell.getLetter();
        for (Button key : keyboard)
            if (key.getText().charAt(0) == letter) {
                onClick(key);
                break;
            }

        printer = new Printer(word);
        printer.start();

    }

    private class Printer extends CountDownTimer {

        ArrayList<Cell> wordCells;
        Word word;
        private final static long interval = 300;

        public Printer(Word word) {
            super((word.getSize() + 1) * interval, interval);
            wordCells = new ArrayList<>(word.getCells());
            this.word = word;
        }

        @Override
        public void onTick(long millisUntilFinished) {

            if (wordCells.size() > 0) {
                Cell tempCell = wordCells.get(0);
                onClick(cells[tempCell.getRow()][tempCell.getCol()]);
                wordCells.remove(0);
            }
        }

        @Override
        public void onFinish() {
            for (Cell cell : word.getCells())
                onClick(cells[cell.getRow()][cell.getCol()]);

            onClick(btnAddWord);
            restoreAllListeners();
        }
    }


    public void pressCell(Cell cell) {
        onClick(cells[cell.getRow()][cell.getCol()]);
    }

    public void pressKeyboardKey(char letter) {
        for (Button key : keyboard)
            if (key.getText().charAt(0) == letter) {
                key.performClick();
                break;
            }
    }


    public void pressAddWordButton() {
        btnAddWord.performClick();
    }


    public interface BaldaReceiver {
        void receiveBalda(Balda balda);
    }

    private void showKeyboard() {

        if (keyboardLayout.getVisibility() == View.VISIBLE)
            return;

        controlLayout.startAnimation(cntrlPnlDisapAnim);
        keyboardLayout.setVisibility(View.VISIBLE);
        keyboardView.startAnimation(kbrdApAnim);
    }

    private void hideKeyboard() {
        controlLayout.startAnimation(cntrlPnlApAnim);
        keyboardLayout.setVisibility(View.INVISIBLE);
        keyboardView.startAnimation(kbrdDisapAnim);
    }

    public interface OnAddWordListener {
        void onWordAdded();
    }

    private void showOkButton() {
        if (!balda.isCurrentPlayerComputer())
            if (!btnAddWord.isEnabled()) {
                btnAddWord.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.btn_ok_ap);
                btnAddWord.startAnimation(animation);
            }
        btnAddWord.setEnabled(true);
    }

    private void hideOkButton() {
        if (!balda.isCurrentPlayerComputer())
            if (btnAddWord.isEnabled()) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.btn_ok_disap);
                btnAddWord.startAnimation(animation);
                btnAddWord.setVisibility(View.INVISIBLE);
            }
        btnAddWord.setEnabled(false);
    }


    //Ниже вообще какая то хуйня невьебенная творится

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(context, "onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onPause();
        setTheme();

        //Toast.makeText(context, "onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onPause();
        //Toast.makeText(context, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

}