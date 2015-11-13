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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    //Various Globals
    private String LogName;

    //Adapter global stuff
    private ArrayList<String> Log = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    //Chat Reader Global
    private ChatLogReader readChat;

    //Preference Globals
    private String ColorScheme;
    private String Font;
    private Boolean TimeStamp;
    private int textColor;
    private Typeface FontStyle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        //Setup up the ListView
        lstDisplay = (ListView)findViewById(R.id.lstLogDisp);
        Back = (RelativeLayout)findViewById(R.id.Layout);

        //Gets the Name of the Log
        Intent intent = getIntent();
        LogName = intent.getStringExtra(LogsList.LOG_NAME);

        //Get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        TimeStamp = SharedPrefs.getBoolean("TimeStampEnabled", false);

        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);

        //Setup the Chat Log Reader
        readChat = new ChatLogReader(LogName);

        LinkedList<Message> MsgList = readChat.getNewerMessages();

        int count = 0;
        while(count < MsgList.size()){
            Message Msg = MsgList.get(count);

            String timeStamp = "";
            if(TimeStamp) {
                timeStamp = String.format("%tr", Msg.getTime());
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

    }
}
