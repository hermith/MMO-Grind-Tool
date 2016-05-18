package net.hermith.efficientgrinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class EfficientGrinderStarting extends Activity implements OnClickListener {
	
	private ImageButton efficiency, economy, timers;
	private Button swap;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    
    private void init() {
    	efficiency = (ImageButton) findViewById(R.id.bMainEfficiency);
    	economy = (ImageButton) findViewById(R.id.bMainEconomy);
    	timers = (ImageButton) findViewById(R.id.bMainTimers);
    	economy.setOnClickListener(this);
    	efficiency.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bMainEfficiency:
			Intent i = new Intent("net.hermith.efficientgrinder.exppermin.EFFI");
			startActivity(i);
			break;
		case R.id.bMainEconomy:
			Intent u = new Intent("net.hermith.efficientgrinder.economy.ECON");
			startActivity(u);
			break;
		case R.id.bMainTimers:
			//TODO
			break;
		
		}
		
	}
}