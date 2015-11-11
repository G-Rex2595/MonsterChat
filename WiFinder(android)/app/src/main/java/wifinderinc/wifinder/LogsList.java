package wifinderinc.wifinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cole Baughn on 11/11/2015.
 */
public class LogsList extends AppCompatActivity {

    public final static String LOG_NAME = ".LOG";

    private ListView lstLogs;
    private TextView lblTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_list);

        lstLogs = (ListView) findViewById(R.id.lstLogList);
        lblTitle = (TextView) findViewById(R.id.txtLogsList);

        //Array of Logs
        ArrayList<String> LogList = new ArrayList<>(11);
        LogList.add("Placeholder");

        //set up click listener
        lstLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open Log and send log name
                Intent intent = new Intent(LogsList.this, LogView.class);
                TextView temp = (TextView) view;
                intent.putExtra(LOG_NAME, temp.getText().toString());
                startActivity(intent);
            }


        });

        //load array into list
        ListAdapter RoomAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,LogList);
        lstLogs.setAdapter(RoomAdpt);
    }
}
