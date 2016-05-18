package net.hermith.efficientgrinder.exppermin;

import net.hermith.efficientgrinder.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ShowLastRuns extends Activity implements OnClickListener {
	
	Button back, clear;
	ListView lvLastRuns;
	String[] runs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showlastruns);
		init();
	}

	public void init() {
		back = (Button) findViewById(R.id.bLastrunsBack);
		clear = (Button) findViewById(R.id.bLastrunsClear);
		lvLastRuns = (ListView) findViewById(R.id.lvExplastruns);
		
		back.setOnClickListener(this);
		clear.setOnClickListener(this);

		String lastRuns = getLastRuns();
		runs = lastRuns.split("\n");
		
		lvLastRuns.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.lv_item, runs));

	}
	
	private String getLastRuns() {
		SharedPreferences sharedP = getSharedPreferences("lastruns",
				MODE_PRIVATE);
		return sharedP.getString("lastruns", "");

	}
	
	private boolean deleteLastRuns() {
		SharedPreferences sharedP = getSharedPreferences("lastruns",
				MODE_PRIVATE);
		SharedPreferences.Editor editP = sharedP.edit();
		editP.putString("lastruns", "");
		editP.commit();
		runs = new String[0];
		return true;
	}
	
	private void clearLastruns() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage("Are you sure you want to clear the list?")
		.setCancelable(true)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deleteLastRuns();
				lvLastRuns.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
						R.layout.lv_item, runs));
				lvLastRuns.invalidateViews();
			}
		})
		.setNegativeButton("No, get me away!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		AlertDialog alert = b.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bLastrunsBack:
			finish();
			break;
		case R.id.bLastrunsClear:
			clearLastruns();
			break;
		}

	}

}