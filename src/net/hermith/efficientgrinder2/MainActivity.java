package net.hermith.efficientgrinder2;

import net.hermith.efficientgrinder2.activities.economy.EconomyMainActivity;
import net.hermith.efficientgrinder2.activities.efficiency.EfficiencyMainActivity;
import net.hermith.efficientgrinder2.activities.Preferences;
import net.hermith.efficientgrinder2.activities.TimerMainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener {

    private ImageButton efficiency, economy, timers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        init();
    }

    private void init() {
        efficiency = (ImageButton) findViewById(R.id.bMainEfficiency);
        economy = (ImageButton) findViewById(R.id.bMainEconomy);
        timers = (ImageButton) findViewById(R.id.bMainTimers);
        economy.setOnClickListener(this);
        efficiency.setOnClickListener(this);
        timers.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);
        ab.setHomeButtonEnabled(true);
        ab.setIcon(R.drawable.ic_launcher);

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Log.d("wat", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(MainActivity.this, Preferences.class);
                startActivity(i);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bMainEfficiency:
                Intent i = new Intent(MainActivity.this, EfficiencyMainActivity.class);
                startActivity(i);
                break;
            case R.id.bMainEconomy:
                Intent u = new Intent(MainActivity.this, EconomyMainActivity.class);
                startActivity(u);
                break;
            case R.id.bMainTimers:
                Intent n = new Intent(MainActivity.this, TimerMainActivity.class);
                startActivity(n);
                break;
        }
    }
}