package net.hermith.efficientgrinder2.activities;

import net.hermith.efficientgrinder2.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class TimerMainActivity extends SherlockActivity {

    private Chronometer mChronometerTop, mChronometerMid, mChronometerBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_timer_main);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mChronometerTop = (Chronometer) findViewById(R.id.chronometer1);
        mChronometerMid = (Chronometer) findViewById(R.id.chronometer2);
        mChronometerBot = (Chronometer) findViewById(R.id.chronometer3);

        mChronometerTop.start();
        mChronometerMid.start();
        mChronometerBot.start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.timersurecancel).setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                }).setNegativeButton("No, don't stop my timers!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alert = b.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.timer);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_efficiency_clean); // TODO

        inflater.inflate(R.menu.menu_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Log.d("" + item.getTitle(), "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(TimerMainActivity.this, Preferences.class);
                startActivity(i);
                return true;
            case R.id.help:
                callDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void callDialog() {
        // TODO Auto-generated method stub

    }

}
