package wifinderinc.wifinder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.w3c.dom.Text;

public class ChatRoomView extends AppCompatActivity{

    EditText txtbxInput;
    Button btnSend;
    TextView txtvDisplay;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_view);

        txtbxInput = (EditText)findViewById(R.id.txtMessageInput);
        btnSend = (Button)findViewById(R.id.btnSendMessage);
        txtvDisplay = (TextView)findViewById(R.id.txtChatDisplay);




        txtbxInput.clearFocus();
        txtbxInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtbxInput.setText("");
                txtbxInput.setTextColor(Color.BLACK);
            }
        });
    }

    public void btnSendMessage_Click(View v){
        String Default = "Message Here";

        txtvDisplay.setText(txtvDisplay.getText()+ "\n"+txtbxInput.getText());
        txtbxInput.setText(Default.subSequence(0, Default.length()));
        txtbxInput.setTextColor(Color.GRAY);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtbxInput.getWindowToken(), 0);

        return;
    }

}
