package net.hermith.efficientgrinder2.activities.efficiency;

import java.text.DecimalFormat;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.hermith.efficientgrinder2.R;
import net.hermith.efficientgrinder2.timemath.TimeMath;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

public class EfficiencyCountdown extends SherlockActivity implements OnClickListener {

    private String lastRuns;

    private final String EXP = "experience";
    private final String PER = "percent";

    private DecimalFormat df;

    private String results, stringToSave;
    private String resTop, resMid;
    private String resBot = "";
    private int time;

    private Chronometer timer;
    private Button done;
    private EditText doneNumber;

    private double current;
    private String name, type, location;
    private boolean save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_efficiency_countdown);
        init();
        timer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.efficiency);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_efficiency_clean);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void init() {
        lastRuns = getLastRuns();

        df = new DecimalFormat("#.##");

        timer = (Chronometer) findViewById(R.id.cExpTimer);
        done = (Button) findViewById(R.id.bDone);
        doneNumber = (EditText) findViewById(R.id.etDonepercent);

        doneNumber.setEnabled(false);
        doneNumber.setVisibility(View.INVISIBLE);

        done.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        current = bundle.getDouble("percent");
        name = bundle.getString("game");
        location = bundle.getString("location");
        save = bundle.getBoolean("save");
        type = bundle.getString("type");
        Log.d("Check", type);
    }

    public void saveToFile() {
        SharedPreferences sharedP = getSharedPreferences("lastruns", MODE_PRIVATE);
        SharedPreferences.Editor editP = sharedP.edit();
        editP.putString("lastruns", stringToSave + lastRuns);
        editP.commit();
        resBot += "\n\nThis result has been saved";
        results += "\n\nThis result has been saved";

    }

    @Override
    public void onClick(View v) {
        stoppedIt();
        doneNumber.setEnabled(true);
        doneNumber.setVisibility(View.VISIBLE);
        doneNumber.requestFocus();
        done.setText("Enter your " + type);

        if (doubleParseable(doneNumber.getText().toString()) && !doneNumber.getText().toString().equals("")) {
            if (checkNumbers()) {
                startResultScreen();
                // showResultDialog();
            } else {
                doneNumber.setTextColor(Color.RED);
            }
        } else if (doneNumber.getText().toString().equals("")) {
            // NOTHING
        } else {
            doneNumber.setBackgroundColor(Color.RED);
        }
    }

    private void stoppedIt() {
        timer.stop();
        time = TimeMath.getSeconds(timer.getText().toString());

    }

    private boolean checkNumbers() {
        double parsed;
        try {
            parsed = Double.parseDouble(doneNumber.getText().toString());
        } catch (Exception e) {
            return false;
        }
        if (type.equals(PER)) {
            if (parsed > 100 || current > parsed) {
                return false;
            }
        } else if (type.equals(EXP)) {
            if (current > parsed) {
                return false;
            }
        }
        return true;
    }

    private void createString(double percentMin) {

        String currentTime[] = TimeMath.getCurrentTime();
        Log.d("Time", "" + currentTime[0] + currentTime[1]);

        if (name == null || name.equals("")) {
            name = "";
        } else {
            name = name + ": ";
        }
        if (location == null || location.equals("")) {
            location = "";
        } else {
            // location = location;
        }

        if (type.equals(PER)) {
            results = "You grinded from " + current + "% to " + doneNumber.getText().toString() + "%." + " A "
                    + df.format((Double.parseDouble(doneNumber.getText().toString()) - current)) + "% increase over "
                    + time + " seconds.";

            stringToSave = currentTime[0] + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<b>" + name + location + "</b><br>"
                    + currentTime[1] + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<b><big>" + df.format(percentMin) + "%/min.\n"
                    + "</b></big>";

            resTop = results;
            resMid = df.format(percentMin) + "%/min";

        } else if (type.equals(EXP)) {
            results = "You grinded from " + current + " exp to " + doneNumber.getText().toString() + " exp." + " A "
                    + df.format((Double.parseDouble(doneNumber.getText().toString()) - current))
                    + " exp increase over " + time + " seconds.";

            stringToSave = currentTime[0] + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<b>" + name + location + "</b><br>"
                    + currentTime[1] + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<b><big>" + df.format(percentMin) + " exp/min.\n";

            resTop = results;
            resMid = df.format(percentMin) + " exp/min";

        }
        results += stringToSave + "\n";

        if (type.equals(PER)) {
            resBot = calculateTimeToLevelUp(percentMin);
            results += resBot;
        }
    }

    private String calculateTimeToLevelUp(double percentMin) {
        double toLevelUp = 100 - Double.parseDouble(doneNumber.getText().toString());
        double perSec = percentMin / 60;
        int seconds = (int) (toLevelUp / perSec);
        int minToLevelUp = (seconds / 60);
        int secToLevelUp = (seconds % 60);
        return "Time to level up: " + minToLevelUp + " minutes " + secToLevelUp + " seconds.";
    }

    private double getPerMinute() {
        double earned = Double.parseDouble(doneNumber.getText().toString()) - current;
        return (earned / time) * 60;
    }

    private String getLastRuns() {
        SharedPreferences sharedP = getSharedPreferences("lastruns", MODE_PRIVATE);
        return sharedP.getString("lastruns", "");

    }

    private void startResultScreen() {
        double finalResult = getPerMinute();
        createString(finalResult);
        if (save)
            saveToFile();
        Bundle bund = new Bundle();
        bund.putString("top", resTop);
        bund.putString("mid", resMid);
        bund.putString("bot", resBot);
        bund.putString("game", name);
        bund.putString("location", location);
        Log.d("Before", "" + resBot);
        Intent i = new Intent(EfficiencyCountdown.this, EfficiencyResult.class);
        i.putExtras(bund);
        startActivity(i);
        finish();

    }

    public boolean doubleParseable(String toTest) {
        try {
            Double.parseDouble(toTest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
