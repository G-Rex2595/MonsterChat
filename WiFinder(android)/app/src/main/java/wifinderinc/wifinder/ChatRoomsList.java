package wifinderinc.wifinder;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Cole Baughn on 11/10/2015.
 */
public class ChatRoomsList extends AppCompatActivity {
    public final static String ROOM_NAME = ".ROOM";
    public final static String USER_NAME = ".USER";
    public final static String MANAGER_NAME = ".MANAGER";

    //global variable for objects
    private ChatRoomManager manager;
    private Button btnCreate;
    private ListView lstRooms;
    private TextView lblTitle;
    private RelativeLayout Back;
    private TextView CreateBox;
    private Button btnRef;
    private TextView RefBox;

    //set up adapter
    private LinkedList<String> RoomList = new LinkedList<>();
    private ArrayList<String> RoomNames = new ArrayList<>();
    private ArrayAdapter<String> RoomAdpt;

    //other globals
    private String NewRoomName = "";
    //private String NewRoomPass = "";
    private String user;
    private boolean isFocus = true;

    //Preference Globals
    private String ColorScheme;
    private String Font;
    private int textColor;
    private Typeface FontStyle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms_list);

        //Setup UI Globals
        btnCreate = (Button)findViewById(R.id.btnCreateRoom);
        lstRooms = (ListView) findViewById(R.id.lstChatRooms);
        lblTitle = (TextView) findViewById(R.id.txtChatRoomsList);
        CreateBox = (TextView) findViewById(R.id.CreateBack);
        btnRef = (Button) findViewById(R.id.btnRefresh);
        RefBox = (TextView) findViewById(R.id.RefreshBack);
        Back = (RelativeLayout) findViewById(R.id.Layout);

        Intent intent = getIntent();
        user = intent.getStringExtra(HomePage.USER_NAME);

        manager = new ChatRoomManager("" + System.currentTimeMillis(), this);
        RoomList = manager.getAvailableRooms();

        RoomNames.add("Global");

        int count = 0;
        while(count < RoomList.size()){
            RoomNames.add(RoomList.get(count));
            count++;
        }

        //Get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");

        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);



        //set item listener
        lstRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when item is selected open chat rooms view and send the room name
                Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                TextView temp = (TextView) view;
                intent.putExtra(ROOM_NAME, temp.getText().toString());
                intent.putExtra(USER_NAME, ChatRoomsList.this.user);
                startActivity(intent);
            }


        });

        //part of creating the list
        RoomAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , RoomNames){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(textColor);
                text.setTypeface(FontStyle);


                return view;
            }
        };
        lstRooms.setAdapter(RoomAdpt);
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

        //Set Backgrounds
        ChatRoomsList.this.Back.setBackgroundColor(backColor);
        lblTitle.setBackgroundColor(backColor);
        lstRooms.setBackgroundColor(backColor);

        //Set Button Background
        btnCreate.setBackgroundColor(btnColor);
        btnRef.setBackgroundColor(btnColor);

        //Set Highlights
        CreateBox.setBackgroundColor(textColor);
        RefBox.setBackgroundColor(textColor);

        //Set text color
        lblTitle.setTextColor(textColor);
        btnCreate.setTextColor(textColor);
        btnRef.setTextColor(textColor);

        //Set divider color
        ColorDrawable divColor = new ColorDrawable(textColor);
        lstRooms.setDivider(divColor);
        lstRooms.setDividerHeight(3);

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

        btnCreate.setTypeface(FontStyle);
        lblTitle.setTypeface(FontStyle);
    }

    public void btnRef_Click(View v){
        manager.close();
        manager = new ChatRoomManager("" + System.currentTimeMillis(), this);
        RoomList = manager.getAvailableRooms();

        int count = 0;
        while(count < RoomList.size()){
            RoomNames.add(RoomList.get(count));
            count++;
        }
        RoomAdpt.notifyDataSetChanged();
    }

    public void btnCreateRoom_Click(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText NewName = new EditText(this);
        //final EditText NewPass = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        NewName.setInputType(InputType.TYPE_CLASS_TEXT);
        //NewPass.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(NewName);
        //builder.setView(NewPass);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewRoomName = NewName.getText().toString();
                //NewRoomPass = NewPass.getText().toString();

                if(NewRoomName.compareTo("") == 0){
                    return;
                }else {
                    Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                    intent.putExtra(ROOM_NAME, NewRoomName);
                    intent.putExtra(USER_NAME, ChatRoomsList.this.user);
                    startActivity(intent);
                }
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
}
