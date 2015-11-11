package wifinderinc.wifinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Cole Baughn on 11/11/2015.
 */
public class LogView extends AppCompatActivity {


    private ListView lstDisplay;
    private String LogName;

    private ArrayList<String> Log = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);

        lstDisplay = (ListView)findViewById(R.id.lstLogDisp);

        Intent intent = getIntent();
        LogName = intent.getStringExtra(LogsList.LOG_NAME);

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Log);
        lstDisplay.setAdapter(adapter);

    }
}
