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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Cole Baughn on 11/11/2015.
 */
public class LogView extends AppCompatActivity {

    //UI Globals
    private ListView lstDisplay;
    private RelativeLayout Back;
    private Button btnHome;
    private Button btnDeleteLog;
    private Button btnSettings;
    private TextView MenuBox;

    //Various Globals
    private String LogName;
    private int LogPos;

    //Adapter global stuff
    private ArrayList<String> Log = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    //Chat Reader Global
    private ChatLogReader readChat;

    //Preference Globals
    private String ColorScheme;
    private String Font;
    private Boolean TimeFormat;
    private Boolean TimeStamp;
    private int textColor;
    private Typeface FontStyle;

    private boolean isFocus = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        //Setup up the ListView
        lstDisplay = (ListView)findViewById(R.id.lstLogDisp);
        Back = (RelativeLayout)findViewById(R.id.Layout);
        btnHome = (Button)findViewById(R.id.btnHome);
        btnDeleteLog = (Button)findViewById(R.id.btnDeleteLog);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        MenuBox = (TextView)findViewById(R.id.MenuBack);

        //Gets the Name of the Log
        Intent intent = getIntent();
        LogName = intent.getStringExtra(LogsList.LOG_NAME);
        LogPos = intent.getIntExtra(LogsList.LOG_POS, 0);

        //Get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        TimeStamp = SharedPrefs.getBoolean("TimeStampEnabled", false);
        TimeFormat = SharedPrefs.getBoolean("24hrEnabled", false);

        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,Log){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(textColor);
                text.setTypeface(FontStyle);


                return view;
            }
        };

        lstDisplay.setAdapter(adapter);

        //Setup the Chat Log Reader
        readChat = new ChatLogReader(LogName);

        LinkedList<Message> MsgList = readChat.getNewerMessages();

        int count = 0;
        while(count < MsgList.size()){
            Message Msg = MsgList.get(count);

            String timeStamp = "";
            if(TimeStamp) {

                SimpleDateFormat formatT = new SimpleDateFormat("hh:mm a");
                if(TimeFormat){
                    formatT = new SimpleDateFormat("HH:mm");
                }

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(Msg.getTime());
                timeStamp = formatT.format(c.getTime());
            }
            final String message = String.format("%s:         %s\n%s\n", Msg.getName(), timeStamp, Msg.getMessage());
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.add(message);
                    adapter.notifyDataSetChanged();
                }
            });
            count++;
        }



        //Setup the adapter
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Log){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(textColor);
                text.setTypeface(FontStyle);


                return view;
            }
        };
        lstDisplay.setAdapter(adapter);

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
        LogView.this.Back.setBackgroundColor(backColor);
        lstDisplay.setBackgroundColor(backColor);

        //Set button background colors
        btnHome.setBackgroundColor(btnColor);
        btnSettings.setBackgroundColor(btnColor);
        btnDeleteLog.setBackgroundColor(btnColor);

        //set highlight color
        MenuBox.setBackgroundColor(textColor);

        //set text colors
        btnHome.setTextColor(textColor);
        btnSettings.setTextColor(textColor);
        btnDeleteLog.setTextColor(textColor);

        //set divider color
        ColorDrawable divColor = new ColorDrawable(textColor);
        lstDisplay.setDivider(divColor);
        lstDisplay.setDividerHeight(3);
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
        btnDeleteLog.setTypeface(FontStyle);

    }

    public void btnSettings_Click(View v){
        Intent intent = new Intent(this, Preferences.class);
        intent.putExtra(Preferences.EXTRA_SHOW_FRAGMENT, Preferences.Prefs.class.getName());
        intent.putExtra(Preferences.EXTRA_NO_HEADERS, true);
        this.startActivity(intent);
    }

    public void btnHome_Click(View v){
        Intent intent = new Intent(this, HomePage.class);
        this.startActivity(intent);
    }

    public void BtnDeleteLog_Click(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Log?");

        final LogManager logMan = new LogManager(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<LoggedChat> LogList = logMan.getLogs();
                LogList.get(LogPos).delete();
                finish();
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
