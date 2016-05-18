package net.hermith.efficientgrinder2.timemath;

import android.text.format.Time;
import android.util.Log;

public class TimeMath {

    public static String[] getCurrentTime() {
        Time time = new Time();
        time.setToNow();

        String timeToHandle = time.toString();

        // String year = timeToHandle.substring(0, 3);
        String month = timeToHandle.substring(4, 6);
        String day = timeToHandle.substring(6, 8);
        String hour = timeToHandle.substring(9, 11);
        String min = timeToHandle.substring(11, 13);
        return new String[]{month + "." + day, hour + ":" + min};

    }

    public static int getSeconds(String time) {
        String[] times = time.split(":");
        int min = Integer.parseInt(times[0]);
        int sec = Integer.parseInt(times[1]);
        Log.d("Time", "" + (min * 60) + sec);
        int t = (min * 60) + sec;
        if (t == 0)
            return 1;
        return t;
    }

}
