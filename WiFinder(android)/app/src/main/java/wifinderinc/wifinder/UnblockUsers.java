package wifinderinc.wifinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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
        BlockedList.add("derp " + BlockedUsers.size());

        for(BlockedUser curr : BlockedUsers ){
            String str = curr.getUsername() + " blocked at " + curr.getTime();
            BlockedList.add(str);
        }

        BlockAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , BlockedList){
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                return view;
            }
        };
        UnblockList.setAdapter(BlockAdpt);

    }
}
