/*TODO: 
 * fikse utskriving te fil;
 */
package net.hermith.efficientgrinder.exppermin;

import net.hermith.efficientgrinder.R;
import net.hermith.efficientgrinder.R.id;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ExpPerMinute extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private final String SAVE = "savetofile";
	private final String REMEMBER = "rememberlocandgame";
	private final String RADIO = "radiobuttonchoice";
	private final String EXP = "experience";
	private final String PER = "percent";

	private String type;

	private ImageButton help, grind, showLastruns;
	private CheckBox save, remember;
	private RadioGroup datatype;
	private TextView percent;
	private EditText etGame, etLocation, etPercent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.efficiency);
		init();
		setStandards();

	}

	public void init() {
		help = (ImageButton) findViewById(R.id.ibHelp);
		grind = (ImageButton) findViewById(R.id.bGrind);
		showLastruns = (ImageButton) findViewById(R.id.ibLastRunsFromEfficiency);
		datatype = (RadioGroup) findViewById(R.id.rgTypeofdata);
		save = (CheckBox) findViewById(R.id.cbSavetofile);
		remember = (CheckBox) findViewById(R.id.cbOtherthing);
		percent = (TextView) findViewById(R.id.tvPercentage);
		etGame = (EditText) findViewById(R.id.etGame);
		etLocation = (EditText) findViewById(R.id.etLocation);
		etPercent = (EditText) findViewById(R.id.etPercentage);

		showLastruns.setOnClickListener(this);
		help.setOnClickListener(this);
		grind.setOnClickListener(this);
		datatype.setOnCheckedChangeListener(this);

	}

	public void setStandards() {
		SharedPreferences sharedP = getSharedPreferences("exp", MODE_PRIVATE);

		int radioState = Integer.parseInt(sharedP.getString(RADIO, "0"));

		if (radioState == R.id.rbPercent) {
			datatype.check(R.id.rbPercent);
			type = PER;
		} else if (radioState == R.id.rbExperience) {
			datatype.check(R.id.rbExperience);
			type = EXP;
		} else {
			datatype.check(R.id.rbPercent);
			type = PER;
		}

		String loc = sharedP.getString("location", null);
		String game = sharedP.getString("game", null);
		if (loc == null)
			loc = "";
		if (game == null)
			game = "";
		etGame.setText(game);
		etLocation.setText(loc);


		save.setChecked(sharedP.getBoolean(SAVE, true));
		remember.setChecked(sharedP.getBoolean(REMEMBER, true));
	}

	public void saveStandards() {
		SharedPreferences sharedP = getSharedPreferences("exp", MODE_PRIVATE);
		SharedPreferences.Editor editP = sharedP.edit();

		if (remember.isChecked()) {
			editP.putString(RADIO, "" + datatype.getCheckedRadioButtonId());
			editP.putBoolean(SAVE, save.isChecked());
			editP.putBoolean(REMEMBER, remember.isChecked());
			editP.putString("game", etGame.getText().toString());
			editP.putString("location", etLocation.getText().toString());
			editP.commit();
		} else {
			editP.putString(RADIO, "" + datatype.getCheckedRadioButtonId());
			editP.putBoolean(SAVE, save.isChecked());
			editP.putBoolean(REMEMBER, remember.isChecked());
			editP.putString("game", "");
			editP.putString("location", "");
			editP.commit();
		}
	}

	public boolean checkNumbers() {
		double parsed;
		try {
			parsed = Double.parseDouble(etPercent.getText().toString());
		} catch (Exception e) {
			return false;
		}
		if (type.equals(PER)) {
			if (parsed > 100) {
				return false;
			}
		} else if (type.equals(EXP)) {
			// Check nothing, already parseable
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case id.ibHelp:
			callDialog();
			break;
		case id.ibLastRunsFromEfficiency:
			Intent lastRuns = new Intent(ExpPerMinute.this, ShowLastRuns.class);
			startActivity(lastRuns);
			break;
		case id.bGrind:
			Log.d("Testing numbers", "Test");
			if (checkNumbers()) {
				saveStandards();

				Bundle bundle = new Bundle();
				bundle.putDouble(PER,
						Double.parseDouble(etPercent.getText().toString()));
				bundle.putString("location", etLocation.getText().toString());
				bundle.putString("game", etGame.getText().toString());
				bundle.putBoolean("save", save.isChecked());
				Log.d("Test", type);
				bundle.putString("type", type);

				Intent i = new Intent(ExpPerMinute.this, ExpCountDown.class);
				i.putExtras(bundle);
				startActivity(i);
				finish();
			} else {
				etPercent.setTextColor(Color.RED);
			}
			break;
		default:
			Log.d("HAHA LOL", "Sup");
			break;
		}

	}

	private void callDialog() {
		final Dialog help = new Dialog(this);
		help.setContentView(R.layout.dialog_exphelp);
		help.setTitle("Help - Exp per minute");
		Button okay = (Button) help.findViewById(R.id.bHelpOK);
		okay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				help.dismiss();
			}
		});
		help.show();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbPercent:
			percent.setText("Current percent:");
			type = PER;
			break;
		case R.id.rbExperience:
			percent.setText("Current experience:");
			type = EXP;
			break;
		}

	}

}
