package wifinderinc.wifinder;

import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Cole Baughn on 11/9/2015.
 */

public class HomePage extends AppCompatActivity {
    //Globals to pass to next activity
    public final static String USER_NAME = ".USER";

    //UI Globals
    private EditText txtbxUser;
    private Button btnChatList;
    private Button btnLogs;
    private Button btnPrefs;
    private TextView UserBox;
    private TextView RoomsBox;
    private TextView LogsBox;
    private TextView SettingsBox;
    private RelativeLayout Back;
    private TextView Title;

    //Preference Globals
    private SharedPreferences SharedPrefs;
    private String ColorScheme;
    private String Font;
    private int textColor;
    private Typeface FontStyle;

    //Focus
    private Boolean isFocus = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Setup UI controls
        txtbxUser = (EditText)findViewById(R.id.editUser);
        btnChatList = (Button) findViewById(R.id.btnChatRooms);
        btnLogs = (Button) findViewById(R.id.btnChatLogs);
        btnPrefs = (Button) findViewById(R.id.btnSettings);
        UserBox = (TextView) findViewById(R.id.UserBack);
        RoomsBox = (TextView) findViewById(R.id.ChatRoomsBack);
        LogsBox = (TextView) findViewById(R.id.ChatLogsBack);
        SettingsBox = (TextView) findViewById(R.id.SettingsBack);
        Title = (TextView)findViewById(R.id.lblTitle);
        Back = (RelativeLayout) findViewById(R.id.Layout);

        //set up text change lisener for user input
        txtbxUser.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editPref = SharedPrefs.edit();
                editPref.putString("UserName", txtbxUser.getText().toString());
                editPref.commit();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String user = txtbxUser.getText().toString();
                if(!user.matches("[a-zA-Z0-9]+") && user.length() > 0){
                    txtbxUser.setText(user.replaceAll("[^A-Za-z0-9]", ""));
                    incorrectUser("Your User Name may only contain letters and numbers.");
                    return;
                }
            }
        });

        //setup errorlog
        ErrorLog.initialize(this);

        //setup blocker
        Blocker.initialize(this);

        //Get Preferences
        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ColorScheme = SharedPrefs.getString("Colors", "Default");
        Font = SharedPrefs.getString("Fonts", "Default");
        String userN = SharedPrefs.getString("UserName", "Anonymous");

        txtbxUser.setText(userN);
        //Set Preferences
        SetColors(ColorScheme);
        SetFont(Font);



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

    private void incorrectUser(String msg){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Incorrect User Name");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

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


        HomePage.this.Back.setBackgroundColor(backColor);

        btnChatList.setBackgroundColor(btnColor);
        btnLogs.setBackgroundColor(btnColor);
        btnPrefs.setBackgroundColor(btnColor);

        txtbxUser.setBackgroundColor(txtBackColor);

        UserBox.setBackgroundColor(textColor);
        RoomsBox.setBackgroundColor(textColor);
        LogsBox.setBackgroundColor(textColor);
        SettingsBox.setBackgroundColor(textColor);

        Title.setTextColor(textColor);
        txtbxUser.setTextColor(textColor);
        btnChatList.setTextColor(textColor);
        btnLogs.setTextColor(textColor);
        btnPrefs.setTextColor(textColor);

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

        txtbxUser.setTypeface(FontStyle);
        btnChatList.setTypeface(FontStyle);
        btnLogs.setTypeface(FontStyle);
        btnPrefs.setTypeface(FontStyle);
        Title.setTypeface(FontStyle);
    }

    //Opens the Chat Rooms List page
    public void btnChatRooms_Click(View v){
        String userN = txtbxUser.getText().toString();
        if (userN.length() == 0){
            incorrectUser("User name cannot be empty");
            return;
        }
        Context cont = this;
        Intent intent = new Intent(this, ChatRoomsList.class);
        intent.putExtra(USER_NAME, userN);
        this.startActivity(intent);
    }

    //Opens the Logs List Page
    public void btnLogs_Click(View v){
        Intent intent = new Intent(this, LogsList.class);
        this.startActivity(intent);
    }

    //Opens the Settings Page
    public void btnSettings_Click(View v){
        Intent intent = new Intent(this, Preferences.class);
        intent.putExtra(Preferences.EXTRA_SHOW_FRAGMENT, Preferences.Prefs.class.getName());
        intent.putExtra(Preferences.EXTRA_NO_HEADERS, true);
        this.startActivity(intent);
    }

    public void btntest_Click(View v){
        Intent intent = new Intent(this, Preferences.class);
        intent.putExtra(Preferences.EXTRA_SHOW_FRAGMENT, Preferences.Prefs.class.getName());
        intent.putExtra(Preferences.EXTRA_NO_HEADERS, true);
        this.startActivity(intent);
    }
}
