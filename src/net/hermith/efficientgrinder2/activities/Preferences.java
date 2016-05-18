package net.hermith.efficientgrinder2.activities;

import net.hermith.efficientgrinder2.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Preferences extends SherlockPreferenceActivity {

    public static String MODE_ONE_GAME = "onegame";
    public static String MODE_ONE_GAME_NAME = "onegamename";
    public static String MODE_ALWAYS_SAVE_EFFI = "alwayssaveeffi";
    public static String MODE_ALWAYS_REMEMBER_EFFI = "alwaysremembereffi";
    public static String MODE_ALWAYS_PERCENT_EFFI = "alwayspercenteffi";
    public static String MODE_ALWAYS_SAVE_ECON = "alwayssaveecon";
    public static String MODE_ALWAYS_REMEMBER_ECON = "alwaysrememberecon";
    public static String MODE_NEVER_ITEMS_ECON = "neveritemsecon";
    public static String TO_THE_PLAYSTORE = "totheplaystore";


    private CheckBoxPreference mOneGameCheck;
    private EditTextPreference mOneGameName;
    private Preference mToThePlaystore;
//	
//	private boolean one_game;
//	private String one_game_name;
//	private boolean always_save_effi;
//	private boolean always_remember_effi;
//	private boolean always_percent_effi;
//	private boolean always_save_econ;
//	private boolean always_remember_econ;
//	private boolean never_items_econ;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        mOneGameCheck = (CheckBoxPreference) findPreference(MODE_ONE_GAME);
        mOneGameName = (EditTextPreference) findPreference(MODE_ONE_GAME_NAME);
        mOneGameName.setSummary(mOneGameName.getText());
        setGamenameBox();
        mOneGameCheck.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mOneGameCheck.isChecked()) {
                    mOneGameName.setEnabled(true);
                } else {
                    mOneGameName.setEnabled(false);
                }
                return true;
            }
        });

        mOneGameName.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mOneGameName.setText(newValue.toString());
                mOneGameName.setSummary(newValue.toString());
                return true;
            }
        });

        mToThePlaystore = (Preference) findPreference(TO_THE_PLAYSTORE);
        mToThePlaystore.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                String url = "https://play.google.com/store/apps/details?id=net.hermith.efficientgrinder";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            }
        });

    }

    public void setGamenameBox() {
        if (mOneGameCheck.isChecked()) {
            mOneGameName.setEnabled(true);
        } else {
            mOneGameName.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getSupportMenuInflater();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.preferences);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_launcher);

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
//	
//	private void forOtherClasses() {
//		p = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
//		e = p.edit();
//		
//		one_game = p.getBoolean(MODE_ONE_GAME, false);
//		one_game_name = p.getString(MODE_ONE_GAME_NAME, "");
//		always_save_effi = p.getBoolean(MODE_ALWAYS_SAVE_EFFI, false);
//		always_remember_effi = p.getBoolean(MODE_ALWAYS_REMEMBER_EFFI, false);
//		always_percent_effi = p.getBoolean(MODE_ALWAYS_PERCENT_EFFI, false);
//		always_save_econ = p.getBoolean(MODE_ALWAYS_SAVE_ECON, false);
//		always_remember_econ = p.getBoolean(MODE_ALWAYS_REMEMBER_ECON, false);
//		never_items_econ = p.getBoolean(MODE_NEVER_ITEMS_ECON, false);
//	}

}