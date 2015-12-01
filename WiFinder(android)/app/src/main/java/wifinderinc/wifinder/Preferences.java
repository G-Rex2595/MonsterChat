package wifinderinc.wifinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;

import java.util.List;

/**
 * Created by Cole Baughn on 11/12/2015.
 */
public class Preferences extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onBuildHeaders(List<Header> target) {
        try {
            loadHeadersFromResource(R.xml.header, target);
        }
        catch (Exception e) {
            ErrorLog.writeToLog(e);
        }
    }

    public static class Prefs extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            try {
                addPreferencesFromResource(R.xml.preferences);
                Preference UnblockUsers = (Preference) findPreference("UnblockUsers");

                /*UnblockUsers.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent( , UnblockUsers.class);
                        startActivity(intent);
                        return false;
                    }
                });*/
            } catch (Exception e) {
                ErrorLog.writeToLog(e);
            }
        }
    }

    protected boolean isValidFragment (String fragmentName)
    {
        return Prefs.class.getName().equals(fragmentName);
    }
}
