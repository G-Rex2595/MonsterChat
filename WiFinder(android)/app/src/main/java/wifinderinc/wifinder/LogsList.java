package wifinderinc.wifinder;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cole Baughn on 11/11/2015.
 */
public class LogsList extends AppCompatActivity {
    //Globals for next activity
    public final static String LOG_NAME = ".LOG";

    //UI Globals
    private ListView lstLogs;
    private TextView lblTitle;
    private LogManager logMan;
    private RelativeLayout Back;

    //Globals for adapter
    private ArrayList<LoggedChat> LogList;
    private ArrayList<String> LogNames;
    private ArrayAdapter<String> adapter;

    //Preference Globals
    private String ColorScheme;
    private String Font;
    private int textColor;
    private Typeface FontStyle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_list);

        //setup UI globals
        lstLogs = (ListView) findViewById(R.id.lstLogList);
        lblTitle = (TextView) findViewById(R.id.UserBack);
        Back = (RelativeLayout) findViewById(R.id.Layout);


        logMan = new LogManager(this);

        //Array of Logs
        LogList = logMan.getLogs();

        LogNames = new ArrayList<>();
        int count = 0;
      //  LogNames.add("Test" + LogList.size());
        while(count < LogList.size()){
            LoggedChat logInfo = LogList.get(count);
            LogNames.add(logInfo.getRoomName() + " " + logInfo.getDate());
            count++;
        }

        //Get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");

        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);

        //set up click listener
        lstLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open Log and send log name
                Intent intent = new Intent(LogsList.this, LogView.class);
                TextView temp = (TextView) view;
                LoggedChat log = LogList.get(position);
                intent.putExtra(LOG_NAME, log.getFileName());
                startActivity(intent);
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

    private void SetColors(String ColorScheme){
        int backColor = Color.WHITE;
        textColor = Color.BLACK;
        switch (ColorScheme){
            case "Default":
                break;
            case "Nuclear":
                backColor = Color.BLACK;
                textColor = Color.argb(255, 29, 255, 31);
                break;
            case "DOS":
                backColor = Color.BLACK;
                textColor = Color.WHITE;
                break;
            case "1969":
                backColor = Color.CYAN;
                textColor = Color.YELLOW;
                break;
        }

        //set background colors
        LogsList.this.Back.setBackgroundColor(backColor);
        lblTitle.setBackgroundColor(backColor);
        lstLogs.setBackgroundColor(backColor);

        //set text colors
        lblTitle.setTextColor(textColor);

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

        lblTitle.setTypeface(FontStyle);
    }
}
