package wifinderinc.wifinder;

import android.os.Bundle;
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
