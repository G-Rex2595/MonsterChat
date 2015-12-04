package wifinderinc.wifinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Cole Baughn on 12/1/2015.
 */
public class UnblockUsers extends AppCompatActivity {

    private ListView UnblockList;
    private TextView Title;
    private ArrayList<BlockedUser> BlockedUsers;


    //set up adapter
    private ArrayList<String> BlockedList = new ArrayList<>();
    private ArrayAdapter<String> BlockAdpt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock_users);

        Title = (TextView) findViewById(R.id.lblTitle);
        UnblockList = (ListView) findViewById(R.id.listView);

        BlockedUsers = Blocker.getBlockedUsers();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatT = new SimpleDateFormat("hh:mm a");
        String time;


        for(BlockedUser curr : BlockedUsers) {
            c.setTimeInMillis(curr.getTime());
            time = formatT.format(c.getTime());
            String str = curr.getUsername() + " blocked at " + time;
            BlockedList.add(str);
        }

        UnblockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when item is selected open chat rooms view and send the room name
                BlockedUser currSelection = BlockedUsers.get(position);
                promptUnblock(currSelection, position);
            }


        });
        BlockAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , BlockedList){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                return view;
            }
        };
        UnblockList.setAdapter(BlockAdpt);

    }

    private void promptUnblock(BlockedUser unblockUser, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unblock " + unblockUser.getUsername() + "?");


        // Set up the buttons
        final BlockedUser unblU = unblockUser;
        final int pos = position;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Blocker.unblock(unblU);
                BlockedList.remove(pos);
                BlockAdpt.notifyDataSetChanged();
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
