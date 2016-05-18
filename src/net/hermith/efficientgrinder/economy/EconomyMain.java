package net.hermith.efficientgrinder.economy;

import net.hermith.efficientgrinder.R;
import net.hermith.efficientgrinder.R.id;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class EconomyMain extends Activity implements OnClickListener, OnCheckedChangeListener {

	private final String RADIO = "radiogroupOptionEcon";
	private final String SAVE = "saveEcon";
	private final String REMEMBER = "rememberEcon";

	private int highestChecked;

	private CheckBox save, remember;
	private RadioGroup numOfOptions;
	private ImageButton lastruns, help, startGrind;
	private TextView tvWarning;

	private EditText etOnePrice, etOneName, etTwoPrice, etTwoName, etThreePrice, etThreeName;
	private EditText etOneAmount, etTwoAmount, etThreeAmount, etMoney, etLocation, etGame;

	private LinearLayout first, second, third, top;
	private LinearLayout[] lins;
	private EditText[][] allET;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.economy_main);
		init();
		setStandards();

	}

	// Initializes buttons and text fields
	private void init() {
		save = (CheckBox) findViewById(R.id.cbSavetofileEconomy);
		remember = (CheckBox) findViewById(R.id.cbRememberEconomy);
		numOfOptions = (RadioGroup) findViewById(R.id.rgNumberofTypesEconomy);
		lastruns = (ImageButton) findViewById(R.id.ibLastRunsFromEconomy);
		help = (ImageButton) findViewById(R.id.ibHelpEconomy);
		startGrind = (ImageButton) findViewById(R.id.ibEconStartGrind);

		tvWarning = (TextView) findViewById(R.id.tvWarning);
		tvWarning.setTextColor(Color.RED);

		etMoney = (EditText) findViewById(R.id.etEconMoney);
		etLocation = (EditText) findViewById(R.id.etLocationEcon);
		etGame = (EditText) findViewById(R.id.etGameEcon);

		etOnePrice = (EditText) findViewById(R.id.etOnePrice);
		etOneName = (EditText) findViewById(R.id.etOneName);
		etOneAmount = (EditText) findViewById(R.id.etOneAmount);
		etTwoPrice = (EditText) findViewById(R.id.etTwoPrice);
		etTwoName = (EditText) findViewById(R.id.etTwoName);
		etTwoAmount = (EditText) findViewById(R.id.etTwoAmount);
		etThreePrice = (EditText) findViewById(R.id.etThreePrice);
		etThreeName = (EditText) findViewById(R.id.etThreeName);
		etThreeAmount = (EditText) findViewById(R.id.etThreeAmount);

		lastruns.setOnClickListener(this);
		startGrind.setOnClickListener(this);
		help.setOnClickListener(this);
		numOfOptions.setOnCheckedChangeListener(this);

		first = (LinearLayout) findViewById(R.id.groupFirstEcon);
		second = (LinearLayout) findViewById(R.id.groupSecondEcon);
		third = (LinearLayout) findViewById(R.id.groupThirdEcon);
		top = (LinearLayout) findViewById(R.id.tvsAnnotationsTop);

		LinearLayout[] lans = { first, second, third };
		lins = lans;

	}

	// Loads standards from preferences and set them accordingly
	private void setStandards() {
		SharedPreferences sharedP = getSharedPreferences("econ", MODE_PRIVATE);
		int radioState = Integer.parseInt(sharedP.getString(RADIO, "0"));
		Log.d("radioState:", "" + radioState);
		int thisOne = -1;
		switch (radioState) {
		case R.id.rbItemsOff:
			numOfOptions.check(R.id.rbItemsOff);
			highestChecked = 0;
			Log.d("Data set:", "Items off");
			break;
		case R.id.rbOneAdd:
			numOfOptions.check(R.id.rbOneAdd);
			thisOne = 0;
			highestChecked = 1;
			Log.d("Data set:", "Items 1");
			break;
		case R.id.rbTwoAdd:
			numOfOptions.check(R.id.rbTwoAdd);
			highestChecked = 2;
			thisOne = 1;
			Log.d("Data set:", "Items 2");
			break;
		case R.id.rbThreeAdd:
			numOfOptions.check(R.id.rbThreeAdd);
			highestChecked = 3;
			thisOne = 2;
			Log.d("Data set:", "Items 3");
			break;
		case 0:
			numOfOptions.check(R.id.rbItemsOff);
			highestChecked = 0;
			Log.d("Data set", "Base: 1");
		}
		hideAllBut(thisOne);

		etGame.setText(sharedP.getString("game", ""));
		etLocation.setText(sharedP.getString("location", ""));
		etOnePrice.setText(sharedP.getString("onePrice", ""));
		etTwoPrice.setText(sharedP.getString("twoPrice", ""));
		etThreePrice.setText(sharedP.getString("threePrice", ""));
		etOneName.setText(sharedP.getString("oneName", ""));
		etTwoName.setText(sharedP.getString("twoName", ""));
		etThreeName.setText(sharedP.getString("threeName", ""));

		save.setChecked(sharedP.getBoolean(SAVE, true));
		remember.setChecked(sharedP.getBoolean(REMEMBER, true));
	}

	// Hides text fields
	private void hideAllBut(int thisOne) {
		for (LinearLayout l : lins) {
			l.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		}
		for (int i = 2; i > thisOne; i--) {
			lins[i].setVisibility(View.GONE);
		}
		if (thisOne == -1)
			top.setVisibility(View.GONE);
	}

	// Saves standards to properties
	private void saveStandards() {
		SharedPreferences sharedP = getSharedPreferences("econ", MODE_PRIVATE);
		SharedPreferences.Editor editP = sharedP.edit();

		if (remember.isChecked()) {
			editP.putString(RADIO, "" + numOfOptions.getCheckedRadioButtonId());
			editP.putBoolean(SAVE, save.isChecked());
			editP.putBoolean(REMEMBER, remember.isChecked());
			editP.putString("location", etLocation.getText().toString());
			editP.putString("game", etGame.getText().toString());
			editP.putString("onePrice", etOnePrice.getText().toString());
			editP.putString("oneName", etOneName.getText().toString());
			editP.putString("twoPrice", etTwoPrice.getText().toString());
			editP.putString("twoName", etTwoName.getText().toString());
			editP.putString("threePrice", etThreePrice.getText().toString());
			editP.putString("threeName", etThreeName.getText().toString());
			editP.commit();
		} else {
			editP.putString(RADIO, "" + numOfOptions.getCheckedRadioButtonId());
			editP.putBoolean(SAVE, save.isChecked());
			editP.putBoolean(REMEMBER, remember.isChecked());
			editP.putString("location", "");
			editP.putString("game", "");
			editP.putString("onePrice", "");
			editP.putString("oneName", "");
			editP.putString("twoPrice", "");
			editP.putString("twoName", "");
			editP.putString("threePrice", "");
			editP.putString("threeName", "");
			editP.commit();
		}
	}

	// Checks whether or not all numbers that needs to be parseable are
	// parseable.
	private boolean checkNumbers() {
		boolean hasBadOccured = false;
		EditText[] lastOccured = new EditText[6];
		int counter = 0;
		if (highestChecked > 0) {
			if (!parseAbleInt(etOnePrice.getText().toString())) {
				etOnePrice.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etOnePrice.refreshDrawableState();
				lastOccured[counter] = etOnePrice;
				counter++;
			}
			if (!parseAbleInt(etOneAmount.getText().toString())) {
				etOneAmount.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etOneAmount.refreshDrawableState();
				lastOccured[counter] = etOneAmount;
				counter++;
			}
		}
		if (highestChecked > 1) {
			if (!parseAbleInt(etTwoPrice.getText().toString())) {
				etTwoPrice.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etTwoPrice.refreshDrawableState();
				lastOccured[counter] = etTwoPrice;
				counter++;
			}
			if (!parseAbleInt(etTwoAmount.getText().toString())) {
				etTwoAmount.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etTwoAmount.refreshDrawableState();
				lastOccured[counter] = etTwoAmount;
				counter++;
			}
		}
		if (highestChecked > 2) {
			if (!parseAbleInt(etThreePrice.getText().toString())) {
				etThreePrice.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etThreePrice.refreshDrawableState();
				lastOccured[counter] = etThreePrice;
				counter++;
			}

			if (!parseAbleInt(etThreeAmount.getText().toString())) {
				etThreeAmount.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
				hasBadOccured = true;
				etThreeAmount.refreshDrawableState();
				lastOccured[counter] = etThreeAmount;
				counter++;
			}
		}

		if (!parseAbleInt(etMoney.getText().toString())) {
			etMoney.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SCREEN);
			hasBadOccured = true;
			etMoney.refreshDrawableState();
		}

		// If all above are parseable the function will return true
		if (hasBadOccured) {
			tvWarning.setText("Error: Check your numbers");
			if (highestChecked > 0) {
				if (lastOccured[0] != null && lastOccured[0].hasFocus() && lastOccured[1] != null) {
					lastOccured[1].requestFocus();
					return false;
				}
				if (lastOccured[0] != null) {
					lastOccured[0].requestFocus();
				}
			}
			return false;
		} else {
			return true;
		}
	}

	// A quick parsechecker.
	private boolean parseAbleInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Standard onClick listener.
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.ibHelpEconomy:
			callDialog();
			break;
		case id.ibLastRunsFromEconomy:
			Intent lastRuns = new Intent(EconomyMain.this, ShowLastRunsEcon.class);
			startActivity(lastRuns);
			break;
		case id.ibEconStartGrind:
			saveStandards();
			if (checkNumbers()) {
				Bundle bundy = new Bundle();
				bundy.putString("money", etMoney.getText().toString());
				bundy.putString("location", etLocation.getText().toString());
				bundy.putString("game", etGame.getText().toString());
				bundy.putInt("checked", highestChecked);
				bundy.putBoolean("save", save.isChecked());
				if (highestChecked > 0) {
					bundy.putString("onePrice", etOnePrice.getText().toString());
					bundy.putString("oneName", etOneName.getText().toString());
					bundy.putString("oneAmount", etOneAmount.getText().toString());
				}
				if (highestChecked > 1) {
					bundy.putString("twoPrice", etTwoPrice.getText().toString());
					bundy.putString("twoName", etTwoName.getText().toString());
					bundy.putString("twoAmount", etTwoAmount.getText().toString());
				}
				if (highestChecked > 2) {
					bundy.putString("threePrice", etThreePrice.getText().toString());
					bundy.putString("threeName", etThreeName.getText().toString());
					bundy.putString("threeAmount", etThreeAmount.getText().toString());
				}

				Intent i = new Intent(EconomyMain.this, EconCountDown.class);
				i.putExtras(bundy);
				startActivity(i);
				finish();
			} else {
				// TODO: If number checker doesn't go through, set to red text
				// in approriate field
			}
			break;
		}

	}

	// Calls help dialog when help button is pressed.
	private void callDialog() {
		final Dialog help = new Dialog(this);
		help.setContentView(R.layout.dialog_econhelp);
		help.setTitle("Help - Economy");
		Button okay = (Button) help.findViewById(R.id.bHelpOK);
		okay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				help.dismiss();
			}
		});
		help.show();

	}

	// Standard checkedChange listener.
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) { // TODO
		int thisOne = -1;
		switch (checkedId) {
		case R.id.rbItemsOff:
			highestChecked = 0;
			break;
		case R.id.rbOneAdd:
			thisOne = 0;
			highestChecked = 1;
			break;
		case R.id.rbTwoAdd:
			thisOne = 1;
			highestChecked = 2;
			break;
		case R.id.rbThreeAdd:
			thisOne = 2;
			highestChecked = 3;
			break;

		}
		hideAllBut(thisOne);
	}

}
