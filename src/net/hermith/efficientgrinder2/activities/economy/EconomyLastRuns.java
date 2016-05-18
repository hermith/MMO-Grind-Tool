package net.hermith.efficientgrinder2.activities.economy;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.hermith.efficientgrinder2.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class EconomyLastRuns extends SherlockActivity implements OnClickListener {

    Button back, clear;
    ListView lvLastRuns;
    String[] runs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lastruns);
        init();
    }

    public void init() {
        back = (Button) findViewById(R.id.bLastrunsBackEcon);
        clear = (Button) findViewById(R.id.bLastrunsClearEcon);
        lvLastRuns = (ListView) findViewById(R.id.lvlastrunsEcon);

        back.setOnClickListener(this);
        clear.setOnClickListener(this);

        String lastRuns = getLastRuns();
        runs = lastRuns.split("!%!");
        Spanned[] runsSpanned = new Spanned[runs.length];
        for (int i = 0; i < runs.length; i++) {
            runsSpanned[i] = Html.fromHtml(runs[i]);
        }

        lvLastRuns.setAdapter(new ArrayAdapter<Spanned>(getApplicationContext(), R.layout.lv_item, runsSpanned));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getSupportMenuInflater();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.prevrunsecon);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_economy_clean);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Log.d("" + item.getTitle(), "" + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLastrunsBackEcon:
                finish();
                break;
            case R.id.bLastrunsClearEcon:
                clearLastruns();
                break;
        }

    }

    private String getLastRuns() {
        SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
        return sharedP.getString("lastrunsEcon", "");

    }

    private boolean deleteLastRuns() {
        SharedPreferences sharedP = getSharedPreferences("econ_lastruns", MODE_PRIVATE);
        SharedPreferences.Editor editP = sharedP.edit();
        editP.putString("lastrunsEcon", "");
        editP.commit();
        runs = new String[0];
        return true;
    }

    private void clearLastruns() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Are you sure you want to clear the list?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLastRuns();
                        lvLastRuns
                                .setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lv_item, runs));
                        lvLastRuns.invalidateViews();
                    }
                }).setNegativeButton("No, get me away!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alert = b.create();
        alert.show();
    }

}