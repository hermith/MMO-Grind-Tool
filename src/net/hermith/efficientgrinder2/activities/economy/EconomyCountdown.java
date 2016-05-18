package net.hermith.efficientgrinder2.activities.economy;


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
import android.widget.LinearLayout;
import android.widget.TextView;

public class EconomyCountdown extends SherlockActivity implements OnClickListener {

    private String lastRuns;

    private final String LOC = "location";
    private final String MONEY = "money";

    private String stringToSave;
    private String resTop, resMid, resBot;
    private int time;

    private Chronometer timer;
    private Button done;
    private EditText etOneAmount, etTwoAmount, etThreeAmount, etMoney;
    private LinearLayout llOne, llTwo, llThree, llMoney;
    private TextView tvOne, tvTwo, tvThree;

    private int money, highestChecked, itemValue;
    private int onePrice, oneAmount, twoPrice, twoAmount, threePrice, threeAmount;
    private int moneyResult, oneResult, twoResult, threeResult;
    private String location, game, oneName, twoName, threeName;
    private boolean save;
    private boolean firstTime = true;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_economy_countdown);
        if (firstTime) {
            init();
            retrieveBundle();
            timer.start();
        }
        firstTime = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.economy);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_economy_clean);

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

        timer = (Chronometer) findViewById(R.id.cEconTimer);
        done = (Button) findViewById(R.id.bDoneEcon);

        etMoney = (EditText) findViewById(R.id.etDoneMoneyEcon);
        etOneAmount = (EditText) findViewById(R.id.etOneDoneAmountEcon);
        etTwoAmount = (EditText) findViewById(R.id.etTwoDoneAmountEcon);
        etThreeAmount = (EditText) findViewById(R.id.etThreeDoneAmontEcon);
        tvOne = (TextView) findViewById(R.id.tvOneDoneAmountEcon);
        tvTwo = (TextView) findViewById(R.id.tvTwoDoneAmountEcon);
        tvThree = (TextView) findViewById(R.id.tvThreeDoneAmountEcon);
        llMoney = (LinearLayout) findViewById(R.id.llMoneyEcon);
        llOne = (LinearLayout) findViewById(R.id.llFirstEcon);
        llTwo = (LinearLayout) findViewById(R.id.llSecondEcon);
        llThree = (LinearLayout) findViewById(R.id.llThirdEcon);

        llMoney.setVisibility(View.GONE);
        llOne.setVisibility(View.GONE);
        llTwo.setVisibility(View.GONE);
        llThree.setVisibility(View.GONE);

        done.setOnClickListener(this);
    }

    public void retrieveBundle() {
        Bundle b = getIntent().getExtras();
        highestChecked = b.getInt("checked");
        money = parseString(b.getString(MONEY));
        game = b.getString("game");
        location = b.getString(LOC);
        save = b.getBoolean("save");
        Log.d("Save?:", "" + save);

        if (highestChecked > 0) {
            onePrice = parseString(b.getString("onePrice"));
            oneAmount = parseString(b.getString("oneAmount"));
            oneName = b.getString("oneName");
            tvOne.setText(oneName);
        }
        if (highestChecked > 1) {
            twoPrice = parseString(b.getString("twoPrice"));
            twoAmount = parseString(b.getString("twoAmount"));
            twoName = b.getString("twoName");
            tvTwo.setText(twoName);
        }
        if (highestChecked > 2) {
            threePrice = parseString(b.getString("threePrice"));
            threeAmount = parseString(b.getString("threeAmount"));
            threeName = b.getString("threeName");
            tvThree.setText(threeName);
        }
    }

    public void saveToFile() {
        SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
        SharedPreferences.Editor editP = sharedP.edit();
        editP.putString("lastrunsEcon", stringToSave + lastRuns);
        editP.commit();
    }

    private int parseString(String s) {
        return Integer.parseInt(s);
    }

    private void stoppedIt() {
        timer.stop();
        time = TimeMath.getSeconds(timer.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if (!clicked) {
            stoppedIt();
            done.setText("Enter your data");
            llMoney.setVisibility(View.VISIBLE);
            llMoney.requestFocus();
            if (highestChecked > 0) {
                llOne.setVisibility(View.VISIBLE);
            }
            if (highestChecked > 1) {
                llTwo.setVisibility(View.VISIBLE);
            }
            if (highestChecked > 2) {
                llThree.setVisibility(View.VISIBLE);
            }
            clicked = true;
        } else if (clicked) {
            if (checkNumbers()) {
                startResultScreen();
            } else {
                done.setTextColor(Color.RED);
                done.setText("Check your numbers");
            }
        }

    }

    private boolean checkNumbers() {
        if (highestChecked > 0) {
            if (!pAble(etOneAmount.getText().toString())) {
                return false;
            }
        }
        if (highestChecked > 1) {
            if (!pAble(etTwoAmount.getText().toString())) {
                return false;
            }
        }
        if (highestChecked > 2) {
            if (!pAble(etThreeAmount.getText().toString())) {
                return false;
            }
        }
        if (!pAble(etMoney.getText().toString())) {
            return false;
        }
        return true;
    }

    private void createString() {
        if (game == null || game.equals("")) {
            game = "";
        } else {
            game += ": ";
        }
        if (location == null || location.equals("")) {
            location = "";
        } else {
            //location += " : ";
        }

        // Top result
        calculateItemsandMoneyUsed();
        String currentTime[] = TimeMath.getCurrentTime();
        String itemsUsed = "";
        if (highestChecked > 0) {
            itemsUsed = "<br>Items:" + "&nbsp;" + oneName + ": " + (oneResult - oneAmount) + "<br>";
        }
        if (highestChecked > 1) {
            itemsUsed += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + twoName + ": " + (twoResult - twoAmount) + "<br>";
        }
        if (highestChecked > 2) {
            itemsUsed += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + threeName + ": " + (threeResult - threeAmount) + "<br>";
        }
        resTop = "You grinded for " + time + " seconds. " + itemsUsed;
        stringToSave = currentTime[0] + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<b>" + game + location + "</b><br>"
                + currentTime[1] + itemsUsed + "<br>";

        // Mid result
        int moneyPerMin = getPerMinute();
        resMid = "<b><big>Earned: " + (moneyPerMin) + " per minute</b></big><br>";
        resMid += "Money gained: " + moneyResult + " total";
        if (highestChecked > 0)
            if (itemValue < 0) {
                resMid += "<br>Item loss: " + itemValue + " money";
            } else if (itemValue >= 0) {
                resMid += "<br>Item gain: " + itemValue + " money ";
            }
        stringToSave += resMid + "!%!";

        // Bottom
        if (save)
            resBot = "This result has been saved";
    }

    private void calculateItemsandMoneyUsed() {

        itemValue = 0;

        // Items
        if (highestChecked > 0) {
            oneResult = parseString(etOneAmount.getText().toString());
            itemValue += (parseString(etOneAmount.getText().toString()) - oneAmount) * onePrice;
        }
        if (highestChecked > 1) {
            twoResult = parseString(etTwoAmount.getText().toString());
            itemValue += (parseString(etTwoAmount.getText().toString()) - twoAmount) * twoPrice;
            Log.d("Value", "" + (parseString(etTwoAmount.getText().toString()) - twoAmount));
        }
        if (highestChecked > 2) {
            threeResult = parseString(etThreeAmount.getText().toString());
            itemValue += (parseString(etThreeAmount.getText().toString()) - threeAmount) * threePrice;
        }

        // Money earned
        moneyResult = (parseString(etMoney.getText().toString()) - money);

    }

    private int getPerMinute() {
        /*
		 * double earned = moneyResult-itemValue; Log.d("Earned", "" + earned);
		 * return (int) (earned / time) * 60;
		 */

        double moneyMin = (moneyResult / time) * 60;
        double itemMin = (itemValue / time) * 60;
        return (int) (moneyMin + itemMin);
    }

    private String getLastRuns() {
        SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
        return sharedP.getString("lastrunsEcon", "");

    }

    private void startResultScreen() {
        createString();
        if (save) {
            saveToFile();
        }
        Log.d("Log", stringToSave);

        Bundle bund = new Bundle();
        bund.putString("top", resTop);
        bund.putString("mid", resMid);
        bund.putString("bot", resBot);
        bund.putString("game", game);
        bund.putString("location", location);
        Intent i = new Intent(EconomyCountdown.this, EconomyResult.class);
        i.putExtras(bund);
        startActivity(i);
        finish();
    }

    private boolean pAble(String s) {
        if (s == null)
            return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
