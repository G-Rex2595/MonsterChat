package wifinderinc.wifinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cole Baughn on 11/11/2015.
 */
public class LogsList extends AppCompatActivity {
    //Globals for next activity
    public final static String LOG_NAME = ".LOG";
    public final static String LOG_POS = "LOGPOS";

    //UI Globals
    private ListView lstLogs;
    private LogManager logMan;
    private RelativeLayout Back;
    private Button btnHome;
    private Button btnTitle;
    private Button btnSettings;
    private TextView MenuBox;

    //Globals for adapter
    private ArrayList<LoggedChat> LogList;
    private ArrayList<String> LogNames;
    private ArrayAdapter<String> adapter;

    //Preference Globals
    private String ColorScheme;
    private Boolean TimeFormat;
    private String Font;
    private int textColor;
    private Typeface FontStyle;

    private boolean isFocus = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_list);

        //setup UI globals
        lstLogs = (ListView) findViewById(R.id.lstLogList);
        Back = (RelativeLayout) findViewById(R.id.Layout);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnTitle = (Button) findViewById(R.id.btnTitle);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        MenuBox = (TextView) findViewById(R.id.MenuBack);

        //Get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        TimeFormat = SharedPrefs.getBoolean("24hrEnabled", false);

        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);


        logMan = new LogManager(this);

        //Array of Logs
        LogList = logMan.getLogs();

        LogNames = new ArrayList<>();
        int count = 0;
      //  LogNames.add("Test" + LogList.size());
        while(count < LogList.size()){
            LoggedChat logInfo = LogList.get(count);
            SimpleDateFormat formatT = new SimpleDateFormat("MM/dd hh:mm a");
            if(TimeFormat){
                formatT = new SimpleDateFormat("MM/dd HH:mm");
            }

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(logInfo.getDate());
            String logDate = formatT.format(c.getTime());
            String currName = logInfo.getRoomName().replace("#", " ");
            LogNames.add(currName + "     " + logDate);
            count++;
        }


        //set up click listener
        lstLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open Log and send log name
                Intent intent = new Intent(LogsList.this, LogView.class);
                TextView temp = (TextView) view;
                LoggedChat log = LogList.get(position);
                intent.putExtra(LOG_NAME, log.getFileName());
                intent.putExtra(LOG_POS, position);
                startActivity(intent);
            }


        });

        lstLogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                promptDeleteLog(position);

                return true;
            }
        });

        //load array into list
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,LogNames){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(textColor);
                text.setTypeface(FontStyle);


                return view;
            }
        };

        lstLogs.setAdapter(adapter);

    }

    private void promptDeleteLog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Log?");

        final int pos = position;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoggedChat log = LogList.get(pos);
                log.delete();
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void SetColors(String ColorScheme){
        int backColor = Color.WHITE;
        int btnColor = Color.LTGRAY;
        textColor = Color.BLACK;
        switch (ColorScheme){
            case "Default":
                break;
            case "Nuclear":
                backColor = Color.BLACK;
                btnColor = Color.BLACK;
                textColor = Color.argb(255, 29, 255, 31);
                break;
            case "DOS":
                backColor = Color.BLACK;
                btnColor = Color.BLACK;
                textColor = Color.WHITE;
                break;
            case "1969":
                backColor = Color.CYAN;
                btnColor = Color.argb(255,245,159,159);
                textColor = Color.YELLOW;
                break;
        }

        //set background colors
        LogsList.this.Back.setBackgroundColor(backColor);
        lstLogs.setBackgroundColor(backColor);
        btnTitle.setBackgroundColor(backColor);

        //Set button background colors
        btnHome.setBackgroundColor(btnColor);
        btnSettings.setBackgroundColor(btnColor);
        btnTitle.setBackgroundColor(btnColor);

        //Set Highlights
        MenuBox.setBackgroundColor(textColor);

        //set text colors
        btnHome.setTextColor(textColor);
        btnSettings.setTextColor(textColor);
        btnTitle.setTextColor(textColor);

        //set divider color
        ColorDrawable divColor = new ColorDrawable(textColor);
        lstLogs.setDivider(divColor);
        lstLogs.setDividerHeight(3);
    }

    private void SetFont(String ColorScheme){
        FontStyle = Typeface.DEFAULT;

        switch (ColorScheme){
            case "Nuclear":
            case "DOS":
                FontStyle = Typeface.MONOSPACE;
                break;
            case "1969":
                FontStyle = Typeface.SANS_SERIF;
                break;
        }

        btnHome.setTypeface(FontStyle);
        btnSettings.setTypeface(FontStyle);
        btnTitle.setTypeface(FontStyle);
    }

    public void btnSettings_Click(View v){
        Intent intent = new Intent(this, Preferences.class);
        intent.putExtra(Preferences.EXTRA_SHOW_FRAGMENT, Preferences.Prefs.class.getName());
        intent.putExtra(Preferences.EXTRA_NO_HEADERS, true);
        this.startActivity(intent);
    }

    public void btnHome_Click(View v){
        super.onBackPressed();
    }

    public void btnDeleteAllLogs_Click(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Logs?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(LoggedChat log : LogList){
                    log.delete();
                }
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void onResume() {
        super.onResume();
        if(!isFocus){
            finish();
            startActivity(getIntent());
        }

        isFocus = true;
    }

    protected void onPause() {
        super.onPause();
        isFocus = false;
    }
}
