package wifinderinc.wifinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Cole Baughn on 11/9/2015.
 */

public class HomePage extends AppCompatActivity {
    public final static String USER_NAME = ".USER";

    private EditText txtbxUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        txtbxUser = (EditText)findViewById(R.id.editUser);

    }

    private void incorrectUser(){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Your User Name can not contain spaces");
        dlgAlert.setTitle("Incorrect User Name");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    //Opens the Chat Rooms List page
    public void btnChatRooms_Click(View v){
        String user = txtbxUser.getText().toString();
        if(user.contains(" ")){
            incorrectUser();
            return;
        }

        Intent intent = new Intent(this, ChatRoomsList.class);
        intent.putExtra(USER_NAME, txtbxUser.getText().toString());
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
        this.startActivity(intent);
    }
}
