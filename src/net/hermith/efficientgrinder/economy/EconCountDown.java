package net.hermith.efficientgrinder.economy;

import java.text.DecimalFormat;

import net.hermith.efficientgrinder.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EconCountDown extends Activity implements OnClickListener {

	private String lastRuns;

	private final String LOC = "location";
	private final String MONEY = "money";

	private String results, stringToSave;
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
	private boolean firstTime = false;
	private boolean clicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_countdown);
		if (!firstTime) {
			init();
			retrieveBundle();
			timer.start();
		}
		firstTime = true;
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
		money = p(b.getString(MONEY));
		game = b.getString("game");
		location = b.getString(LOC);
		save = b.getBoolean("save");
		Log.d("Save?:", "" + save);

		if (highestChecked > 0) {
			onePrice = p(b.getString("onePrice"));
			oneAmount = p(b.getString("oneAmount"));
			oneName = b.getString("oneName");
			tvOne.setText(oneName);
		}
		if (highestChecked > 1) {
			twoPrice = p(b.getString("twoPrice"));
			twoAmount = p(b.getString("twoAmount"));
			twoName = b.getString("twoName");
			tvTwo.setText(twoName);
		}
		if (highestChecked > 2) {
			threePrice = p(b.getString("threePrice"));
			threeAmount = p(b.getString("threeAmount"));
			threeName = b.getString("threeName");
			tvThree.setText(threeName);
		}

	}

	private int p(String s) {
		return Integer.parseInt(s);
	}

	private String getLastRuns() {
		SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
		return sharedP.getString("lastrunsEcon", "");

	}

	public void saveToFile() {
		SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
		SharedPreferences.Editor editP = sharedP.edit();
		editP.putString("lastrunsEcon", stringToSave + lastRuns);
		editP.commit();

	}

	private void stoppedIt() {
		timer.stop();
		time = getSeconds(timer.getText().toString());
	}

	private int getSeconds(String time) {
		String[] times = time.split(":");
		int min = Integer.parseInt(times[0]);
		int sec = Integer.parseInt(times[1]);
		Log.d("Time", "" + (min * 60) + sec);
		return (min * 60) + sec;
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
		Intent i = new Intent(EconCountDown.this, EconResultScreen.class);
		i.putExtras(bund);
		startActivity(i);
		finish();
	}

	private void createString() {
		if (game == null || game.equals("")) {
			game = "";
		} else {
			game += " : ";
		}
		if (location == null || location.equals("")) {
			location = "";
		} else {
			location += " : ";
		}

		// Top result
		calculateItemsandMoneyUsed();
		String itemsUsed = "";
		if (highestChecked > 0) {
			itemsUsed = "\nItems:\n" + oneName + ": " + (oneResult - oneAmount) + "\n";
		}
		if (highestChecked > 1) {
			itemsUsed += twoName + ": " + (twoResult - twoAmount) + "\n";
		}
		if (highestChecked > 2) {
			itemsUsed += threeName + ": " + (threeResult - threeAmount) + "\n";
		}
		resTop = "You grinded for " + time + " seconds. " + itemsUsed;
		stringToSave = getCurrentTime() + game + location + itemsUsed + "\n";

		// Mid result
		int moneyPerMin = getPerMinute();
		resMid = "Earned: " + (moneyPerMin) + " per minute\n";
		resMid += "Money gained: " + moneyResult + " total";
		if (highestChecked > 0)
			if (itemValue < 0) {
				resMid += "\nItem loss: " + itemValue + " money";
			} else if (itemValue >= 0) {
				resMid += "\nItem gain: " + itemValue + " money ";
			}
		stringToSave += resMid + "!%!";

		// Bottom
		if (save)
			resBot = "This result has been saved";
	}

	private int getPerMinute() {
		/*double earned = moneyResult-itemValue;
		Log.d("Earned", "" + earned);
		return (int) (earned / time) * 60;*/
		
		double moneyMin = (moneyResult/time)*60;
		double itemMin = (itemValue/time)*60;
		return (int) (moneyMin+itemMin);
	}

	private void calculateItemsandMoneyUsed() {

		itemValue = 0;

		// Items
		if (highestChecked > 0) {
			oneResult = p(etOneAmount.getText().toString());
			itemValue += (p(etOneAmount.getText().toString()) - oneAmount) * onePrice;
		}
		if (highestChecked > 1) {
			twoResult = p(etTwoAmount.getText().toString());
			itemValue += (p(etTwoAmount.getText().toString()) - twoAmount) * twoPrice;
			Log.d("Value", "" + (p(etTwoAmount.getText().toString()) - twoAmount));
		}
		if (highestChecked > 2) {
			threeResult = p(etThreeAmount.getText().toString());
			itemValue += (p(etThreeAmount.getText().toString()) - threeAmount) * threePrice;
		}

		// Money earned
		moneyResult = (p(etMoney.getText().toString())-money);

	}

	private String getCurrentTime() {
		Time time = new Time();
		time.setToNow();

		String timeToHandle = time.toString();

		String year = timeToHandle.substring(0, 3);
		String month = timeToHandle.substring(4, 6);
		String day = timeToHandle.substring(6, 8);
		String hour = timeToHandle.substring(9, 11);
		String min = timeToHandle.substring(11, 13);

		return month + "." + day + " " + hour + ":" + min + " : ";

	}
}
