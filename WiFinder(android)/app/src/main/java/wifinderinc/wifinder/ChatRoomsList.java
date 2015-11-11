package wifinderinc.wifinder;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private Button btnCreate;
    private ListView lstRooms;
    private TextView lblTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms_list);
        btnCreate = (Button)findViewById(R.id.btnCreateRoom);
        lstRooms = (ListView) findViewById(R.id.lstChatRooms);
        lblTitle = (TextView) findViewById(R.id.txtChatRoomsList);

        //list of rooms
        ArrayList<String> RoomList = new ArrayList<>();
        RoomList.add("Global");

        //set item listener
        lstRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when item is selected open chat rooms view and send the room name
                Intent intent = new Intent(ChatRoomsList.this, ChatRoomView.class);
                TextView temp = (TextView) view;
                intent.putExtra(ROOM_NAME, temp.getText().toString());
                startActivity(intent);
            }


        });

        //part of creating the list
        ListAdapter RoomAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,RoomList);
        lstRooms.setAdapter(RoomAdpt);
    }
}
