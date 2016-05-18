package net.hermith.efficientgrinder2.activities.efficiency;

import net.hermith.efficientgrinder2.R;
import net.hermith.efficientgrinder2.R.id;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import net.hermith.efficientgrinder2.activities.Preferences;

public class EfficiencyMainActivity extends SherlockActivity implements OnClickListener,
        OnCheckedChangeListener {

    private final String SAVE = "savetofile";
    private final String REMEMBER = "rememberlocandgame";
    private final String RADIO = "radiobuttonchoice";
    private final String EXP = "experience";
    private final String PER = "percent";

    private String type;

    private Button mButtonGrind;
    private CheckBox mCheckBoxSave, mCheckBoxRemember;
    private RadioGroup mRadioGroupInputType;
    private TextView mTextViewPercent;
    private EditText mEditTextGame, mEditTextLocation, mEditTextPercent;

    private LinearLayout mLinearLayoutQuickOptions;

    private boolean mBoolAlwaySave, mBoolAlwaysRemember, mBoolAlwaysPercent, mBoolOneGame;
    private String mStringGameName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_efficiency_main);
        init();
        setStandards();
        loadPreferences();

    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mBoolAlwaySave = prefs.getBoolean(Preferences.MODE_ALWAYS_SAVE_EFFI, false);
        mBoolAlwaysRemember = prefs.getBoolean(Preferences.MODE_ALWAYS_REMEMBER_EFFI, false);
        mBoolAlwaysPercent = prefs.getBoolean(Preferences.MODE_ALWAYS_PERCENT_EFFI, false);
        mBoolOneGame = prefs.getBoolean(Preferences.MODE_ONE_GAME, false);
        mStringGameName = prefs.getString(Preferences.MODE_ONE_GAME_NAME, "");

        if (mBoolAlwaySave && mBoolAlwaysRemember && mBoolAlwaysPercent) {
            mLinearLayoutQuickOptions.setVisibility(View.GONE);
        } else {
            mLinearLayoutQuickOptions.setVisibility(View.VISIBLE);
        }

        if (mBoolAlwaySave) {
            mCheckBoxSave.setVisibility(View.GONE);
            mCheckBoxSave.setChecked(true);
        } else {
            mCheckBoxSave.setVisibility(View.VISIBLE);
        }

        if (mBoolAlwaysRemember) {
            mCheckBoxRemember.setVisibility(View.GONE);
            mCheckBoxRemember.setChecked(true);
        } else {
            mCheckBoxRemember.setVisibility(View.VISIBLE);
        }

        if (mBoolAlwaysPercent) {
            mRadioGroupInputType.setVisibility(View.GONE);
            mRadioGroupInputType.check(R.id.rbPercent);
        } else {
            mRadioGroupInputType.setVisibility(View.VISIBLE);
        }

        if (mBoolOneGame) {
            mEditTextGame.setText(mStringGameName);
            mEditTextGame.setVisibility(View.GONE);
        } else {
            mEditTextGame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        loadPreferences();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.efficiency);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_efficiency_clean);

        inflater.inflate(R.menu.menu_econ, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Log.d("" + item.getTitle(), "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.previous_runs:
                Intent lastRuns = new Intent(EfficiencyMainActivity.this, EfficiencyLastRuns.class);
                startActivity(lastRuns);
                return true;
            case R.id.settings:
                Intent i = new Intent(EfficiencyMainActivity.this, Preferences.class);
                startActivity(i);
                return true;
            case R.id.help:
                callDialog();
                return true;
            case android.R.id.home:
                saveStandards();
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void init() {
        mButtonGrind = (Button) findViewById(R.id.bGrind);
        mRadioGroupInputType = (RadioGroup) findViewById(R.id.rgTypeofdata);
        mCheckBoxSave = (CheckBox) findViewById(R.id.cbSavetofile);
        mCheckBoxRemember = (CheckBox) findViewById(R.id.cbOtherthing);
        mTextViewPercent = (TextView) findViewById(R.id.tvPercentage);
        mEditTextGame = (EditText) findViewById(R.id.etGameEffi);
        mEditTextLocation = (EditText) findViewById(R.id.etLocationEffi);
        mEditTextPercent = (EditText) findViewById(R.id.etPercentage);

        mLinearLayoutQuickOptions = (LinearLayout) findViewById(R.id.quickoptseffi);

        mButtonGrind.setOnClickListener(this);
        mRadioGroupInputType.setOnCheckedChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        saveStandards();
        finish();
        super.onBackPressed();
    }

    public void setStandards() {
        SharedPreferences sharedP = getSharedPreferences("exp", MODE_PRIVATE);

        int radioState = Integer.parseInt(sharedP.getString(RADIO, "0"));

        if (radioState == R.id.rbPercent) {
            mRadioGroupInputType.check(R.id.rbPercent);
            type = PER;
        } else if (radioState == R.id.rbExperience) {
            mRadioGroupInputType.check(R.id.rbExperience);
            type = EXP;
        } else {
            mRadioGroupInputType.check(R.id.rbPercent);
            type = PER;
        }

        String loc = sharedP.getString("location", null);
        String game = sharedP.getString("game", null);
        if (loc == null)
            loc = "";
        if (game == null)
            game = "";
        mEditTextGame.setText(game);
        mEditTextLocation.setText(loc);


        mCheckBoxSave.setChecked(sharedP.getBoolean(SAVE, true));
        mCheckBoxRemember.setChecked(sharedP.getBoolean(REMEMBER, true));
    }

    public void saveStandards() {
        SharedPreferences sharedP = getSharedPreferences("exp", MODE_PRIVATE);
        SharedPreferences.Editor editP = sharedP.edit();

        if (mCheckBoxRemember.isChecked()) {
            editP.putString(RADIO, "" + mRadioGroupInputType.getCheckedRadioButtonId());
            editP.putBoolean(SAVE, mCheckBoxSave.isChecked());
            editP.putBoolean(REMEMBER, mCheckBoxRemember.isChecked());
            editP.putString("game", mEditTextGame.getText().toString());
            editP.putString("location", mEditTextLocation.getText().toString());
            editP.commit();
        } else {
            editP.putString(RADIO, "" + mRadioGroupInputType.getCheckedRadioButtonId());
            editP.putBoolean(SAVE, mCheckBoxSave.isChecked());
            editP.putBoolean(REMEMBER, mCheckBoxRemember.isChecked());
            editP.putString("game", "");
            editP.putString("location", "");
            editP.commit();
        }
    }

    public boolean checkNumbers() {
        double parsed;
        try {
            parsed = Double.parseDouble(mEditTextPercent.getText().toString());
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
            case id.bGrind:
                Log.d("Testing numbers", "Test");
                if (checkNumbers()) {
                    saveStandards();

                    Bundle bundle = new Bundle();
                    bundle.putDouble(PER,
                            Double.parseDouble(mEditTextPercent.getText().toString()));
                    bundle.putString("location", mEditTextLocation.getText().toString());
                    bundle.putString("game", mEditTextGame.getText().toString());
                    bundle.putBoolean("save", mCheckBoxSave.isChecked());
                    Log.d("Test", type);
                    bundle.putString("type", type);

                    Intent i = new Intent(EfficiencyMainActivity.this, EfficiencyCountdown.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                } else {
                    mEditTextPercent.setTextColor(Color.RED);
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
                mTextViewPercent.setText("Current percent:");
                type = PER;
                break;
            case R.id.rbExperience:
                mTextViewPercent.setText("Current experience:");
                type = EXP;
                break;
        }

    }

}
