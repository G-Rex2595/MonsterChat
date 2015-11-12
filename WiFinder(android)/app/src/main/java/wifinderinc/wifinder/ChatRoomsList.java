package wifinderinc.wifinder;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

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

    //set up adapter
    private ArrayList<String> RoomList = new ArrayList<>();
    private ArrayAdapter<String> RoomAdpt;

    //other globals
    private String NewRoomName = "";
    //private String NewRoomPass = "";
    private String user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms_list);
        btnCreate = (Button)findViewById(R.id.btnCreateRoom);
        lstRooms = (ListView) findViewById(R.id.lstChatRooms);
        lblTitle = (TextView) findViewById(R.id.txtChatRoomsList);

        Intent intent = getIntent();
        user = intent.getStringExtra(HomePage.USER_NAME);


        manager = new ChatRoomManager("" + System.currentTimeMillis(), this);

        //list of rooms

        RoomList.add("Global");

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
        RoomAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,RoomList);
        lstRooms.setAdapter(RoomAdpt);
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
                    RoomList.add(NewRoomName);
                    RoomAdpt.notifyDataSetChanged();
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
