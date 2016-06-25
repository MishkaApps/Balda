package mikhailbolgov.balda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mikhailbolgov.balda.R;

/**
 * Created by Михаил on 15.05.2015.
 */
public class Keyboard extends Fragment {
    private Button keyboard[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyboard = new Button[32];

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View keyboardView = inflater.inflate(R.layout.keyboard, container, false);

        keyboard[0] = (Button) keyboardView.findViewById(R.id.key_a);
        keyboard[1] = (Button) keyboardView.findViewById(R.id.key_b);
        keyboard[2] = (Button) keyboardView.findViewById(R.id.key_v);
        keyboard[3] = (Button) keyboardView.findViewById(R.id.key_g);
        keyboard[4] = (Button) keyboardView.findViewById(R.id.key_d);
        keyboard[5] = (Button) keyboardView.findViewById(R.id.key_e);
        keyboard[6] = (Button) keyboardView.findViewById(R.id.key_zh);
        keyboard[7] = (Button) keyboardView.findViewById(R.id.key_z);

        keyboard[8] = (Button) keyboardView.findViewById(R.id.key_i);
        keyboard[9] = (Button) keyboardView.findViewById(R.id.key_j);
        keyboard[10] = (Button) keyboardView.findViewById(R.id.key_k);
        keyboard[11] = (Button) keyboardView.findViewById(R.id.key_l);
        keyboard[12] = (Button) keyboardView.findViewById(R.id.key_m);
        keyboard[13] = (Button) keyboardView.findViewById(R.id.key_n);
        keyboard[14] = (Button) keyboardView.findViewById(R.id.key_o);
        keyboard[15] = (Button) keyboardView.findViewById(R.id.key_p);

        keyboard[16] = (Button) keyboardView.findViewById(R.id.key_r);
        keyboard[17] = (Button) keyboardView.findViewById(R.id.key_s);
        keyboard[18] = (Button) keyboardView.findViewById(R.id.key_t);
        keyboard[19] = (Button) keyboardView.findViewById(R.id.key_u);
        keyboard[20] = (Button) keyboardView.findViewById(R.id.key_f);
        keyboard[21] = (Button) keyboardView.findViewById(R.id.key_kh);
        keyboard[22] = (Button) keyboardView.findViewById(R.id.key_ts);
        keyboard[23] = (Button) keyboardView.findViewById(R.id.key_ch);

        keyboard[24] = (Button) keyboardView.findViewById(R.id.key_sh);
        keyboard[25] = (Button) keyboardView.findViewById(R.id.key_shch);
        keyboard[26] = (Button) keyboardView.findViewById(R.id.key_ss);
        keyboard[27] = (Button) keyboardView.findViewById(R.id.key_y);
        keyboard[28] = (Button) keyboardView.findViewById(R.id.key_hs);
        keyboard[29] = (Button) keyboardView.findViewById(R.id.key_re);
        keyboard[30] = (Button) keyboardView.findViewById(R.id.key_yu);
        keyboard[31] = (Button) keyboardView.findViewById(R.id.key_ya);

        for(int keyCtr = 0; keyCtr < 32; ){
            keyboard[keyCtr].setOnClickListener((View.OnClickListener) getActivity());
            ++keyCtr;
        }

        return keyboardView;
    }


    public boolean isKey(View v) {
        for(int keyCtr = 0; keyCtr < 32; ){
            if(v.getId() == keyboard[keyCtr].getId())
                return true;
            ++keyCtr;
        }
        return false;
    }

    public boolean pressKey(char ch){
        for(int keyCtr = 0; keyCtr < 32; ){
            if(keyboard[keyCtr].getText().charAt(0) == ch){
                keyboard[keyCtr].callOnClick();
            return true;
            }
            ++keyCtr;
        }

        return false;
    }
}
