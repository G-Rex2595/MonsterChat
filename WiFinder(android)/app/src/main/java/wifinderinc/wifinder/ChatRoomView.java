package wifinderinc.wifinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private Button btnHome;
    private Button btnInsertImg;
    private Button btnSettings;
    private TextView MenuBox;

    //Various Globals
    private ChatRoomManager manager;
    private String RoomName;
    private String UserName;

    //Adapter Globals
    private ArrayList<String> Head = new ArrayList<>();
    private ArrayList<String> Message = new ArrayList<>();
    private ArrayList<BitmapDrawable> Images = new ArrayList<>();
    private ArrayList<String> UserIds = new ArrayList<>();
    private ChatList adapter;

    //Preference globals
    private String ColorScheme;
    private String Font;
    private Boolean TimeFormat;
    private Boolean TimeStamps;
    private int textColor;
    private Typeface FontStyle;

    public static final int GET_FROM_GALLERY = 1;
    private Bitmap insertImg = null;

    private Boolean isFocus = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_view);

        //get Preferences
        SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        TimeStamps = SharedPrefs.getBoolean("TimeStampEnabled", false);
        TimeFormat = SharedPrefs.getBoolean("24hrEnabled", false);

        //assign globals to the proper controls
        txtbxInput = (EditText)findViewById(R.id.txtMessageInput);
        btnSend = (Button)findViewById(R.id.btnSendMessage);
        lstDisplay = (ListView)findViewById(R.id.lstChatDisp);
        InputBox = (TextView) findViewById(R.id.InputBack);
        SendBox = (TextView)findViewById(R.id.SendBack);
        Back = (RelativeLayout)findViewById(R.id.Background);
        btnHome = (Button)findViewById(R.id.btnHome);
        btnInsertImg = (Button)findViewById(R.id.btnInsertImg);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        MenuBox = (TextView)findViewById(R.id.MenuBack);

        SetColors(ColorScheme);
        SetFont(Font);

        //get values that are passed down
        Intent intent = getIntent();
        RoomName = intent.getStringExtra(ChatRoomsList.ROOM_NAME);
        UserName = intent.getStringExtra(ChatRoomsList.USER_NAME);
        String welcomeName = RoomName;
        if(welcomeName.contains("#")){
            welcomeName = welcomeName.replace("#", "");
        }
        Head.add("Welcome to " + welcomeName + ", " + UserName + "!");
        Message.add("");
        Images.add(null);
        UserIds.add("default");

        lstDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when item is selected open chat rooms view and send the room name
                ViewGroup vG = (ViewGroup) view;
                TextView headView = (TextView)vG.getChildAt(0);
                String textStr = headView.getText().toString();
                String Name = textStr.split(":")[0];

                if(Name.compareTo(UserName) != 0 && !Name.contains("e t")) {
                    promptBlock(Name, UserIds.get(position));
                }
            }


        });

        //set up adapter
        adapter=new ChatList(this, Head, Message, Images, textColor, FontStyle );

        lstDisplay.setAdapter(adapter);

        //manager = new ChatRoomManager(UserName, this);
        manager = ChatRoomsList.manager;
        manager.setUsername(UserName);
        manager.getCurrentChatRoom().setChatRoomView(this);


        //make it so text color changes to black when selected
        txtbxInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean bool) {
                if (bool) {
                    txtbxInput.setText("");
                    txtbxInput.setTextColor(textColor);
                }
            }
        });
    }

    private void promptBlock(String Name, String UserId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Block " + Name + "?");


        // Set up the buttons
        final String uName = Name;
        final String uId = UserId;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar c = Calendar.getInstance();
                BlockedUser BlockThis = new BlockedUser(uName, uId, c.getTimeInMillis());
                Blocker.block(BlockThis);
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

    private void scrollMyListViewToBottom() {
        lstDisplay.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lstDisplay.setSelection(adapter.getCount() - 1);
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
        btnHome.setBackgroundColor(btnColor);
        btnSettings.setBackgroundColor(btnColor);
        btnInsertImg.setBackgroundColor(btnColor);

        //set highlights
        InputBox.setBackgroundColor(textColor);
        SendBox.setBackgroundColor(textColor);
        MenuBox.setBackgroundColor(textColor);

        //set text colors
        txtbxInput.setTextColor(textColor);
        btnSend.setTextColor(textColor);
        btnHome.setTextColor(textColor);
        btnSettings.setTextColor(textColor);
        btnInsertImg.setTextColor(textColor);

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
        btnHome.setTypeface(FontStyle);
        btnSettings.setTypeface(FontStyle);
        btnInsertImg.setTypeface(FontStyle);
    }

    public void addMessage(Message m) {
        Calendar c = Calendar.getInstance();

        String timeStamp = "";
        if (TimeStamps) {
            SimpleDateFormat formatT = new SimpleDateFormat("hh:mm a");
            if(TimeFormat){
                formatT = new SimpleDateFormat("HH:mm");
            }
            timeStamp = formatT.format(c.getTime());
        }

        final String stamp = timeStamp;
        final Message msg = m;
        runOnUiThread(new Runnable() {
            public void run() {
                Head.add(String.format("%s:         %s", msg.getName(), stamp));
                Message.add(msg.getMessage());

                BitmapDrawable bd = null;
                Bitmap b = msg.getPicture();
                if (b != null) {
                    bd = new BitmapDrawable(getResources(), b);
                }

                Images.add(bd);
                UserIds.add(msg.getID());

                if(Head.size() > 500){
                    Head.remove(0);
                    Message.remove(0);
                    Images.remove(0);
                    UserIds.remove(0);
                }
                adapter.notifyDataSetChanged();
            }
        });
        scrollMyListViewToBottom();
    }

    public void btnGallery_onClick(View v){
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                GET_FROM_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        Bitmap bitmap = null;
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        insertImg = bitmap;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isFocus){
            SharedPreferences SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            ColorScheme = SharedPrefs.getString("Colors", "Default");
            Font = SharedPrefs.getString("Fonts", "Default");
            TimeStamps = SharedPrefs.getBoolean("TimeStampEnabled", false);
            TimeFormat = SharedPrefs.getBoolean("24hrEnabled", false);
            SetColors(ColorScheme);
            SetFont(Font);
            adapter.setTextColor(textColor);
            adapter.setFontStyle(FontStyle);
            adapter.notifyDataSetChanged();
        }
        //finish();
        //startActivity(getIntent());
        manager.onResume();
        //TODO: call ChatRoomManager onresume
        isFocus = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.onPause();
        //manager.close();

        //manager.onPause();
        //TODO: call ChatRoomManager onpause
        isFocus = false;
    }

    public void btnSendMessage_Click(View v){
        /*String Default = "Message Here";

        if(txtbxInput.getCurrentTextColor() == Color.GRAY){
            return;
        }*/

        String message = txtbxInput.getText().toString();


       /* txtbxInput.setText(Default.subSequence(0, Default.length()));
        txtbxInput.setTextColor(Color.GRAY);*/

        if(message.length() == 0 && insertImg == null){
            return;
        }

        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(txtbxInput.getWindowToken(), 0);

        if(insertImg != null && message.length() == 0) {
            message = " ";
        }

        manager.getCurrentChatRoom().sendMessage(message, insertImg);    //change null to the user's picture

        insertImg = null;
        txtbxInput.clearFocus();


    }

    public void onBackPressed()
    {
        manager.close();
        ChatRoomsList.manager = null;
        super.onBackPressed();
        Log.d("BackButton", "Pressed");
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
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
