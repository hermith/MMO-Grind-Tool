package net.hermith.efficientgrinder.exppermin;

import net.hermith.efficientgrinder.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ExpResultScreen extends Activity implements OnClickListener{
	String resTop, resMid, resBot;
	TextView top, mid, bot;
	Button ok, lastruns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exp_result);
		getBundle();
		init();
		
	}

	private void getBundle() {
		Bundle bund = getIntent().getExtras();
		resTop = bund.getString("top");
		resMid = bund.getString("mid");
		resBot = bund.getString("bot");
		Log.d("After", resTop + resMid + resBot);
	}

	private void init() {
		top = (TextView) findViewById(R.id.tvExpResultTop);
		mid = (TextView) findViewById(R.id.tvExpResultPercent);
		bot = (TextView) findViewById(R.id.tvExpResultBot);
		
		ok = (Button) findViewById(R.id.bExpResultOK);
		lastruns = (Button) findViewById(R.id.bExpResultLastRuns);
		
		ok.setOnClickListener(this);
		lastruns.setOnClickListener(this);
		
		top.setText(resTop);
		mid.setText(resMid); // mid.setText(mid);
		bot.setText(resBot);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bExpResultOK:
			finish();
			break;
		case R.id.bExpResultLastRuns:
			Intent i = new Intent(ExpResultScreen.this, ShowLastRuns.class);
			startActivity(i);
			finish();
		}
		
	}

}
