package wifinderinc.wifinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatRoomView extends AppCompatActivity{
    //UI Globals
    private EditText txtbxInput;
    private Button btnSend;
    private ListView lstDisplay;
    private TextView InputBox;
    private TextView SendBox;
    private RelativeLayout Back;

    //Various Globals
    private ChatRoomManager manager;
    private String RoomName;
    private String UserName;

    //Adapter Globals
    private ArrayList<String> Chat = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    //Preference globals
    private String ColorScheme;
    private String Font;
    private Boolean TimeStamps;
    private int textColor;
    private Typeface FontStyle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_view);

        //get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        TimeStamps = SharedPrefs.getBoolean("TimeStampEnabled", false);

        //assign globals to the proper controls
        txtbxInput = (EditText)findViewById(R.id.txtMessageInput);
        btnSend = (Button)findViewById(R.id.btnSendMessage);
        lstDisplay = (ListView)findViewById(R.id.lstChatDisp);
        InputBox = (TextView) findViewById(R.id.InputBack);
        SendBox = (TextView)findViewById(R.id.SendBack);
        Back = (RelativeLayout)findViewById(R.id.Background);

        SetColors(ColorScheme);
        SetFont(Font);

        //get values that are passed down
        Intent intent = getIntent();
        RoomName = intent.getStringExtra(ChatRoomsList.ROOM_NAME);
        UserName = intent.getStringExtra(ChatRoomsList.USER_NAME);

        Chat.add("Welcome to " + RoomName + ", " + UserName + "!");

        //set up adapter
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Chat){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(textColor);
                text.setTypeface(FontStyle);


                return view;
            }
        };
        lstDisplay.setAdapter(adapter);

        //manager = new ChatRoomManager(UserName, this);
        manager = new ChatRoomManager("" + System.currentTimeMillis(), this);
        manager.joinRoom(RoomName);
        manager.setUsername(UserName);
        manager.getCurrentChatRoom().setChatRoomView(this);


        //make it so text color changes to black when selected
        txtbxInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean bool) {
                if(bool) {
                    txtbxInput.setText("");
                    txtbxInput.setTextColor(textColor);
                }
            }
        });
    }

    private void SetColors(String ColorScheme){
        int backColor = Color.WHITE;
        int btnColor = Color.LTGRAY;
        int txtBackColor = Color.WHITE;
        textColor = Color.BLACK;
        switch (ColorScheme){
            case "Default":
                break;
            case "Nuclear":
                backColor = Color.BLACK;
                btnColor = Color.BLACK;
                txtBackColor = Color.argb(255, 17, 100, 5);
                textColor = Color.argb(255, 29, 255, 31);
                break;
            case "DOS":
                backColor = Color.BLACK;
                btnColor = Color.BLACK;
                txtBackColor = Color.BLACK;
                textColor = Color.WHITE;
                break;
            case "1969":
                backColor = Color.CYAN;
                btnColor = Color.argb(255,245,159,159);
                txtBackColor = Color.RED;
                textColor = Color.YELLOW;
                break;
        }

        //set background colors
        ChatRoomView.this.Back.setBackgroundColor(backColor);
        lstDisplay.setBackgroundColor(backColor);
        txtbxInput.setBackgroundColor(txtBackColor);

        //set button background
        btnSend.setBackgroundColor(btnColor);

        //set highlights
        InputBox.setBackgroundColor(textColor);
        SendBox.setBackgroundColor(textColor);

        //set text colors
        txtbxInput.setTextColor(textColor);
        btnSend.setTextColor(textColor);

        //set divider colors
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

        txtbxInput.setTypeface(FontStyle);
        btnSend.setTypeface(FontStyle);
    }

    public void addMessage(Message m) {
        Calendar c = Calendar.getInstance();

        String timeStamp = "";
        if(TimeStamps) {
             timeStamp = String.format("%tr", c);
        }
        final String message = String.format("%s:         %s\n%s\n", m.getName(), timeStamp, m.getMessage());
        runOnUiThread(new Runnable() {
            public void run() {
                Chat.add(message);
                adapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        //manager.onResume();
        //TODO: call ChatRoomManager onresume
    }

    @Override
    public void onPause() {
        super.onPause();
        //manager.onPause();
        //TODO: call ChatRoomManager onpause
    }

    public void btnSendMessage_Click(View v){
        String Default = "Message Here";

        if(txtbxInput.getCurrentTextColor() == Color.GRAY){
            return;
        }

        String message = txtbxInput.getText().toString();

        txtbxInput.setText(Default.subSequence(0, Default.length()));
        txtbxInput.setTextColor(Color.GRAY);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtbxInput.getWindowToken(), 0);

        manager.getCurrentChatRoom().sendMessage(message);

        txtbxInput.clearFocus();
    }

}
