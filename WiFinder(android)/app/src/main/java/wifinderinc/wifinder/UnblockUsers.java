package wifinderinc.wifinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cole Baughn on 12/1/2015.
 */
public class UnblockUsers extends AppCompatActivity {

    ListView UnblockList;
    TextView Title;
    Blocker blocker;
    ArrayList<BlockedUser> BlockedUsers;
    ArrayList<String> BlockedList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock_users);

        Title = (TextView) findViewById(R.id.lblTitle);
        UnblockList = (ListView) findViewById(R.id.listView);
        blocker.initialize(this);

        BlockedUsers = blocker.getBlockedUsers();

        for(BlockedUser curr : BlockedUsers ){
            String str = curr.getUsername() + " blocked at " + curr.getTime();
            BlockedList.add(str);
        }

    }
}
