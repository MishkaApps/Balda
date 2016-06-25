package mikhailbolgov.balda;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by mbolg on 28.03.2016.
 */
public class Balance implements Serializable{
    private int difficulty;
    private int[] allocation;
    public Balance(int difficulty){
        this.difficulty = difficulty;


        switch (difficulty){
            case Modes.PLAYER_VS_COMPUTER_E:
            case Modes.PLAYER_VS_COMPUTER_E_SECOND:
                int[] temp1 = {5, 5, 5, 5, 4, 4, 4, 4, 4, 3};
                allocation = temp1;
                break;
            case Modes.PLAYER_VS_COMPUTER:
            case Modes.PLAYER_VS_COMPUTER_SECOND:
                int[] temp2 = {7, 5, 5, 5, 5, 5, 5, 4, 4, 4};
                allocation = temp2;
                break;
            case Modes.PLAYER_VS_COMPUTER_H:
            case Modes.PLAYER_VS_COMPUTER_H_SECOND:
                int[] temp3 = {10, 6, 6, 6, 5, 5, 5, 5, 4, 4};
                allocation = temp3;
                break;
        }
    }

    public int getLength(){
        int res = allocation[(new Random()).nextInt(allocation.length - 1)];
        return res;
    }
}
