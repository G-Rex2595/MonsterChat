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
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cole Baughn on 11/10/2015.
 */
public class ChatRoomsList extends AppCompatActivity {
    public final static String ROOM_NAME = ".ROOM";
    public final static String USER_NAME = ".USER";
    public final static String MANAGER_NAME = ".MANAGER";

    //global variable for objects
    public static ChatRoomManager manager = null;
    private Button btnCreate;
    private ListView lstRooms;
    private RelativeLayout Back;
    private TextView CreateBox;
    private Button btnHome;
    private Button btnTitle;
    private Button btnSettings;
    private TextView MenuBox;

    //set up adapter
    private LinkedList<String> RoomList = new LinkedList<>();
    private ArrayList<String> RoomNames = new ArrayList<>();
    private ArrayAdapter<String> RoomAdpt;

    //other globals
    private String NewRoomName = "";
    private String NewRoomPass = "";
    private String InputPass = "";
    private String user;
    private boolean isFocus = true;

    //Preference Globals
    private String ColorScheme;
    private String Font;
    private int textColor;
    private Typeface FontStyle;

    Timer timer = new Timer();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms_list);

        //Setup UI Globals
        btnCreate = (Button)findViewById(R.id.btnCreateRoom);
        lstRooms = (ListView) findViewById(R.id.lstChatRooms);
        CreateBox = (TextView) findViewById(R.id.CreateBack);
        Back = (RelativeLayout) findViewById(R.id.Layout);
        btnHome = (Button)findViewById(R.id.btnHome);
        btnTitle = (Button)findViewById(R.id.btnTitle);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        MenuBox = (TextView)findViewById(R.id.MenuBack);

        Intent intent = getIntent();
        user = intent.getStringExtra(HomePage.USER_NAME);

        if (manager == null)
            manager = new ChatRoomManager("" + System.currentTimeMillis(), this);
        RoomList = manager.getUnformattedRooms();

        RoomNames.add("Global");

        for(String roomInfo : RoomList){
            if(roomInfo.compareTo("Global") == 0){
                continue;
            }

            if(roomInfo.contains("-")){
                RoomNames.add(roomInfo.substring(0, roomInfo.indexOf('-')) + " \uD83D\uDD12" );
            }
            else{
                RoomNames.add(roomInfo);
            }

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
                String textStr = ((TextView) view).getText().toString();
                InputPass = "";
                if (textStr.contains("\uD83D\uDD12")) {
                    getInputPass(textStr.split(" ")[0]);

                }else {

                    manager.joinRoom(textStr, InputPass);   //TODO:  change null to the password the user entered
                    if (manager.getCurrentChatRoom() == null)   //could not join room
                    {
                        return;
                    }

                    Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                    TextView temp = (TextView) view;
                    intent.putExtra(ROOM_NAME, temp.getText().toString());
                    intent.putExtra(USER_NAME, ChatRoomsList.this.user);
                    startActivity(intent);
                }
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


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RoomList = manager.getUnformattedRooms();

                        Log.d("RoomsCount", "" + RoomList.size());
                        for (String s : RoomList)
                            Log.d("Rooms", s);

                        RoomNames.clear();

                        RoomNames.add("Global");

                        for (String roomInfo : RoomList) {
                            if (roomInfo.compareTo("Global") == 0) {
                                continue;
                            }
                            if (roomInfo.contains("-")) {
                                RoomNames.add(roomInfo.substring(0, roomInfo.indexOf('-')) + " \uD83D\uDD12");
                            } else {
                                RoomNames.add(roomInfo);
                            }
                        }
                        RoomAdpt.notifyDataSetChanged();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void getInputPass(final String RoomName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set up the input
        final EditText inPass = new EditText(this);
        inPass.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Password Required");
        builder.setView(inPass);

        String checkPass = "";

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tempPass = inPass.getText().toString();
                //NewRoomPass = NewPass.getText().toString();

                if (!tempPass.matches("[a-zA-Z0-9]+")) {
                    invalidPassword();
                    return;
                } else {
                    manager.joinRoom(RoomName, tempPass);
                    if (manager.getCurrentChatRoom() == null)   //could not join room
                    {
                        return;
                    }

                    Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                    intent.putExtra(ROOM_NAME, RoomName);
                    intent.putExtra(USER_NAME, ChatRoomsList.this.user);
                    startActivity(intent);
                }
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
        timer.cancel();
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
        lstRooms.setBackgroundColor(backColor);
        btnTitle.setBackgroundColor(backColor);

        //Set Button Background
        btnCreate.setBackgroundColor(btnColor);
        btnHome.setBackgroundColor(btnColor);
        btnSettings.setBackgroundColor(btnColor);

        //Set Highlights
        CreateBox.setBackgroundColor(textColor);
        MenuBox.setBackgroundColor(textColor);

        //Set text color
        btnCreate.setTextColor(textColor);
        btnHome.setTextColor(textColor);
        btnSettings.setTextColor(textColor);
        btnTitle.setTextColor(textColor);

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
        btnHome.setTypeface(FontStyle);
        btnSettings.setTypeface(FontStyle);
        btnTitle.setTypeface(FontStyle);
    }

    public void btnCreateRoom_Click(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter chat room name");

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

                if (!NewRoomName.matches("[a-zA-Z0-9]+")) {
                    incorrectChatRoomName();
                    return;
                } else {
                    promptPassword();
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

    public void promptPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter chat room name");

        // Set up the input
        final EditText NewPass = new EditText(this);
        NewPass.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Passowrd (Optional)");
        builder.setView(NewPass);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewRoomPass = NewPass.getText().toString();

                if (!NewRoomPass.matches("[a-zA-Z0-9]+")) {
                    invalidPassword();
                    return;
                } else {
                    manager.joinRoom(NewRoomName, NewRoomPass);   //TODO:  change null to the password the user entered
                    if (manager.getCurrentChatRoom() == null)   //could not join room
                    {
                        return;
                    }

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
                manager.joinRoom(NewRoomName, "");   //TODO:  change null to the password the user entered
                if (manager.getCurrentChatRoom() == null)   //could not join room
                {
                    return;
                }

                Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                intent.putExtra(ROOM_NAME, NewRoomName);
                intent.putExtra(USER_NAME, ChatRoomsList.this.user);
                startActivity(intent);
            }
        });

        builder.show();
    }

    public void incorrectChatRoomName()
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("The Chat Room Name may only contain letters and numbers.");
        dlgAlert.setTitle("Incorrect Chat Room Name");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void invalidPassword()
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("The Password may only contain letters and numbers.");
        dlgAlert.setTitle("Invalid Password");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void btnSettings_Click(View v){
        Intent intent = new Intent(this, Preferences.class);
        intent.putExtra(Preferences.EXTRA_SHOW_FRAGMENT, Preferences.Prefs.class.getName());
        intent.putExtra(Preferences.EXTRA_NO_HEADERS, true);
        this.startActivity(intent);
    }

    public void btnHome_Click(View v){
        manager.close();
        ChatRoomsList.manager = null;
        super.onBackPressed();
    }
}
