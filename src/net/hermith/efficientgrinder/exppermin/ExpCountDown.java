package net.hermith.efficientgrinder.exppermin;

import java.text.DecimalFormat;

import net.hermith.efficientgrinder.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class ExpCountDown extends Activity implements OnClickListener {

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
		setContentView(R.layout.expcountdown);
		init();
		timer.start();
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

	private String getLastRuns() {
		SharedPreferences sharedP = getSharedPreferences("lastruns",
				MODE_PRIVATE);
		return sharedP.getString("lastruns", "");

	}

	public void saveToFile() {
		SharedPreferences sharedP = getSharedPreferences("lastruns",
				MODE_PRIVATE);
		SharedPreferences.Editor editP = sharedP.edit();
		editP.putString("lastruns", stringToSave + lastRuns);
		editP.commit();
		resBot += "\n\nThis result has been saved";
		results += "\n\nThis result has been saved";
		
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
		stoppedIt();
		doneNumber.setEnabled(true);
		doneNumber.setVisibility(View.VISIBLE);
		doneNumber.requestFocus();
		done.setText("Enter your " + type);

		if (doubleParseable(doneNumber.getText().toString())
				&& !doneNumber.getText().toString().equals("")) {
			if (checkNumbers()) {
				startResultScreen();
				//showResultDialog();
			} else {
				doneNumber.setTextColor(Color.RED);
			}
		} else if (doneNumber.getText().toString().equals("")) {
			// NOTHING
		} else {
			doneNumber.setBackgroundColor(Color.RED);
		}
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

	private void startResultScreen() {
		double finalResult = getPerMinute();
		createString(finalResult);
		if (save)
			saveToFile();
		Bundle bund = new Bundle();
		bund.putString("top", resTop);
		bund.putString("mid", resMid);
		bund.putString("bot", resBot);
		Log.d("Before", "" + resBot);
		Intent i = new Intent(ExpCountDown.this, ExpResultScreen.class);
		i.putExtras(bund);
		startActivity(i);
		finish();
		
	}
	
	private double getPerMinute() {
		double earned = Double.parseDouble(doneNumber.getText().toString())
				- current;
		return (earned / time) * 60;
	}

	private void createString(double percentMin) {

		String currentTime = getCurrentTime();
		Log.d("Time", currentTime);

		if (name == null || name.equals("")) {
			name = "";
		} else {
			name += " : ";
		}
		if (location == null || name.equals("")) {
			location = "";
		} else {
			location += " : ";
		}

		if (type.equals(PER)) {
			results = "You grinded from "
					+ current
					+ "% to "
					+ doneNumber.getText().toString()
					+ "%."
					+ " A "
					+ df.format((Double.parseDouble(doneNumber.getText()
							.toString()) - current)) + "% increase over "
					+ time + " seconds.";
			
			stringToSave = currentTime + name + location
					+ df.format(percentMin) + "%/min.\n";
			
			resTop = results;
			resMid = df.format(percentMin) + "%/min";

		} else if (type.equals(EXP)) {
			results = "You grinded from "
					+ current
					+ " exp to "
					+ doneNumber.getText().toString()
					+ " exp."
					+ " A "
					+ df.format((Double.parseDouble(doneNumber.getText()
							.toString()) - current)) + " exp increase over "
					+ time + " seconds.";
			
			stringToSave = currentTime + name + location
					+ df.format(percentMin) + " exp/min.\n";
			
			resTop = results;
			resMid = df.format(percentMin) + " exp/min";

		}
		results += stringToSave + "\n";
		
		if (type.equals(PER)) {
			resBot = timeToLevelUp(percentMin);
			results += resBot;
		}
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

	private String timeToLevelUp(double percentMin) {
		double toLevelUp = 100 - Double.parseDouble(doneNumber.getText()
				.toString());
		double perSec = percentMin/60;
		int seconds = (int) (toLevelUp/perSec);
		int minToLevelUp = (int) (seconds / 60);
		int secToLevelUp = (int) (seconds % 60);
		return "Time to level up: " + minToLevelUp + " minutes " + secToLevelUp
				+ " seconds.";
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
