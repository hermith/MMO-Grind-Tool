package net.hermith.efficientgrinder2.activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Karl
 * Date: 05.06.13
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */
public class CountdownService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
