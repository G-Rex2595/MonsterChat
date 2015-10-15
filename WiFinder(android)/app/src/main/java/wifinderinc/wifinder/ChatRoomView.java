package wifinderinc.wifinder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Calendar;

public class ChatRoomView extends AppCompatActivity{

    private EditText txtbxInput;
    private Button btnSend;
    private TextView txtvDisplay;
    private ChatRoomManager manager;
    private String RoomName;
    private String UserName;
    private ChatRoom CurrentChatRoom;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_view);

        txtbxInput = (EditText)findViewById(R.id.txtMessageInput);
        btnSend = (Button)findViewById(R.id.btnSendMessage);
        txtvDisplay = (TextView)findViewById(R.id.txtChatDisplay);
        RoomName = "Global";
        UserName = "Default";

        //manager = new ChatRoomManager(UserName, this);
        manager = new ChatRoomManager("" + System.currentTimeMillis(), this);
        CurrentChatRoom = manager.joinRoom(RoomName);
        CurrentChatRoom.setChatRoomView(this);

        txtvDisplay.setMovementMethod(new ScrollingMovementMethod());

        txtbxInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean bool) {
                if(bool == true) {
                    txtbxInput.setText("");
                    txtbxInput.setTextColor(Color.BLACK);
                }
            }
        });
    }

    public void addMessage(Message m) {
        Calendar c = Calendar.getInstance();

        final String message = String.format("%s:         %tr\n%s\n", m.getName(), c, m.getMessage());
        runOnUiThread(new Runnable(){
            public void run()
            {
                txtvDisplay.append(message);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        manager.onResume();
        //TODO: call ChatRoomManager onresume
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.onPause();
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

        CurrentChatRoom.sendMessage(message);

        txtbxInput.clearFocus();
    }

}
