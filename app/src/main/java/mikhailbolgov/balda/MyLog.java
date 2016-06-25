package mikhailbolgov.balda;

import android.util.Log;

/**
 * Created by Михаил on 19.05.2015.
 */
public class MyLog {
    private final static String LOG = "my log";
    private final static String FLAG = "flag";

    public static void log(){
        Log.d(LOG, FLAG);
    }

    public static void log(String string){
        Log.d(LOG, string);
    }
}
